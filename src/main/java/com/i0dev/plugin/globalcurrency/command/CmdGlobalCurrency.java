package com.i0dev.plugin.globalcurrency.command;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.manager.InventoryManager;
import com.i0dev.plugin.globalcurrency.manager.LogManager;
import com.i0dev.plugin.globalcurrency.manager.SQLManager;
import com.i0dev.plugin.globalcurrency.object.Pair;
import com.i0dev.plugin.globalcurrency.template.AbstractCommand;
import com.i0dev.plugin.globalcurrency.utility.MsgUtil;
import com.i0dev.plugin.globalcurrency.utility.Utility;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdGlobalCurrency extends AbstractCommand {

    @Getter
    public static final CmdGlobalCurrency instance = new CmdGlobalCurrency();

    LogManager logger;

    @Override
    public void initialize() {
        logger = LogManager.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender, args);
        } else {
            switch (args[0].toLowerCase()) {
                case "reload":
                    reload(sender, args);
                    break;
                case "version":
                case "ver":
                    version(sender, args);
                    break;
                case "add":
                    add(sender, args);
                    break;
                case "remove":
                    remove(sender, args);
                    break;
                case "set":
                    set(sender, args);
                    break;
                case "shop":
                    shop(sender, args);
                    break;
                case "bal":
                case "balance":
                    balance(sender, args);
                    break;
                case "pay":
                    pay(sender, args);
                    break;
                case "checkbal":
                    checkBal(sender, args);
                    break;
                case "help":
                default:
                    help(sender, args);
            }
        }
    }

    public void add(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "add")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (args.length < 3) {
            help(sender, args);
            return;
        }
        Player player = MsgUtil.getPlayer(args[1]);
        if (player == null) {
            MsgUtil.msg(sender, msg().getString("cantFindPlayer"), new Pair<>("{player}", args[1]));
            return;
        }
        Integer amount = Utility.getInt(args[2]);
        if (amount == null) {
            MsgUtil.msg(sender, msg().getString("invalidNumber"));
            return;
        }

        SQLManager.getInstance().addAmount(player.getUniqueId(), amount);

        MsgUtil.msg(sender, msg().getString("addedToPlayer"),
                new Pair<>("{player}", player.getName()),
                new Pair<>("{amount}", amount + "")
        );


        logger.log(sender.getName() + " has added " + amount + " currency to " + player.getName());
    }

    public void remove(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "remove")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (args.length < 3) {
            help(sender, args);
            return;
        }
        Player player = MsgUtil.getPlayer(args[1]);
        if (player == null) {
            MsgUtil.msg(sender, msg().getString("cantFindPlayer"), new Pair<>("{player}", args[1]));
            return;
        }
        Integer amount = Utility.getInt(args[2]);
        if (amount == null) {
            MsgUtil.msg(sender, msg().getString("invalidNumber"));
            return;
        }

        SQLManager.getInstance().removeAmount(player.getUniqueId(), amount);

        MsgUtil.msg(sender, msg().getString("removedFromPlayer"),
                new Pair<>("{player}", player.getName()),
                new Pair<>("{amount}", amount + "")
        );

        logger.log(sender.getName() + " has removed " + amount + " currency to " + player.getName());

    }

    public void set(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "set")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (args.length < 3) {
            help(sender, args);
            return;
        }
        Player player = MsgUtil.getPlayer(args[1]);
        if (player == null) {
            MsgUtil.msg(sender, msg().getString("cantFindPlayer"), new Pair<>("{player}", args[1]));
            return;
        }
        Integer amount = Utility.getInt(args[2]);
        if (amount == null) {
            MsgUtil.msg(sender, msg().getString("invalidNumber"));
            return;
        }

        SQLManager.getInstance().setAmount(player.getUniqueId(), amount);

        MsgUtil.msg(sender, msg().getString("setPlayer"),
                new Pair<>("{player}", player.getName()),
                new Pair<>("{amount}", amount + "")
        );

        logger.log(sender.getName() + " has set " + player.getName() + " balance to " + amount);
    }

    public void shop(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "shop")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (!(sender instanceof Player)) {
            MsgUtil.msg(sender, msg().getString("cantUseAsConsole"));
            return;
        }

        ((Player) sender).openInventory(InventoryManager.getInstance().getShopInventory());
    }

    public void balance(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "balance")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }

        if (!(sender instanceof Player)) {
            MsgUtil.msg(sender, msg().getString("cantUseAsConsole"));
            return;
        }

        long balance = SQLManager.getInstance().getAmount(((Player) sender).getUniqueId());

        MsgUtil.msg(sender, msg().getString("yourBalance"), new Pair<>("{amount}", balance + ""));
    }

    //todo
    public void pay(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "pay")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
    }

    public void checkBal(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "checkbal")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (args.length < 2) {
            help(sender, args);
            return;
        }
        Player player = MsgUtil.getPlayer(args[1]);
        if (player == null) {
            MsgUtil.msg(sender, msg().getString("cantFindPlayer"), new Pair<>("{player}", args[1]));
            return;
        }

        long balance = SQLManager.getInstance().getAmount(player.getUniqueId());

        MsgUtil.msg(sender, msg().getString("playersBalance"),
                new Pair<>("{player}", player.getName()),
                new Pair<>("{amount}", balance + "")
        );
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return tabCompleteHelper(args[0], Arrays.asList("reload", "help", "version", "add", "remove", "set", "shop", "balance", "pay", "checkBal"));
        if (args[0].equalsIgnoreCase("add")) {
            if (!hasPermission(sender, "add")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
            if (args.length == 3)
                return tabCompleteHelper(args[2], numbers);
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (!hasPermission(sender, "remove")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
            if (args.length == 3)
                return tabCompleteHelper(args[2], numbers);
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (!hasPermission(sender, "set")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
            if (args.length == 3)
                return tabCompleteHelper(args[2], numbers);
        }
        if (args[0].equalsIgnoreCase("pay")) {
            if (!hasPermission(sender, "pay")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
            if (args.length == 3)
                return tabCompleteHelper(args[2], numbers);
        }
        if (args[0].equalsIgnoreCase("checkbal")) {
            if (!hasPermission(sender, "checkbal")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
        }
        return blank;
    }
}
