package com.zhongyong.jamod.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.jamod.model.User;
import com.zhongyong.jamod.utils.RegExpValidatorUtils;
import com.zhongyong.smarthome.App;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.api.HttpResult;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

import static com.zhongyong.jamod.apis.ApiClient.call;

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.arp_phoneNumber_edit)
    EditText phoneEdit;
    @Bind(R.id.arp_clear_phone_lyt)
    RelativeLayout clearPhoneLyt;
    @Bind(R.id.arp_verifyTx_name)
    EditText nameEdit;
    @Bind(R.id.arp_verifyTx_password)
    EditText passwordEdit;
    @Bind(R.id.arp_next_btn)
    TextView nextBtn;
    User mUser = new User();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
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
        //电话号码的监听
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!StringUtils.isEmpty(phoneEdit.getText().toString())) {
                    clearPhoneLyt.setVisibility(View.VISIBLE);
                } else {
                    clearPhoneLyt.setVisibility(View.GONE);
                }

            }
        });
        //清空按钮的监听
        clearPhoneLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneEdit.setText("");
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(phoneEdit.getText().toString())) {
                    showToast("手机号码不能为空！");
                    return;
                }
                if (StringUtils.isEmpty(nameEdit.getText().toString())) {
                    showToast("昵称不能为空");
                    return;
                }
                if (StringUtils.isEmpty(passwordEdit.getText().toString())) {
                    showToast("密码不能为空");
                    return;
                }
                if (!(RegExpValidatorUtils.IsTelephone(phoneEdit.getText().toString()))) {
                    showToast("对不起，请输入正确的手机号码");
                    return;
                }
                mUser.setTelephone(phoneEdit.getText().toString());
                mUser.setUserName(nameEdit.getText().toString());
                mUser.setPassword(passwordEdit.getText().toString());
                call(ApiClient.getApis().register(mUser), new MySubscriber<HttpResult<User>>() {
                    @Override
                    public void onError(Throwable e) {
                        showToast(getResources().getString(R.string.systemError));
                    }

                    @Override
                    public void onNext(HttpResult<User> result) {
                        if (result.getResultCode() == 200) {
                            showToast(result.getMessage());
                            App.setUser(result.data);
                            EventBus.getDefault().post(result.data);
                            finish();
                        } else {
                            showToast(result.getMessage());
                        }
                    }
                });

            }
        });
    }
}
