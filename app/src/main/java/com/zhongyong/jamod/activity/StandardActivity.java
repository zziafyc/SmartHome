package com.zhongyong.jamod.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongyong.jamod.event.StandardEvent;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

public class StandardActivity extends BaseActivity {
    @Bind(R.id.acn_edt_value)
    EditText valueEdt;
    @Bind(R.id.title_right)
    TextView rightTv;
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    int position;
    String standardValue;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_standard;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", -1);
            standardValue = intent.getStringExtra("standardValue");
        }
        backIv.setVisibility(View.VISIBLE);
        rightTv.setText("保存");
        rightTv.setVisibility(View.VISIBLE);
        valueEdt.setText(standardValue);
        valueEdt.setSelection(standardValue.length());

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
                if (valueEdt.getText().toString().isEmpty()) {
                    showToast("对不起，请填写相关标准值");
                    return;
                } else {
                    if (position == 0) {
                        SharePreferenceUtils.put(StandardActivity.this, "PM2.5", valueEdt.getText().toString());
                    } else if (position == 1) {
                        SharePreferenceUtils.put(StandardActivity.this, "CO2", valueEdt.getText().toString());
                    } else if (position == 2) {
                        SharePreferenceUtils.put(StandardActivity.this, "VOC", valueEdt.getText().toString());
                    } else if (position == 3) {
                        SharePreferenceUtils.put(StandardActivity.this, "HCHO", valueEdt.getText().toString());
                    } else if (position == 4) {
                        SharePreferenceUtils.put(StandardActivity.this, "temperature", valueEdt.getText().toString());
                    } else if (position == 5) {
                        SharePreferenceUtils.put(StandardActivity.this, "humidity", valueEdt.getText().toString());
                    }
                    EventBus.getDefault().post(new StandardEvent("standardValue", valueEdt.getText().toString()));
                    finish();
                }
            }
        });

    }
}
