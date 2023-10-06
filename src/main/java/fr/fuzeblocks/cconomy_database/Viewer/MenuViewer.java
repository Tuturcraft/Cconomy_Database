package fr.fuzeblocks.cconomy_database.Viewer;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Manager.Database.Utils.DatabaseUtils;
import fr.fuzeblocks.cconomy_database.Utils.ItemFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;


public class MenuViewer extends DatabaseUtils {
    public void loadMenu(Player player) {
        YamlConfiguration config = LanguageManager.getConfig();
        ViewerManager manager = new ViewerManager(config.getString(LanguageManager.getKey() + "InventoryName"),9,player);
        try {
            manager.setItem(ItemFactory.CreateItemStack(Material.PAPER, ChatColor.valueOf(config.getString(LanguageManager.getKey() + "PaperofMoney")) + String.valueOf(getMoney(getConnection(),player.getUniqueId()))),4);
            manager.setItem(ItemFactory.CreateItemStack(Material.EMERALD_BLOCK,"§aSend money"),0);
            player.openInventory(manager.getInventory());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
    }


