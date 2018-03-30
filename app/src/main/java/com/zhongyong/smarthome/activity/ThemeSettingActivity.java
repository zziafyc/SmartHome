package com.zhongyong.smarthome.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zhongyong.smarthome.App;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.model.ColorManager;

import butterknife.Bind;

public class ThemeSettingActivity extends BaseActivity {
    @Bind(R.id.cab_titleBack_iv)
    ImageView backIv;
    private final int[] layouts = {R.id.skin_01, R.id.skin_02, R.id.skin_03, R.id.skin_04, R.id.skin_05};

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_motive;

    }

    @Override
    protected void initViews() {
        backIv.setVisibility(View.VISIBLE);
        setCustomTitle("主题");
        int colorArr[] = ColorManager.getInstance().getSkinColor(this);
        for (int i = 0; i < layouts.length; i++) {
            View view = findViewById(layouts[i]);
            View color = view.findViewById(R.id.motive_item_color);
            View selected = view.findViewById(R.id.motive_item_selected);
            color.setBackgroundColor(colorArr[i]);
            if (colorArr[i] == App.mPreference.getSkinColorValue()) {
                selected.setVisibility(View.VISIBLE);
            }
            color.setOnClickListener(new OnSkinColorClickListener(i));
        }

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class OnSkinColorClickListener implements OnClickListener {

        private int position;

        public OnSkinColorClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < layouts.length; i++) {
                View view = findViewById(layouts[i]);
                View selected = view.findViewById(R.id.motive_item_selected);
                selected.setVisibility(i == position ? View.VISIBLE : View.GONE);
                ColorManager.getInstance().setSkinColor(ThemeSettingActivity.this,
                        position);
            }
        }
    }


}
