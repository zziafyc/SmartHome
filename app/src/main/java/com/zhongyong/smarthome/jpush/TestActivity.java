package com.zhongyong.smarthome.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;

import butterknife.Bind;
import cn.jpush.android.api.JPushInterface;

public class TestActivity extends BaseActivity {

    @Bind(R.id.notification_content)
    TextView notificationContentTv;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_notification_content;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            if (bundle != null) {
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
            }
            notificationContentTv.setText("接收到的推送内容为：" + content);
        }

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

}
