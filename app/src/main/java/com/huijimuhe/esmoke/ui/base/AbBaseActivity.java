package com.huijimuhe.esmoke.ui.base;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huijimuhe.esmoke.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * Created by Huijimuhe on 2016/3/14.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.ui.base
 * please enjoy the day and night when you work hard on this.
 */
public abstract class AbBaseActivity extends AppCompatActivity {

    //umeng share
    protected UMSocialService mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShare();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mShareController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 初始化友盟分享
     */
    private void initShare(){
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, "wxa0809e5e9dc97d5e", "e66209006a6dbacc121c56ae066d3920");
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, "wxa0809e5e9dc97d5e", "e66209006a6dbacc121c56ae066d3920");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        // 设置微信朋友分享内容
        WeiXinShareContent weiXinShareContent = new WeiXinShareContent();
        weiXinShareContent.setShareContent("你老公这次真要戒烟了.");
        weiXinShareContent.setTitle("你老公这次真要戒烟了");
        weiXinShareContent.setShareImage(new UMImage(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo)));
        weiXinShareContent.setTargetUrl("http://mp.weixin.qq.com/s?__biz=MzIwMjI2NDI3Ng==&mid=2652690396&idx=1&sn=0bd0431ad1c1eca05806bea66fac6652#rd");
        mShareController.setShareMedia(weiXinShareContent);
        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("你老公这次真要戒烟了");
        circleMedia.setTitle("你老公这次真要戒烟了");
        circleMedia.setShareImage(new UMImage(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo)));
        circleMedia.setTargetUrl("http://mp.weixin.qq.com/s?__biz=MzIwMjI2NDI3Ng==&mid=2652690396&idx=1&sn=0bd0431ad1c1eca05806bea66fac6652#rd");
        // 设置分享内容
        mShareController.setShareMedia(circleMedia);
    }

    public void showAd(){

    }
}
