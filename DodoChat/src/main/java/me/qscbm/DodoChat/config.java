package me.qscbm.DodoChat;

import org.bukkit.configuration.file.FileConfiguration;

public class config {
    public static FileConfiguration getConfig() {
        return DodoChat.getInstance().getConfig();
    }
}