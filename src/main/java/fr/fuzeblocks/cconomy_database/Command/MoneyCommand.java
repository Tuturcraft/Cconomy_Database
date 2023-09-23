package fr.fuzeblocks.cconomy_database.Command;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Manager.Database.DbConnection;
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

public class MoneyCommand implements CommandExecutor {
    private CconomyDatabase instance;
    public MoneyCommand(CconomyDatabase instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("Cconomy.commandes.money")) {
                if (args != null && args.length != 0) {
                    if (args.length == 4) {
                        Player target = Bukkit.getPlayerExact(args[2]);
                        UUID targetuuid = target.getUniqueId();
                        if (targetuuid != null) {
                          Double money =  Double.valueOf(args[3]);
                        switch (args.toString()) {
                            case "add":
                                try {
                                    addMoney(getConnection(), targetuuid, money);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            case "set":
                                try {
                                    setMoney(getConnection(),targetuuid, money);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            case "break":
                                try {
                                    removeMoney(getConnection(),targetuuid,money);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                        }

                        }  else {
                            player.sendMessage(args[2] + "doesn't exist !");
                        }
                    } else {
                        if (args[0].equalsIgnoreCase("solde")) {
                            try {
                                player.sendMessage(String.valueOf(getMoney(getConnection(),player.getUniqueId())));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    player.sendMessage("§cUsage <add,set,remove>");
                }
            }
        } else {
            sender.sendMessage("You are not a player !");
        }
        return false;
    }
    private void setMoney(Connection connection,UUID uuid,Double money) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `players_money` SET `uuid`=?,`money`=?");
        preparedStatement.setString(1,uuid.toString());
        preparedStatement.setDouble(2,money);
        preparedStatement.executeQuery();
        preparedStatement.close();
    }
    private void addMoney(Connection connection,UUID uuid,Double money) throws SQLException {
            final PreparedStatement getmoney = connection.prepareStatement("SELECT `uuid`, `money` FROM `players_money` WHERE uuid = ?");
            getmoney.setString(1,uuid.toString());
            ResultSet resultmoney = getmoney.executeQuery();
            if (resultmoney.next()) {
             Double lastmoney =   resultmoney.getDouble(2);
             double finalemoney = lastmoney + money;
                final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `players_money` SET `uuid`=?,`money`=?");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setDouble(2, finalemoney);
                preparedStatement.executeQuery();
            }
        getmoney.close();
        }

        private double getMoney(Connection connection,UUID uuid) throws SQLException {
            final PreparedStatement getmoney = connection.prepareStatement("SELECT `uuid`, `money` FROM `players_money` WHERE uuid = ?");
            getmoney.setString(1,uuid.toString());
            ResultSet resultmoney = getmoney.executeQuery();
            if (resultmoney.next()) {
                return resultmoney.getDouble(2);
            }
            getmoney.close();
            return 0.0;
        }
    private void removeMoney(Connection connection,UUID uuid,Double money) throws SQLException {
        final PreparedStatement getmoney = connection.prepareStatement("SELECT `uuid`, `money` FROM `players_money` WHERE uuid = ?");
        getmoney.setString(1,uuid.toString());
        ResultSet resultmoney = getmoney.executeQuery();
        if (resultmoney.next()) {
            Double lastmoney =   resultmoney.getDouble(2);
            double finalemoney = lastmoney - money;
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `players_money` SET `uuid`=?,`money`=?");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setDouble(2, finalemoney);
            preparedStatement.executeQuery();
        }
        getmoney.close();
    }
        private Connection getConnection() throws SQLException {
            final DbConnection moneyconnection = instance.getDatabaseManager().getMoneyconnection();
            return moneyconnection.getConnection();
        }
    }
