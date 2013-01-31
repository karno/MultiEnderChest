package com.troidworks.bukkit.multiEnderChest.stacking;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Karno
 * Date: 13/01/31
 * Time: 13:04
 */
public class StackRepresentation implements Serializable {
    private final int type;
    private final int amount;
    private final short durability;
    private final byte data;
    private final HashMap<EnchantmentRepresentation, Integer> enchants;

    public static StackRepresentation[] fromItemStacks(ItemStack[] stacks) {
        StackRepresentation[] r = new StackRepresentation[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) {
                r[i] = StackRepresentation.fromItemStack(stacks[i]);
            } else {
                r[i] = null;
            }
        }
        return r;
    }

    public static StackRepresentation fromItemStack(ItemStack stack) {
        return new StackRepresentation(stack);
    }

    private StackRepresentation(ItemStack item) {
        if (item == null)
            throw new NullPointerException("item can not be null.");
        this.type = item.getTypeId();
        this.amount = item.getAmount();
        this.durability = item.getDurability();
        this.data = item.getData().getData();
        this.enchants = new HashMap<EnchantmentRepresentation, Integer>(
                EnchantmentRepresentation.createMap(item.getEnchantments())
        );
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.type, this.amount,
                this.durability);
        Material material = Material.getMaterial(this.type);
        if (material != null) {
            item.setData(material.getNewData(this.data));
        } else {
            item.setData(new MaterialData(this.type, this.data));
        }

        for (EnchantmentRepresentation enchant : enchants.keySet()) {
            item.addUnsafeEnchantment(enchant.getEnchantment(), enchants.get(enchant));
        }

        return item;
    }
}
