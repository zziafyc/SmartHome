package com.zhongyong.smarthome.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhongyong.smarthome.widget.LoadingPopup;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    private static final int NON_CODE = -1;
    protected Context mContext;
    protected View rootView;
    protected float mScreenDensity;
    protected int mScreenHeight;
    protected int mScreenWidth;
    protected boolean isVisible;
    Toast mToast;
    public String TAG;
    protected LoadingPopup loadingPop;
    private Handler myHandler = new Handler();
    private Runnable mLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (loadingPop == null) {
                loadingPop = new LoadingPopup(getActivity());
            }
            loadingPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
    };
    private Runnable mHideLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (loadingPop != null) {
                loadingPop.dismiss();
            }
        }
    };

    /**
     * 布局
     */
    protected abstract int getContentViewLayout();

    /**
     * init activity view and bind event
     */
    protected abstract void initViews();

    /**
     * 初始化适配器
     */
    protected abstract void initAdapter();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听器
     */
    protected abstract void initListener();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            getExtraArguments(getArguments());
        }
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        TAG = this.getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewLayout(), container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenDensity = displayMetrics.density;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
        initViews();
        initAdapter();
        initData();
        initListener();
    }

    protected void getExtraArguments(Bundle arguments) {

    }

    //实现fragment的懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void lazyLoad() {

    }

    protected boolean isBindEventBusHere() {
        return false;
    }

    public void showToast(String msg) {
        if (null != msg) {
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mToast.setText(msg);
            }
            mToast.show();
        }
    }

    /**
     * startActivity
     *
     * @param clazz target Activity
     */
    protected void go(Class<? extends Activity> clazz) {
        _goActivity(clazz, null, NON_CODE, false);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz  target Activity
     * @param bundle
     */
    protected void go(Class<? extends Activity> clazz, Bundle bundle) {
        _goActivity(clazz, bundle, NON_CODE, false);
    }

    protected void goForResult(Class<? extends Activity> clazz, int requestCode) {
        _goActivity(clazz, null, requestCode, false);
    }

    protected void goForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
        _goActivity(clazz, bundle, requestCode, false);
    }

    /**
     * Activity 跳转
     *
     * @param clazz  目标activity
     * @param bundle 传递参数
     * @param finish 是否结束当前activity
     */
    private void _goActivity(Class<? extends Activity> clazz, Bundle bundle, int requestCode, boolean finish) {
        if (null == clazz) {
            throw new IllegalArgumentException("you must pass a target activity where to go.");
        }
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (requestCode > NON_CODE) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (finish) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroy() {
        mToast = null;
        super.onDestroy();
    }

    public synchronized void hideLoading() {

        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                myHandler.post(mHideLoadingRunnable);
            }
        });
    }

    public synchronized void showLoading() {
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                myHandler.post(mLoadingRunnable);
            }
        });
    }

    //onDetach方法在rest fragment时，其子fragment并没有rest，重写是为了reset子fragment
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field mChildFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            mChildFragmentManager.setAccessible(true);
            mChildFragmentManager.set(this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mToast != null) {
            mToast.cancel();
        }
    }
}