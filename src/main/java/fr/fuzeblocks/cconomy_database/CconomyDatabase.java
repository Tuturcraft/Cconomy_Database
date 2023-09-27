package fr.fuzeblocks.cconomy_database;

import fr.fuzeblocks.cconomy_database.Command.MoneyCommand;
import fr.fuzeblocks.cconomy_database.Completer.MoneyCompleter;
import fr.fuzeblocks.cconomy_database.Listener.CheckDatabaseListener;
import fr.fuzeblocks.cconomy_database.Listener.InventoryInteract;
import fr.fuzeblocks.cconomy_database.Manager.Database.CreateTable;
import fr.fuzeblocks.cconomy_database.Manager.Database.DatabaseManager;
import fr.fuzeblocks.cconomy_database.Manager.Database.DbConnection;
import fr.fuzeblocks.cconomy_database.Server.ListOnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class CconomyDatabase extends JavaPlugin {
    static DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        databaseManager = new DatabaseManager(this);
        new CreateTable(getConnection());
        this.getCommand("money").setExecutor(new MoneyCommand());
        this.getCommand("money").setTabCompleter(new MoneyCompleter());
        Bukkit.getPluginManager().registerEvents(new CheckDatabaseListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryInteract(this), this);
        Bukkit.getPluginManager().registerEvents(new ListOnlinePlayer(this),this);
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
}
