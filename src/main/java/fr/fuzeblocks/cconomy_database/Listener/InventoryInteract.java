package fr.fuzeblocks.cconomy_database.Listener;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Command.MoneyCommand;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer;
import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.UUID;

import static fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer.updateInventory;

public class InventoryInteract implements Listener {
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
                    ListOnlinePlayer listOnlinePlayer = new ListOnlinePlayer(player, "§aSendMoney", instance);
                }
            }
        }

        if (event.getView().getTitle().equals("§aMontant")) {
            event.setCancelled(true);
            getMontant(event);
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

    public void antiInteract(String name, InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(name)) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getType().equals(Material.REDSTONE_BLOCK)) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§bCancel")) event.getWhoClicked().closeInventory();
        }
    }

    public void playerHead(InventoryClickEvent event) {
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

    public void getMontant(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getType().equals(Material.PAPER)) {
            UUID uuid = Bukkit.getPlayerExact(ClickedHead).getUniqueId();
            Player player = (Player) event.getWhoClicked();
            YamlConfiguration config = LanguageManager.getConfig();
            String key = LanguageManager.getKey();
            switch (event.getCurrentItem().getItemMeta().getDisplayName().toString()) {
                case "§a0.5":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 0.5) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 0.5);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 0.5);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "§a1.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 1.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 1.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 1.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "§a5.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 5.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 5.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 5.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;


                case "§a10.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 10.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 10.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 10.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "§a500.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 500.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 1000.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 1000.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "§a1000.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 1000.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 1000.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 1000.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "§a5000.0":
                    try {
                        if (MoneyCommand.getMoney(CconomyDatabase.getConnection(), player.getUniqueId()) >= 5000.0) {
                            MoneyCommand.removeMoney(CconomyDatabase.getConnection(), player.getUniqueId(), 5000.0);
                            MoneyCommand.addMoney(CconomyDatabase.getConnection(), uuid, 5000.0);
                            player.sendMessage(config.getString(key + "Pay"));
                        } else {
                            player.sendMessage(config.getString(key + "NoMoney"));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;

            }
        }
    }
}
