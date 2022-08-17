package me.qscbm.DodoChat;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class config {
    public static File configFile;

    static Configuration config;

    public static void loadConfig() throws IOException {
        if (!DodoChat.getInstance().getDataFolder().exists()) {
            DodoChat.getInstance().getDataFolder().mkdir();
        }

        configFile = new File(DodoChat.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            FileOutputStream outputStream = new FileOutputStream(configFile);
            InputStream in = DodoChat.getInstance().getResourceAsStream("config.yml");
            in.transferTo(outputStream);
        }
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(DodoChat.getInstance().getDataFolder(), "config.yml"));
    }

    public static Configuration getConfig() {
        return config;
    }
}