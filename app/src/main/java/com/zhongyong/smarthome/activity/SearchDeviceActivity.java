package com.zhongyong.smarthome.activity;

import android.view.View;
import android.widget.TextView;

import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.widget.RadarView;

import butterknife.Bind;

/**
 * Created by fyc on 2017/8/14.
 */

public class SearchDeviceActivity extends BaseActivity {
    @Bind(R.id.device_search)
    RadarView searchRadar;
    @Bind(R.id.title_right)
    TextView mRightTv;
    public  static int count;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_search_device;
    }

    @Override
    protected void initViews() {
        mRightTv.setVisibility(View.VISIBLE);
        mRightTv.setText("完成");
        searchRadar.start();
        searchRadar.setmImageUrl("http://p5.qhimg.com/t01ba4f7909f15de5fc.jpg");
    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mRightTv.setOnClickListener(view->{
            searchRadar.stop();
            count=8;
            finish();
        });
    }


}
