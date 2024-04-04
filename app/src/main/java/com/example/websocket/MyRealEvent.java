package com.example.websocket;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSourceListener;

public class MyRealEvent {
    private OkHttpClient client;
    private String url;
    private RealEventSource realEventSource;

    public MyRealEvent(String url) {
        client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.DAYS)
                .writeTimeout(1, TimeUnit.DAYS)
                .readTimeout(1, TimeUnit.DAYS).build();
        this.url = url;
    }

    public void connect(EventSourceListener eventSourceListener) {
        Request request = new Request.Builder().url(url).build();
        realEventSource = new RealEventSource(request, eventSourceListener);
        realEventSource.connect(client);
    }


    public void disconnect() {
        if (realEventSource != null)
            realEventSource.cancel();
    }


}
