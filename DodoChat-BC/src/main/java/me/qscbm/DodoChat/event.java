package me.qscbm.DodoChat;

import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class event implements Listener {
    OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    WebSocket mWebSocket;
    @EventHandler
    public void PlayerJoin(PostLoginEvent event) throws IOException {
        String message = config.getConfig().getString("settings.JoinMessage.format");
        String channelId = config.getConfig().getString("settings.JoinMessage.channelId");
        ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), null, null), channelId, true );
    }

    @EventHandler
    public void PlayerQuit(PlayerDisconnectEvent event) throws IOException {
        String message = config.getConfig().getString("settings.LeaveMessage.format");
        String channelId = config.getConfig().getString("settings.LeaveMessage.channelId");
        ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), null, null), channelId, true);
    }

    @EventHandler
    public void PlayerChangeServer(ServerSwitchEvent event) throws IOException {
        String server = event.getPlayer().getServer().getInfo().getName();
        String message = config.getConfig().getString("settings.ChanceServer.format");
        String channelId = config.getConfig().getString("settings.ChanceServer.channelId");
        if(config.getConfig().getStringList("settings.ChanceServer.servers").contains(server)) {
            ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, event.getPlayer(), server, null), channelId, true);
        }
    }

    @EventHandler
    public void PlayerSendMessage(ChatEvent event) throws IOException {
        if (!event.isCommand()) {
            String message = config.getConfig().getString("settings.SendServerMessage.format");
            String channelId = config.getConfig().getString("settings.SendServerMessage.channelId");
            ChannelTextApi.setChannelMessageSend(DodoChat.Authorization, DodoChat.parsePlaceholders(message, (ProxiedPlayer) event.getSender(), event.getMessage(), null), channelId, true);
        };
    }
}
