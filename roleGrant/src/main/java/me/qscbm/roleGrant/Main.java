package me.qscbm.roleGrant;

import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.Event.Event;
import io.github.mcchampions.DodoOpenJava.Event.EventManage;
import io.github.mcchampions.DodoOpenJava.Event.Listener;
import io.github.mcchampions.DodoOpenJava.api.Role;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main implements Listener {
    public static void main(String[] args) {
        EventManage e = new EventManage();
        e.register(new Main(),"按照文档填");
    }

    @Override
    public void event(Event ev) {
        String jsontext = ev.getParam();
        String islandId = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("islanId");
        String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("dodoId");
        String Emoji = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("reactionEmoji").getString("id");
        // 判断事件类型
        if(JSONObject.parseObject(jsontext).getJSONObject("data").getString("eventType") == "3001") {
            // 判断触发事件是否发生在指定messageId上（表情反应的那条消息的ID）
            if (JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("reactionTarget").getString("id") == "消息ID") {
                // 判断是否为指定的表情列表
                switch (Emoji) {
                    //表情ID是否为“48”（就是那个0，方框中里面有个0的那个）
                    case "48":
                        try {
                            Role.setRoleMemberAdd("按照文档填写",islandId,id,"身份组1的ID",true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    //表情ID是否为“49”（就是那个1，方框中里面有个1的那个）
                    case "49":
                        try {
                            Role.setRoleMemberAdd("按照文档填写",islandId,id,"身份组2的ID",true);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    //如果都不是，则不做处理
                    default:
                        break;
                }
            }
        }
    }
}