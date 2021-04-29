package io.github.idonans.example.lang;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.github.idonans.example.lang.databinding.ActivityMainBinding;
import io.github.idonans.lang.util.ViewUtil;
import io.github.idonans.lang.widget.ContentLoadingViewHelper;

public class MainActivity extends AppCompatActivity {

    private ContentLoadingViewHelper mContentLoadingViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContentLoadingViewHelper = new ContentLoadingViewHelper() {
            @Override
            protected void onShow() {
                ViewUtil.setVisibilityIfChanged(binding.progressBar, View.VISIBLE);
            }

            @Override
            protected void onHide() {
                ViewUtil.setVisibilityIfChanged(binding.progressBar, View.GONE);
            }
        };
        mContentLoadingViewHelper.setMinDelayShowTimeMs(1000);
        mContentLoadingViewHelper.setMinShowTimeMs(2000);
        binding.showProgress.setOnClickListener(v -> mContentLoadingViewHelper.show());
        binding.hideProgress.setOnClickListener(v -> mContentLoadingViewHelper.hide());
    }

}
