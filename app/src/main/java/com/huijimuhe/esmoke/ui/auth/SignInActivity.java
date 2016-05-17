package com.huijimuhe.esmoke.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.api.BaseClient;
import com.huijimuhe.esmoke.api.WebApi;
import com.huijimuhe.esmoke.bean.AuthResponseBean;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.domain.EaseMobService;
import com.huijimuhe.esmoke.domain.PrefService;
import com.huijimuhe.esmoke.ui.base.AbBaseActivity;
import com.huijimuhe.esmoke.ui.main.MainActivity;
import com.huijimuhe.esmoke.ui.main.WebActivity;
import com.huijimuhe.esmoke.ui.scene.SceneActivity;
import com.huijimuhe.esmoke.utils.ToastUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;

import java.lang.ref.WeakReference;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SignInActivity extends AbBaseActivity implements View.OnClickListener{

    public static final int AUTH_SUCCESS = 0;
    public static final int AUTH_FAILED = 1;
    public static final int OPEN_WEIXIN = 2;

    private TextView mTvAgreement;
    private Button mBtnWeixin;
    private MyHandler handler;
    private UMSocialService mLoginController = UMServiceFactory.getUMSocialService("com.umeng.login");

    public static Intent newIntent() {
        Intent intent = new Intent(AppContext.getInstance(),
                SignInActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mBtnWeixin=(Button) findViewById(R.id.btn_weixin);
        mBtnWeixin.setOnClickListener(this);
        handler=new MyHandler(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weixin:
                openAuth(SHARE_MEDIA.WEIXIN);
                break;
        }
    }

    private void disableViews() {
        mBtnWeixin.setEnabled(false);
    }

    private void enableViews() {
        mBtnWeixin.setEnabled(true);
    }

    /**
     * 授权。如果授权成功，则获取用户信息</br>
     */
    private void openAuth(final SHARE_MEDIA platform) {
        mLoginController.doOauthVerify(SignInActivity.this, platform, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
                disableViews();
                ToastUtils.show(SignInActivity.this, "授权开始...");
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                handler.sendEmptyMessage(AUTH_FAILED);
                Log.d("Monolog-signin", e.getMessage());
            }

            @Override
            public void onComplete(Bundle value, final SHARE_MEDIA platform) {
                String uid = value.getString("uid");
                if (TextUtils.isEmpty(uid)) {
                    handler.sendEmptyMessage(AUTH_FAILED);
                    Log.d("Monolog-signin", "umeng uid empty");
                    return;
                }
                mLoginController.getPlatformInfo(SignInActivity.this, platform, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == StatusCode.ST_CODE_SUCCESSED) {
                            Message msg = new Message();
                            msg.what = OPEN_WEIXIN;
                            Bundle b = new Bundle();
                            b.putString("nickname", info.get("nickname").toString());
                            b.putString("openid", info.get("openid").toString());
                            b.putString("unionid", info.get("unionid").toString());
                            b.putString("sex", info.get("sex").toString());
                            b.putString("headimgurl", info.get("headimgurl").toString());
                            msg.setData(b);
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(AUTH_FAILED);
                            Log.d("Monolog-signin", "get Auth code" + String.valueOf(status));
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                handler.sendEmptyMessage(AUTH_FAILED);
            }
        });
    }

    /**
     * 注册用户
     * @param result
     */
    private void postWeixin(Bundle result) {
        /**
         *sex         * nickname         * unionid         * openid         * province         * headimgurl         *
         */
        String name = result.getString("nickname");
        String openid = result.getString("openid");
        String token = result.getString("unionid");
        String gender = result.getString("sex").equals("1") ? "m" : "f";
        String avatar = result.getString("headimgurl");
        String type = "weixin";

        WebApi.open(name, openid, gender, avatar, token, type, new TextHttpResponseHandler() {
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                handler.sendEmptyMessage(AUTH_FAILED);
                Log.d("Monolog-signin", "post error" + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Gson gson = new Gson();
                    AuthResponseBean response = gson.fromJson(responseString, AuthResponseBean.class);
                    PrefService.getInstance(SignInActivity.this).setToken(response.getToken());
                    PrefService.getInstance(SignInActivity.this).setUser(response.getUser());
                    handler.sendEmptyMessage(AUTH_SUCCESS);
                } catch (Exception ex) {
                    handler.sendEmptyMessage(AUTH_FAILED);
                    Log.d("esmoke-signin", "SUCEESEE ERROR" + ex.getMessage());
                }
            }
        });
    }

    private static class MyHandler extends Handler {

        WeakReference<SignInActivity> mAct;

        public  MyHandler(SignInActivity act){
            mAct=new WeakReference<>(act);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OPEN_WEIXIN:
                    mAct.get().postWeixin(msg.getData());
                    break;
                case AUTH_FAILED:
                    mAct.get().enableViews();
                    ToastUtils.show(mAct.get(), "授权失败...");
                    break;
                case AUTH_SUCCESS:
                    mAct.get(). enableViews();
                    EaseMobService.getInstance().easeMobLogin(this);
                    if(PrefService.getInstance(mAct.get().getApplicationContext()).isInstalled())  {
                        mAct.get().startActivity(SceneActivity.newIntent());
                        PrefService.getInstance(mAct.get().getApplicationContext()).setInstalled();
                    } else {
                        mAct.get().startActivity(MainActivity.newIntent());
                    }
                    mAct.get(). finish();
                    break;
                default:
                    mAct.get().enableViews();
                    break;
            }
        }
    }

    /**
     * 用户协议文字的扩展
     */
    private class NoLineClickSpan extends ClickableSpan {
        public NoLineClickSpan() {
            super();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            startActivity(WebActivity.newIntent(BaseClient.URL_USER_AGREEMENT, "用户协议"));
        }
    }
}
