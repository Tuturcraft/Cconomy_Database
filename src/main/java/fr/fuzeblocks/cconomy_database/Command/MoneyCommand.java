package fr.fuzeblocks.cconomy_database.Command;

import fr.fuzeblocks.cconomy_database.Configuration.Language.LanguageManager;
import fr.fuzeblocks.cconomy_database.Maj.MajFixer;
import fr.fuzeblocks.cconomy_database.Manager.Database.Utils.DatabaseUtils;
import fr.fuzeblocks.cconomy_database.Viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class MoneyCommand extends DatabaseUtils implements CommandExecutor {

    private String key = LanguageManager.getKey();
    private YamlConfiguration config = LanguageManager.getConfig();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You are not a player !");
            return true;
        }

        Player player = (Player) sender;


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
        }else if (subCommand.equalsIgnoreCase("maj")) {
            if (player.hasPermission("Cconomy.commandes.money.admin")) {
                new MajFixer(getConnection(),player);
            }
        } else if (subCommand.equalsIgnoreCase("menu")) {
            new MenuViewer().loadMenu(player);
        } else if (subCommand.equalsIgnoreCase("view")) {
            if (args.length == 2) {
                try {
                   player.sendMessage("The player's money: " + args[1]  + " §ais : " + getMoney(getConnection(), Objects.requireNonNull(Bukkit.getPlayerExact(args[1]).getUniqueId())));
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
                    player.sendMessage("§aYou have well add : " + money + "§a to the player : " + target.getName());
                } else {
                    player.sendMessage("§cYou don't have perms");
                }
            } else if (subCommand.equalsIgnoreCase("set")) {
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    setMoney(getConnection(), targetUUID, money);
                    player.sendMessage("§aYou have well set : " + money + "§a to the player : " + target.getName());
                } else {
                    player.sendMessage("§cYou don't have perms");
                }
            } else if (subCommand.equalsIgnoreCase("remove")) {
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    try {
                        removeMoney(getConnection(), targetUUID, money);
                        player.sendMessage("§aYou have well remove : " + money + "§a to the player : " + target.getName());
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

    }
