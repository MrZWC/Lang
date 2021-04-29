/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.idonans.lang.widget;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import java.lang.ref.WeakReference;

import io.github.idonans.core.thread.Threads;
import io.github.idonans.lang.LangLog;

/**
 * 延迟显示 loading, 并且当 loading 已经显示时，至少持续显示 loading 的时间.
 */
@UiThread
public class ContentLoadingViewHelper {

    private int mMinShowTimeMs = 500;
    private int mMinDelayShowTimeMs = 500;

    @Nullable
    private Runnable mShowRunnable;
    @Nullable
    private Runnable mHideRunnable;
    private long mLastShowTimeMs;

    public void setMinShowTimeMs(int minShowTimeMs) {
        mMinShowTimeMs = minShowTimeMs;
    }

    public int getMinShowTimeMs() {
        return mMinShowTimeMs;
    }

    public void setMinDelayShowTimeMs(int minDelayShowTimeMs) {
        mMinDelayShowTimeMs = minDelayShowTimeMs;
    }

    public int getMinDelayShowTimeMs() {
        return mMinDelayShowTimeMs;
    }

    public void show() {
        if (mShowRunnable != null) {
            // 已经显示了
            LangLog.v("already show");
            return;
        }

        // 中断前一个 hide
        mHideRunnable = null;

        mShowRunnable = new Runnable() {
            @Override
            public void run() {
                if (mShowRunnable != this) {
                    // show runnable 已经失效
                    return;
                }
                mLastShowTimeMs = System.currentTimeMillis();
                ContentLoadingViewHelper.this.onShow();
            }
        };
        Threads.postUi(new WeakRunnable(mShowRunnable), mMinDelayShowTimeMs);
    }

    public void hide() {
        if (mHideRunnable != null) {
            // 已经隐藏了
            LangLog.v("already hide");
            return;
        }

        // 中断前一个 show
        mShowRunnable = null;

        mHideRunnable = new Runnable() {
            @Override
            public void run() {
                if (mHideRunnable != this) {
                    // hide runnable 已经失效
                    return;
                }
                mLastShowTimeMs = 0;
                ContentLoadingViewHelper.this.onHide();
            }
        };

        final long delay;
        if (mLastShowTimeMs > 0) {
            long alreadyShowTimeMs = System.currentTimeMillis() - mLastShowTimeMs;
            if (alreadyShowTimeMs > mMinShowTimeMs) {
                // 已经显示了足够长时间
                delay = 0;
            } else {
                delay = mMinShowTimeMs - alreadyShowTimeMs;
            }
        } else {
            // 尚未显示，立即隐藏
            delay = 0;
        }

        if (delay > 0) {
            Threads.postUi(new WeakRunnable(mHideRunnable), delay);
        } else {
            mHideRunnable.run();
        }
    }

    protected void onShow() {
    }

    protected void onHide() {
    }

    private static final class WeakRunnable implements Runnable {
        private final WeakReference<Runnable> mRef;

        private WeakRunnable(Runnable runnable) {
            mRef = new WeakReference<>(runnable);
        }

        @Override
        public void run() {
            final Runnable r = mRef.get();
            if (r != null) {
                r.run();
            }
        }
    }

}
