package com.example.websocket;


import static com.google.common.truth.Truth.assertThat;

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
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

@RunWith(AndroidJUnit4.class)

public class RealEventTest {
    private MyRealEvent myRealEvent;
    private final String URL = "https://echo.websocket.org/.sse";

    @Before
    public void create() {
        myRealEvent = new MyRealEvent(URL);
    }

    @Test
    public void testIsConnect() throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        myRealEvent.connect(new EventSourceListener() {
            @Override
            public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                super.onFailure(eventSource, t, response);
                Assert.fail();
                signal.countDown();
            }

            @Override
            public void onOpen(@NonNull EventSource eventSource, @NonNull Response response) {
                super.onOpen(eventSource, response);
                Assert.assertTrue(true);
                signal.countDown();
            }
        });
        signal.await();
        /*assertThat(signal.getCount()).isEqualTo(0);*/
    }

    @Test
    public void sendAndReceiveMessageTest() throws InterruptedException {
        CountDownLatch signal = new CountDownLatch(1);
        myRealEvent.connect(new EventSourceListener() {
            @Override
            public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
                super.onEvent(eventSource, id, type, data);
                signal.countDown();
            }
        });

        signal.await(10, TimeUnit.SECONDS);
        assertThat(signal.getCount()).isEqualTo(0);
    }

    @After
    public void close() {
        myRealEvent.disconnect();
    }
}
