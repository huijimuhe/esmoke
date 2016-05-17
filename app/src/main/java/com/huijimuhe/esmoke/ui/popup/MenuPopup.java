package com.huijimuhe.esmoke.ui.popup;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.animator.KickBackAnimator;
import com.huijimuhe.esmoke.utils.DevicesUtil;

/**
 * Created by Huijimuhe on 2016/3/13.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.ui.popup
 * please enjoy the day and night when you work hard on this.
 */
public class MenuPopup extends PopupWindow implements View.OnClickListener {

    FragmentActivity  mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private onMenuItemClick menuItemClick;
    private Handler mHandler = new Handler();

    public MenuPopup(FragmentActivity context) {
        mContext = context;
        init();
    }

    public void SetOnMenuItemClick(onMenuItemClick l) {
        menuItemClick = l;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;

        setWidth(mWidth);
        setHeight(mHeight);
    }

    public void show(View anchor) {
        final LinearLayout layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        setContentView(layout);
        ImageView close = (ImageView) layout.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(layout);
                }
            }

        });
        showAnimation(layout);
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.btn_close) {
                continue;
            }
            child.setOnClickListener(this);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "scaleX", 0.5f, 1f);
                    fadeAnim.setDuration(500);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(300);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }

    }

    private void closeAnimation(ViewGroup layout) {
        final View view = layout.findViewById(R.id.btn_close);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(view, "scaleY", 1, 0);
                fadeAnim.setDuration(300);
                fadeAnim.setEvaluator(new FloatEvaluator());
                fadeAnim.start();
                fadeAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
            }
        }, 100);
    }

    @Override
    public void onClick(View v) {
        menuItemClick.onMenuClick(v);
    }

    public interface onMenuItemClick {
        void onMenuClick(View view);
    }
}
