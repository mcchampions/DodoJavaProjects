package me.qscbm.DodoChat;

import com.alibaba.fastjson.JSONObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import okhttp3.*;
import okio.ByteString;


class sendMessage extends WebSocketListener {
    DodoChat p;
    public sendMessage(DodoChat DodoChat) {
        p=DodoChat;
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        String jsontext=bytes.utf8();
        String Message = config.getConfig().getString("settings.SendDodoMessage.format");
        String message = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("messageBody").getString("content");
        String nick = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("member").getString("nickName");
        String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("channelId");
        // 判断消息是否来自指定频道号码
        if(id.equalsIgnoreCase(config.getConfig().getString("settings.SendDodoMessage.channelId"))) {
            for (ProxiedPlayer proxy : ProxyServer.getInstance().getPlayers()) {
                proxy.sendMessage(new TextComponent(DodoChat.parsePlaceholders(Message, null, message, nick)));
            }
        }
    }

}