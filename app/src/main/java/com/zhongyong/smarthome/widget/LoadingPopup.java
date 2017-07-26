package com.zhongyong.smarthome.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zhongyong.smarthome.R;

public class LoadingPopup extends PopupWindow {

    private Context mContext;

    public RelativeLayout loadLyt;
    public View pickPhotoView;

    public LoadingPopup(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        pickPhotoView = LayoutInflater.from(mContext).inflate(R.layout.popup_loading, null);
        loadLyt= (RelativeLayout) pickPhotoView.findViewById(R.id.loading_lyt);
        setContentView(pickPhotoView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new ColorDrawable(0));
    }

}
