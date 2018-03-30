package com.zhongyong.jamod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zhongyong.jamod.event.StandardEvent;
import com.zhongyong.jamod.model.EnvironmentFactorModel;
import com.zhongyong.jamod.model.ModBusGateWayModel;
import com.zhongyong.jamod.utils.ModBusTcpClientUtil;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;
import com.zhongyong.smarthome.utils.StringUtils;
import com.zhongyong.speechawake.Constants;

import org.greenrobot.eventbus.Subscribe;

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
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    @Bind(R.id.lv_factor)
    ListView mListView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    List<EnvironmentFactorModel> mList = new ArrayList<>();
    BasicAdapter<EnvironmentFactorModel> mAdapter;
    private Timer mTimer;
    private ModBusTcpClientUtil client;
    private String ip;
    private int uniId;
    private int clickPosition;
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
                    if (mProgressBar != null) {
                        mProgressBar.setVisibility(View.GONE);
                    }
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
        backIv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        setCustomTitle("详细参数");
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
                if (item.getName().contains("PM2.5")) {
                    holder.setText(R.id.item_standard, "不大于" + item.getStandard() + "ug/m3");
                } else if (item.getName().contains("CO2")
                        || item.getName().contains("VOC")) {
                    holder.setText(R.id.item_standard, "不大于" + item.getStandard() + "PPM");
                } else if (item.getName().contains("HCHO")) {
                    holder.setText(R.id.item_standard, "不大于" + item.getStandard() + " PPM");
                } else {
                    holder.setText(R.id.item_standard, item.getStandard());
                }
                if (item.isStandard()) {
                    holder.setTextColor(R.id.item_value, R.color.colorBlowLabel);
                } else {
                    holder.setTextColor(R.id.item_value, R.color.turnOnRed);
                }
                holder.onClick(R.id.item_standard, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(EnvironmentDetectorDetailActivity.this, StandardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putString("standardValue", item.getStandard());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        clickPosition = position;
                    }
                });
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
                if (uniId == 1) {
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
                } else {
                    client = new ModBusTcpClientUtil(ip, uniId, 0, 2);
                    String response = client.sendRequest();
                    Log.e(TAG, "消息已发送！ ");
                    if (!TextUtils.isEmpty(response)) {
                        //对该16进制数据进行处理
                        String response2 = response.replaceAll(" ", "");
                        List<EnvironmentFactorModel> list = dealWithData2(response2);
                        if (list != null) {
                            Message message = new Message();
                            message.what = MESSAGE_WHAT_SEND;
                            message.obj = list;
                            mHandler.sendMessage(message);
                        }
                    }
                }
            }

        }, 1000, 3000);
    }

    @Override
    protected void initListener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private List<EnvironmentFactorModel> dealWithData(String response) {
        List<EnvironmentFactorModel> list = new ArrayList<>();
        //首先得到该字符串从第
        String temp = response.substring(18, response.length());
        if (temp != null && temp.length() > 0) {
            int k = temp.length() / 4;
            for (int i = 0; i < k; i++) {
                int value = Integer.parseInt(temp.substring(i * 4, i * 4 + 4), 16);
                boolean flag = true;  //表示初始值,在正常范围内
                if (i == 0) {
                    if (value > Double.parseDouble(getStandard("PM2.5"))) {
                        flag = false;
                    }
                    list.add(new EnvironmentFactorModel("PM2.5", value + "ug/m3", getStandard("PM2.5"), flag));
                } else if (i == 1) {
                    if (value > Double.parseDouble(getStandard("CO2"))) {
                        flag = false;
                    }
                    list.add(new EnvironmentFactorModel("CO2浓度", value + "PPM", getStandard("CO2"), flag));
                } else if (i == 2) {
                    if (value / 100 > Double.parseDouble(getStandard("VOC"))) {
                        flag = false;
                    }
                    list.add(new EnvironmentFactorModel("VOC浓度", value / 100.0 + "PPM", getStandard("VOC"), flag));
                } else if (i == 3) {
                    if (value / 100 > Double.parseDouble(getStandard("HCHO"))) {
                        flag = false;
                    }
                    list.add(new EnvironmentFactorModel("HCHO浓度", value / 100.0 + "PPM", getStandard("HCHO"), flag));
                } else if (i == 4) {
                    list.add(new EnvironmentFactorModel("温度指数", value / 10.0 + "℃", getStandard("temperature"), flag));
                } else if (i == 5) {
                    list.add(new EnvironmentFactorModel("湿度指数", value / 10.0 + "%", getStandard("humidity"), flag));
                }
            }
            if (list != null && list.size() > 0) {
                return list;

            }
        }
        return null;

    }

    private List<EnvironmentFactorModel> dealWithData2(String response) {
        List<EnvironmentFactorModel> list = new ArrayList<>();
        //首先得到该字符串从第
        String temp1 = response.substring(18, 22);
        String temp2 = response.substring(22, 26);
        if (!StringUtils.isEmpty(temp1) && !StringUtils.isEmpty(temp2)) {
            int value = Integer.parseInt(temp2 + temp1, 16);
            list.add(new EnvironmentFactorModel("智能电表读数", value / 10.0 + "kw.h"));
        }
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    private String getStandard(String name) {
        if (name.equals("PM2.5")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_PM) + "";
        } else if (name.equals("CO2")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_CO2) + "";
        } else if (name.equals("VOC")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_VOC) + "";
        } else if (name.equals("HCHO")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_HCHO) + "";
        } else if (name.equals("temperature")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_TEMP) + "";
        } else if (name.equals("humidity")) {
            return SharePreferenceUtils.get(this, name, Constants.MODEBUS_STANDARD_HUMI) + "";
        }
        return "无";
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void setStandard(StandardEvent event) {
        if (event.getName().equals("standardValue")) {
            mList.get(clickPosition).setStandard(event.getValue());
            mAdapter.notifyDataSetChanged();
        }

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
