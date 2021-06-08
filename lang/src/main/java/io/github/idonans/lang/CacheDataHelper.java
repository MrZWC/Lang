package io.github.idonans.lang;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import io.github.idonans.core.manager.StorageManager;
import io.github.idonans.core.thread.TaskQueue;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CacheDataHelper<T> {

    private final String mCacheKey;

    @Nullable
    private T mData;

    public CacheDataHelper(String cacheKey, Type type) {
        mCacheKey = cacheKey;

        mData = readCacheData(type);
    }

    @Nullable
    public T getData() {
        return mData;
    }

    @Nullable
    private T readCacheData(Type type) {
        try {
            String json = StorageManager.getInstance()
                    .get(StorageManager.NAMESPACE_SETTING, mCacheKey);
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            return new Gson().fromJson(json, type);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setData(@Nullable final T data) {
        mData = data;
        mSaveQueue.enqueue(() -> {
            try {
                String json;
                if (data == null) {
                    json = null;
                } else {
                    json = new Gson().toJson(data);
                }
                StorageManager.getInstance()
                        .set(StorageManager.NAMESPACE_SETTING, mCacheKey, json);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void saveOnly(@Nullable final T cloneData) {
        mSaveQueue.enqueue(new Runnable() {
            @Override
            public void run() {
                try {
                    //noinspection UnnecessaryLocalVariable
                    final T data = cloneData;
                    String json;
                    if (data == null) {
                        json = null;
                    } else {
                        json = new Gson().toJson(data);
                    }
                    StorageManager.getInstance()
                            .set(StorageManager.NAMESPACE_SETTING, mCacheKey, json);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void runSyncTask(@NonNull final Callable<T> callable) {
        mSyncHolder.set(
                Single.just("")
                        .map(input -> new ObjectRef<T>(callable.call()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(input -> {
                            if (input.object != null) {
                                setData(input.object);
                            }
                        }, e -> {
                            // ignore
                        }));
    }

    protected final TaskQueue mSaveQueue = new TaskQueue(1);
    protected final DisposableHolder mSyncHolder = new DisposableHolder();

    private static final class ObjectRef<T> {
        final T object;

        private ObjectRef(T object) {
            this.object = object;
        }
    }

}
