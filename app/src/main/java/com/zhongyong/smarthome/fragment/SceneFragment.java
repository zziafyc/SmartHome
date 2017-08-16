package com.zhongyong.smarthome.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;
import com.mcxtzhang.layoutmanager.swipecard.RenRenCallback;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.activity.NewSceneActivity;
import com.zhongyong.smarthome.base.BaseFragment;
import com.zhongyong.smarthome.base.recyclerview.CommonAdapter;
import com.zhongyong.smarthome.model.Scene;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/7/26.
 */

public class SceneFragment extends BaseFragment {
    @Bind(R.id.as_scenes_rv)
    RecyclerView mRecyclerView;
    CommonAdapter<Scene> mAdapter;
    List<Scene> mList = new ArrayList<>();

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_scene;
    }

    @Override
    protected void initViews() {
        mRecyclerView.setLayoutManager(new OverLayCardLayoutManager());
    }

    @Override
    protected void initAdapter() {
        mAdapter = new CommonAdapter<Scene>(getActivity(), R.layout.item_scene, mList) {
            @Override
            protected void convert(final com.zhongyong.smarthome.base.recyclerview.ViewHolder holder, final Scene bean, int position) {
                holder.setText(R.id.tvName, bean.getName());
                holder.setText(R.id.tvPresent, bean.getPosition() + " /" + mList.size());
                holder.setNetImage(R.id.iv, bean.getUrl());
                if (bean.getStatus() == 0) {
                    holder.setText(R.id.item_status_tv, "一键开启");
                } else {
                    holder.setText(R.id.item_status_tv, "一键关闭");
                }
                holder.setOnClickListener(R.id.item_status_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getStatus() == 0) {
                            bean.setStatus(1);
                            holder.setText(R.id.item_status_tv, "一键关闭");
                        } else {
                            bean.setStatus(0);
                            holder.setText(R.id.item_status_tv, "一键开启");
                        }
                    }
                });
                holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //需要删除item，和重置position
                        mList.remove(bean);
                        resetListRemove(mList, bean.getPosition());
                    }
                });
                holder.setOnClickListener(R.id.item_scene_cv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("sceneName", bean.getName());
                        go(NewSceneActivity.class, bundle);

                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        //初始化配置
        CardConfig.initConfig(getActivity());
        ItemTouchHelper.Callback callback = new RenRenCallback(mRecyclerView, mAdapter, mList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initData() {
        int i = 1;
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/canting.jpg", "餐厅", 1));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/woshi.jpg", "卧室", 0));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/chufang.jpg", "厨房", 1));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/zoulang.jpg", "走廊", 0));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/mainroom.jpg", "主卧", 1));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/xishoujian.jpg", "洗手间", 1));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/worhi.jpg", "A房间", 0));
        mList.add(new Scene(i++, "http://opor07of8.bkt.clouddn.com/room.jpg", "主卧", 1));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

    }

    public void resetListRemove(List<Scene> list, int position) {
        for (int i = 0; i < list.size(); i++) {
            int flag = list.get(i).getPosition();
            if (flag > position) {
                list.get(i).setPosition(flag-1);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
