package com.idonans.example.lang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}
