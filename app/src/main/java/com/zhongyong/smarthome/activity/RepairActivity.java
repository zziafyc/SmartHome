package com.zhongyong.smarthome.activity;

import android.content.pm.ActivityInfo;
import android.widget.ListView;

import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.model.RepairApply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

import static com.zhongyong.jamod.apis.ApiClient.call;

public class RepairActivity extends BaseActivity {
    @Bind(R.id.repairLv)
    ListView repairLv;
    BasicAdapter<RepairApply> mAdapter;
    List<RepairApply> mList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_repair;
    }

    @Override
    protected void initViews() {
        //设置横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<RepairApply>(this, mList, R.layout.item_repair) {
            @Override
            protected void render(ViewHolder holder, RepairApply item, int position) {
                holder.setText(R.id.item_deviceNum, item.getSerialNum());
                holder.setText(R.id.item_problem, item.getProblem());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                holder.setText(R.id.item_applyTime, format.format(new Date(Long.parseLong(item.getApplyTime()))));
                holder.setText(R.id.item_repairNum, item.getRepairNum());
                if (item.getUrgency() == 0) {
                    holder.setText(R.id.item_urgency, "紧急");
                } else if (item.getUrgency() == 1) {
                    holder.setText(R.id.item_urgency, "重要");
                } else if (item.getUrgency() == 2) {
                    holder.setText(R.id.item_urgency, "一般");
                }
                holder.onClick(R.id.item_sure, v -> {


                });
            }
        };
        repairLv.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        call(ApiClient.getApis().getRepairApplyList(), new MySubscriber<List<RepairApply>>() {
            @Override
            public void onError(Throwable e) {
                showToast(getResources().getString(R.string.systemError));
            }

            @Override
            public void onNext(List<RepairApply> result) {
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
