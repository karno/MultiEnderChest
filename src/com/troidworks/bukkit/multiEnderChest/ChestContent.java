package com.troidworks.bukkit.multiEnderChest;

import com.troidworks.bukkit.multiEnderChest.stacking.StackRepresentation;
import org.bukkit.inventory.ItemStack;

import java.io.*;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 12:45.
 * Generate: IntelliJ IDEA
 */
public class ChestContent {
    public static ChestContent load(File file) throws IOException {
        if (!file.exists() && !file.createNewFile()) {
            throw new RuntimeException("chest file creation failed.");
        }
        return new ChestContent(file);
    }

    private File file;

    private ItemStack[] stacks;

    private ChestContent(File file) {
        this.file = file;
        // load chest contents
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                StackRepresentation[] representations = (StackRepresentation[]) ois.readObject();
                stacks = new ItemStack[representations.length];
                for (int i = 0; i < representations.length; i++) {
                    stacks[i] = representations[i] == null ? null :
                            representations[i].getItemStack();
                }
            } finally {
                ois.close();
                fis.close();
            }
        } catch (Exception ex) {
            MultiEnderChest.writeLog("exception while reading " + file.getName());
            ex.printStackTrace();
            stacks = new ItemStack[0];
        }
    }

    public ItemStack[] getItems() {
        return this.stacks;
    }

    public void setItems(ItemStack[] stack) {
        this.stacks = stack;
    }

    public void save() {
        StackRepresentation[] representations = StackRepresentation.fromItemStacks(this.stacks);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            try {
                oos.writeObject(representations);
            } finally {
                oos.close();
                fos.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
