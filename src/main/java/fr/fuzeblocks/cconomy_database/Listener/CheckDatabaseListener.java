package fr.fuzeblocks.cconomy_database.Listener;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Manager.Database.DbConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class CheckDatabaseListener implements Listener {
    private CconomyDatabase instance;
    public CheckDatabaseListener(CconomyDatabase instance) {
        this.instance = instance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        getCr(getConnection(),player.getUniqueId(),instance.getConfig().getDouble("Config.defaultmoney"),player);

    }
    private Connection getConnection() throws SQLException {
        final DbConnection moneyconnection = instance.getDatabaseManager().getMoneyconnection();
        return moneyconnection.getConnection();
    }
    private void getCr(Connection connection, UUID uuid, Double money,Player player) throws SQLException {
        final PreparedStatement getmoney = connection.prepareStatement("SELECT `uuid`, `money` FROM `players_money` WHERE uuid = ?");
        getmoney.setString(1, uuid.toString());
        ResultSet resultmoney = getmoney.executeQuery();
        if (!resultmoney.next()) {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `players_money`(`uuid`, `money`) VALUES (?, ?)");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setDouble(2, money);
            preparedStatement.executeUpdate();
            Logger.getLogger("Cconomy").info("Successfully created for : " + player.getName());
            preparedStatement.close();
        }
        getmoney.close();
    }


}
