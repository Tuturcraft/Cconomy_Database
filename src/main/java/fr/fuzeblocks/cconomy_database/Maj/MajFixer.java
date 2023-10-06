package fr.fuzeblocks.cconomy_database.Maj;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class MajFixer {
    public MajFixer(Connection connection,Player player) {
        String request = "ALTER TABLE players_money ADD COLUMN possible_player varchar(36)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(request);
            setOwner(connection,player,player.getUniqueId());
            player.sendMessage("§aUpdate complete");
        } catch (Exception e) {
            player.sendMessage("§aYou already have the table : possible_player");
        }
    }
    public static void setOwner(Connection connection, Player player,UUID uuid) {
        String updateQuery = "UPDATE `players_money` SET `possible_player`=? WHERE `uuid`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
