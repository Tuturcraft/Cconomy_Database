package fr.fuzeblocks.cconomy_database.Manager.Database.Utils;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Manager.Database.DbConnection;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseUtils {


    protected void getCr(Connection connection, UUID uuid, Double money, Player player) throws SQLException {
        final PreparedStatement getmoney = connection.prepareStatement("SELECT `uuid`, `money` FROM `players_money` WHERE uuid = ?");
        getmoney.setString(1, uuid.toString());
        ResultSet resultmoney = getmoney.executeQuery();
        if (!resultmoney.next()) {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `players_money` (`uuid`, `money`, `possible_player`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setDouble(2, money);
            preparedStatement.setString(3, player.getName());
            preparedStatement.executeUpdate();
            Logger.getLogger("Cconomy").info("Successfully created for : " + player.getName());
        }
    }
    protected Connection getConnection()  {
        final DbConnection moneyconnection = CconomyDatabase.getDatabaseManager().getMoneyconnection();
        try {
            return moneyconnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected void setMoney(Connection connection, UUID uuid, double money) {
        String updateQuery = "UPDATE `players_money` SET `money`=? WHERE `uuid`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, money);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addMoney(Connection connection, UUID uuid, double money) {
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

    protected double getMoney(Connection connection, UUID uuid) throws SQLException {
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

    protected void removeMoney(Connection connection, UUID uuid, double money) throws SQLException {
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
    protected void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(),sound,10,10);
    }
    protected void playEffect(Player player, Effect effect,Object data) {
        player.playEffect(player.getLocation(),effect,data);
    }
}
