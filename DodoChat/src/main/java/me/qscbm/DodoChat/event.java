package me.qscbm.DodoChat;

import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import okhttp3.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class event implements Listener {
    OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    WebSocket mWebSocket;
    @EventHandler
    public void PlayerJoin(PlayerLoginEvent event) throws IOException {
        String message = config.getConfig().getString("settings.JoinMessage.format");
        String channelId = config.getConfig().getString("settings.JoinMessage.channelId");
        ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), null, null), channelId, true );
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) throws IOException {
        String message = config.getConfig().getString("settings.LeaveMessage.format");
        String channelId = config.getConfig().getString("settings.LeaveMessage.channelId");
        ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), null, null), channelId, true);
    }

    @EventHandler
    public void PlayerSendMessage(AsyncPlayerChatEvent event) throws IOException {
        String message = config.getConfig().getString("settings.SendServerMessage.format");
        String channelId = config.getConfig().getString("settings.SendServerMessage.channelId");
        ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), event.getMessage(), null), channelId, true);
    }
}
