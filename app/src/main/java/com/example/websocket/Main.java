package com.example.websocket;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.websocket.databinding.MainBinding;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainBinding binding = MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.websocket.setOnClickListener(v -> startActivity(new Intent(this,MainActivity.class)));
        binding.sse.setOnClickListener(v -> startActivity(new Intent(this,MainActivitySse.class)));
    }
}
