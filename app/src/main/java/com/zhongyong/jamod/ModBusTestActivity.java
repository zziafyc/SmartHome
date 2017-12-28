package com.zhongyong.jamod;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * Created by fyc on 2017/12/25.
 */

public class ModBusTestActivity extends BaseActivity {
    private static final int MESSAGE_WHAT_SEND = 1;
    @Bind(R.id.request)
    Button requestBtn;
    @Bind(R.id.response)
    TextView responseTv;
    private Timer mTimer;
    private ModBusTcpClient client;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case MESSAGE_WHAT_SEND:
                    String text = responseTv.getText().toString();
                    responseTv.setText(text + "\n" + msg.obj.toString());
                    break;

            }
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_mod_bus;
    }

    @Override
    protected void initViews() {
        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

    }

    private void sendRequest() {
        //定义timer每隔一段时间发送一次
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //这个两个参数需要传递
                client = new ModBusTcpClient("192.168.0.11", 1, 0, 6);
                String response = client.sendRequest();
                Log.e(TAG, "消息已发送！ ");
                if (!TextUtils.isEmpty(response)) {
                    Message message = new Message();
                    message.what = MESSAGE_WHAT_SEND;
                    message.obj = response;
                    mHandler.sendMessage(message);
                }
            }
        }, 1000, 5000);


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
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (client != null) {
            client.close();
        }
    }
}
