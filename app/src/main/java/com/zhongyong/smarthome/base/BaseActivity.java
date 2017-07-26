package com.zhongyong.smarthome.base;


import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.widget.LoadingPopup;

import butterknife.ButterKnife;

public abstract class BaseActivity extends BaseAppCompatActivity {
    private LoadingPopup loadingPop;
    private Toast toast = null;
    private Handler myHandler = new Handler();
    private Runnable mLoadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (loadingPop == null) {
                loadingPop = new LoadingPopup(BaseActivity.this);
            }
            loadingPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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

    @Override
    protected boolean isOverridePendingTransition() {
        return false;
    }


    @Override
    protected boolean hasTitleBar() {
        return findViewById(R.id.titleBar) != null;
    }

    @Override
    protected void setCustomTitle(CharSequence title) {
        if (hasTitleBar()) {
            TextView titleView = ButterKnife.findById(this, R.id.title_tv_message);
            if (titleView != null) {
                titleView.setText(title);
                setTitle("");
            }
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onDestroy() {
        toast = null;
        hideLoading();
        //隐藏输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (toast != null) {
            toast.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onNavigateClick() {
        if (hasTitleBar()) {
            RelativeLayout backView = ButterKnife.findById(this, R.id.actionbar_back);
            if (backView != null && backView.getVisibility() == View.VISIBLE) {
                backView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //当点击退出时，如果输入法弹起，隐藏。by wgy
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected TransitionMode getTransitionMode() {
        return TransitionMode.RIGHT;
    }

    public void showToast(String msg) {
        if (null != msg) {
            if (toast == null) {
                toast = Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    public synchronized void hideLoading() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                myHandler.post(mHideLoadingRunnable);
            }
        });
    }

    public synchronized void showLoading() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                myHandler.post(mLoadingRunnable);
            }
        });
    }

}
