package com.huijimuhe.esmoke.ui.main;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.core.AppContext;
import com.huijimuhe.esmoke.domain.EaseMobService;
import com.huijimuhe.esmoke.ui.Article.ArticleListActivity;
import com.huijimuhe.esmoke.ui.base.AbBaseFragment;
import com.huijimuhe.esmoke.ui.popup.MenuPopup;
import com.huijimuhe.esmoke.utils.DevicesUtil;
import com.huijimuhe.esmoke.widget.ChatInputView;
import com.huijimuhe.esmoke.widget.CigaretteView;
import com.huijimuhe.esmoke.widget.SmokeRingView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;


import java.util.HashMap;

import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

/**
 * Created by Huijimuhe on 2016/3/14.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.ui.base
 * please enjoy the day and night when you work hard on this.
 */
public abstract class AbSmokeBaseFragment extends AbBaseFragment implements MenuPopup.onMenuItemClick {

    /**
     * 是否已经抽完烟
     */
    protected boolean mIsFinished = false;
    protected boolean mIsSoundOpen = true;
    protected CigaretteView mCigaretteView;
    protected SmokeRingView mRingView;
    protected View mBackground;
    protected MenuPopup popup;
    protected SoundPool mPlayer;
    protected HashMap<Integer, Integer> mSounds = new HashMap<>();

    protected ObjectAnimator inAnim;
    protected ObjectAnimator outAnim;

    protected IDanmakuView mDanmakuView;
    protected DanmakuContext mContext;
    protected BaseDanmakuParser mParser;

    protected InputMethodManager inputManager;

    protected ChatInputView mInputView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_smoke, container, false);
        init(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //键盘管理
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //登录聊天室
        EaseMobService.getInstance().enterChatRoom();
        //注册广播接收
        AppContext.getInstance().registChatRoomReceiver(broadCastReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDanmakuView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
            mDanmakuView.resume();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {

        mPlayer.release();
        mPlayer = null;
        mSounds = null;
        mDanmakuView.stop();
        //退出聊天室
        EaseMobService.getInstance().quiteRoom();
        //注销广播接收
        AppContext.getInstance().unregistChatRoomReceiver(broadCastReceiver);
        super.onDestroy();
    }


    private void init(View v) {
        //init ui
        initUI(v);
        //init sounds
        initSounds();
        //init animator
        initAnimator();
        //init danmu
        initDanmu(v);
        //init input
        initInput(v);
        //init menu
        popup = new MenuPopup(getActivity());
        popup.SetOnMenuItemClick(this);
    }

    @Override
    public void onMenuClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share:
                mShareController.postShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE, new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                        Log.d("UMeng Share-->", String.valueOf(i));
                        popup.dismiss();
                    }
                });
                break;
            case R.id.btn_sound:
                if (mIsSoundOpen) {
                    mIsSoundOpen = false;
                    Button iv = (Button) view;
                    Drawable img = getResources().getDrawable(R.drawable.ic_volume_off);
                    img.setBounds(1, 1, DevicesUtil.dip2px(getActivity(),80),  DevicesUtil.dip2px(getActivity(),80));
                    iv.setCompoundDrawables(null, img, null, null);
                } else {
                    mIsSoundOpen = true;
                    Button iv = (Button) view;
                    Drawable img = getResources().getDrawable(R.drawable.ic_volume_on);
                    img.setBounds(1, 1, DevicesUtil.dip2px(getActivity(),80),  DevicesUtil.dip2px(getActivity(),80));
                    iv.setCompoundDrawables(null, img, null, null);
                }
                break;
            case R.id.btn_close:
                popup.dismiss();
                break;
            default:
                break;
        }
    }

    protected abstract void initUI(View v);

    private void initInput(View v) {
        mInputView = (ChatInputView) v.findViewById(R.id.chat_input);
        mInputView.setChatInputMenuListener(new ChatInputView.ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                if (!TextUtils.isEmpty(content)) {
                    addDanmaku(true, content);
                    EaseMobService.getInstance().sendDanmaku(content);
                }
            }
        });
    }

    private void initAnimator() {
        inAnim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.background_suck_in);
        inAnim.setEvaluator(new ArgbEvaluator());
        inAnim.setTarget(mBackground);

        outAnim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.background_suck_out);
        outAnim.setEvaluator(new ArgbEvaluator());
        outAnim.setTarget(mBackground);
    }

    private void initSounds() {
        AssetManager am = getActivity().getAssets();//获得该应用的AssetManager
        mPlayer = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetFileDescriptor in = am.openFd("smoke_in.wav");
            AssetFileDescriptor out = am.openFd("smoke_out.wav");
            mSounds.put(1, mPlayer.load(in, 1));
            mSounds.put(2, mPlayer.load(out, 1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void suckIn() {
        //播放音乐
        if (mIsSoundOpen) {
            mPlayer.stop(mSounds.get(2));
            mPlayer.play(mSounds.get(1), 1, 1, 1, 1, 1);
        }
        //修改背景色
        inAnim.start();
    }

    protected void suckOut() {
        //播放音乐
        if (mIsSoundOpen) {
            mPlayer.stop(mSounds.get(1));
            mPlayer.play(mSounds.get(2), 1, 1, 1, 1, 1);
        }
        //修改背景色
        outAnim.start();
        //吐烟圈
        mRingView.addMyRing();
        EaseMobService.getInstance().sendRingMsg();
    }

    /**
     * 弹幕初始化
     */
    private void initDanmu(View v) {
        // 设置最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3); // 滚动弹幕最大显示3行

        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuView = (IDanmakuView) v.findViewById(R.id.sv_danmaku);
        mContext = DanmakuContext.create();
        mContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_SHADOW, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);
        if (mDanmakuView != null) {
            mParser = createDummyParser();
            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
                @Override
                public void updateTimer(DanmakuTimer timer) {
                }

                @Override
                public void drawingFinished() {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {
                }

                @Override
                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(mParser, mContext);
            mDanmakuView.showFPS(false);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    /**
     * 弹幕数据源
     *
     * @return
     */
    protected BaseDanmakuParser createDummyParser() {
        return new BaseDanmakuParser() {

            @Override
            protected Danmakus parse() {
                return new Danmakus();
            }
        };
    }

    /**
     * 弹幕样式
     *
     * @param drawable
     * @return
     */
    protected SpannableStringBuilder createSpannable(Drawable drawable) {
        String text = "bitmap";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        ImageSpan span = new ImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append("图文混排");
        spannableStringBuilder.setSpan(new BackgroundColorSpan(Color.parseColor("#8A2233B1")), 0, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 发送弹幕
     *
     * @param islive
     */
    protected void addDanmaku(boolean islive, String content) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = islive;
        danmaku.time = mDanmakuView.getCurrentTime() + 1000;
        danmaku.textSize = 25f;// * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.LTGRAY;
        // danmaku.underlineColor = Color.GREEN;
        // danmaku.borderColor = Color.GREEN;
        mDanmakuView.addDanmaku(danmaku);
    }
    /**
     * 发送自动弹幕
     *
     */
    protected void addSysDanmaku(String content) {
        BaseDanmaku danmaku = mContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = true;
        danmaku.time = mDanmakuView.getCurrentTime() + 1000;
        danmaku.textSize = 45f;// * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.textShadowColor = Color.LTGRAY;
        //danmaku.underlineColor = Color.BLUE;
        danmaku.borderColor = Color.BLUE;
        mDanmakuView.addDanmaku(danmaku);
    }
    /**
     * 隐藏键盘
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 消息监听
     */
    BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case AppContext.RING_BROADCAST:
                    Log.d("BroadcastReceiver", "收到烟圈消息");
                    mRingView.addMyRing();
                    break;
                case AppContext.DANMAKU_BROADCAST:
                    Log.d("BroadcastReceiver", "收到弹幕消息" + intent.getStringExtra("danmaku"));
                    addDanmaku(true, intent.getStringExtra("danmaku"));
                    break;
            }
        }
    };
}
