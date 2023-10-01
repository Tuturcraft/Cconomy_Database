package fr.fuzeblocks.cconomy_database.Configuration.Language;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LanguageManager <T> {
    private YamlConfiguration file;
    private File file1;

    public LanguageManager(YamlConfiguration file,File file1) {
        this.file = file;
        this.file1 = file1;
    }
    public void addIndex (String index,T objects) {
        if (!file.contains(index)) {
            file.set(index, objects);
            try {
                file.save(file1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String key;
    private static YamlConfiguration config;

    public static void setKeyAndConfig(LanguageStatus languageStatus, CconomyDatabase instance) {
        if (languageStatus.equals(LanguageStatus.EN_US)) {
            key = "EN_US.";
            config = YamlConfiguration.loadConfiguration(CconomyDatabase.getEN(instance,"EN_US"));
        } else if (languageStatus.equals(LanguageStatus.EN_FR)) {
            key = "EN_FR.";
            config = YamlConfiguration.loadConfiguration(CconomyDatabase.getEN(instance,"EN_FR"));
        }
    }
    public static String getKey() {
        return key;
    }

    public static YamlConfiguration getConfig() {
        return config;
    }
    public void createSection(String index) {
        if (!file.contains(index)) {
            file.createSection(index);
            try {
                file.save(file1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
