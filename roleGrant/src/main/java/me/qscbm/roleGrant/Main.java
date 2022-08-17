package me.qscbm.roleGrant;

import com.alibaba.fastjson2.JSONObject;
import io.github.mcchampions.DodoOpenJava.api.Role;
import okhttp3.*;
import okio.ByteString;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main {
    static Main p;
    static String wssLo = "";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static OkHttpClient wss = new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    static WebSocket mWebSocket;

    public static void main(String[] args) {
        //获取ws地址
        Request requestc = new Request.Builder().url("https://botopen.imdodo.com/api/v1/websocket/connection").addHeader("Content-Type", "application/json").addHeader("Authorization", "按照文档填写")
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .build();

        okHttpClient.newCall(requestc).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                wssLo = JSONObject.parseObject(response.body().string()).getJSONObject("data").getString("endpoint");
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
            p = gameToDoDoChat;
        }

        /**
         * 服务端发送数据，客户端处理
         */
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            String jsontext = bytes.utf8();
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

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            mWebSocket.close(1000, "");
        }
    }
}