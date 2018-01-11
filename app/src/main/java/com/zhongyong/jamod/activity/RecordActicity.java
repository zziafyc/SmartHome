package com.zhongyong.jamod.activity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fbee.zllctl.Contants;
import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.Record;
import com.google.gson.reflect.TypeToken;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2018/1/11.
 */

public class RecordActicity extends BaseActivity {
    @Bind(R.id.actionbar_back)
    RelativeLayout backRv;
    @Bind(R.id.title_tv_message)
    TextView titleTv;
    @Bind(R.id.title_right)
    TextView rightTv;
    @Bind(R.id.recordLv)
    ListView mListView;
    BasicAdapter<Record> mAdapter;
    List<Record> mRecordList = new ArrayList<>();
    DeviceInfo deviceInfo;
    private Vibrator vibrator;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_record;
    }

    @Override
    protected void initViews() {
        titleTv.setText("报警记录");
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setText("删除");
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        deviceInfo = (DeviceInfo) getIntent().getSerializableExtra("deviceInfo");
        registerReceiver(mReceiver, new IntentFilter(Contants.ACTION_ArriveReport));

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<Record>(this, mRecordList, R.layout.item_record) {
            @Override
            protected void render(ViewHolder holder, Record item, int position) {
                int deviceType = item.getDeviceType();
                int ZoneType = item.getZoneType();
                if (item.getState().equals("0")) {
                    TextView state = holder.getSubView(R.id.deviceInfo_state_Tv);
                    state.setTextColor(getResources().getColor(R.color.colorBlue));
                    if (deviceType == 0x0302) {
                        //温湿度传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x0015) {
                        //门磁传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x002B) {
                        //可燃气体传感器
                        state.setText(item.getName() + "-" + "没有可燃气体泄露");

                    } else if (deviceType == 0x0402 && ZoneType == 0x8001) {
                        //一氧化碳传感器
                        state.setText(item.getName() + "-" + "没有CO气体泄露");
                    } else if (deviceType == 0x0402 && (ZoneType == 0x0028 || ZoneType == 0x8000)) {
                        //烟雾探测器
                        state.setText(item.getName() + "-" + "没有烟雾");
                    }
                } else {
                    TextView state = holder.getSubView(R.id.deviceInfo_state_Tv);
                    state.setTextColor(getResources().getColor(R.color.turnOnRed));
                    if (deviceType == 0x0302) {
                        //温湿度传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x0015) {
                        //门磁传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x002B) {
                        //可燃气体传感器
                        state.setText(item.getName() + "-" + "可燃气体泄露");

                    } else if (deviceType == 0x0402 && ZoneType == 0x8001) {
                        //一氧化碳传感器
                        state.setText(item.getName() + "-" + "CO气体泄露");
                    } else if (deviceType == 0x0402 && (ZoneType == 0x0028 || ZoneType == 0x8000)) {
                        //烟雾探测器
                        state.setText(item.getName() + "-" + "有烟雾");
                    }
                }
                holder.setText(R.id.deviceInfo_time_Tv, item.getTime());
            }
        };
        mListView.setAdapter(mAdapter);
        List<Record> recordSp = (List<Record>) SharePreferenceUtils.get(this, "record" + deviceInfo.getUId(), new TypeToken<List<Record>>() {
        }.getType());
        if (recordSp != null && recordSp.size() > 0) {
            mRecordList.clear();
            mRecordList.addAll(recordSp);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        backRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordList.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DeviceInfo data_deviceInfo = (DeviceInfo) intent.getSerializableExtra("updateData");
            if (deviceInfo.getUId() != data_deviceInfo.getUId()) {
                return;
            }
            vibrator.vibrate(500);
            int deviceType = deviceInfo.getDeviceId() & 0xFFFF;
            int ZoneType = deviceInfo.getZoneType() & 0xFFFF;
            Record record = new Record();
            record.setName(deviceInfo.getDeviceName());
            record.setDeviceType(deviceType);
            record.setZoneType(ZoneType);
            if (deviceType == 0x0402 && ZoneType == 0x002B) {
                if (data_deviceInfo.getSensordata() == 2) {
                    record.setState("1");
                } else {
                    record.setState("0");
                }
            } else {
                if (data_deviceInfo.getSensordata() == 1) {
                    record.setState("1");
                } else {
                    record.setState("0");
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String time = formatter.format(curDate);
            record.setTime(time);
            record.setUId(data_deviceInfo.getUId());
            mRecordList.add(record);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (mRecordList != null) {
            SharePreferenceUtils.put(this, "record" + deviceInfo.getUId(), mRecordList);
        }
    }
}
