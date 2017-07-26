package com.zhongyong.smarthome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.fragment.DeviceFragment;
import com.zhongyong.smarthome.fragment.HomeFragment;
import com.zhongyong.smarthome.fragment.MonitorFragment;
import com.zhongyong.smarthome.fragment.SceneFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.nb_rg_naviBottom)
    RadioGroup mRadioGroup;
    HomeFragment mHomeFragment;
    MonitorFragment mMonitorFragment;
    DeviceFragment mDeviceFragment;
    SceneFragment mSceneFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    List<Fragment> mFragmentList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        initFragment();

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.nb_rb_homePage:
                        showFragment(mHomeFragment);
                        break;
                    case R.id.nb_rb_monitor:
                        showFragment(mMonitorFragment);
                        break;
                    case R.id.nb_rb_device:
                        showFragment(mDeviceFragment);
                        break;
                    case R.id.nb_rb_scene:
                        showFragment(mSceneFragment);
                        break;
                }
            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        return layout;
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        mHomeFragment = new HomeFragment();
        mMonitorFragment = new MonitorFragment();
        mDeviceFragment = new DeviceFragment();
        mSceneFragment = new SceneFragment();
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mMonitorFragment);
        mFragmentList.add(mDeviceFragment);
        mFragmentList.add(mSceneFragment);
        showFragment(mHomeFragment);


    }

    private void showFragment(Fragment fragment) {
        hideAllFragment();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (fragment.isAdded()) {
            mFragmentTransaction.show(fragment);

        } else {
            mFragmentTransaction.add(R.id.am_fl_fragmentcontainer, fragment);
        }

        mFragmentTransaction.commit();
    }

    //隐藏所有已经add的帧布局
    private void hideAllFragment() {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            if (fragment != null)
                if (fragment.isAdded()) {
                    mFragmentTransaction.hide(fragment);
                }
        }
        mFragmentTransaction.commit();
    }

}
