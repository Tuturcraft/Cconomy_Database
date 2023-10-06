package fr.fuzeblocks.cconomy_database.Completer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class MoneyCompleter implements TabCompleter {
    private List<String> args = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                this.args.clear();
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    this.args.add("add");
                    this.args.add("set");
                    this.args.add("remove");
                    this.args.add("maj");
                }
                this.args.add("solde");
                this.args.add("menu");
                this.args.add("view");
                return this.args;
            }
            if (args.length == 3) {
                this.args.clear();
                if (player.hasPermission("Cconomy.commandes.money.admin")) {
                    this.args.add("0.5");
                    this.args.add("1.0");
                    this.args.add("5.0");
                    this.args.add("10.0");
                    this.args.add("100.0");
                    this.args.add("500.0");
                    this.args.add("1000.0");
                    this.args.add("5000.0");
                    return this.args;
                }
            }
            return null;
        }
        return null;
    }
}
