package fr.fuzeblocks.cconomy_database.Configuration.Language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class EN_US extends LanguageManager {
    String key = "EN_US.";

    public EN_US(YamlConfiguration file, File file1) {
        super(file,file1);
    }


    public void addMessage() {
        super.createSection("EN_US");
        super.addIndex(key + "Solde","§aYour money is: ");
        super.addIndex(key + "InventoryName","§aView your money");
        super.addIndex(key + "PaperofMoney", "RED");
        super.addIndex(key + "Pay","§aTransaction successful!");
        super.addIndex(key + "NoMoney","§cYou don't have enough money.");
        super.addIndex(key + "PayInventoryName","§aAmount");
        super.addIndex(key + "View","The player's money: ");
    }
}
