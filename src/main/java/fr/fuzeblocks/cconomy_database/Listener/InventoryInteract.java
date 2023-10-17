package fr.fuzeblocks.cconomy_database.Listener;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Manager.Database.Utils.DatabaseUtils;
import fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer;
import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

import static fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer.updateInventory;

public class InventoryInteract extends DatabaseUtils implements Listener {
    private CconomyDatabase instance;
    private String ClickedHead;

    public InventoryInteract(CconomyDatabase instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        playerHead(event);

        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
            antiInteract( LanguageManager.getConfig().getString(LanguageManager.getKey() + "InventoryName"), event);
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aSend money")) {
                Player player = (Player) event.getWhoClicked();
                if (event.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                     new ListOnlinePlayer(player, "§aSendMoney", instance);
                }
            }
        }

        if (event.getView().getTitle().equals("§aMontant")) {
            event.setCancelled(true);
            try {
                getMontant(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        Inventory clickedInventory = event.getClickedInventory();
        String name = event.getView().getTitle();
        if (clickedInventory != null) {
            if (name.equals("§aSendMoney")) {
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem != null) {
                    if (clickedItem.getType() == Material.ARROW) {
                        int currentPage = Integer.parseInt(name.split(" - Page ")[1]);
                        if (clickedItem.getItemMeta().getDisplayName().contains("Page précédente")) {
                            if (currentPage > 1) {
                                currentPage--;
                                updateInventory(name, currentPage);
                            }
                        } else if (clickedItem.getItemMeta().getDisplayName().contains("Page suivante")) {
                            currentPage++;
                            updateInventory(name, currentPage);
                        }
                    }
                }
            }
        }
    }

    public void antiInteract(String name, @NotNull InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(name)) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§bCancel")) event.getWhoClicked().closeInventory();
        }
    }

    public void playerHead(@NotNull InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ListOnlinePlayer.name)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                    event.setCancelled(true);
                    ClickedHead = event.getCurrentItem().getItemMeta().getDisplayName();
                    Inventory payinventory = Bukkit.createInventory(null, 9, "§aMontant");
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a0.5"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a1.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a5.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a10.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a100.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a500.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a1000.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER, "§a5000.0"));
                    payinventory.addItem(ItemFactory.CreateItemStack(Material.REDSTONE_BLOCK, "§bCancel"));

                    event.getWhoClicked().openInventory(payinventory);
                }
            }
        }
    }

    public void getMontant(@NotNull InventoryClickEvent event) throws SQLException {
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getType().equals(Material.PAPER)) {
            UUID uuid = Bukkit.getPlayerExact(ClickedHead).getUniqueId();
            Player player = (Player) event.getWhoClicked();
            YamlConfiguration config = LanguageManager.getConfig();
            String key = LanguageManager.getKey();
            switch (event.getCurrentItem().getItemMeta().getDisplayName().toString()) {
                case "§a0.5":
                    pay(player,0.5,uuid,config,key);
                    break;
                case "§a1.0":
                    pay(player,1.0,uuid,config,key);
                    break;
                case "§a5.0":
                    pay(player,5.0,uuid,config,key);
                    break;
                case "§a10.0":
                    pay(player,10.0,uuid,config,key);
                    break;

                case "§a500.0":
                    pay(player,500.0,uuid,config,key);
                    break;

                case "§a1000.0":
                    pay(player, 1000.0, uuid, config, key);
                    break;
                case "§a5000.0":
                    pay(player, 5000.0, uuid, config, key);
                    break;
            }
        }
    }
    private void pay(@NotNull Player player, double mountant, UUID uuid, YamlConfiguration config, String key) throws SQLException {
        if (getMoney(getConnection(), player.getUniqueId()) >= mountant) {
            removeMoney(getConnection(), player.getUniqueId(), mountant);
            addMoney(getConnection(), uuid, mountant);
            player.sendMessage(config.getString(key + "Pay"));
            playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP);
            playEffect(player, Effect.MOBSPAWNER_FLAMES,1000);
        } else {
            player.sendMessage(config.getString(key + "NoMoney"));
            playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS);
        }
    }
}
