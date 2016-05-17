package com.huijimuhe.esmoke.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.huijimuhe.esmoke.R;
import com.huijimuhe.esmoke.domain.PrefService;
import com.huijimuhe.esmoke.utils.DevicesUtil;

/**
 * Created by Huijimuhe on 2016/3/21.
 * This is a part of Esmoke
 * enjoy
 */
public class CigaBoxView extends View {

    /**
     * 烟的画笔
     */
    private Paint mCigaPaint;

    /**
     * 烟的画笔
     */
    private Paint mSlotPaint;

    /**
     * 烟,包，条的bitmap
     */
    private Bitmap mCigaBitmap, mBoxBitmap, mCartonBitmap;

    /**
     * 烟的数量
     */
    private int mCigaCount;

    /**
     * 控件宽度、高度、横间距、中心点横，中心点竖，列数、行数、缩放比例
     */
    private float mWidth, mHeight, mCigaWidth, mCigaHeight, mCenterY, mCenterX, mColNm, mRowNm, mScale;

    public CigaBoxView(Context context) {
        super(context);
        init();
    }

    public CigaBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CigaBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cal();
        drawSlots(canvas);
        drawCigas(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 根据烟的数量计算等级
     */
    private void cal() {
        //控件高宽
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        //已经抽烟数量
        mCigaCount = PrefService.getInstance(getContext()).getCiga()%20;
        mCigaWidth = mWidth / (10 * 2 + 1);//宽度和间隔等分,公式1+i*2
        mCigaHeight = (mHeight - 20 * 2) / 3;//控件高度一半，上下间隔20
        mCenterY = mHeight / 3;//中心点在控件一半减去间隔40的中间
        Log.d("df", "1包2*10");
    }

    /**
     * 初始化画笔等
     */
    private void init() {

        //ciga paint
        mCigaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCigaPaint.setStyle(Paint.Style.FILL);

        //ciga slot background paint
        mSlotPaint = new Paint();
        mSlotPaint.setStyle(Paint.Style.STROKE);
        mSlotPaint.setStrokeWidth(3);
        mSlotPaint.setAntiAlias(true);
        mSlotPaint.setPathEffect(new DashPathEffect(new float[]{3, 3}, 1f));
        mSlotPaint.setColor(Color.WHITE);

        //ciga bitmap
        mCigaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ciga);
        mBoxBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_box);
        startAnim();
    }

    /**
     * 画虚框
     *
     * @param canvas
     */
    private void drawSlots(Canvas canvas) {
        for (int i = 0; i < 20; i++) {
            canvas.save();
            if (i >= 10) {
                //第二行
                canvas.translate(mCigaWidth + mCigaWidth * (i - 10) * 2, mCenterY * 2 + 20);
            } else {
                //第一行
                canvas.translate(mCigaWidth + mCigaWidth * i * 2, mCenterY - 20);
            }
            RectF rect = new RectF(0 + 4, mCigaHeight / 2 * -1 + 4, mCigaWidth - 4, mCigaHeight / 2 - 4);
            canvas.drawRect(rect, mSlotPaint);
            canvas.restore();
        }

    }

    /**
     * 画烟
     *
     * @param canvas
     */
    private void drawCigas(final Canvas canvas) {
        for (int i = 0; i < mCigaCount; i++) {
            canvas.save();
            if (i >= 10) {
                //第二行
                canvas.translate(mCigaWidth + mCigaWidth * (i - 10) * 2, mCenterY * 2 + 20);
            } else {
                //第一行
                canvas.translate(mCigaWidth + mCigaWidth * i * 2, mCenterY - 20);
            }
            canvas.scale(mScale, mScale);
            RectF rect = new RectF(0, mCigaHeight / 2 * -1, mCigaWidth, mCigaHeight / 2);
            canvas.drawBitmap(mCigaBitmap, null, rect, mCigaPaint);
            canvas.restore();
        }
    }

    /**
     * 画烟盒
     * @param canvas
     */
    private void drawBox(Canvas canvas){
        canvas.save();
        RectF rect = new RectF(0, mHeight- DevicesUtil.dip2px(getContext(),100), mWidth, mHeight);
        canvas.drawBitmap(mBoxBitmap, null, rect, mCigaPaint);
        canvas.restore();
    }
    private void startAnim() {
        ValueAnimator anim = ValueAnimator.ofFloat(0.7f, 1f);
        anim.setDuration(2000);
        anim.setInterpolator(new BounceInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mScale = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        anim.start();
    }
}
