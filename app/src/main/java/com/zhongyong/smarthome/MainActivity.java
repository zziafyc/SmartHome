package com.zhongyong.smarthome;

import android.view.View;
import android.widget.LinearLayout;

import com.zhongyong.smarthome.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected View getLoadingTargetView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.content);
        return layout;
    }
}
