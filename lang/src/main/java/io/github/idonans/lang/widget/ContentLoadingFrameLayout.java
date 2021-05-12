package io.github.idonans.lang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.github.idonans.lang.R;
import io.github.idonans.lang.util.ViewUtil;

public class ContentLoadingFrameLayout extends FrameLayout {

    public ContentLoadingFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public ContentLoadingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentLoadingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ContentLoadingFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private ContentLoadingViewHelper mContentLoadingViewHelper;

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int minShowTimeMs;
        int minDelayShowTimeMs;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentLoadingFrameLayout, defStyleAttr,
                defStyleRes);
        minShowTimeMs = a.getInteger(R.styleable.ContentLoadingFrameLayout_minShowTimeMs, -1);
        minDelayShowTimeMs = a.getInteger(R.styleable.ContentLoadingFrameLayout_minDelayShowTimeMs, -1);
        a.recycle();

        mContentLoadingViewHelper = new ContentLoadingViewHelper() {
            @Override
            protected void onShow() {
                ViewUtil.setVisibilityIfChanged(ContentLoadingFrameLayout.this, View.VISIBLE);
            }

            @Override
            protected void onHide() {
                ViewUtil.setVisibilityIfChanged(ContentLoadingFrameLayout.this, View.GONE);
            }
        };
        setMinShowTimeMs(minShowTimeMs);
        setMinDelayShowTimeMs(minDelayShowTimeMs);
    }

    public void setMinShowTimeMs(int minShowTimeMs) {
        if (minShowTimeMs >= 0) {
            mContentLoadingViewHelper.setMinShowTimeMs(minShowTimeMs);
        }
    }

    public void setMinDelayShowTimeMs(int minDelayShowTimeMs) {
        if (minDelayShowTimeMs >= 0) {
            mContentLoadingViewHelper.setMinDelayShowTimeMs(minDelayShowTimeMs);
        }
    }

    public void show() {
        mContentLoadingViewHelper.show();
    }

    public void hide() {
        mContentLoadingViewHelper.hide();
    }

}
