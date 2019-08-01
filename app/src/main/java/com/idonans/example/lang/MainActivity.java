package com.idonans.example.lang;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.idonans.lang.CharLengthInputFilter;
import com.idonans.lang.RegexInputFilter;
import com.idonans.lang.manager.AppIDManager;
import com.idonans.lang.manager.TmpFileManager;
import com.idonans.lang.thread.Threads;

import java.io.File;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File tmpFile = TmpFileManager.getInstance().createNewTmpFileQuietly(null, null);
        Timber.d("tmp file: %s", tmpFile);

        String appID = AppIDManager.getInstance().getAppID();
        Timber.d("appID:%s", appID);

        Threads.mustMainProcess();
        Threads.mustUi();

        EditText edit = findViewById(R.id.edit);
        edit.setFilters(new InputFilter[]{
                new RegexInputFilter("[^\\r\\n]*")
                , new CharLengthInputFilter(10) {
            @Override
            protected void onInputOverflow() {
                Timber.v("onInputOverflow");
            }
        }});
        edit.setText("中文字符中文字符");
    }
}
