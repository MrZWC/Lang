package io.github.idonans.lang;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.github.idonans.core.util.AssetUtil;

public class AssetsCacheDataHelper<T> extends CacheDataHelper<T> {

    private final String mAssetsPath;

    private T mData;

    public AssetsCacheDataHelper(String cacheKey, String assetsPath, Type type) {
        super(cacheKey, type);
        mAssetsPath = assetsPath;

        mData = super.getData();
        if (mData == null) {
            mData = readAssetsData(type);
        }
    }

    public T getData() {
        return mData;
    }

    private T readAssetsData(Type type) {
        try {
            String json = AssetUtil.readAllAsString(mAssetsPath, null, null);
            if (TextUtils.isEmpty(json)) {
                return null;
            }
            return new Gson().fromJson(json, type);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setData(@Nullable T data) {
        mData = data;
        super.setData(data);
    }

}
