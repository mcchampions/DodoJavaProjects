package me.qscbm.JoinAndLeaveMessage;

import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.Event.Event;
import io.github.mcchampions.DodoOpenJava.Event.EventManage;
import io.github.mcchampions.DodoOpenJava.Event.Listener;
import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.Objects;


public class Main implements Listener {
    public static void main(String[] args) {
        EventManage e = new EventManage();
        e.register(new Main(),"按照文档填");
    }

    @Override
    public void event(Event event) {
        String jsontext = event.getParam();
        String type = JSONObject.parseObject(jsontext).getJSONObject("data").getString("eventType");
        String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("dodoId");
        // 判断事件类型，4001为进群，4002是退群，这里也没有做群号判断，需要的自己做个判断
        if (Objects.equals(type, "4001")) {
            try {
                ChannelTextApi.setChannelMessageSend("按照文档填", "要发送的频道ID","<@!" + id + ">" + "欢迎您来到某某群", true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Objects.equals(type, "4002")) {
            // 判断是不是被踢了，如果是，停止执行后面的代码
            if (JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getIntValue("leaveType") == 2)
                return;
            try {
                ChannelTextApi.setChannelMessageSend("按照文档填", "要发送的频道ID", "有一个人悄悄的退群了哦", true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}