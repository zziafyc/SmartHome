package com.zhongyong.smarthome.activity;

import android.content.pm.ActivityInfo;
import android.widget.ListView;

import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.model.MaintainApply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

import static com.zhongyong.jamod.apis.ApiClient.call;

public class MaintenanceActivity extends BaseActivity {
    @Bind(R.id.maintenanceLv)
    ListView maintenanceLv;
    BasicAdapter<MaintainApply> mAdapter;
    List<MaintainApply> mList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_maintenance;
    }

    @Override
    protected void initViews() {
        //设置横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<MaintainApply>(this, mList, R.layout.item_maintenance) {
            @Override
            protected void render(ViewHolder holder, MaintainApply item, int position) {
                holder.setText(R.id.item_deviceNum, item.getSerialNum());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                holder.setText(R.id.item_applyTime, format.format(new Date(Long.parseLong(item.getGenTime()))));
                holder.onClick(R.id.item_sure, v -> {


                });
            }
        };
        maintenanceLv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        call(ApiClient.getApis().getMaintenanceApplyList(), new MySubscriber<List<MaintainApply>>() {
            @Override
            public void onError(Throwable e) {
                showToast(getResources().getString(R.string.systemError));
            }

            @Override
            public void onNext(List<MaintainApply> result) {
                if (result != null && result.size() > 0) {
                    mList.clear();
                    mList.addAll(result);
                    mAdapter.notifyDataSetChanged();
                } else {
                    showToast("暂无申请记录！");
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }
}
