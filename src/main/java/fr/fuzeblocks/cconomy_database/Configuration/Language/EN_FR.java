package fr.fuzeblocks.cconomy_database.Configuration.Language;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class EN_FR extends LanguageManager {
    public EN_FR(YamlConfiguration file, File file1) {
        super(file, file1);
    }
    String key = "EN_FR.";
    public void addMessage() {
        super.createSection("EN_FR");
        super.addIndex(key + "Solde","§aVotre money est : ");
        super.addIndex(key + "InventoryName","§aVoir votre argent");
        super.addIndex(key + "PaperofMoney", "RED");
        super.addIndex(key + "Pay","§aTransaction réussis");
        super.addIndex(key + "NoMoney","§cVous n'avez pas assez d'argent.");
        super.addIndex(key + "PayInventoryName","§aMontant");
    }
}
