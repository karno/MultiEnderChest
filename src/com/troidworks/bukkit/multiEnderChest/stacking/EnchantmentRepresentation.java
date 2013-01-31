package com.troidworks.bukkit.multiEnderChest.stacking;

import org.bukkit.enchantments.Enchantment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Karno
 * Date: 13/01/31
 * Time: 13:04
 */
public class EnchantmentRepresentation implements Serializable {
    public static Map<EnchantmentRepresentation, Integer> createMap(
            Map<Enchantment, Integer> enchantments) {
        HashMap<EnchantmentRepresentation, Integer> map = new HashMap<EnchantmentRepresentation, Integer>();
        for (Enchantment enchantment : enchantments.keySet()) {
            map.put(new EnchantmentRepresentation(enchantment), enchantments.get(enchantment));
        }
        return map;
    }

    private final int id;

    private EnchantmentRepresentation(Enchantment enchantment) {
        id = enchantment.getId();
    }

    public Enchantment getEnchantment() {
        return Enchantment.getById(id);
    }
}
