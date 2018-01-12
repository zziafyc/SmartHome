package com.zhongyong.jamod.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbee.zllctl.Contants;
import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.zhongyong.smarthome.MyApplication;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.utils.SnackBarUtils;
import com.zhongyong.smarthome.utils.StringUtils;
import com.zhongyong.speechawake.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2018/1/2.
 */

public class ZigBeeDetailActivity extends BaseActivity {
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    @Bind(R.id.layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.zigbee_device_lv)
    ListView mListView;
    TextView mGatewayTv;
    TextView connectStateTv;
    LinearLayout mModBusConnectLayout;
    View headerView;
    View footerView;
    private String flag;
    List<DeviceInfo> mList = new ArrayList<>();
    BasicAdapter<DeviceInfo> mAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("Type");
            switch (type) {
                case Contants.ACTION_NEW_DEVICE:
                    DeviceInfo deviceInfo = (DeviceInfo) intent.getSerializableExtra("dinfo");
                    int uid = deviceInfo.getUId();
                    int deviceType = deviceInfo.getDeviceId();
                    int profileId = deviceInfo.getProfileId();
                    int ZoneType = deviceInfo.getZoneType();
                    if (isExist(uid) == -1) {
                        mList.add(deviceInfo);
                        mAdapter.notifyDataSetChanged();
                        MyApplication.deviceInfos.add(deviceInfo);
                        if (deviceType == 0x0000 || deviceType == 0x0002 || deviceType == 0x0009 || deviceType == 0x0202 || (deviceType == 0x0200 && profileId == 0x0104)) {
                            //开关设备
                            MyApplication.switchDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0302) {
                            //温湿度传感器
                            MyApplication.thtbDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x0015) {
                            //门磁传感器
                            MyApplication.doorDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x002B) {
                            //气体传感器
                            MyApplication.gasDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && ZoneType == 0x8001) {
                            //一氧化碳传感器
                            MyApplication.cogasDeviceInfos.add(deviceInfo);
                        } else if (deviceType == 0x0402 && (ZoneType == 0x0028 || ZoneType == 0x8000)) {
                            //烟雾探测器
                            MyApplication.smokeDeviceInfos.add(deviceInfo);
                        }
                    } else {
                        MyApplication.deviceInfos.set(isExist(uid), deviceInfo);
                    }
                    break;
                case Contants.ACTION_GET_GATEWAYINFO:
                    MyApplication.gatewayInfo = (GatewayInfo) intent.getSerializableExtra("gatewayInfo");
                    break;

            }
        }
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_zigbee_detail_library;
    }

    @Override
    protected void initViews() {
        headerView = getLayoutInflater().inflate(R.layout.header_zigbee, null);
        footerView = getLayoutInflater().inflate(R.layout.foot_zigbee_device, null);
        connectStateTv = (TextView) headerView.findViewById(R.id.connect_show);
        mGatewayTv = (TextView) headerView.findViewById(R.id.gatewayTv);
        mModBusConnectLayout = (LinearLayout) footerView.findViewById(R.id.modBusConnect);
        //初始化相关view
        backIv.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String scene = bundle.getString("scene");
                if (scene.equals("library")) {
                    flag = "library";
                    setCustomTitle("图书馆");
                } else if (scene.equals("classroom")) {
                    flag = "classroom";
                    setCustomTitle("教室");
                } else if (scene.equals("kitchen")) {
                    flag = "kitchen";
                    setCustomTitle("厨房");
                } else if (scene.equals("laboratory")) {
                    flag = "laboratory";
                    setCustomTitle("实验室");
                }
            }
        }
        //注册网关、连接网关
        registerReceiver(mReceiver, new IntentFilter(Contants.ACTION_CALLBACK));
    }

    private void changeSwitchDeviceState(ViewHolder holder, DeviceInfo item) {
        if (StringUtils.isEmpty(mGatewayTv.getText().toString())) {
            showToast("请先连接网关！");
            return;
        }
        ImageView imageView = holder.getSubView(R.id.item_Iv);
        imageView.setVisibility(View.VISIBLE);
        //开关设备
        if (MyApplication.switchDeviceInfos != null && MyApplication.switchDeviceInfos.size() > 0) {
            for (DeviceInfo deviceInfo : MyApplication.switchDeviceInfos) {
                if (deviceInfo.getUId() == item.getUId()) {
                    if (deviceInfo.getDeviceState() == 0) {
                        MyApplication.mSerial.setDeviceState(deviceInfo, 1);
                        deviceInfo.setDeviceState((byte) 1);
                        imageView.setBackgroundResource(R.drawable.switch_open);
                        holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_switch_normal);
                    } else {
                        MyApplication.mSerial.setDeviceState(deviceInfo, 0);
                        deviceInfo.setDeviceState((byte) 0);
                        imageView.setBackgroundResource(R.drawable.switch_close);
                        holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_switch_pressed);
                    }
                    break;
                }
            }
        }
    }

    private void getSwitchDeviceState(ViewHolder holder, DeviceInfo item) {
        ImageView imageView = holder.getSubView(R.id.item_Iv);
        imageView.setVisibility(View.VISIBLE);
        holder.setText(R.id.item_name, item.getDeviceName());
        //开关设备
        if (item.getDeviceState() == 1) {
            holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_switch_normal);
        } else {
            holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_switch_pressed);
        }
        if (MyApplication.switchDeviceInfos != null && MyApplication.switchDeviceInfos.size() > 0) {
            for (DeviceInfo deviceInfo : MyApplication.switchDeviceInfos) {
                if (deviceInfo.getUId() == item.getUId()) {
                    if (deviceInfo.getDeviceState() == 0) {
                        imageView.setBackgroundResource(R.drawable.switch_close);
                    } else {
                        imageView.setBackgroundResource(R.drawable.switch_open);
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<DeviceInfo>(this, mList, R.layout.item_zigbee_device) {
            @SuppressLint("ResourceAsColor")
            @Override
            protected void render(ViewHolder holder, DeviceInfo item, int position) {
                int deviceType = item.getDeviceId() & 0xFFFF;
                int profileId = item.getProfileId() & 0xFFFF;
                int ZoneType = item.getZoneType() & 0xFFFF;
                if (deviceType == 0x0000 || deviceType == 0x0002 || deviceType == 0x0009 || deviceType == 0x0202 || (deviceType == 0x0200 && profileId == 0x0104)) {
                    holder.getSubView(R.id.item_Iv).setVisibility(View.VISIBLE);
                    TextView descriptionTv = holder.getSubView(R.id.item_description);
                    descriptionTv.setVisibility(View.GONE);
                    getSwitchDeviceState(holder, item);
                    holder.onClick(R.id.item_Iv, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeSwitchDeviceState(holder, item);
                        }
                    });
                } else {
                    //传感器系列
                    holder.getSubView(R.id.item_Iv).setVisibility(View.GONE);
                    TextView descriptionTv = holder.getSubView(R.id.item_description);
                    descriptionTv.setVisibility(View.VISIBLE);
                    descriptionTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("deviceInfo", item);
                            go(RecordActicity.class, bundle);
                        }
                    });
                    if (deviceType == 0x0302) {
                        //温湿度传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x0015) {
                        //门磁传感器
                    } else if (deviceType == 0x0402 && ZoneType == 0x002B) {
                        //可燃气体传感器
                        holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_sensor_hvac_burn_gas_normal);
                        holder.setText(R.id.item_name, item.getDeviceName());
                        holder.getSubView(R.id.item_Iv).setVisibility(View.GONE);
                    } else if (deviceType == 0x0402 && ZoneType == 0x8001) {
                        //一氧化碳传感器
                        holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_sensor_co_normal);
                        holder.setText(R.id.item_name, item.getDeviceName());

                    } else if (deviceType == 0x0402 && (ZoneType == 0x0028 || ZoneType == 0x8000)) {
                        //烟雾探测器
                        holder.setBackgroundImage(R.id.item_type_logo, R.drawable.icon_device_sensor_hvac_smog_normal);
                        holder.setText(R.id.item_name, item.getDeviceName());
                    }
                }
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(headerView);
        mListView.addFooterView(footerView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mModBusConnectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("scene", flag);
                go(ModBusGateWayActivity.class, bundle);
            }
        });
        connectStateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                connect();
            }
        });
    }

    private int isExist(int uid) {
        int size = MyApplication.deviceInfos.size();
        int positon = -1;
        for (int i = 0; i < size; i++) {
            if (uid == MyApplication.deviceInfos.get(i).getUId()) {
                positon = i;
                return positon;
            }
        }
        return positon;
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret = MyApplication.mSerial.connectLANZll();  //本地连接
                if (ret > 0) {
                    final String[] boxip = MyApplication.mSerial.getGatewayIps(ret);
                    final String[] boxsnid = MyApplication.mSerial.getBoxSnids(ret);
                    if (flag.equals("library")) {
                        chooseConnect(boxip, boxsnid, Constants.ZIGBEE_SN_LIBRARY);
                    } else if (flag.equals("classroom")) {
                        chooseConnect(boxip, boxsnid, Constants.ZIGBEE_SN_CLASSROOM);
                    } else if (flag.equals("kitchen")) {
                        chooseConnect(boxip, boxsnid, Constants.ZIGBEE_SN_KITCHEN);
                    } else if (flag.equals("laboratory")) {
                        chooseConnect(boxip, boxsnid, Constants.ZIGBEE_SN_LABORATORY);
                    }
                }
            }
        }).start();
    }

    private void chooseConnect(String[] boxip, String[] boxsnid, String snId) {
        for (int i = 0; i < boxsnid.length; i++) {
            if (boxsnid[i].equals(snId)) {
                int k = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGatewayTv.setText(boxip[k]);
                    }
                });
                int conRet = MyApplication.mSerial.connectLANZllByIp(boxip[i], boxsnid[i]);
                if (conRet > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            connectStateTv.setText("已连接");
                            SnackBarUtils.with(mCoordinatorLayout)
                                    .setBgColor(SnackBarUtils.COLOR_WARNING)
                                    .setMessage("已连接网关")
                                    .show();
                            MyApplication.mSerial.getDevices();
                            MyApplication.mSerial.getGateWayInfo();
                        }
                    });
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        MyApplication.getInstance().exit();
        MyApplication.deviceInfos.clear();
    }
}

