package com.troidworks.bukkit.multiEnderChest;

import com.troidworks.bukkit.multiEnderChest.stacking.StackRepresentation;

import java.io.*;
import java.util.HashMap;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 16:14.
 * Generate: IntelliJ IDEA
 */
public class ChestContentHolder {

    final int ChannelCount = 16;

    public final static String DefaultChest = "";

    private static ChestContentHolder instance;

    public static ChestContentHolder getInstance() {
        if (instance == null) {
            instance = new ChestContentHolder();
        }
        return instance;
    }

    private HashMap<String, ChestContent[]> chestContents = null;

    // private constructor
    private ChestContentHolder() {
        if (MultiEnderChest.dataFolder == null) {
            throw new NullPointerException("data folder is not initialized.");
        }
        if (!MultiEnderChest.dataFolder.exists() && !MultiEnderChest.dataFolder.mkdir()) {
            throw new RuntimeException("data folder could not generate.");
        }
        this.load();
    }

    public ChestContent getContent(String key, int i) {
        if (!chestContents.containsKey(key)) {
            chestContents.put(key, ChestContent.getEmpties(ChannelCount));
        }
        return chestContents.get(key)[i];
    }

    private File getFile() {
        return new File(MultiEnderChest.dataFolder, "contents.dat");
    }

    private void load() {
        File file = getFile();
        try {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                HashMap<String, StackRepresentation[][]> reprs = null;
                if (file.exists()) {
                    fis = new FileInputStream(file);
                    ois = new ObjectInputStream(fis);
                    reprs = (HashMap<String, StackRepresentation[][]>) ois.readObject();
                }
                if (reprs != null) {
                    chestContents = new HashMap<String, ChestContent[]>();
                    for (String key : reprs.keySet()) {
                        MultiEnderChest.writeLog("writing chest: " + key);
                        chestContents.put(key, ChestContent.fromRepresentations(reprs.get(key)));
                    }
                } else {
                    loadDefault();
                }
            } finally {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }
        } catch (Exception ex) {
            System.err.println("An error is occured while reading chest contents:");
            ex.printStackTrace();
        }
    }

    private void loadDefault() {
        MultiEnderChest.writeLog("Default chest loaded.");
        chestContents = new HashMap<String, ChestContent[]>();
    }


    public void save() {
        HashMap<String, StackRepresentation[][]> map = new HashMap<String, StackRepresentation[][]>();
        for (String key : chestContents.keySet()) {
            map.put(key, ChestContent.toRepresentations(chestContents.get(key)));
        }
        File file = getFile();
        try {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = new FileOutputStream(file);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(map);
            } finally {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
        } catch (Exception ex) {
            System.err.println("An error is occured while writing chest contents:");
            ex.printStackTrace();
        }
    }
}
