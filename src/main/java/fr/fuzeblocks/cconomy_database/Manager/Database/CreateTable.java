package fr.fuzeblocks.cconomy_database.Manager.Database;

import java.sql.Connection;
import java.sql.Statement;

public class CreateTable {
    public CreateTable(Connection connection) {
        String request = "CREATE TABLE IF NOT EXISTS players_money (uuid varchar(36) PRIMARY KEY NOT NULL, money double, possible_player varchar(36))";
        try (Statement statement = connection.createStatement()) {
            statement.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
