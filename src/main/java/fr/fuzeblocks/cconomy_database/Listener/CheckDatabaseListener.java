package fr.fuzeblocks.cconomy_database.Listener;

import fr.fuzeblocks.cconomy_database.CconomyDatabase;
import fr.fuzeblocks.cconomy_database.Manager.Database.Utils.DatabaseUtils;
import fr.fuzeblocks.cconomy_database.Update.UpdateStatus;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.SQLException;

public class CheckDatabaseListener extends DatabaseUtils implements Listener {
    private CconomyDatabase instance;
    public CheckDatabaseListener(CconomyDatabase instance) {
        this.instance = instance;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    getCr(getConnection(),player.getUniqueId(),instance.getConfig().getDouble("Config.defaultmoney"),player);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }.runTaskAsynchronously(instance);
        if (CconomyDatabase.updateStatus.equals(UpdateStatus.UPDATE) && player.isOp()) {
            TextComponent message = new TextComponent("There is a new update be sure to run /money maj");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/money maj"));
            player.spigot().sendMessage(message);
        }
    }
}
