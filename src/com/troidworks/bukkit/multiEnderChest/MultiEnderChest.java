package com.troidworks.bukkit.multiEnderChest;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 11:26.
 * Generate: IntelliJ IDEA
 */
public final class MultiEnderChest extends JavaPlugin {

    public static File dataFolder;

    private static MultiEnderChest instance;

    public static void writeLog(String log) {
        if (instance != null) {
            instance.getLogger().info(log);
        } else {
            System.out.println(log);
        }
    }

    @Override
    public void onDisable() {
        instance = null;
        this.getLogger().info("Deactivated MultiEnderChest.");
        ChestContentHolder.getInstance().save();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getLogger().info("Activated MultiEnderChest.");
        preparePlugin();
        hookPlugin();
    }

    private void preparePlugin() {
        dataFolder = getDataFolder();
    }

    private void hookPlugin() {
        this.getServer().getPluginManager().registerEvents(new CompositeListener(), this);
    }
}
