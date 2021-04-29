package io.github.idonans.example.lang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.github.idonans.example.lang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setMinDelayShowTimeMs(1000);
        binding.progressBar.setMinShowTimeMs(2000);
        binding.showProgress.setOnClickListener(v -> binding.progressBar.show());
        binding.hideProgress.setOnClickListener(v -> binding.progressBar.hide());
    }

}
