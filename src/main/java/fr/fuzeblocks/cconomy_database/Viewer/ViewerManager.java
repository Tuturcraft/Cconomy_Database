package fr.fuzeblocks.cconomy_database.Viewer;

import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ViewerManager {
    private String name;
    private int size;
    private InventoryHolder holder;
    private Inventory inventory;
    public ViewerManager(String name, int size, InventoryHolder holder) {
        this.name = name;
        this.size = size;
        this.holder = holder;
        this.inventory = Bukkit.createInventory(holder,size,name);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public void setHolder(InventoryHolder holder) {
        this.holder = holder;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public void setItem(ItemStack item,int index) {
        inventory.setItem(index,item);
    }
    public void addItem(ItemStack item) {
        inventory.addItem(item);
    }
}
