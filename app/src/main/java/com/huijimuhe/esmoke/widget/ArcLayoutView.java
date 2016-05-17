package com.huijimuhe.esmoke.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Huijimuhe on 2016/3/14.
 * This is a copy of Esmoke
 * belongs to com.huijimuhe.esmoke.widget
 * please enjoy the day and night when you work hard on this.
 */
public class ArcLayoutView extends View {
    private Paint mPaint;

    public ArcLayoutView(Context context) {
        super(context);
    }

    public ArcLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
