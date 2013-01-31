package com.troidworks.bukkit.multiEnderChest;

import java.io.File;
import java.io.IOException;

/**
 * User: karno
 * Date: 13/01/30
 * Time: 16:14.
 * Generate: IntelliJ IDEA
 */
public class ChestContentHolder {

    final int ChannelCount = 16;

    private static ChestContentHolder instance;

    public static ChestContentHolder getInstance() {
        if (instance == null) {
            instance = new ChestContentHolder();
        }
        return instance;
    }

    // private constructor
    private ChestContentHolder() {
        if (MultiEnderChest.dataFolder == null) {
            throw new NullPointerException("data folder is not initialized.");
        }
        if (!MultiEnderChest.dataFolder.exists() && !MultiEnderChest.dataFolder.mkdir()) {
            throw new RuntimeException("data folder could not generate.");
        }

        contents = new ChestContent[ChannelCount];
        for (int i = 0; i < ChannelCount; i++) {
            File file = new File(MultiEnderChest.dataFolder,
                    "chest_" + Integer.toHexString(i) + ".dat");
            try {
                contents[i] = ChestContent.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ChestContent[] contents;

    public ChestContent getContent(int i) {
        return contents[i];
    }

    public void save() {
        for (ChestContent content : contents) {
            content.save();
        }
    }

}
