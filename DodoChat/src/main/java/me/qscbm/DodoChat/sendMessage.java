package me.qscbm.DodoChat;

import com.alibaba.fastjson.JSONObject;
import io.github.mcchampions.DodoOpenJava.Event.Event;
import io.github.mcchampions.DodoOpenJava.Event.Listener;
import org.bukkit.Bukkit;

class sendMessage implements Listener {
    @Override
    public void event(Event e) {
        String jsontext = e.getParam();
        String Message = config.getConfig().getString("settings.SendDodoMessage.format");
        String message = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("messageBody").getString("content");
        String nick = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("member").getString("nickName");
        String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("channelId");
        // 判断消息是否来自指定频道号码
        if(id.equalsIgnoreCase(config.getConfig().getString("settings.SendDodoMessage.channelId"))) {
            Bukkit.getServer().broadcastMessage((DodoChat.parsePlaceholders(Message, null, message, nick)));
        }
    }

}