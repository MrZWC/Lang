package com.idonans.lang.manager;

import com.idonans.lang.Constants;
import com.idonans.lang.LibLog;
import com.idonans.lang.Singleton;

import java.util.UUID;

/**
 * 不同进程的值不同. 进程重启之后值不会变更。但是如果应用程序卸载了或者清除了数据，值会变更。
 */
public class AppIDManager {

    private static final Singleton<AppIDManager> sInstance =
            new Singleton<AppIDManager>() {
                @Override
                protected AppIDManager create() {
                    return new AppIDManager();
                }
            };

    public static AppIDManager getInstance() {
        return sInstance.get();
    }

    private static final String KEY_APP_ID = Constants.GLOBAL_PREFIX + "app_id";
    private String mAppID;

    private AppIDManager() {
        LibLog.v("init");
        mAppID = StorageManager.getInstance().getOrSetLock(
                StorageManager.NAMESPACE_SETTING,
                KEY_APP_ID,
                UUID.randomUUID().toString());

        LibLog.v("AppID=%s", mAppID);
    }

    public String getAppID() {
        return mAppID;
    }

}
