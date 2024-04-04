package com.example.websocket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.websocket.databinding.ActivityMainBinding;
import com.example.websocket.databinding.ActivityMainSseBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okio.ByteString;

public class MainActivitySse extends AppCompatActivity {


    MyRealEvent realEvent;

    ActivityMainSseBinding binding;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainSseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        realEvent=new MyRealEvent("https://echo.websocket.org/.sse");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        binding.list.setAdapter(adapter);


        binding.connect.setOnClickListener(v -> realEvent());
    }

    private void realEvent() {
        binding.progress.setVisibility(View.VISIBLE);
        realEvent.connect(eventSourceListener);
    }

    EventSourceListener eventSourceListener = new EventSourceListener() {
        @Override
        public void onClosed(@NonNull EventSource eventSource) {
            super.onClosed(eventSource);
            runOnUiThread(() -> binding.progress.setVisibility(View.GONE));

        }

        @Override
        public void onEvent(@NonNull EventSource eventSource, @Nullable String id, @Nullable String type, @NonNull String data) {
            super.onEvent(eventSource, id, type, data);
            runOnUiThread(() -> {
                if (type != null && type.equals("time"))
                    adapter.add("Receive: " + data);
                binding.list.setSelection(adapter.getCount() - 1);
            });
        }

        @Override
        public void onFailure(@NonNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
            super.onFailure(eventSource, t, response);
            runOnUiThread(() -> binding.progress.setVisibility(View.GONE));

        }

        @Override
        public void onOpen(@NonNull EventSource eventSource, @NonNull Response response) {
            super.onOpen(eventSource, response);
            runOnUiThread(() -> binding.progress.setVisibility(View.GONE));

        }
    };


}