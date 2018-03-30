package com.zhongyong.jamod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.jamod.model.ModBusGateWayModel;
import com.zhongyong.smarthome.App;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.api.HttpResult;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;
import com.zhongyong.smarthome.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.zhongyong.jamod.apis.ApiClient.call;

/**
 * Created by fyc on 2017/12/28.
 */

public class CreateModbusActivity extends BaseActivity {
    @Bind(R.id.title_right)
    TextView rightTv;
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    @Bind(R.id.acn_edt_ip)
    EditText ipEdt;
    @Bind(R.id.acn_edt_id)
    EditText idEdt;
    @Bind(R.id.acn_edt_name)
    EditText nameEdt;
    @Bind(R.id.title_tv_message)
    TextView titleTv;
    List<ModBusGateWayModel> mModBusGateWayModels;
    String preferenceGateWay;
    String sceneId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_gateway_add;
    }

    @Override
    protected void initViews() {
        backIv.setVisibility(View.VISIBLE);
        titleTv.setText("编辑设备信息");
        rightTv.setText("保存");
        rightTv.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                preferenceGateWay = bundle.getString("preferenceGateWay");
                sceneId = bundle.getString("sceneId");
            }
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
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(nameEdt.getText().toString())) {
                    showToast("请输入设备名称");
                    return;

                }
                if (StringUtils.isEmpty(ipEdt.getText().toString())) {
                    showToast("请输入正确的IP地址");
                    return;

                }
                if (StringUtils.isEmpty(idEdt.getText().toString())) {
                    showToast("请输入正确的单元ID");
                    return;

                }
                ModBusGateWayModel newModel = new ModBusGateWayModel(nameEdt.getText().toString(), ipEdt.getText().toString(), Integer.parseInt(idEdt.getText().toString()));
                if (App.getUser() != null) {
                    newModel.setUserId(App.getUser().getUserId());
                    newModel.setSceneId(sceneId);
                    call(ApiClient.getApis().addModBus(newModel), new MySubscriber<HttpResult<Void>>() {
                        @Override
                        public void onError(Throwable e) {
                            showToast(getResources().getString(R.string.systemError));
                        }

                        @Override
                        public void onNext(HttpResult<Void> result) {
                            if (result.getResultCode() == 200) {
                                showToast(result.getMessage());
                                newModel.setFlag(true);
                                EventBus.getDefault().post(newModel);
                                finish();
                            } else {
                                showToast(result.getMessage());
                            }
                        }
                    });

                } else {
                    mModBusGateWayModels = (List<ModBusGateWayModel>) SharePreferenceUtils.get(CreateModbusActivity.this, preferenceGateWay, new TypeToken<List<ModBusGateWayModel>>() {
                    }.getType());
                    if (mModBusGateWayModels == null) {
                        mModBusGateWayModels = new ArrayList<>();
                    }
                    mModBusGateWayModels.add(newModel);
                    SharePreferenceUtils.put(CreateModbusActivity.this, preferenceGateWay, mModBusGateWayModels);
                    EventBus.getDefault().post(newModel);
                    finish();
                }
            }
        });

    }
}
