package fr.fuzeblocks.cconomy_database.Command;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageStatus;
import fr.fuzeblocks.cconomy_database.Viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static fr.fuzeblocks.cconomy_database.CconomyDatabase.getConnection;

public class MoneyCommand implements CommandExecutor {
    private CconomyDatabase instance;

    public MoneyCommand(CconomyDatabase instance) {
        this.instance = instance;
    }
    private String key = LanguageManager.getKey();
    private YamlConfiguration config = LanguageManager.getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player !");
            return true;
        }

        Player player = (Player) sender;


        if (!player.hasPermission("Cconomy.commandes.money")) {
            player.sendMessage("§cYou don't have permission.");
            return true;
        }

        if (args == null || args.length == 0) {
            player.sendMessage("§cUsage: /money <add,set,remove,solde,menu,view> [player] [amount]");
            return true;
        }

        String subCommand = args[0];

        if (subCommand.equalsIgnoreCase("solde")) {
            try {
                player.sendMessage(config.getString(key + "Solde") + getMoney(getConnection(), player.getUniqueId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (subCommand.equalsIgnoreCase("menu")) {
            MenuViewer.loadMenu(player);
        } else if (subCommand.equalsIgnoreCase("view")) {
            if (args.length == 2) {
                try {
                   player.sendMessage("The player's money: " + args[1]  + "§ais : "+ getMoney(getConnection(), Objects.requireNonNull(Bukkit.getPlayerExact(args[1]).getUniqueId())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                player.sendMessage("§cUsage: /money view <player>");
            }
        } else if (args.length == 3) {
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                player.sendMessage(args[1] + " doesn't exist!");
                return true;
            }
            UUID targetUUID = target.getUniqueId();
            double money = Double.parseDouble(args[2]);

            if (subCommand.equalsIgnoreCase("add")) {
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    addMoney(getConnection(), targetUUID, money);
                    player.sendMessage("§aYou have well add : " + money + "§a to the player : " + player.getName());
                } else {
                    player.sendMessage("§cYou don't have perms");
                }
            } else if (subCommand.equalsIgnoreCase("set")) {
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    setMoney(getConnection(), targetUUID, money);
                    player.sendMessage("§aYou have well set : " + money + "§a to the player : " + player.getName());
                } else {
                    player.sendMessage("§cYou don't have perms");
                }
            } else if (subCommand.equalsIgnoreCase("remove")) {
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    try {
                        removeMoney(getConnection(), targetUUID, money);
                        player.sendMessage("§aYou have well remove : " + money + "§a to the player : " + player.getName());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    player.sendMessage("§cYou don't have perms !");
                }
            }
        } else {
            player.sendMessage("§cUsage: /money <add,set,remove,solde,menu,view> [player] [amount]");
        }
        return true;
    }
    public static void setMoney(Connection connection, UUID uuid, double money) {
        String updateQuery = "UPDATE `players_money` SET `money`=? WHERE `uuid`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, money);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMoney(Connection connection, UUID uuid, double money) {
        String selectQuery = "SELECT `money` FROM `players_money` WHERE `uuid`=?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, uuid.toString());
            try (ResultSet resultmoney = selectStatement.executeQuery()) {
                if (resultmoney.next()) {
                    double lastmoney = resultmoney.getDouble("money");
                    double finalemoney = lastmoney + money;
                    String updateQuery = "UPDATE `players_money` SET `money`=? WHERE `uuid`=?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setDouble(1, finalemoney);
                        updateStatement.setString(2, uuid.toString());
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static double getMoney(Connection connection, UUID uuid) throws SQLException {
        String selectQuery = "SELECT `money` FROM `players_money` WHERE `uuid`=?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, uuid.toString());
            try (ResultSet resultmoney = selectStatement.executeQuery()) {
                    if (resultmoney.next()) {
                            return resultmoney.getDouble("money");
                    }
            }
        }
        return 0.0;
    }

    public static void removeMoney(Connection connection, UUID uuid, double money) throws SQLException {
        String selectQuery = "SELECT `money` FROM `players_money` WHERE `uuid`=?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, uuid.toString());
            try (ResultSet resultmoney = selectStatement.executeQuery()) {
                if (resultmoney.next()) {
                    double lastmoney = resultmoney.getDouble("money");
                    double finalemoney = lastmoney - money;
                    String updateQuery = "UPDATE `players_money` SET `money`=? WHERE `uuid`=?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setDouble(1, finalemoney);
                        updateStatement.setString(2, uuid.toString());
                        updateStatement.executeUpdate();
                }
            }
        }
    }

    }
