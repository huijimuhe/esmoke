package com.huijimuhe.esmoke.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.animator.BezierAnimator;

import java.util.Random;


public class SmokeRingView extends RelativeLayout{

    private Interpolator line = new LinearInterpolator();
    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator dce = new DecelerateInterpolator();
    private Interpolator accdec = new AccelerateDecelerateInterpolator();
    private Interpolator[] interpolators ;

    private int mHeight;
    private int mWidth;
    private int mRingHeight;
    private int mRingWidth;
    private int mOriginalX;
    private int mOriginalY;

    private LayoutParams mParam;

    private Drawable mOtherRing;
    private Drawable mMyRing;
    private Drawable[] drawables ;
    private Random random = new Random();

    public SmokeRingView(Context context) {
        super(context);
    }

    public SmokeRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

        drawables = new Drawable[2];
        mMyRing = getResources().getDrawable(R.drawable.ic_my_ring);
        mOtherRing = getResources().getDrawable(R.drawable.ic_other_ring);

        drawables[0]= mOtherRing;
        drawables[1]= mMyRing;
        mRingHeight = mOtherRing.getIntrinsicHeight();
        mRingWidth = mOtherRing.getIntrinsicWidth();

        mParam = new LayoutParams(mRingHeight, mRingWidth);
        mParam.addRule(CENTER_HORIZONTAL, TRUE);
        mParam.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mOriginalX = 5;
        mOriginalY = mHeight-50;
    }

    public void addMyRing() {
        if(getChildCount()>40){
            return;
        }

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[1]);
        imageView.setLayoutParams(mParam);
        addView(imageView);

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    public void addOtherRing() {
        if(getChildCount()>40){
            return;
        }

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawables[0]);
        imageView.setLayoutParams(mParam);
        addView(imageView);

        Animator set = getAnimator(imageView);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Animator getAnimator(View target){
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();

        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(4)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {

        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierValueAnimator(View target) {

        BezierAnimator evaluator = new BezierAnimator(getPointF(2),getPointF(1));

        ValueAnimator animator = ValueAnimator.ofObject(evaluator,new PointF(mOriginalX, mOriginalY),new PointF(0,mHeight/2));//ȫ��һ��
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth-10));
        pointF.y = random.nextInt((mHeight - 100))/scale;
        return pointF;
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListenr(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            target.setAlpha(1-animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;
        public AnimEndListener(View target) {
            this.target = target;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView((target));
        }
    }
}
