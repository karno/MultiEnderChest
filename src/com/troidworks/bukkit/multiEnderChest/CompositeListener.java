package com.troidworks.bukkit.multiEnderChest;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.Wool;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 12:01.
 * Generate: IntelliJ IDEA
 */
@SuppressWarnings("UnusedDeclaration")
public class CompositeListener implements Listener {

    SortedMap<String, Byte> lastChannels = new TreeMap<String, Byte>();
    SortedMap<String, Location> lastLocations = new TreeMap<String, Location>();
    SortedMap<Byte, Inventory> inventoryCache = new TreeMap<Byte, Inventory>();

    public void onPlayerLogout(PlayerQuitEvent e) {
        lastChannels.remove(e.getPlayer().getName());
    }

    // set target chest when interacted to chest
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        // check performed action
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // ensure block is ender chest
        Block block = e.getClickedBlock();
        if (block == null || block.getType() != Material.ENDER_CHEST) return;
        Player player = e.getPlayer();

        // getContent below block of enderchest
        Block belowBlock = block.getRelative(0, -1, 0);

        // check below block is wool
        if (belowBlock == null || belowBlock.getType() != Material.WOOL) {
            // if below block is not wool, do default action
            // remove last channel dict
            lastChannels.remove(player.getName());
            return;
        }

        // avoid to opening chest.
        e.setCancelled(true);

        // getContent wool
        Wool wool = (Wool) belowBlock.getState().getData();
        DyeColor color = wool.getColor();
        byte channel = color.getWoolData();
        Location location = block.getLocation();

        // store temporary data
        lastChannels.put(player.getName(), channel);
        lastLocations.put(player.getName(), location);

        // fetch cache
        Inventory inventory = inventoryCache.get(channel);

        if (inventory == null) {

            // initialze inventory
            // first argument of createInventory could be null.
            inventory = Bukkit.createInventory(null, 27, "Ender Chest #" + channel);

            // load contents
            inventory.setContents(ChestContentHolder
                    .getInstance().getContent(channel).getItems());

            // store into cache
            inventoryCache.put(channel, inventory);
        }

        // open virtual inventory.
        e.getPlayer().openInventory(inventory);
        e.getPlayer().playNote(location, (byte) 1, (byte) 1);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent e) {
        // check inventory is (created) chest
        if (e.getInventory().getType() != InventoryType.CHEST) return;

        // check inventory is extended ender chest
        if (!e.getInventory().getName().startsWith("Ender Chest #")) return;

        String name = e.getPlayer().getName();

        // check last channels table
        if (!lastChannels.containsKey(name)) return;

        // get channel
        byte channel = lastChannels.get(name);

        // save inventory contents
        ChestContentHolder.getInstance().getContent(channel)
                .setItems(e.getInventory().getContents());

        Location location = lastLocations.get(name);
        if (location == null) return;
        ((Player) e.getPlayer()).playNote(location, (byte) 1, (byte) 0);
    }
}
