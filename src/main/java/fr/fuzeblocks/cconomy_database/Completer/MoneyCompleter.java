package fr.fuzeblocks.cconomy_database.Completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class MoneyCompleter implements TabCompleter {
    private List<String> args = new ArrayList<>();
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 1) {
            this.args.clear();
            this.args.add("add");
            this.args.add("set");
            this.args.add("remove");
            this.args.add("solde");
            return this.args;
        }
        if (args.length == 3) {
            this.args.clear();
            this.args.add("0.5");
            this.args.add("1");
            this.args.add("5");
            this.args.add("10");
            this.args.add("100");
            this.args.add("500");
            this.args.add("1000");
            this.args.add("5000");
            return this.args;
        }
        return null;
    }
}
