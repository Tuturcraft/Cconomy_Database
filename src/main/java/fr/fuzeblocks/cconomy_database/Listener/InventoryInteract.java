package fr.fuzeblocks.cconomy_database.Listener;
import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer;
import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
            antiInteract("§aView your money",event);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aSend money")) {
                Player player = (Player) event.getWhoClicked();
                if (event.getCurrentItem().getType().equals(Material.EMERALD_BLOCK)) {
                    ListOnlinePlayer listOnlinePlayer = new ListOnlinePlayer(player,"§aSendMoney",instance);
                }
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
    public void antiInteract(String name, InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase(name)) {
            event.setCancelled(true);
        }
    }
    public void playerHead(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ListOnlinePlayer.name)){
        if (event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
             event.setCancelled(true);
             ClickedHead = event.getCurrentItem().getItemMeta().getDisplayName();
             Inventory payinventory = Bukkit.createInventory(null,9,"§aMontant");

             payinventory.addItem(ItemFactory.CreateItemStack(Material.PAPER,"§a0.5"));
            }
            }
        }
    }
}
