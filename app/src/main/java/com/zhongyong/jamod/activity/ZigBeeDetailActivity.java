package com.zhongyong.jamod.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fbee.zllctl.Contants;
import com.fbee.zllctl.DeviceInfo;
import com.fbee.zllctl.GatewayInfo;
import com.google.gson.reflect.TypeToken;
import com.zhongyong.jamod.model.OneSwitchModel;
import com.zhongyong.jamod.model.ThreeSwitchModel;
import com.zhongyong.jamod.model.TwoSwitchModel;
import com.zhongyong.jamod.model.ZigBeeSceneModel;
import com.zhongyong.smarthome.MyApplication;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;
import com.zhongyong.smarthome.utils.SnackBarUtils;
import com.zhongyong.speechawake.Constant;

import butterknife.Bind;

/**
 * Created by fyc on 2018/1/2.
 */

public class ZigBeeDetailActivity extends BaseActivity {
    @Bind(R.id.layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.modBusConnect)
    LinearLayout mModBusConnectLayout;
    @Bind(R.id.gatewayTv)
    TextView mGatewayTv;
    @Bind(R.id.connect_show)
    TextView connectStateTv;
    private String flag;
    private ZigBeeSceneModel mSceneModel;
    private String preferenceZigBee;
    private ProgressBar mProgressBar;
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
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String scene = bundle.getString("scene");
                if (scene.equals("library")) {
                    flag = "library";
                    return R.layout.activity_zigbee_detail_library;
                } else if (scene.equals("classroom")) {
                    flag = "classroom";
                    return R.layout.activity_zigbee_detail_classroom;
                } else if (scene.equals("kitchen")) {
                    flag = "kitchen";
                    return R.layout.activity_zigbee_detail_kitchen;
                } else if (scene.equals("laboratory")) {
                    flag = "laboratory";
                    return R.layout.activity_zigbee_detail_laboratory;
                }
            }
        }
        return 0;
    }

    @Override
    protected void initViews() {
        //初始化相关view
        if (flag.equals("library")) {
            setCustomTitle("图书馆");
            preferenceZigBee = "preferenceZigBee_library";
            ZigBeeSceneModel sceneModel = (ZigBeeSceneModel) SharePreferenceUtils.get(ZigBeeDetailActivity.this, preferenceZigBee, new TypeToken<ZigBeeSceneModel>() {
            }.getType());
            if (sceneModel != null) {
                mSceneModel = sceneModel;
            } else {
                mSceneModel = new ZigBeeSceneModel();
            }
            //二路墙面开关
            TwoSwitchModel twoSwitchModel = mSceneModel.getTwoSwitchModel();
            if (twoSwitchModel != null) {
                ImageView imageView1 = (ImageView) findViewById(R.id.item1_Iv);
                if (twoSwitchModel.getLeftState().equals("0")) {
                    imageView1.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView1.setBackgroundResource(R.drawable.switch_open);
                }
                ImageView imageView2 = (ImageView) findViewById(R.id.item2_Iv);
                if (twoSwitchModel.getRightState().equals("0")) {
                    imageView2.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView2.setBackgroundResource(R.drawable.switch_open);
                }
            }
            ImageView imageView1 = (ImageView) findViewById(R.id.item1_Iv);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoSwitchModel.getLeftState().equals("0")) {
                        imageView1.setBackgroundResource(R.drawable.switch_open);
                        twoSwitchModel.setLeftState("1");
                    } else {
                        imageView1.setBackgroundResource(R.drawable.switch_close);
                        twoSwitchModel.setLeftState("0");
                    }
                }
            });
            ImageView imageView2 = (ImageView) findViewById(R.id.item2_Iv);
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoSwitchModel.getRightState().equals("0")) {
                        imageView2.setBackgroundResource(R.drawable.switch_open);
                        twoSwitchModel.setRightState("1");
                    } else {
                        imageView2.setBackgroundResource(R.drawable.switch_close);
                        twoSwitchModel.setRightState("0");
                    }
                }
            });
            //三路墙面开关
            ThreeSwitchModel threeSwitchModel = mSceneModel.getThreeSwitchModel();
            if (threeSwitchModel != null) {
                ImageView imageView3 = (ImageView) findViewById(R.id.item3_Iv);
                if (threeSwitchModel.getLeftState().equals("0")) {
                    imageView3.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView3.setBackgroundResource(R.drawable.switch_open);
                }
                ImageView imageView4 = (ImageView) findViewById(R.id.item4_Iv);
                if (threeSwitchModel.getMiddleState().equals("0")) {
                    imageView4.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView4.setBackgroundResource(R.drawable.switch_open);
                }
                ImageView imageView5 = (ImageView) findViewById(R.id.item5_Iv);
                if (threeSwitchModel.getRightState().equals("0")) {
                    imageView5.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView5.setBackgroundResource(R.drawable.switch_open);
                }
            }
            ImageView imageView3 = (ImageView) findViewById(R.id.item3_Iv);
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (threeSwitchModel.getLeftState().equals("0")) {
                        imageView3.setBackgroundResource(R.drawable.switch_open);
                        threeSwitchModel.setLeftState("1");
                    } else {
                        imageView3.setBackgroundResource(R.drawable.switch_close);
                        threeSwitchModel.setLeftState("0");
                    }
                }
            });
            ImageView imageView4 = (ImageView) findViewById(R.id.item4_Iv);
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (threeSwitchModel.getMiddleState().equals("0")) {
                        imageView4.setBackgroundResource(R.drawable.switch_open);
                        threeSwitchModel.setMiddleState("1");
                    } else {
                        imageView4.setBackgroundResource(R.drawable.switch_close);
                        threeSwitchModel.setMiddleState("0");
                    }
                }
            });
            ImageView imageView5 = (ImageView) findViewById(R.id.item5_Iv);
            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (threeSwitchModel.getRightState().equals("0")) {
                        imageView5.setBackgroundResource(R.drawable.switch_open);
                        threeSwitchModel.setRightState("1");
                    } else {
                        imageView5.setBackgroundResource(R.drawable.switch_close);
                        threeSwitchModel.setRightState("0");
                    }
                }
            });
        } else if (flag.equals("classroom")) {
            setCustomTitle("教室");
            preferenceZigBee = "preferenceZigBee_classroom";
            ZigBeeSceneModel sceneModel = (ZigBeeSceneModel) SharePreferenceUtils.get(ZigBeeDetailActivity.this, preferenceZigBee, new TypeToken<ZigBeeSceneModel>() {
            }.getType());
            if (sceneModel != null) {
                mSceneModel = sceneModel;
            } else {
                mSceneModel = new ZigBeeSceneModel();
            }
            //单面开关
            OneSwitchModel oneSwitchModel = mSceneModel.getOneSwitchModel();
            if (oneSwitchModel != null) {
                ImageView imageView1 = (ImageView) findViewById(R.id.item1_classroom_Iv);
                if (oneSwitchModel.getState().equals("0")) {
                    imageView1.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView1.setBackgroundResource(R.drawable.switch_open);
                }
            }
            ImageView imageView1 = (ImageView) findViewById(R.id.item1_classroom_Iv);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (oneSwitchModel.getState().equals("0")) {
                        imageView1.setBackgroundResource(R.drawable.switch_open);
                        oneSwitchModel.setState("1");
                    } else {
                        imageView1.setBackgroundResource(R.drawable.switch_close);
                        oneSwitchModel.setState("0");
                    }
                }
            });
            //二路墙面开关
            TwoSwitchModel twoSwitchModel = mSceneModel.getTwoSwitchModel();
            if (twoSwitchModel != null) {
                ImageView imageView2 = (ImageView) findViewById(R.id.item2_classroom_Iv);
                if (twoSwitchModel.getLeftState().equals("0")) {
                    imageView2.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView2.setBackgroundResource(R.drawable.switch_open);
                }
                ImageView imageView3 = (ImageView) findViewById(R.id.item3_classroom_Iv);
                if (twoSwitchModel.getRightState().equals("0")) {
                    imageView3.setBackgroundResource(R.drawable.switch_close);
                } else {
                    imageView3.setBackgroundResource(R.drawable.switch_open);
                }
            }
            ImageView imageView2 = (ImageView) findViewById(R.id.item2_classroom_Iv);
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoSwitchModel.getLeftState().equals("0")) {
                        imageView2.setBackgroundResource(R.drawable.switch_open);
                        twoSwitchModel.setLeftState("1");
                    } else {
                        imageView2.setBackgroundResource(R.drawable.switch_close);
                        twoSwitchModel.setLeftState("0");
                    }
                }
            });
            ImageView imageView3 = (ImageView) findViewById(R.id.item3_classroom_Iv);
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoSwitchModel.getRightState().equals("0")) {
                        imageView3.setBackgroundResource(R.drawable.switch_open);
                        twoSwitchModel.setRightState("1");
                    } else {
                        imageView3.setBackgroundResource(R.drawable.switch_close);
                        twoSwitchModel.setRightState("0");
                    }
                }
            });
        } else if (flag.equals("kitchen")) {
            setCustomTitle("厨房");
        } else if (flag.equals("laboratory")) {
            setCustomTitle("实验室");
        }

        //注册网关、连接网关
        registerReceiver(mReceiver, new IntentFilter(Contants.ACTION_CALLBACK));

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
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
                        chooseConnect(boxip, boxsnid, Constant.ZIGBEE_SN_LIBRARY);
                    } else if (flag.equals("classroom")) {
                        chooseConnect(boxip, boxsnid, Constant.ZIGBEE_SN_CLASSROOM);
                    } else if (flag.equals("kitchen")) {
                        chooseConnect(boxip, boxsnid, Constant.ZIGBEE_SN_KITCHEN);
                    } else if (flag.equals("laboratory")) {
                        chooseConnect(boxip, boxsnid, Constant.ZIGBEE_SN_LABORATORY);
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
                            connectStateTv.setText("已连接");
                            SnackBarUtils.with(mCoordinatorLayout)
                                    .setBgColor(SnackBarUtils.COLOR_WARNING)
                                    .setMessage("已连接网关")
                                    .show();
                            MyApplication.mSerial.getDevices();
                        }
                    });
                }
                break;
            }
        }
    }
}

