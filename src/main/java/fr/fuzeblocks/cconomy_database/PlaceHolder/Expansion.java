package fr.fuzeblocks.cconomy_database.PlaceHolder;
import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Manager.Database.Utils.DatabaseUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class Expansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "cconomyd";
    }

    @Override
    public String getAuthor() {
        return "fuzeblocks";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        if (identifier.equals("uuid")) {
            return  player.getUniqueId().toString();
        }

        if (identifier.equals("money")) {
            double money = 0;
            try {
                money = getMoney(CconomyDatabase.getConnection(), player.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error";
            }
            return String.valueOf(money);
        }
        return null;
    }
    public boolean register() {
        if (super.register()) {
            getLogger().info("Extension Cconomy_Database enregistrée avec succès !");
        } else {
            getLogger().warning("Erreur lors de l'enregistrement de l'extension Cconomy_Database !");
        }
        return false;
    }
    private double getMoney(Connection connection, UUID uuid) throws SQLException {
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
}