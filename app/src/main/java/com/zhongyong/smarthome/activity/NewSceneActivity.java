package com.zhongyong.smarthome.activity;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.adapter.ViewHolder;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.base.BasicAdapter;
import com.zhongyong.smarthome.base.ItemMenuDeleteCreator;
import com.zhongyong.smarthome.model.Device;
import com.zhongyong.smarthome.utils.ImageUtils;
import com.zhongyong.smarthome.widget.AutoPlayViewPager;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by fyc on 2017/8/3.
 */

public class NewSceneActivity extends BaseActivity {
    @Bind(R.id.title_right)
    TextView saveTv;
    @Bind(R.id.sn_devices_lv)
    SwipeMenuListView mListView;
    @Bind(R.id.ft_single_fab)
    FloatingActionButton mFloatingActionButton;
    AutoPlayViewPager mViewPager;
    PagerAdapter mPagerAdapter;
    List<String> images = new ArrayList<>();
    View header;
    BasicAdapter<Device> mAdapter;
    List<Device> mList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_scene_new;
    }

    @Override
    protected void initViews() {
        if(!TextUtils.isEmpty(getIntent().getStringExtra("sceneName"))){
            String title=getIntent().getStringExtra("sceneName");
            setCustomTitle(title);
        }
        saveTv.setVisibility(View.VISIBLE);
        saveTv.setText("保存");
        header = getLayoutInflater().inflate(R.layout.header_device, null);
        mViewPager= (AutoPlayViewPager) header.findViewById(R.id.at_banner_viewpager);
        initBanner();


    }

    private void initBanner() {
        images.add("http://opor07of8.bkt.clouddn.com/woshi.jpg");
        images.add("http://opor07of8.bkt.clouddn.com/room.jpg");
        images.add("http://opor07of8.bkt.clouddn.com/worhi.jpg");
        mViewPager.setPageMargin(30);
        mViewPager.setOffscreenPageLimit(3);
        mPagerAdapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position = position % images.size();
                ImageView view = new ImageView(NewSceneActivity.this);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageUtils.setCornerImageNo(view, images.get(position));
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return images == null ? 0 : Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setPageTransformer(true, new AlphaPageTransformer(new ScaleInTransformer()));
        // 以下两个方法不是必须的，因为有默认值
        mViewPager.setDirection(AutoPlayViewPager.Direction.LEFT);// 设置播放方向
        mViewPager.start(); // 开始轮播
    }

    @Override
    protected void initAdapter() {
        mAdapter = new BasicAdapter<Device>(this, mList, R.layout.item_device) {
            @Override
            protected void render(ViewHolder holder, Device device, int position) {
                holder.setText(R.id.itemAdd_ID_tv, device.getDeviceId());
                holder.setText(R.id.itemAdd_nickName_tv, device.getDeviceName());
                if (device.getDeviceType() == 1) {
                    holder.setImageResources(R.id.itemAdd_pic_iv, R.drawable.switchpic);
                } else if (device.getDeviceType() == 2) {
                    holder.setImageResources(R.id.itemAdd_pic_iv, R.drawable.televisionpic);
                } else if (device.getDeviceType() == 3) {
                    holder.setImageResources(R.id.itemAdd_pic_iv, R.drawable.conditionpic);
                }
            }

        };
        mListView.setAdapter(mAdapter);
        mListView.setMenuCreator(new ItemMenuDeleteCreator(this));
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

        mListView.addHeaderView(header);
        //设置mFloatingActionButton依附
        mFloatingActionButton.attachToListView(mListView);
        mFloatingActionButton.show();
    }

    @Override
    protected void initData() {
        mList.add(new Device("开关01", "fajdfljadl", 1, 1));
        mList.add(new Device("电视01", "fajdfljadl", 0, 2));
        mList.add(new Device("空调01", "fajdfljadl", 0, 3));
        mList.add(new Device("空调02", "agagagfadf", 1, 3));
        mList.add(new Device("开关01", "fajdfljadl", 1, 1));
        mList.add(new Device("电视01", "fajdfljadl", 0, 2));
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initListener() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go(AddDeviceActivity.class);
            }
        });
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(List<Device> event){
        mList.addAll(event);
        mAdapter.notifyDataSetChanged();

    }
}
