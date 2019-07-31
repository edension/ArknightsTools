package com.nogii.basesdk.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initEvent() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResource();
    }

    protected void releaseResource() {
    }
}
