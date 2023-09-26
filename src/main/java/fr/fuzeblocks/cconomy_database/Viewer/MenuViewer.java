package fr.fuzeblocks.cconomy_database.Viewer;

import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static fr.fuzeblocks.cconomy_database.CconomyDatabase.getConnection;
import static fr.fuzeblocks.cconomy_database.Command.MoneyCommand.getMoney;

public class MenuViewer  {
    public static void loadMenu(Player player) {
        ViewerManager manager = new ViewerManager("§aView your money",9,player);
        try {
            manager.setItem(ItemFactory.CreateItemStack(Material.PAPER,String.valueOf(getMoney(getConnection(),player.getUniqueId()))),4);
            manager.setItem(ItemFactory.CreateItemStack(Material.EMERALD_BLOCK,"§aSend money"),0);
            player.openInventory(manager.getInventory());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
    }


