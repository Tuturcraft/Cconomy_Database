package fr.fuzeblocks.cconomy_database.Command;

import fr.fuzeblocks.cconomy_database.Viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static fr.fuzeblocks.cconomy_database.CconomyDatabase.getConnection;

public class MoneyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Cconomy.commandes.money")) {
                if (args != null && args.length != 0) {
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        UUID targetuuid = target.getUniqueId();
                        if (target != null) {
                            if (player.hasPermission("Cconomy.commandes.money.admin")) {
                                double money = Double.parseDouble(args[2]);
                                if (args[0].equalsIgnoreCase("add")) {
                                    addMoney(getConnection(), targetuuid, money, player);
                                }
                                if (args[0].equalsIgnoreCase("set")) {
                                    setMoney(getConnection(), targetuuid, money, player);
                                }
                                if (args[0].equalsIgnoreCase("remove")) {
                                    try {
                                        removeMoney(getConnection(), targetuuid, money, player);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            } else {
                                player.sendMessage("§aYou don't have perms");
                            }
                        }  else {
                            player.sendMessage(args[2] + "doesn't exist !");
                        }
                    } else if (args[0].equalsIgnoreCase("solde")){
                            try {
                                player.sendMessage("§aYour money is : " + getMoney(getConnection(), player.getUniqueId()));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (args[0].equalsIgnoreCase("menu")) {
                        MenuViewer.loadMenu(player);
                    } else {
                        player.sendMessage("§cUsage <player + montant>");
                    }
                }  else {
                    player.sendMessage("§cUsage <add,set,remove,solde>");
                }
            }
        }  else {
            sender.sendMessage("You are not a player !");
        }
        return false;
    }
    public static void setMoney(Connection connection, UUID uuid, double money, Player player) {
        String updateQuery = "UPDATE `players_money` SET `money`=? WHERE `uuid`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, money);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
            player.sendMessage("§aYou have well set :" + money + "§a to the player :" + player.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addMoney(Connection connection, UUID uuid, double money, Player player) {
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
                        player.sendMessage("§aYou have well add :" + money + "§a to the player :" + player.getName());
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

    public static void removeMoney(Connection connection, UUID uuid, double money, Player player) throws SQLException {
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
                        player.sendMessage("§aYou have well remove :" + money + "§a to the player :" + player.getName());

                }
            }
        }
    }

    }
