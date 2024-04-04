package com.example.websocket;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.websocket.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayAdapter<String> adapter;
    MyWebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webSocket = new MyWebSocket("wss://echo.websocket.org");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        binding.list.setAdapter(adapter);
        binding.send.setOnClickListener(v -> {
            if (binding.send.getText().toString().equals("Connect")) {
                binding.send.setEnabled(false);
                webSocket();
            } else {
                if (binding.editText.getText().toString().trim().isEmpty())
                    return;

                sendMessage(binding.editText.getText().toString().trim());
                binding.editText.setText("");

            }
        });
    }

    private void webSocket() {
        binding.progress.setVisibility(View.VISIBLE);
        webSocket.connect(webSocketListener);
    }


    WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
            runOnUiThread(() -> {
                binding.send.setText("Connect");
                binding.send.setEnabled(true);
                binding.progress.setVisibility(View.GONE);
            });
        }

        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
            Log.i("TAG", "onClosing: " + reason);
        }

        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.i("TAG", "onFailure: " + t);
            runOnUiThread(() -> {
                binding.send.setText("Connect");
                binding.send.setEnabled(true);
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            });

        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            Log.i("TAG", "onMessage: " + text);
            runOnUiThread(() -> {
                adapter.add("Receive: " + text);
                binding.list.setSelection(adapter.getCount() - 1);
            });
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.i("TAG", "onMessage: " + bytes.toString());

        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);

            try {
                Log.i("TAG", "onOpen: " + response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                binding.send.setText("Send");
                binding.send.setEnabled(true);
                binding.progress.setVisibility(View.GONE);
            });
        }
    };

    private void sendMessage(String message) {
        adapter.add("Send: " + message);
        binding.list.setSelection(adapter.getCount() - 1);
        webSocket.sendMessage(message);
    }


}