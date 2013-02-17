package com.troidworks.bukkit.multiEnderChest;

import org.bukkit.permissions.Permissible;
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
        ChestContentHolder.getInstance().save();
    }

    @Override
    public void onEnable() {
        instance = this;
        preparePlugin();
        hookPlugin();
    }

    private void preparePlugin() {
        dataFolder = getDataFolder();
        this.saveDefaultConfig();
        // initialize holder
        ChestContentHolder.getInstance();
    }

    private void hookPlugin() {
        this.getServer().getPluginManager().registerEvents(new CompositeListener(this), this);
    }

    public boolean checkPermision(Permissible player) {
        return player.hasPermission("multienderchest.use");
    }

    public boolean isShareExtendedChests() {
        return this.getConfig().getBoolean("share_contents");
    }

    public boolean isWarnOnPermissionDenied() {
        return this.getConfig().getBoolean("warn_on_permission_denied");
    }
}
