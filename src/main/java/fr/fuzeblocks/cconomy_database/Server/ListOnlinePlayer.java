package fr.fuzeblocks.cconomy_database.Server;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ListOnlinePlayer implements Listener {
    private static Player player;
    private static int currentPage = 1;
    private static int maxInventorySize = 54;
    private static int itemsPerPage = 45;
    private static List<ItemStack> itemsToDisplay;

    public static String name;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxInventorySize() {
        return maxInventorySize;
    }

    public void setMaxInventorySize(int maxInventorySize) {
        this.maxInventorySize = maxInventorySize;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public List<ItemStack> getItemsToDisplay() {
        return itemsToDisplay;
    }

    public void setItemsToDisplay(List<ItemStack> itemsToDisplay) {
        this.itemsToDisplay = itemsToDisplay;
    }

    public String getName() {
        return name;
    }
    private CconomyDatabase instance;

    public ListOnlinePlayer(CconomyDatabase instance) {
        this.instance = instance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListOnlinePlayer(Player player, String name, CconomyDatabase instance) {
        this.player = player;
        this.name = name;
        this.instance = instance;
        initializeItems();
        updateInventory(name, 1);
    }

    public static void initializeItems() {
        itemsToDisplay = new ArrayList<>();
            for (Player player1 : Bukkit.getOnlinePlayers()) {
                if (!player1.getDisplayName().equals(player.getDisplayName())) {
                    ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
                    SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
                    playerheadmeta.setOwningPlayer(player1);
                    playerheadmeta.setDisplayName(player1.getDisplayName());
                    playerhead.setItemMeta(playerheadmeta);
                    itemsToDisplay.add(playerhead);
                }
            }

        }

    public static void updateInventory(String name, int newPage) {
        ListOnlinePlayer.name = name + " - Page " + newPage;
        currentPage = newPage;
        Inventory inventory = Bukkit.createInventory(null, maxInventorySize, ListOnlinePlayer.name);
        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, itemsToDisplay.size());
        for (int i = startIndex; i < endIndex; i++) {
            inventory.addItem(itemsToDisplay.get(i));
        }
        if (currentPage > 1) {
            inventory.setItem(45, createNavigationItem(Material.ARROW, "§aPage précédente"));
        }
        if (endIndex < itemsToDisplay.size()) {
            inventory.setItem(53, createNavigationItem(Material.ARROW, "§aPage suivante"));
        }
        player.openInventory(inventory);
    }


    private static ItemStack createNavigationItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }



}
