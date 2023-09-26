package fr.fuzeblocks.cconomy_database.Manager.Database;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseManager {
    private DbConnection moneyconnection;
    private CconomyDatabase instance;

    private String key = "Database.";

    public DatabaseManager(CconomyDatabase instance) {
        this.instance = instance;
        ConfigurationSection config = instance.getConfig();;
        this.moneyconnection = new DbConnection(new DbCredentials(config.getString(key + "host"),config.getString(key + "username"),config.getString(key + "password"),config.getString(key + "databasename"),config.getInt(key + "port")));
    }
    public void close() {
        this.moneyconnection.close();
    }

    public DbConnection getMoneyconnection() {
        return moneyconnection;
    }
}
