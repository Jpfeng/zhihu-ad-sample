# zhihu-ad-sample

实现知乎首页广告样式

## 实现原理

给 `RecyclerView` 添加滚动监听，获取当前可见条目的位置范围，遍历这些条目的 `ViewHolder` 并找到相应的广告效果条目。然后根据列表的高度，条目的位置等信息计算出一个条目的偏移量，或者条目说动画效果的进度。传给自定义 View 进行绘制。

```java
list.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();

        // 遍历当前可见
        for (int i = first; i <= last; i++) {
            RecyclerView.ViewHolder holder = list.findViewHolderForAdapterPosition(i);
            // 找到需要操作的 item
            if (holder instanceof ListAdapter.ParallaxItemHolder) {
                int itemTop = holder.itemView.getTop();
                int listH = layoutManager.getHeight();
                int parallaxTop = ((ListAdapter.ParallaxItemHolder) holder).pvParallax.getTop();
                int parallaxH = ((ListAdapter.ParallaxItemHolder) holder).pvParallax.getMeasuredHeight();

                // 计算偏移比例
                float offset = (float) (itemTop + parallaxTop) / (listH - parallaxH);
                // 限制在 0.0 ~ 1.0 之间
                offset = Math.max(Math.min(offset, 1f), 0f);
                ((ListAdapter.ParallaxItemHolder) holder).pvParallax.setOffset(offset);
            }
        }
    }
});
```

### 视差效果

![parallax-preview](https://github.com/Jpfeng/zhihu-ad-sample/blob/master/media/parallax.gif)

视差效果视图继承自 `ImageView`。将 `ImageView` 的 `scaleType` 固定为 `ScaleType.MATRIX`，使用 `Matrix` 控制图片的缩放及位移。根据传入的偏移量生成矩阵。

```java
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
```

### 波纹效果

![ripple-preview](https://github.com/Jpfeng/zhihu-ad-sample/blob/master/media/ripple.gif)

波纹效果使用自定义 View，在 `Canvas` 上绘制相应的图片并擦除波纹的圆形部分。

```java
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

    mPaint.setXfermode(MOD_CLEAR);
    mDrawableBottom.draw(canvas);
    int layer1 = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
    mDrawableTop.draw(canvas);
    float r1 = (float) (Math.hypot(width, height) * mOffset);
    mOvalRect.set(width - r1, height - r1, width + r1, height + r1);
    canvas.drawOval(mOvalRect, mPaint);
    canvas.restoreToCount(layer1);
}
```
