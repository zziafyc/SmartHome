package com.zhongyong.smarthome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhongyong.jamod.activity.ZigBeeDetailActivity;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by fyc on 2017/12/29.
 */

public class IntelligentCampusFragment extends BaseFragment {
    @Bind(R.id.item_library)
    RelativeLayout mLibraryRv;
    @Bind(R.id.item_classroom)
    RelativeLayout mClassroomRv;
    @Bind(R.id.item_kitchen)
    RelativeLayout mKitchenRv;
    @Bind(R.id.item_laboratory)
    RelativeLayout mLaboratoryRv;

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_intelligent_campus;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initAdapter() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mLibraryRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZigBeeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scene", "library");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mClassroomRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZigBeeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scene", "classroom");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mKitchenRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZigBeeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scene", "kitchen");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mLaboratoryRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZigBeeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scene", "laboratory");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
