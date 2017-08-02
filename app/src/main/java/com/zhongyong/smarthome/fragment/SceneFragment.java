package com.zhongyong.smarthome.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mcxtzhang.layoutmanager.swipecard.CardConfig;
import com.mcxtzhang.layoutmanager.swipecard.OverLayCardLayoutManager;
import com.mcxtzhang.layoutmanager.swipecard.RenRenCallback;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseFragment;
import com.zhongyong.smarthome.base.recyclerview.CommonAdapter;
import com.zhongyong.smarthome.model.SwipeCardBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/7/26.
 */

public class SceneFragment extends BaseFragment {
    @Bind(R.id.as_scenes_rv)
    RecyclerView mRecyclerView;
    CommonAdapter<SwipeCardBean> mAdapter;
    List<SwipeCardBean> mList = new ArrayList<>();

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
        mAdapter = new CommonAdapter<SwipeCardBean>(getActivity(), R.layout.item_scene, mList) {
            @Override
            protected void convert(com.zhongyong.smarthome.base.recyclerview.ViewHolder holder, SwipeCardBean bean, int position) {
                holder.setText(R.id.tvName, bean.getName());
                holder.setText(R.id.tvPrecent, bean.getPostition() + " /" + mList.size());
                holder.setNetImage(R.id.iv, bean.getUrl());
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
        mList.add(new SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_03/2016_03_25/201603259771458878793312_origin.jpg", "张"));
        mList.add(new SwipeCardBean(i++, "http://p14.go007.com/2014_11_02_05/a03541088cce31b8_1.jpg", "旭童"));
        mList.add(new SwipeCardBean(i++, "http://news.k618.cn/tech/201604/W020160407281077548026.jpg", "多种type"));
        mList.add(new SwipeCardBean(i++, "http://www.kejik.com/image/1460343965520.jpg", "多种type"));
        mList.add(new SwipeCardBean(i++, "http://cn.chinadaily.com.cn/img/attachement/jpg/site1/20160318/eca86bd77be61855f1b81c.jpg", "多种type"));
        mList.add(new SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_04/2016_04_12/201604124411460430531500.jpg", "多种type"));
        mList.add(new SwipeCardBean(i++, "http://imgs.ebrun.com/resources/2016_04/2016_04_24/201604244971461460826484_origin.jpeg", "多种type"));
        mList.add(new SwipeCardBean(i++, "http://www.lnmoto.cn/bbs/data/attachment/forum/201408/12/074018gshshia3is1cw3sg.jpg", "多种type"));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {

    }
}
