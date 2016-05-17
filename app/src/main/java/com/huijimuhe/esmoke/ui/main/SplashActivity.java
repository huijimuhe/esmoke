package com.huijimuhe.esmoke.ui.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.domain.EaseMobService;
import com.huijimuhe.esmoke.ui.auth.SignInActivity;
import com.huijimuhe.esmoke.utils.PermUtils;
import com.umeng.update.UmengUpdateAgent;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int sleepTime = 4 * 1000;//等4秒
    private PermUtils mPermUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //权限判断
                mPermUtils = new PermUtils(SplashActivity.this);
                mPermUtils.setOnApplyPermissionListener(new PermUtils.OnApplyPermissionListener() {
                    @Override
                    public void onAfterApplyAllPermission() {
                        // Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                        runApp();
                    }
                });
                if (Build.VERSION.SDK_INT < 23) {
                    //如果系统版本低于23，直接跑应用的逻辑
                    // Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
                    runApp();
                } else {
                    //如果权限全部申请了，那就直接跑应用逻辑
                    if (mPermUtils.isAllRequestedPermissionGranted()) {
                        // Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                        runApp();
                    } else {
                        //如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                        // Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                        mPermUtils.applyPermissions();
                    }
                }
            }
        }, 500);
    }
    private void runApp(){

        setupSplashAd();
        initService();
    }

    private void initService(){
        //umeng
        UmengUpdateAgent.update(this);
        //easemob
        EaseMobService.getInstance().init(getApplicationContext()).initEnvr();
        //传入跳转的intent，若传入intent，初始化时目标activity应传入null
        String token = AppContext.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
           startActivity(SignInActivity.newIntent());
        } else {
            startActivity(MainActivity.newIntent());
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {

    }
}
