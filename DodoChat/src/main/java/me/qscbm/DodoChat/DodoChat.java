package me.qscbm.DodoChat;

import com.alibaba.fastjson.JSONObject;

import io.github.mcchampions.DodoOpenJava.Event.EventManage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import okhttp3.*;

import io.github.mcchampions.DodoOpenJava.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class DodoChat extends JavaPlugin {
    public static Boolean EnableDodoMessage,EnableServerMessage,EnableJoinMessage,EnableLeaveMessage,EnableChanceMessage;
    public static String Authorization;
    private static DodoChat instance;

    public static String parsePlaceholders(String initialString, Player player, String parm, String sender) {
        String tempString = initialString;

        tempString = tempString.replaceAll("%message%", parm);
        tempString = tempString.replaceAll("%player%", player.getDisplayName());
        tempString = tempString.replaceAll("%server%", parm);
        tempString = tempString.replaceAll("%sender%", sender);

        return tempString;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new event(), this);
        saveConfig();
        getLogger().info("DodoChat已加载");
        Authorization = Utils.Authorization(config.getConfig().getString("settings.botClientId"), config.getConfig().getString("settings.botToken"));
        EnableDodoMessage = config.getConfig().getBoolean("settings.SendDodoMessage.Enable");
        EnableServerMessage = config.getConfig().getBoolean("settings.SendServerMessage.Enable");
        EnableChanceMessage = config.getConfig().getBoolean("settings.ChanceServer.Enable");
        EnableJoinMessage = config.getConfig().getBoolean("settings.JoinMessage.Enable");
        EnableLeaveMessage = config.getConfig().getBoolean("settings.LeaveMessage.Enable");
        if (EnableDodoMessage) {
            EventManage e = new EventManage();
            e.register(new sendMessage(), Authorization);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("DodoChat已卸载");
    }



    public static DodoChat getInstance() {
        return instance;
    }
}
