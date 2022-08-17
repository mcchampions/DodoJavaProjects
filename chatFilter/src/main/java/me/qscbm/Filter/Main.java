package me.qscbm.Filter;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.Utils;
import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import io.github.mcchampions.DodoOpenJava.api.MemberApi;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    static Main p;
    static String wssLo="";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    static WebSocket mWebSocket;

    static String a;

    public static void main(String[] args) throws IOException {
        a = Utils.sendRequest("https://mcchampions.github.io/database.json");
        byte[] b = a.getBytes();
        a = new String(b,"GBK");
        Request requestc = new Request.Builder().url("https://botopen.imdodo.com/api/v1/websocket/connection").addHeader("Content-Type", "application/json").addHeader("Authorization", "该填啥就填啥")
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .build();

        okHttpClient.newCall(requestc).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                wssLo= JSONObject.parseObject(response.body().string()).getJSONObject("data").getString("endpoint");
                //TODO 建立wss链接
                //getLogger().info(wssLo);
                response.close();
                Request request = new Request.Builder()
                        .url(wssLo).build();
                mWebSocket = wss.newWebSocket(request, new WsListenerC(p));//TODO 这里是处理wss发来的数据
            }
        });
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

    private static class WsListenerC extends WebSocketListener {
        Main p;
        public WsListenerC(Main gameToDoDoChat) {
            p=gameToDoDoChat;
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            String jsontext=bytes.utf8();
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

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            mWebSocket.close(1000,"");
        }
    }
}