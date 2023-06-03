package com.i0dev.plugin.globalcurrency.template;

import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.utility.MsgUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class AbstractCommand implements CommandExecutor, TabExecutor {

    protected GlobalCurrencyPlugin plugin;
    public boolean isLoaded = false;
    public String description = "Do that";

    public FileConfiguration cnf() {
        return plugin.getConfig();
    }

    public FileConfiguration msg() {
        return plugin.getCustomConfig("messages.yml");
    }

    public void initialize() {

    }

    public void deinitialize() {

    }

    private String commandName;

    public abstract void execute(CommandSender sender, String[] args);

    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(this.commandName)) return false;
        execute(sender, args);
        return true;
    }


    // Tab Complete Section

    protected List<String> blank = new ArrayList<>();
    protected List<String> players = null;
    protected List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(this.commandName)) return null;
        return tabComplete(sender, args);
    }

    public List<String> tabCompleteHelper(String arg, Collection<String> options) {
        if (options == null) return null;
        if (arg.equalsIgnoreCase("") || arg.equalsIgnoreCase(" "))
            return new ArrayList<>(options);
        else
            return options.stream().filter(s -> s.toLowerCase().startsWith(arg.toLowerCase())).collect(Collectors.toList());
    }

    // Permission
    protected boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission(GlobalCurrencyPlugin.PERMISSION_PREFIX + "." + permission);
    }


    // Basic Commands

    protected void reload(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "reload")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        MsgUtil.msg(sender, msg().getString("reloadedConfig"));
        plugin.reloadConfig();
    }


    protected void help(CommandSender sender, String[] args) {
        MsgUtil.msg(sender, msg().getString("helpPageTitle"));

        List<String> commands = Arrays.asList(
                "framework help &f- &7Show help",
                "framework reload &f- &7Reload plugin",
                "framework version &f- &7Show plugin version"
        );

        for (String command : commands) {
            MsgUtil.msg(sender, msg().getString("helpPageFormat").replace("{command}", command));
        }

    }

    protected void version(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "version")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }


        PluginDescriptionFile desc = plugin.getDescription();
        String authors = desc.getAuthors().toString();

        MsgUtil.msg(sender, "&9&l" + desc.getName() + " Plugin");
        MsgUtil.msg(sender, "&7Version: &f" + desc.getVersion());
        MsgUtil.msg(sender, "&7Authors: &f" + authors.substring(1, authors.length() - 1));
        MsgUtil.msg(sender, "&7Website: &f" + desc.getWebsite());
    }


}