package com.zhongyong.smarthome.widget;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zhongyong.smarthome.model.ColorManager;
import com.zhongyong.smarthome.model.OnColorChangedListener;

public class ColorImageView extends AppCompatImageView implements OnColorChangedListener {
	private int color;
	private boolean isColorEnabled = true;

	public ColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ColorImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ColorImageView(Context context) {
		super(context);
	}

	@Override
	public void onColorChanged(int color) {
		if (this.color == color) {
			return;
		}
		this.color = color;
		if (isColorEnabled) {
			setColorFilter(new LightingColorFilter(color, 1));
		}
	}

	public void setColorFilterEnabled(boolean isColorEnabled) {
		if (this.isColorEnabled == isColorEnabled) {
			return;
		}
		this.isColorEnabled = isColorEnabled;
		if (isColorEnabled) {
			setColorFilter(new LightingColorFilter(color, 1));
		} else {
			setColorFilter(null);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		ColorManager.getInstance().addListener(this);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		ColorManager.getInstance().removeListener(this);
		super.onDetachedFromWindow();
	}
}
