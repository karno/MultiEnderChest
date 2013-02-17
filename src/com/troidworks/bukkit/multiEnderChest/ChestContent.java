package com.troidworks.bukkit.multiEnderChest;

import com.troidworks.bukkit.multiEnderChest.stacking.StackRepresentation;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 12:45.
 * Generate: IntelliJ IDEA
 */
public class ChestContent {
    public static ChestContent[] fromRepresentations(StackRepresentation[][] representations) {
        ArrayList<ChestContent> list = new ArrayList<ChestContent>();
        for (StackRepresentation[] reprs : representations) {
            list.add(new ChestContent(reprs));
        }
        return list.toArray(new ChestContent[list.size()]);
    }

    public static StackRepresentation[][] toRepresentations(ChestContent[] contents) {
        StackRepresentation[][] representations = new StackRepresentation[contents.length][];
        for (int i = 0; i < contents.length; i++) {
            representations[i] = contents[i].getRepresentations();
        }
        return representations;
    }

    public static ChestContent[] getEmpties(int channel) {
        ChestContent[] ret = new ChestContent[channel];
        for (int i = 0; i < channel; i++) {
            ret[i] = getEmpty();
        }
        return ret;
    }

    public static ChestContent getEmpty() {
        return new ChestContent();
    }

    private ChestContent() {
        stacks = new ItemStack[0];
    }

    public ChestContent(StackRepresentation[] r) {
        stacks = new ItemStack[r.length];
        for (int i = 0; i < r.length; i++) {
            stacks[i] = r[i] != null ? r[i].getItemStack() : null;
        }
    }

    private ItemStack[] stacks;

    public ItemStack[] getItems() {
        return this.stacks;
    }

    public void setItems(ItemStack[] stack) {
        this.stacks = stack;
    }

    public StackRepresentation[] getRepresentations() {
        return StackRepresentation.fromItemStacks(this.stacks);
    }
}
