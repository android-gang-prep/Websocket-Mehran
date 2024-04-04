package com.example.websocket;


import static com.google.common.truth.Truth.assertThat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@RunWith(AndroidJUnit4.class)

public class WebSocketTest {
    private MyWebSocket webSocket;
    private final String URL = "wss://echo.websocket.org";

    @Before
    public void create() {
        webSocket = new MyWebSocket(URL);
    }

    @Test
    public void testIsConnect() throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        webSocket.connect(new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                Assert.assertTrue(true);
                signal.countDown();
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                Assert.fail();
                signal.countDown();
            }
        });
        signal.await();
        /*assertThat(signal.getCount()).isEqualTo(0);*/
    }

    @Test
    public void sendAndReceiveMessageTest() throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        String message = "Hi";
        webSocket.connect(new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
                webSocket.send(message);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                if (text.equals(message)) {
                    Assert.assertTrue(true);
                    signal.countDown();
                }

            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                Assert.fail();
                signal.countDown();
            }
        });
        signal.await(5, TimeUnit.SECONDS);
        assertThat(signal.getCount()).isEqualTo(0);
    }

    @After
    public void close() {
        webSocket.disconnect();
    }
}
