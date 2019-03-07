package com.jpfeng.adv;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2019/3/6
 */
public class ParallaxView extends AppCompatImageView {

    public ParallaxView(Context context) {
        this(context, null);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(ScaleType.MATRIX);
        setImageMatrix(generateMatrix(0));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setImageMatrix(generateMatrix(0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setImageMatrix(generateMatrix(0));
    }

    private Matrix generateMatrix(float offset) {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return null;
        }

        int drawableW = drawable.getIntrinsicWidth();
        int drawableH = drawable.getIntrinsicHeight();
        int viewW = getMeasuredWidth();
        int viewH = getMeasuredHeight();

        // 生成 Matrix
        Matrix matrix = new Matrix();
        float scale = (float) viewW / drawableW;
        matrix.preScale(scale, scale);
        matrix.preTranslate(0, -((drawableH - viewH) * scale * offset));
        return matrix;
    }

    public void setOffset(float offset) {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }

        // 使用 Matrix 进行变换
        setImageMatrix(generateMatrix(offset));
    }
}
