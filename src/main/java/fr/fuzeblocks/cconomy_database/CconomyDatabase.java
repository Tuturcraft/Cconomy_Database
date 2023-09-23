package fr.fuzeblocks.cconomy_database;

import fr.fuzeblocks.cconomy_database.Command.MoneyCommand;
import fr.fuzeblocks.cconomy_database.Completer.MoneyCompleter;
import fr.fuzeblocks.cconomy_database.Listener.CheckDatabaseListener;
import fr.fuzeblocks.cconomy_database.Manager.Database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CconomyDatabase extends JavaPlugin {
    DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        databaseManager = new DatabaseManager();
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("money").setTabCompleter(new MoneyCompleter());
        Bukkit.getPluginManager().registerEvents(new CheckDatabaseListener(this),this);
    }

    @Override
    public void onDisable() {
        databaseManager.close();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
