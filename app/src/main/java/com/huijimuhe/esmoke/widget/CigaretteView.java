package com.huijimuhe.esmoke.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.animator.KickBackAnimator;

/**
 * Created by Huijimuhe on 2016/3/12.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.Widget
 * please enjoy the day and night when you work hard on this.
 */
public class CigaretteView extends RelativeLayout {

    private static final int SPEED_NORMAL = 15;
    private static final int SPEED_FASET = 50;
    private static final int SPPED_SLOW = 30;

    private View mView;
    private View mBody;
    private View mFilter;
    private View mFire;
    private int mBodyHeight;
    private float mOrignalY;
    private onSuckListener mOnSuck;

    public void setOnSuckListener(onSuckListener l) {
        mOnSuck = l;
    }

    public CigaretteView(Context context) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.widget_cigarette, this, true);
        initUI();
    }

    public CigaretteView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mView = LayoutInflater.from(context).inflate(R.layout.widget_cigarette, this, true);
        initUI();
    }

    public CigaretteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = LayoutInflater.from(context).inflate(R.layout.widget_cigarette, this, true);
        initUI();
        /**
         * 获得所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CigaretteView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CigaretteView_bodyHeight:
                    mBodyHeight = a.getInt(attr, 0);
                    break;
            }

        }
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams param = mBody.getLayoutParams();
        param.height = getHeight() - mFilter.getHeight();
        mBody.setLayoutParams(param);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private void initUI() {
        mBody = mView.findViewById(R.id.iv_body);
        mFilter = mView.findViewById(R.id.iv_filter);
        mOrignalY = mBody.getTranslationY();
    }

    public void start() {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(mBody, "translationY", mBody.getTranslationY(), mBody.getTranslationY() + 30)
                .setDuration(500);
        KickBackAnimator kickAnimator = new KickBackAnimator();
        kickAnimator.setDuration(100);
        fadeAnim.setEvaluator(kickAnimator);
        fadeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                float len=mBody.getTranslationY()-mOrignalY;
                if(len>mBody.getHeight()-100){
                   mOnSuck.onFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        fadeAnim.start();

    }

    public void reset() {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(mBody, "translationY", mBody.getTranslationY(), mOrignalY)
                .setDuration(500);
        KickBackAnimator kickAnimator = new KickBackAnimator();
        kickAnimator.setDuration(300);
        fadeAnim.setEvaluator(kickAnimator);
        fadeAnim.start();
    }

    public int getmBodyHeight() {
        return mBodyHeight;
    }

    public void setmBodyHeight(int mBodyHeight) {
        this.mBodyHeight = mBodyHeight;
    }

    public interface onSuckListener {
        void onFinish();
    }
}
