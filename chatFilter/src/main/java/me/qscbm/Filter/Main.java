package me.qscbm.Filter;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.Event.Event;
import io.github.mcchampions.DodoOpenJava.Event.EventManage;
import io.github.mcchampions.DodoOpenJava.Event.Listener;
import io.github.mcchampions.DodoOpenJava.Utils;
import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import io.github.mcchampions.DodoOpenJava.api.MemberApi;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main implements Listener {
    static String a;

    public static void main(String[] args) throws IOException {
        a = Utils.sendRequest("https://mcchampions.github.io/database.json");
        byte[] b = a.getBytes();
        a = new String(b,"GBK");
        EventManage e = new EventManage();
        e.register(new Main(), "Authorization,请按照文档填写");
    }

    public static boolean Test(String parm) {
        Boolean e = false;
        JSONArray word = JSONObject.parseObject(a).getJSONArray("words");
        for (int c = 0; c < word.size(); c++) {
            if (c == 0) e = false;
            if (parm ==  word.get(c)) e =true;
        }
        return e;
    }

    @Override
    public void event(Event event) {
        String jsontext = event.getParam();
        String type = JSONObject.parseObject(jsontext).getJSONObject("data").getString("eventType");
        // 这里没做频道号和群号的判断和消息类型，如果需要自行添加
        if (type == "4001") {
            String messageid = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("messageId");
            String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("dodoId");
            String message = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("messageBody").getString("content");
            String channelId = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("messageBody").getString("channelId");
            String islandId = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getJSONObject("messageBody").getString("islandId");
            if(Test(message)) {
                try {
                    ChannelTextApi.referencedMessage("按照文档里的填",channelId, "你已触发违规词，处罚禁言10分钟，并撤回违规消息",messageid,true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    ChannelTextApi.setChannelMessageWithdrawWithReason("按照文档里的填",messageid,"违规",true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    MemberApi.setMemberReasonrMuteAdd("按照文档里的填",islandId,id,60*10,"违规",true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}