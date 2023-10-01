package fr.fuzeblocks.cconomy_database;

import fr.fuzeblocks.cconomy_database.Command.MoneyCommand;
import fr.fuzeblocks.cconomy_database.Completer.MoneyCompleter;
import fr.fuzeblocks.cconomy_database.Configuration.Language.EN_FR;
import fr.fuzeblocks.cconomy_database.Configuration.Language.EN_US;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageStatus;
import fr.fuzeblocks.cconomy_database.Listener.CheckDatabaseListener;
import fr.fuzeblocks.cconomy_database.Listener.InventoryInteract;
import fr.fuzeblocks.cconomy_database.Manager.Database.CreateTable;
import fr.fuzeblocks.cconomy_database.Manager.Database.DatabaseManager;
import fr.fuzeblocks.cconomy_database.Manager.Database.DbConnection;
import fr.fuzeblocks.cconomy_database.PlaceHolder.Expansion;
import fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class CconomyDatabase extends JavaPlugin {
    static DatabaseManager databaseManager;
    public static LanguageStatus languageStatus;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLanguage();
        LanguageManager.setKeyAndConfig(languageStatus, this);
        databaseManager = new DatabaseManager(this);
        new CreateTable(getConnection());
        Expansion cconomyExpansion = new Expansion();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            cconomyExpansion.register();
        } else {
            getLogger().warning("PlaceholderAPI n'est pas installé, votre extension ne fonctionnera pas correctement.");
        }
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("money").setTabCompleter(new MoneyCompleter());
        Bukkit.getPluginManager().registerEvents(new CheckDatabaseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryInteract(this), this);
        Bukkit.getPluginManager().registerEvents(new ListOnlinePlayer(this), this);
    }

    @Override
    public void onDisable() {
        databaseManager.close();
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static Connection getConnection() {
        final DbConnection moneyconnection = getDatabaseManager().getMoneyconnection();
        try {
            return moneyconnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getLanguage() {
        ConfigurationSection config = this.getConfig();
        int language = config.getInt("Config.Language");
        switch (language) {
            case 1:
                languageStatus = LanguageStatus.EN_US;
                File EnUs =  getEN(this,"EN_US");
                EN_US us = new EN_US(YamlConfiguration.loadConfiguration(EnUs),EnUs);
                us.addMessage();
                break;
            case 2:
                languageStatus = LanguageStatus.EN_FR;
                File EnFr = getEN(this,"EN_FR");
                EN_FR fr = new EN_FR(YamlConfiguration.loadConfiguration(EnFr),EnFr);
                fr.addMessage();
                break;
            default:
                languageStatus = LanguageStatus.EN_US;
                File EnUs1 =  getEN(this,"EN_US");
                EN_US us1 = new EN_US(YamlConfiguration.loadConfiguration(EnUs1),EnUs1);
                us1.addMessage();

                break;
        }
    }

    public static File getEN(CconomyDatabase plugin, String child) {
        File file = new File(plugin.getDataFolder() + "/Language/", child + ".yml");
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}

