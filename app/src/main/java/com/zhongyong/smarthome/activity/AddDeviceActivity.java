package com.zhongyong.smarthome.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.yanzhenjie.recyclerview.swipe.widget.ListItemDecoration;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.recyclerview.ItemViewDelegate;
import com.zhongyong.smarthome.base.recyclerview.MultiItemTypeAdapter;
import com.zhongyong.smarthome.base.recyclerview.ViewHolder;
import com.zhongyong.smarthome.model.Device;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/8/3.
 */

public class AddDeviceActivity extends BaseActivity {
    private static final int TYPE_HEADER = 51;
    private static final int TYPE_CONTENT = 52;
    @Bind(R.id.title_right)
    TextView finishTv;
    @Bind(R.id.fd_devices_sv)
    SwipeMenuRecyclerView recyclerView;
    MultiItemTypeAdapter<Device> mAdapter;
    List<Device> mList = new ArrayList<>();
    List<Device> mChooseList=new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_add_device;
    }

    @Override
    protected void initViews() {
        setCustomTitle("新增设备");
        finishTv.setVisibility(View.VISIBLE);
        finishTv.setText("完成");
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLongPressDragEnabled(true); // 开启拖拽。
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ListItemDecoration(getResources().getColor(R.color.grey_bg)));
    }

    @Override
    protected void initAdapter() {
        mAdapter = new MultiItemTypeAdapter<Device>(this, mList) {
            @Override
            public int getItemViewType(int position) {
                if (mList.get(position).getDeviceType() > 0) {
                    return TYPE_CONTENT;
                } else {
                    return TYPE_HEADER;
                }
            }

            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                super.onViewHolderCreated(holder, itemView);
            }
        };
        mAdapter.addItemViewDelegate(TYPE_HEADER, new StickyDelegate());
        mAdapter.addItemViewDelegate(TYPE_CONTENT, new ContentDelegate());
        recyclerView.setAdapter(mAdapter);
    }

    //头部item布局
    class StickyDelegate implements ItemViewDelegate<Device> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_menu_sticky;
        }

        @Override
        public boolean isForViewType(Device item, int position) {
            return item.getDeviceType() == 0;
        }

        @Override
        public void convert(ViewHolder holder, Device device, int position) {
            if (device.getFlag() == 1) {
                holder.setText(R.id.item_tv_top, "开关设备");
            } else if (device.getFlag() == 2) {
                holder.setText(R.id.item_tv_top, "电视设备");
            } else if (device.getFlag() == 3) {
                holder.setText(R.id.item_tv_top, "空调设备");
            } else {
                holder.setText(R.id.item_tv_top, "其他设备");
            }
        }
    }

    //内容item布局
    class ContentDelegate implements ItemViewDelegate<Device> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_device_choose;
        }

        @Override
        public boolean isForViewType(Device item, int position) {
            return !(item.getDeviceType() == 0);
        }

        @Override
        public void convert(final ViewHolder holder, final Device device, int position) {
            holder.setText(R.id.item_deviceName_tv, device.getDeviceName());
            holder.setText(R.id.item_deviceId_tv, device.getDeviceId());
            if (device.getDeviceType() == 1) {
                holder.setBackgroundRes(R.id.item_pic_iv, R.drawable.switchpic);
            } else if (device.getDeviceType() == 2) {
                holder.setBackgroundRes(R.id.item_pic_iv, R.drawable.televisionpic);
            } else if (device.getDeviceType() == 3) {
                holder.setBackgroundRes(R.id.item_pic_iv, R.drawable.conditionpic);
            }
            if (device.isChoose()) {
                holder.setImageResource(R.id.item_choose_img,R.drawable.xuanzehaoyou_dagou);
            } else {
                holder.setImageResource(R.id.item_choose_img,R.drawable.xuanzehaoyou_kuang);
            }
            holder.setOnClickListener(R.id.item_choose_img, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (device.isChoose()) {
                        mChooseList.remove(device);
                        device.setChoose(false);
                        holder.setBackgroundRes(R.id.item_choose_img, R.drawable.xuanzehaoyou_kuang);
                    } else {
                        mChooseList.add(device);
                        device.setChoose(true);
                        holder.setBackgroundRes(R.id.item_choose_img, R.drawable.xuanzehaoyou_dagou);
                    }
                }
            });
        }
    }


    @Override
    protected void initData() {
        mList.add(new Device(1));
        mList.add(new Device("开关01", "fajdfljadl", 1, 1));
        mList.add(new Device("开关02", "agagagfadf", 0, 1));
        mList.add(new Device("开关03", "hhahdfadfafa", 0, 1));
        mList.add(new Device("开关04", "gggffa", 1, 1));
        mList.add(new Device("开关05", "kksdfad", 1, 1));

        mList.add(new Device(2));
        mList.add(new Device("电视01", "fajdfljadl", 0, 2));
        mList.add(new Device("电视02", "agagagfadf", 1, 2));
        mList.add(new Device("电视03", "hhahdfadfafa", 1, 2));

        mList.add(new Device(3));
        mList.add(new Device("空调01", "fajdfljadl", 0, 3));
        mList.add(new Device("空调02", "agagagfadf", 1, 3));
        mList.add(new Device("空调03", "hhahdfadfafa", 1, 3));
        mList.add(new Device("空调04", "gggffa", 0, 3));
        mList.add(new Device("空调05", "kksdfad", 1, 3));
        mList.add(new Device("空调06", "gggffa", 0, 3));
        mList.add(new Device("空调07", "kksdfad", 1, 3));

        mList.add(new Device(4));


    }

    @Override
    protected void initListener() {
        OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {

                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                if (mList.get(fromPosition).getDeviceType() > 0 && mList.get(toPosition).getDeviceType() > 0) {
                    // Item被拖拽时，交换数据，并更新adapter。
                    Collections.swap(mList, fromPosition, toPosition);
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                }
                return true;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                int position = srcHolder.getAdapterPosition();
                // Item被侧滑删除时，删除数据，并更新adapter。
                mList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        };
        recyclerView.setOnItemMoveListener(mItemMoveListener);// 监听拖拽，更新UI。
        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(mChooseList);
                finish();
            }
        });
    }

}

