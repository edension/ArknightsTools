package com.nogii.basesdk;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nogii.basesdk.util.ToastUtil;

public class BaseApplication extends Application {

        private static Context mAppContext;

        private static BaseApplication mApplication;

        private static Handler mHandler;

        private static long mMainThreadId;

        @Override
        public void onCreate() {
            super.onCreate();

            mAppContext  = this;

            mApplication = this;

            mHandler = new Handler();

            mMainThreadId = android.os.Process.myTid();

            ToastUtil.setAppContext(mAppContext);
        }

        public static Context getAppContext() {
            return mAppContext;
        }

        public static BaseApplication getApplication() {
            return mApplication;
        }

        public static Handler getHandler() {
            return mHandler;
        }

        public static long getMainThreadId() {
            return mMainThreadId;
        }

        @Override
        public void onTerminate() {
            super.onTerminate();
        }
}

