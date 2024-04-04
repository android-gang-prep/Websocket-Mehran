package com.example.websocket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

public class MyWebSocket {
    private OkHttpClient client;
    private String url;
    private okhttp3.WebSocket webSocket;

    public MyWebSocket(String url) {
        client = new OkHttpClient();
        this.url = url;
    }

    public void connect(WebSocketListener webSocketListener) {
        Request request = new Request.Builder().url(url).build();
        webSocket = client.newWebSocket(request, webSocketListener);
    }

    public void sendMessage(String message) {
        if (webSocket != null)
            webSocket.send(message);
    }


    public void disconnect() {
        if (webSocket != null)
            webSocket.close(1000,"Disconnect");
    }


}
