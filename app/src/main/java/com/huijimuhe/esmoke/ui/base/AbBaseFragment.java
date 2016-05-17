package com.huijimuhe.esmoke.ui.base;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.huijimuhe.esmoke.R;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;


/**
 * Created by Huijimuhe on 2016/3/30.
 * This is a part of Esmoke
 * enjoy
 */
public abstract class AbBaseFragment extends Fragment {
    //umeng share
    protected UMSocialService mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShare();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), "wxa0809e5e9dc97d5e", "e66209006a6dbacc121c56ae066d3920");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        // 设置微信朋友圈分享内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent("你的朋友在抽我戒烟,点击看看怎么帮助他.");
        circleMedia.setTitle("你的朋友在抽我戒烟,点击看看怎么帮助他.");
        circleMedia.setShareImage(new UMImage(getActivity(), BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo)));
        circleMedia.setTargetUrl("http://mp.weixin.qq.com/s?__biz=MzIxNTE5MzU5OA==&mid=404074295&idx=1&sn=17bb070a0fdb2f098643c622f3064f1a#rd");
        // 设置分享内容
        mShareController.setShareMedia(circleMedia);
    }
}