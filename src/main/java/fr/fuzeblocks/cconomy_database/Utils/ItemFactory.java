package fr.fuzeblocks.cconomy_database.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemFactory {

    public static ItemStack CreateItemStack(Material material,String name,int anInt) {
        ItemStack item = new ItemStack(material,anInt);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack CreateItemStack(Material material, String name, int anInt, List<String> lore) {
        ItemStack item = new ItemStack(material,anInt);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack CreateItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material,1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
