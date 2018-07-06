package com.zhongyong.smarthome.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyong.smarthome.jpush.ExampleUtil;
import com.zhongyong.smarthome.jpush.TagAliasOperatorHelper;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.SharePreferenceUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.Bind;

import static com.zhongyong.smarthome.jpush.TagAliasOperatorHelper.ACTION_SET;
import static com.zhongyong.smarthome.jpush.TagAliasOperatorHelper.sequence;


public class PushSettingActivity extends BaseActivity {
    @Bind(R.id.title_right)
    TextView rightTv;
    @Bind(R.id.aps_edt_pushTag)
    EditText pushTagEdt;
    String preferenceTag;
    int action = -1;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_push_setting;
    }

    @Override
    protected void initViews() {
        rightTv.setText("保存");
        rightTv.setVisibility(View.VISIBLE);
        preferenceTag = (String) SharePreferenceUtils.get(this, "pushTag", "");
        if (!TextUtils.isEmpty(preferenceTag)) {
            pushTagEdt.setText(preferenceTag);
            pushTagEdt.setSelection(pushTagEdt.getText().length());
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
        rightTv.setOnClickListener(v -> {
            Set<String> tags = null;
            tags = getInPutTags();
            if (tags == null) {
                return;
            }
            action = ACTION_SET;
            TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
            tagAliasBean.action = action;
            tagAliasBean.tags = tags;
            tagAliasBean.isAliasAction = false;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), sequence, tagAliasBean);

            //设置本地数据
            SharePreferenceUtils.put(this, "pushTag", pushTagEdt.getText().toString());
            showToast("保存成功");
            finish();
        });
    }

    /**
     * 获取输入的tags
     */
    private Set<String> getInPutTags() {
        String tag = pushTagEdt.getText().toString().trim();
        // 检查 tag 的有效性
        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                showToast("格式不对");
                Toast.makeText(getApplicationContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return null;
            }
            tagSet.add(sTagItme);
        }
        if (tagSet.isEmpty()) {
            showToast("tag不能为空");
            return null;
        }
        return tagSet;
    }
}
