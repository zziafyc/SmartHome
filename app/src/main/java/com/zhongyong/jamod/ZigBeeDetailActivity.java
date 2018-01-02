package com.zhongyong.jamod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by fyc on 2018/1/2.
 */

public class ZigBeeDetailActivity extends BaseActivity {
    @Bind(R.id.modBusConnect)
    LinearLayout mModBusConnectLayout;
    private String flag;

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
        return R.layout.activity_zigbee_detail_library;
    }

    @Override
    protected void initViews() {
        if (flag.equals("library")) {
            setCustomTitle("图书馆");
        } else if (flag.equals("classroom")) {
            setCustomTitle("教室");
        } else if (flag.equals("kitchen")) {
            setCustomTitle("厨房");
        } else if (flag.equals("laboratory")) {
            setCustomTitle("实验室");
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
        mModBusConnectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("scene", flag);
                go(ModBusGateWayActivity.class, bundle);
            }
        });

    }
}
