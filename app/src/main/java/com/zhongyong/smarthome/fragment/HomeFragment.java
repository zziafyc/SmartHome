package com.zhongyong.smarthome.fragment;

import android.widget.RelativeLayout;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.activity.MaintenanceActivity;
import com.zhongyong.smarthome.activity.PushSettingActivity;
import com.zhongyong.smarthome.activity.RepairActivity;
import com.zhongyong.smarthome.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by fyc on 2017/7/26.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.fm_repair_lyt)
    RelativeLayout repairLayout;
    @Bind(R.id.fm_maintenance_lyt)
    RelativeLayout maintenanceLayout;
    @Bind(R.id.fm_setting_lyt)
    RelativeLayout pushSettingLayout;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        repairLayout.setOnClickListener(v -> {
            go(RepairActivity.class);
        });
        maintenanceLayout.setOnClickListener(v->{
            go(MaintenanceActivity.class);
        });
        pushSettingLayout.setOnClickListener(v->{
            go(PushSettingActivity.class);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
