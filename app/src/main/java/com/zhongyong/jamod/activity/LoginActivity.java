package com.zhongyong.jamod.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;
import com.zhongyong.jamod.apis.ApiClient;
import com.zhongyong.jamod.apis.MySubscriber;
import com.zhongyong.jamod.model.User;
import com.zhongyong.smarthome.App;
import com.zhongyong.smarthome.R;
import com.zhongyong.smarthome.api.HttpResult;
import com.zhongyong.smarthome.base.BaseActivity;
import com.zhongyong.smarthome.utils.StringUtils;
import com.zhongyong.speechawake.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhongyong.jamod.apis.ApiClient.call;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.al_phoneNumber_edit)
    EditText phoneEdit;
    @Bind(R.id.al_code_label_lyt)
    RelativeLayout phoneLyt;
    @Bind(R.id.al_password_edit)
    EditText passwordEdit;
    @Bind(R.id.al_clear_password_lyt)
    RelativeLayout clearPasswordLyt;
    @Bind(R.id.al_clear_phone_lyt)
    RelativeLayout clearPhoneLyt;
    @Bind(R.id.al_register_txt)
    TextView registerTv;
    @Bind(R.id.al_login_btn)
    TextView loginBtn;
    @Bind(R.id.al_weixin_lyt)
    RelativeLayout weiXinLoginRv;
    @Bind(R.id.al_qq_lyt)
    LinearLayout qqLoginLv;
    @Bind(R.id.al_verify_lyt)
    RelativeLayout verifyLoginRv;
    private ProgressDialog dialog;
    User mUser = new User();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        setCustomTitle("登录");
        dialog = new ProgressDialog(this);
        if (App.getUser() != null) {
            phoneEdit.setText(App.getUser().getTelephone());
            passwordEdit.setText(App.getUser().getPassword());
        }
        if (phoneEdit.getText().toString().trim().length() > 0) {
            CharSequence text = phoneEdit.getText();
            if (text instanceof Spannable) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
        if (passwordEdit.getText().toString().trim().length() > 0) {
            CharSequence text = passwordEdit.getText();
            if (text instanceof Spannable) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
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
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // login();
                login2();
            }
        });
        //注册新用户
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        //微信登录
        weiXinLoginRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weiXinLogin();
            }
        });

        //qq登录
        qqLoginLv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qqLogin();
            }
        });

        //微博登录
        verifyLoginRv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyLogin();
            }
        });

        //输入框监听
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if (StringUtils.isEmpty(phoneEdit.getText().toString())) {
                    clearPhoneLyt.setVisibility(View.GONE);
                } else {
                    clearPhoneLyt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                if (StringUtils.isEmpty(passwordEdit.getText().toString())) {
                    clearPasswordLyt.setVisibility(View.GONE);
                } else {
                    clearPasswordLyt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //清除监听
        clearPhoneLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneEdit.setText("");
            }
        });
        clearPasswordLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEdit.setText("");
            }
        });

    }

    private void login() {
        String tel = phoneEdit.getText().toString();
        String pwd = passwordEdit.getText().toString();
        if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
            showToast(getResources().getString(R.string.inputMessage));
            return;
        } else {
            mUser.setTelephone(tel);
            mUser.setPassword(pwd);
        }
        call(ApiClient.getApis().login(mUser), new MySubscriber<HttpResult<User>>() {
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

    private void login2() {
        String tel = phoneEdit.getText().toString();
        String pwd = passwordEdit.getText().toString();
        if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
            showToast(getResources().getString(R.string.inputMessage));
            return;
        } else {
            mUser.setUserName(tel);
            mUser.setPassword(pwd);
        }
        String credentials = tel + "_web" + ":" + pwd;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constants.API_URL_NIAGARA)
                        .get()
                        .addHeader("Authorization", auth)
                        .addHeader("Cache-Control", "no-cache").build();

                try {
                    Response response = client.newCall(request).execute();
                    String message = response.message();
                    if (message.contains("OK")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("绿能平台授权成功！");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("绿能平台授权失败！");
                            }
                        });
                        return;
                    }
                    subscriber.onNext(response.message());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).flatMap(new Func1<String, Observable<HttpResult<User>>>() {
            @Override
            public Observable<HttpResult<User>> call(String s) {
                return ApiClient.getApis().login(mUser);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HttpResult<User>>() {
                    @Override
                    public void call(HttpResult<User> result) {
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

    private void register() {
        go(RegisterActivity.class);
    }

    private void weiXinLogin() {
        // UMShareAPI.get(mContext).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    private void qqLogin() {
        //UMShareAPI.get(mContext).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
    }

    private void verifyLogin() {
        // UMShareAPI.get(mContext).doOauthVerify(this, SHARE_MEDIA.SINA, authListener);
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA media) {
            SocializeUtils.safeShowDialog(dialog);

        }

        @Override
        public void onComplete(SHARE_MEDIA media, int i, Map<String, String> map) {
            //友盟将回调过来的信息，都封装了，方便同意得到
            SocializeUtils.safeCloseDialog(dialog);
            String uid = map.get("uid");
            String name = map.get("name");
            String sex = map.get("gender");
            String imageUrl = map.get("iconurl");
            Log.e("fyc", uid + " " + name + " " + sex + " " + imageUrl);
            if (media == SHARE_MEDIA.QQ) {
                mUser.setQq(uid);
            } else if (media == SHARE_MEDIA.WEIXIN) {
                mUser.setWeChat(map.get("unionid"));
            } else if (media == SHARE_MEDIA.SINA) {

            }
           /* call(ApiClient.getApis().register(mUser), new MySubscriber<BaseResp<User>>() {
                @Override
                public void onError(Throwable e) {
                    showToast(getResources().getString(R.string.systemError));
                }

                @Override
                public void onNext(final BaseResp<User> resp) {
                    if (resp.resultCode == Constants.RespCode.SUCCESS) {
                        if (resp.status == Constants.RespCode.SUCCESS) {
                            //用户登录成功
                            App.setUser(resp.data);
                            goAndFinish(MainActivity.class);

                        } else if (resp.status == Constants.RespCode.ONLINE) {
                            showToast("对不起，该用户已在其他终端登录");
                        }
                    }
                }
            });
*/
        }

        @Override
        public void onError(SHARE_MEDIA media, int i, Throwable throwable) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "失败：" + throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA media, int i) {
            SocializeUtils.safeCloseDialog(dialog);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void setRegister(User user) {
        if (App.getUser() != null) {
            phoneEdit.setText(App.getUser().getTelephone());
            passwordEdit.setText(App.getUser().getPassword());
            if (phoneEdit.getText().toString().trim().length() > 0) {
                CharSequence text = phoneEdit.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
            if (passwordEdit.getText().toString().trim().length() > 0) {
                CharSequence text = passwordEdit.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        }
    }
}
