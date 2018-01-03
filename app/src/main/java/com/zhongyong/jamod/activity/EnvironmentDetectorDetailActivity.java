package com.zhongyong.jamod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.zhongyong.jamod.model.EnvironmentFactorModel;
import com.zhongyong.jamod.model.ModBusGateWayModel;
import com.zhongyong.jamod.utils.ModBusTcpClientUtil;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * Created by fyc on 2017/12/28.
 */

public class EnvironmentDetectorDetailActivity extends BaseActivity {
    private static final int MESSAGE_WHAT_SEND = 1;
    @Bind(R.id.lv_factor)
    ListView mListView;
    List<EnvironmentFactorModel> mList = new ArrayList<>();
    BasicAdapter<EnvironmentFactorModel> mAdapter;
    private Timer mTimer;
    private ModBusTcpClientUtil client;
    private String ip;
    private int uniId;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case MESSAGE_WHAT_SEND:
                    mList.clear();
                    mList.addAll((Collection<? extends EnvironmentFactorModel>) msg.obj);
                    mAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_environment_detail;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ModBusGateWayModel model = (ModBusGateWayModel) bundle.getSerializable("modBusGateWayModel");
                if (model != null) {
                    ip = model.getIp();
                    uniId = model.getUnitId();
                }
            }
        }

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<EnvironmentFactorModel>(EnvironmentDetectorDetailActivity.this, mList, R.layout.item_factor) {
            @Override
            protected void render(ViewHolder holder, EnvironmentFactorModel item, int position) {
                holder.setText(R.id.item_name, item.getName());
                holder.setText(R.id.item_value, item.getValue());
            }
        };
        mListView.setAdapter(mAdapter);

    }

    @Override
    protected void initData() {
        //定义timer每隔一段时间发送一次
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //这个两个参数需要传递
                client = new ModBusTcpClientUtil(ip, uniId, 0, 6);
                String response = client.sendRequest();
                Log.e(TAG, "消息已发送！ ");
                if (!TextUtils.isEmpty(response)) {
                    //对该16进制数据进行处理
                    String response2 = response.replaceAll(" ", "");
                    List<EnvironmentFactorModel> list = dealWithData(response2);
                    if (list != null) {
                        Message message = new Message();
                        message.what = MESSAGE_WHAT_SEND;
                        message.obj = list;
                        mHandler.sendMessage(message);
                    }
                }
            }

        }, 1000, 5000);

    }

    @Override
    protected void initListener() {

    }

    private List<EnvironmentFactorModel> dealWithData(String response) {
        List<EnvironmentFactorModel> list = new ArrayList<>();
        //首先得到该字符串从第
        String temp = response.substring(18, response.length());
        if (temp != null && temp.length() > 0) {
            int k = temp.length() / 4;
            for (int i = 0; i < k; i++) {
                int value = Integer.parseInt(temp.substring(i * 4, i * 4 + 4), 16);
                if (i == 0) {
                    list.add(new EnvironmentFactorModel("PM2.5", value + ""));
                } else if (i == 1) {
                    list.add(new EnvironmentFactorModel("CO2浓度", value + ""));
                } else if (i == 2) {
                    list.add(new EnvironmentFactorModel("CO浓度", value / 100.0 + ""));
                } else if (i == 3) {
                    list.add(new EnvironmentFactorModel("甲醛HCHO浓度", value / 100.0 + ""));
                } else if (i == 4) {
                    list.add(new EnvironmentFactorModel("温度指数", value / 10.0 + "℃"));
                } else if (i == 5) {
                    list.add(new EnvironmentFactorModel("湿度指数", value / 10.0 + "%"));
                }
            }
            if (list != null && list.size() > 0) {
                return list;

            }
        }
        return null;

    }
}
