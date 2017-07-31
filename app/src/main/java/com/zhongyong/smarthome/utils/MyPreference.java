package com.zhongyong.smarthome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhongyong.smarthome.model.ColorManager;


public class MyPreference {
	private final static String KEY_APP_SKIN = "key_app_skin";
	private final static String KEY_SELECTED = "key_selected";
	private final static String KEY_CHECKED = "key_checked";
	private final static String KEY_DELETE = "key_delete";
	private static final String  KEY_APP_SKIN_POSITON ="key_skin_position";

	private SharedPreferences mPreferences;

	public MyPreference(Context context) {
		mPreferences = context.getSharedPreferences("motive_preference",
				Context.MODE_PRIVATE);
	}

	public void setSkinColorValue(int color) {
		mPreferences.edit().putInt(KEY_APP_SKIN, color).commit();
	}

	public int getSkinColorValue() {
		return mPreferences.getInt(KEY_APP_SKIN, ColorManager.DEFAULT_COLOR);
	}
	public void setSkinColorPosition(int position){
		mPreferences.edit().putInt(KEY_APP_SKIN_POSITON, position).commit();
	}
	public int getSkinColorPosition(){
		return mPreferences.getInt(KEY_APP_SKIN_POSITON, 0);
	}

	public void setSelectedEnabled(boolean isEnabled) {
		mPreferences.edit().putBoolean(KEY_SELECTED, isEnabled).commit();
	}

	public boolean getSelectedEnabled() {
		return mPreferences.getBoolean(KEY_SELECTED, false);
	}

	public void setCheckedEnabled(boolean isEnabled) {
		mPreferences.edit().putBoolean(KEY_CHECKED, isEnabled).commit();
	}

	public boolean getCheckedEnabled() {
		return mPreferences.getBoolean(KEY_CHECKED, false);
	}

	public void setDeleteEnabled(boolean isEnabled) {
		mPreferences.edit().putBoolean(KEY_DELETE, isEnabled).commit();
	}

	public boolean getDeleteEnabled() {
		return mPreferences.getBoolean(KEY_DELETE, false);
	}
}
