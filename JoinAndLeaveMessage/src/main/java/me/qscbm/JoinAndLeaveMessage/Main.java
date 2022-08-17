package me.qscbm.JoinAndLeaveMessage;

import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.api.ChannelTextApi;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Main {
    static Main p;
    static String wssLo="";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    static WebSocket mWebSocket;
    public static void main(String[] args) {
        Request requestc = new Request.Builder().url("https://botopen.imdodo.com/api/v1/websocket/connection").addHeader("Content-Type", "application/json").addHeader("Authorization", "按照文档填写")
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

    private static class WsListenerC extends WebSocketListener {
        Main p;
        public WsListenerC(Main gameToDoDoChat) {
            p=gameToDoDoChat;
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            String jsontext=bytes.utf8();
            String type = JSONObject.parseObject(jsontext).getJSONObject("data").getString("eventType");
            String id = JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getString("dodoId");
            // 判断事件类型，4001为进群，4002是退群，这里也没有做群号判断，需要的自己做个判断
            if (Objects.equals(type, "4001")) {
                try {
                    ChannelTextApi.setChannelMessageSend("按照文档填", "要发送的频道ID","<@!" + id + ">" + "欢迎您来到某某群", true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(type, "4002")){
                // 判断是不是被踢了，如果是，停止执行后面的代码
                if (JSONObject.parseObject(jsontext).getJSONObject("data").getJSONObject("eventBody").getIntValue("leaveType") == 2) return;
                try {
                    ChannelTextApi.setChannelMessageSend("按照文档填", "要发送的频道ID","有一个人悄悄的退群了哦", true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            mWebSocket.close(1000,"");
        }
    }
}