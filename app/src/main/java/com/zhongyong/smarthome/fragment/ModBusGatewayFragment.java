package com.zhongyong.smarthome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.zhongyong.jamod.CreateModbusIpActivity;
import com.zhongyong.jamod.EnvironmentDetectorDetailActivity;
import com.zhongyong.jamod.ModBusGateWayModel;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseFragment;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.base.ItemMenuDeleteCreator;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/12/28.
 */

public class ModBusGatewayFragment extends BaseFragment {
    @Bind(R.id.gatewaysLv)
    SwipeMenuListView mMenuListView;
    List<ModBusGateWayModel> mList = new ArrayList<>();
    BasicAdapter<ModBusGateWayModel> mAdapter;
    View footer;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_modbus;
    }

    @Override
    protected void initViews() {
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_gateway_add, null);

    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<ModBusGateWayModel>(getActivity(), mList, R.layout.item_gateway) {
            @Override
            protected void render(ViewHolder holder, ModBusGateWayModel item, int position) {
                holder.setText(R.id.itemAdd_name_tv, item.getName());
                holder.setText(R.id.itemAdd_ip_tv, item.getIp());
                holder.setText(R.id.itemAdd_id_tv, item.getUnitId() + "");
            }
        };
        mMenuListView.setAdapter(mAdapter);
        mMenuListView.setMenuCreator(new ItemMenuDeleteCreator(getActivity()));
        mMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mList.remove(position);
                //重新设置偏好设置数据
                SharePreferenceUtils.put(getActivity(), "modBusGateWayModels", mList);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("modBusGateWayModel", mList.get(position));
                go(EnvironmentDetectorDetailActivity.class, bundle);
            }
        });
        if (mMenuListView.getFooterViewsCount() == 0) {
            mMenuListView.addFooterView(footer);
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    go(CreateModbusIpActivity.class);
                }
            });
        }

    }

    @Override
    protected void initData() {
        List<ModBusGateWayModel> mModBusGateWayModels = (List<ModBusGateWayModel>) SharePreferenceUtils.get(getActivity(), "modBusGateWayModels", new TypeToken<List<ModBusGateWayModel>>() {
        }.getType());
        if (mModBusGateWayModels != null && mModBusGateWayModels.size() > 0) {
            mList.addAll(mModBusGateWayModels);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void AddGatewayEvent(ModBusGateWayModel model) {
        if (model != null) {
            mList.add(model);
            mAdapter.notifyDataSetChanged();
        }

    }
}
