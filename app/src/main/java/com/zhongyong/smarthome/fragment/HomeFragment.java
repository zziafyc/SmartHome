package com.zhongyong.smarthome.fragment;

import android.os.Vibrator;
import android.widget.ImageView;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseFragment;

import butterknife.Bind;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by fyc on 2017/7/26.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.imageView)
    ImageView imageView;
    private Vibrator vibrator;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews() {
        // 获取系统振动器服务
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        // 设置滑动解锁-解锁的监听
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
