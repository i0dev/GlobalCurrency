package com.i0dev.plugin.globalcurrency.object;


import com.i0dev.plugin.globalcurrency.GlobalCurrencyPlugin;
import com.i0dev.plugin.globalcurrency.template.AbstractCommand;
import com.i0dev.plugin.globalcurrency.template.AbstractHook;
import com.i0dev.plugin.globalcurrency.template.AbstractManager;
import com.i0dev.plugin.globalcurrency.utility.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class CorePlugin extends JavaPlugin {

    Set<AbstractManager> managers = new HashSet<>();
    Set<AbstractCommand> commands = new HashSet<>();
    Set<AbstractHook> hooks = new HashSet<>();

    Set<ConfigFile> configs = new HashSet<>();

    @Override
    public void onEnable() {
        startup();
        getLogger().fine("\u001B[32m" + getDescription().getName() + " by: " + getDescription().getAuthors().get(0) + " has been enabled.");
    }

    public abstract void startup();

    public void shutdown() {

    }

    @Override
    public void onDisable() {
        shutdown();
        HandlerList.unregisterAll(GlobalCurrencyPlugin.getPlugin());
        Bukkit.getScheduler().cancelTasks(GlobalCurrencyPlugin.getPlugin());
        commands.forEach(AbstractCommand::deinitialize);
        hooks.forEach(AbstractHook::deinitialize);
        managers.forEach(AbstractManager::deinitialize);
        getLogger().fine("\u001B[31m" + getDescription().getName() + " by: " + getDescription().getAuthors().get(0) + " has been disabled.");
    }


    public void registerManager(AbstractManager manager) {
        if (manager.isLoaded()) manager.deinitialize();
        manager.initialize();
        manager.setLoaded(true);
        if (manager.isListener())
            getServer().getPluginManager().registerEvents(manager, GlobalCurrencyPlugin.getPlugin());
    }


    public void registerHook(AbstractHook hook, String hookName) {
        hook.setName(hookName);
        if (hook.isLoaded()) hook.deinitialize();
        hook.initialize();
        hook.setLoaded(true);
        hooks.add(hook);
        System.out.println("\u001B[32mRegistered Hook: " + hookName);
    }

    public void registerCommand(AbstractCommand command, String commandName) {
        command.setCommandName(commandName);
        command.setPlugin(GlobalCurrencyPlugin.getPlugin());
        if (command.isLoaded()) command.deinitialize();
        command.initialize();
        command.setLoaded(true);

        getCommand(command.getCommandName()).setExecutor(command);
        getCommand(command.getCommandName()).setTabCompleter(command);

        commands.add(command);
    }

    public void registerConfig(String name) {
        ConfigUtil.loadConfig(name);
        configs.add(new ConfigFile(name, YamlConfiguration.loadConfiguration(new File(getDataFolder(), name))));
    }

    public void reloadConfig() {
        configs.forEach(ConfigUtil::reloadConfig);
    }

    public <T> T getHook(Class<T> clazz) {
        return (T) hooks.stream().filter(hook -> hook.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public boolean isHookEnabled(String hookName) {
        return hooks.stream().anyMatch(hook -> hook.getName().equalsIgnoreCase(hookName));
    }

    public boolean isPluginEnabled(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }

    public FileConfiguration cnf() {
        return getCustomConfig("config.yml");
    }

    public FileConfiguration msg() {
        return getCustomConfig("messages.yml");
    }

    public FileConfiguration getCustomConfig(String name) {
        return configs.stream().filter(configFile -> configFile.getFileName().equalsIgnoreCase(name)).findFirst().orElseThrow().getConfig();
    }
}
