package com.jpfeng.adv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

/**
 * @author Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2019/3/6
 */
public class RippleView extends View {

    private static final int DIRECTION_RB_LT = 1;
    private static final int DIRECTION_LT_RB = 2;

    private Drawable mDrawableTop;
    private Drawable mDrawableBottom;
    private float mOffset;
    private int mDirection;
    private RectF mOvalRect;
    private Paint mPaint;
    private PorterDuffXfermode MOD_CLEAR;
    private PorterDuffXfermode MODE_XOR;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mOffset = 0f;
        mDirection = DIRECTION_RB_LT;
        mOvalRect = new RectF();
        mPaint = new Paint();
        MOD_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        MODE_XOR = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        mPaint.setXfermode(MOD_CLEAR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mDrawableTop || null == mDrawableBottom) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mDrawableTop.setBounds(0, 0, width, height);
        mDrawableBottom.setBounds(0, 0, width, height);

        if (0 == mOffset) {
            mDrawableTop.draw(canvas);
            return;

        } else if (1 == mOffset) {
            mDrawableBottom.draw(canvas);
            return;
        }

        switch (mDirection) {
            case DIRECTION_RB_LT:
                mPaint.setXfermode(MOD_CLEAR);
                mDrawableBottom.draw(canvas);
                int layer1 = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
                mDrawableTop.draw(canvas);
                float r1 = (float) (Math.hypot(width, height) * mOffset);
                mOvalRect.set(width - r1, height - r1, width + r1, height + r1);
                canvas.drawOval(mOvalRect, mPaint);
                canvas.restoreToCount(layer1);
                break;

            case DIRECTION_LT_RB:
                mPaint.setXfermode(MODE_XOR);
                mDrawableTop.draw(canvas);
                int layer2 = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
                mDrawableBottom.draw(canvas);
                float r2 = (float) (Math.hypot(width, height) * (1 - mOffset));
                mOvalRect.set(-r2, -r2, r2, r2);
                canvas.drawOval(mOvalRect, mPaint);
                canvas.restoreToCount(layer2);
                break;
        }
    }

    public void setDrawables(@DrawableRes int top, @DrawableRes int bottom) {
        setDrawables(getResources().getDrawable(top),
                getResources().getDrawable(bottom));
    }

    public void setDrawables(Drawable top, Drawable bottom) {
        mDrawableTop = top;
        mDrawableBottom = bottom;
        invalidate();
    }

    public void setOffset(float offset) {
        if (offset == mOffset) {
            return;
        }

        // 如果过了临界点，切换波纹方向
        if (0 == mOffset && offset > 0) {
            mDirection = DIRECTION_RB_LT;
        } else if (1 == mOffset && offset < 1) {
            mDirection = DIRECTION_LT_RB;
        }

        mOffset = offset;
        invalidate();
    }
}
