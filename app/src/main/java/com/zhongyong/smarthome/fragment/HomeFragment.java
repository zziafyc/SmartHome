package com.zhongyong.smarthome.fragment;

import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseFragment;
import com.zhongyong.smarthome.widget.SlideUnlockView;
import com.zhongyong.speechawake.WakeUpActivity;

import butterknife.Bind;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by fyc on 2017/7/26.
 */

public class HomeFragment extends BaseFragment {
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.slideUnlockView)
    SlideUnlockView slideUnlockView;
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
        slideUnlockView.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean unLock) {
                // 如果是true，证明解锁
                if (unLock) {
                    // 启动震动器 100ms
                    vibrator.vibrate(100);
                    // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
                    imageView.setVisibility(View.VISIBLE);
                    go(WakeUpActivity.class);

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // 重置一下滑动解锁的控件
        slideUnlockView.reset();
    }
}
