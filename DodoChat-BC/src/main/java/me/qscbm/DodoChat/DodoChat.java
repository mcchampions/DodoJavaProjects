package me.qscbm.DodoChat;

import com.alibaba.fastjson.JSONObject;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import okhttp3.*;

import io.github.mcchampions.DodoOpenJava.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class DodoChat extends Plugin {
    public static Boolean EnableDodoMessage,EnableServerMessage,EnableJoinMessage,EnableLeaveMessage,EnableChanceMessage;
    public static String Authorization;
    private static DodoChat instance;

    static DodoChat p;
    static String wssLo="";
    static OkHttpClient okHttpClient = new OkHttpClient();
    static OkHttpClient wss=new OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS) //保活心跳
            .build();
    static WebSocket mWebSocket;

    public static String parsePlaceholders(String initialString, ProxiedPlayer player, String parm,String sender) {
        String tempString = initialString;

        tempString = tempString.replaceAll("%message%", parm);
        tempString = tempString.replaceAll("%player%", player.getDisplayName());
        tempString = tempString.replaceAll("%server%", parm);
        tempString = tempString.replaceAll("%sender%", sender);

        return tempString;
    }

    @Override
    public void onEnable() {
        instance = this;
        getProxy().getPluginManager().registerListener(this, new event());
        getLogger().info("DodoChat已加载");
        try {
            config.loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Authorization = Utils.Authorization(config.getConfig().getString("settings.botClientId"), config.getConfig().getString("settings.botToken"));
        EnableDodoMessage = config.getConfig().getBoolean("settings.SendDodoMessage.Enable");
        EnableServerMessage = config.getConfig().getBoolean("settings.SendServerMessage.Enable");
        EnableChanceMessage = config.getConfig().getBoolean("settings.ChanceServer.Enable");
        EnableJoinMessage = config.getConfig().getBoolean("settings.JoinMessage.Enable");
        EnableLeaveMessage = config.getConfig().getBoolean("settings.LeaveMessage.Enable");
        if (EnableDodoMessage) {
            Request requestc = new Request.Builder().url("https://botopen.imdodo.com/api/v1/websocket/connection").addHeader("Content-Type", "application/json").addHeader("Authorization", Authorization)
                    .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                    .build();

            okHttpClient.newCall(requestc).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    wssLo=JSONObject.parseObject(response.body().string()).getJSONObject("data").getString("endpoint");
                    //TODO 建立wss链接
                    //getLogger().info(wssLo);
                    response.close();
                    Request request = new Request.Builder()
                            .url(wssLo).build();
                    mWebSocket = wss.newWebSocket(request, new sendMessage(p));//TODO 这里是处理wss发来的数据
                }
            });
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("DodoChat已卸载");
        if (EnableDodoMessage) {
            mWebSocket.close(1000, "");
        }
    }



    public static DodoChat getInstance() {
        return instance;
    }
}
