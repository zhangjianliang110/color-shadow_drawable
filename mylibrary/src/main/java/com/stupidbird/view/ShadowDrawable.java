package com.stupidbird.view;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

/**
 * 阴影图
 *
 * 该阴影的计算方式跟CardView阴影计算完全一致，这里只是把阴影绘制拆分成每个角、每条边，以及绘制起点、终点的控制，阴影颜色控制
 *
 * CardView阴影颜色不可选、不能控制只显示其中某一边不显示阴影，无法达到设计效果
 * {@link android.support.v7.widget.RoundRectDrawableWithShadow }
 *
 * Created by zhangjianliang on 2018/12/13
 *
 * 使用方式：
 * <code>

  View view = findViewById(R.id.xxx);
  Resources resources = getResources();
  ColorStateList backgroundColor = ColorStateList.valueOf(resources.getColor(R.color.white));//设置背景色

  ShadowDrawable shadow = new ShadowDrawable(resources, backgroundColor, CommonUtils.dip2px(2), CommonUtils.dip2px(5),
  CommonUtils.dip2px(5));
  shadow.setShadowColor(ColorUtils.getColor(R.color.common_color_fd5c59), ColorUtils.getColor(R.color.common_color_ffcfcf));

  shadow.setShadowCorner(ShadowDrawable.ShadowCorner.LEFT_TOP);
  shadow.setShadowSide(ShadowDrawable.ShadowSide.TOP);

  shadow.setBackgroundColor(ColorUtils.getColor(android.R.color.transparent));
  shadow.setShadowDistance(0, 0, 0, CommonUtils.dip2px(7));

  ViewUtil.setViewBackground(view, shadow);
 *
 * </code>
 */
public class ShadowDrawable extends Drawable {

    private final RectF mCornerRect = new RectF();

    // used to calculate content padding
    private static final double COS_45 = Math.cos(Math.toRadians(45));

    private static final float SHADOW_MULTIPLIER = 1.5f;

    private final int mInsetShadow; // extra shadow to avoid gaps between card and shadow

    private Paint mPaint;

    private Paint mCornerShadowPaint;

    private Paint mEdgeShadowPaint;

    private final RectF mCardBounds;

    private float mCornerRadius;

    private Path mCornerShadowPath;

    // actual value set by developer
    private float mRawMaxShadowSize;

    // multiplied value to account for shadow offset
    private float mShadowSize;

    // actual value set by developer
    private float mRawShadowSize;

    private ColorStateList mBackground;

    private boolean mDirty = true;

    private int mShadowStartColor;

    private int mShadowEndColor;

    private boolean mAddPaddingForCorners = true;

    /**
     * If shadow size is set to a value above max shadow, we print a warning
     */
    private boolean mPrintedShadowClipWarning = false;

    private int mCornerType = ShadowCorner.ALL;//默认四个角都有阴影

    private int mSideType = ShadowSide.ALL;//默认四条边都有阴影

    /**用于某些情况下，阴影起点/终点需要缩进的情况*/
    private int mLeftShadowDistance;//阴影与左边的距离
    private int mTopShadowDistance;//阴影与上边的距离
    private int mRightShadowDistance;//阴影与右边的距离
    private int mBottomShadowDistance;//阴影与下边的距离

    /**
     * 创建阴影背景
     *
     * @param backgroundColor 背景色
     * @param radius          圆角
     * @param shadowSize      阴影宽度
     * @param maxShadowSize   阴影最大宽度
     */
    public ShadowDrawable(Resources resources, ColorStateList backgroundColor, float radius, float shadowSize,
            float maxShadowSize) {
        mShadowStartColor = resources.getColor(R.color.shadowview_shadow_start_color);
        mShadowEndColor = resources.getColor(R.color.shadowview_shadow_end_color);
        mInsetShadow = resources.getDimensionPixelSize(R.dimen.shadowview_compat_inset_shadow);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        setBackground(backgroundColor);
        mCornerShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCornerShadowPaint.setStyle(Paint.Style.FILL);
        mCornerRadius = (int) (radius + .5f);
        mCardBounds = new RectF();
        mEdgeShadowPaint = new Paint(mCornerShadowPaint);
        mEdgeShadowPaint.setAntiAlias(false);
        setShadowSize(shadowSize, maxShadowSize);
    }

    private void setBackground(ColorStateList color) {
        mBackground = (color == null) ? ColorStateList.valueOf(Color.TRANSPARENT) : color;
        mPaint.setColor(mBackground.getColorForState(getState(), mBackground.getDefaultColor()));
    }

    /**
     * 转偶数
     */
    private int toEven(float value) {
        int i = (int) (value + .5f);
        if (i % 2 == 1) {
            return i - 1;
        }
        return i;
    }

    /**
     * 设置阴影颜色
     */
    public void setShadowColor(int startColor, int endColor) {
        if (mShadowStartColor == startColor && mShadowEndColor == endColor) {
            return;
        }
        mShadowStartColor = startColor;
        mShadowEndColor = endColor;
        mDirty = true;
        invalidateSelf();
    }

    void setAddPaddingForCorners(boolean addPaddingForCorners) {
        mAddPaddingForCorners = addPaddingForCorners;
        invalidateSelf();
    }

    /**
     *
     */
    public void setShadowDistance(int leftShadowDistance, int topShadowDistance, int rightShadowDistance, int bottomShadowDistance) {
        mLeftShadowDistance = leftShadowDistance;
        mRightShadowDistance = rightShadowDistance;
        mBottomShadowDistance = bottomShadowDistance;
        mTopShadowDistance = topShadowDistance;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        mCornerShadowPaint.setAlpha(alpha);
        mEdgeShadowPaint.setAlpha(alpha);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mDirty = true;
    }

    private void setShadowSize(float shadowSize, float maxShadowSize) {
        if (shadowSize < 0f) {
            throw new IllegalArgumentException("Invalid shadow size " + shadowSize + ". Must be >= 0");
        }
        if (maxShadowSize < 0f) {
            throw new IllegalArgumentException("Invalid max shadow size " + maxShadowSize + ". Must be >= 0");
        }
        shadowSize = toEven(shadowSize);
        maxShadowSize = toEven(maxShadowSize);
        if (shadowSize > maxShadowSize) {
            shadowSize = maxShadowSize;
            if (!mPrintedShadowClipWarning) {
                mPrintedShadowClipWarning = true;
            }
        }
        if (mRawShadowSize == shadowSize && mRawMaxShadowSize == maxShadowSize) {
            return;
        }
        mRawShadowSize = shadowSize;
        mRawMaxShadowSize = maxShadowSize;
        mShadowSize = (int) (shadowSize * SHADOW_MULTIPLIER + mInsetShadow + .5f);
        mDirty = true;
        invalidateSelf();
    }

    @Override
    public boolean getPadding(Rect padding) {
        int vOffset = (int) Math
                .ceil(calculateVerticalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners));
        int hOffset = (int) Math
                .ceil(calculateHorizontalPadding(mRawMaxShadowSize, mCornerRadius, mAddPaddingForCorners));
        padding.set(hOffset, vOffset, hOffset, vOffset);
        return true;
    }

    private float calculateVerticalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize * SHADOW_MULTIPLIER + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize * SHADOW_MULTIPLIER;
        }
    }

    private float calculateHorizontalPadding(float maxShadowSize, float cornerRadius, boolean addPaddingForCorners) {
        if (addPaddingForCorners) {
            return (float) (maxShadowSize + (1 - COS_45) * cornerRadius);
        } else {
            return maxShadowSize;
        }
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final int newColor = mBackground.getColorForState(stateSet, mBackground.getDefaultColor());
        if (mPaint.getColor() == newColor) {
            return false;
        }
        mPaint.setColor(newColor);
        mDirty = true;
        invalidateSelf();
        return true;
    }

    @Override
    public boolean isStateful() {
        return (mBackground != null && mBackground.isStateful()) || super.isStateful();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setCornerRadius(float radius) {
        if (radius < 0f) {
            throw new IllegalArgumentException("Invalid radius " + radius + ". Must be >= 0");
        }
        radius = (int) (radius + .5f);
        if (mCornerRadius == radius) {
            return;
        }
        mCornerRadius = radius;
        mDirty = true;
        invalidateSelf();
    }

    /** 设置四个角的阴影显示 */
    public void setShadowCorner(int cornerType) {
        if (mCornerType == cornerType) {
            return;
        }
        mCornerType = cornerType;
        mDirty = true;
        invalidateSelf();
    }

    /** 设置四条边的阴影显示 */
    public void setShadowSide(int sideType) {
        if (mSideType == sideType) {
            return;
        }
        mSideType = sideType;
        mDirty = true;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDirty) {
            buildComponents(getBounds());
            mDirty = false;
        }
        canvas.translate(0, mRawShadowSize / 2);
        drawShadow(canvas);
        canvas.translate(0, -mRawShadowSize / 2);
        adaptDrawRoundRect(canvas, mCardBounds, mCornerRadius, mPaint);
    }

    /**
     * This helper is set by CardView implementations.
     * Prior to API 17, canvas.drawRoundRect is expensive; which is why we need this interface
     * to draw efficient rounded rectangles before 17.
     */
    private void adaptDrawRoundRect(Canvas canvas, RectF bounds, float cornerRadius, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint);
        } else {
            final float twoRadius = cornerRadius * 2;
            final float innerWidth = bounds.width() - twoRadius - 1;
            final float innerHeight = bounds.height() - twoRadius - 1;
            if (cornerRadius >= 1f) {
                // increment corner radius to account for half pixels.
                float roundedCornerRadius = cornerRadius + .5f;
                mCornerRect.set(-roundedCornerRadius, -roundedCornerRadius, roundedCornerRadius,
                        roundedCornerRadius);
                int saved = canvas.save();
                canvas.translate(bounds.left + roundedCornerRadius,
                        bounds.top + roundedCornerRadius);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerHeight, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.translate(innerWidth, 0);
                canvas.rotate(90);
                canvas.drawArc(mCornerRect, 180, 90, true, paint);
                canvas.restoreToCount(saved);
                //draw top and bottom pieces
                canvas.drawRect(bounds.left + roundedCornerRadius - 1f, bounds.top,
                        bounds.right - roundedCornerRadius + 1f, bounds.top + roundedCornerRadius, paint);

                canvas.drawRect(bounds.left + roundedCornerRadius - 1f, bounds.bottom - roundedCornerRadius,
                        bounds.right - roundedCornerRadius + 1f, bounds.bottom, paint);
            }
            // center
            canvas.drawRect(bounds.left, bounds.top + cornerRadius, bounds.right, bounds.bottom - cornerRadius, paint);
        }
    }

    private void drawShadow(Canvas canvas) {
        final float inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2;
        final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
        final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
        drawCorner(canvas, inset);
        drawSide(canvas, inset, drawHorizontalEdges, drawVerticalEdges);
    }

    /** 画左边阴影 */
    private void drawLeftSide(Canvas canvas, float inset, boolean drawVerticalEdges) {
        if (!drawVerticalEdges) {
            return;
        }
        int saved = canvas.save();
        float left = 0;
        float right = mCardBounds.height() - 2 * mCornerRadius - mTopShadowDistance - mBottomShadowDistance;
        float realShadow = mCornerRadius + mShadowSize - inset;
        if (!hasLeftBottomCorner()) {
            left = -realShadow;
        }
        if (!hasLeftTopCorner()) {
            right += realShadow;
        }
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - mCornerRadius - mBottomShadowDistance);
        canvas.rotate(270f);
        canvas.drawRect(left, -mCornerRadius - mShadowSize, right, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画上边阴影 */
    private void drawTopSide(Canvas canvas, float inset, boolean drawHorizontalEdges) {
        if (!drawHorizontalEdges) {
            return;
        }
        int saved = canvas.save();
        float left = 0;
        float right = mCardBounds.width() - 2 * inset - mLeftShadowDistance - mRightShadowDistance;
        //float right = mCardBounds.width() - mCornerRadius - mShadowSize - mRightShadowDistance - mLeftShadowDistance;
        float realShadow = mCornerRadius + mShadowSize - inset;
        if (!hasLeftTopCorner()) {
            left = -realShadow;
        }
        if (!hasRightTopCorner()) {
            right += realShadow;
        }
        canvas.translate(mCardBounds.left + inset + mLeftShadowDistance, mCardBounds.top + mCornerRadius);
        //canvas.translate(mCardBounds.left + inset + mLeftShadowDistance, mCardBounds.top + inset);
        canvas.drawRect(left, -mCornerRadius - mShadowSize, right, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画右边阴影 */
    private void drawRightSide(Canvas canvas, float inset, boolean drawVerticalEdges) {
        if (!drawVerticalEdges) {
            return;
        }
        int saved = canvas.save();
        float left = 0;
        float right = mCardBounds.height() - 2 * mCornerRadius - mTopShadowDistance - mBottomShadowDistance;
        float realShadow = mCornerRadius + mShadowSize - inset;
        if (!hasRightTopCorner()) {
            left = -realShadow;
        }
        if (!hasRightBottomCorner()) {
            right += realShadow;
        }
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + mCornerRadius + mTopShadowDistance);
        canvas.rotate(90f);
        canvas.drawRect(left, -mCornerRadius - mShadowSize, right, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画底边阴影 */
    private void drawBottomSide(Canvas canvas, float inset, boolean drawHorizontalEdges) {
        if (!drawHorizontalEdges) {
            return;
        }
        int saved = canvas.save();
        float left = 0;
        float right = mCardBounds.width() - 2 * inset - mLeftShadowDistance - mRightShadowDistance;
        float realShadow = mCornerRadius + mShadowSize - inset;
        if (!hasRightBottomCorner()) {
            left = -realShadow;
        }
        if (!hasLeftBottomCorner()) {
            right += realShadow;
        }
        canvas.translate(mCardBounds.right - inset - mRightShadowDistance, mCardBounds.bottom - mCornerRadius);
        //canvas.translate(mCardBounds.right - inset - mRightShadowDistance, mCardBounds.bottom - inset);
        canvas.rotate(180f);
        canvas.drawRect(left, -mCornerRadius - mShadowSize, right, -mCornerRadius, mEdgeShadowPaint);
        canvas.restoreToCount(saved);
    }

    private void drawSide(Canvas canvas, float inset, boolean drawHorizontalEdges, boolean drawVerticalEdges) {
        switch (mSideType) {
            case ShadowSide.LEFT://左
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.TOP://上
                drawTopSide(canvas, inset, drawHorizontalEdges);
                break;
            case ShadowSide.RIGHT://右
                drawRightSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.BOTTOM://下
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.TOP://左、上
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.RIGHT://左、右
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.BOTTOM://左、下
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.TOP | ShadowSide.RIGHT://上、右
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawRightSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.TOP | ShadowSide.BOTTOM://上、下
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                break;
            case ShadowSide.RIGHT | ShadowSide.BOTTOM://右、下
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.TOP | ShadowSide.BOTTOM://左、上、下
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.TOP | ShadowSide.RIGHT://左、上、右
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.LEFT | ShadowSide.RIGHT | ShadowSide.BOTTOM://左、右、下
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            case ShadowSide.RIGHT | ShadowSide.TOP | ShadowSide.BOTTOM://右、上、下
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                break;
            case ShadowSide.ALL:
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
            default://默认四个角
                drawTopSide(canvas, inset, drawHorizontalEdges);
                drawRightSide(canvas, inset, drawVerticalEdges);
                drawBottomSide(canvas, inset, drawHorizontalEdges);
                drawLeftSide(canvas, inset, drawVerticalEdges);
                break;
        }
    }

    private void drawCorner(Canvas canvas, float inset) {
        switch (mCornerType) {
            case ShadowCorner.LEFT_TOP://左上
                drawLeftTop(canvas, inset);
                break;
            case ShadowCorner.RIGHT_TOP://右上
                drawRightTop(canvas, inset);
                break;
            case ShadowCorner.LEFT_BOTTOM://左下
                drawLeftBottom(canvas, inset);
                break;
            case ShadowCorner.RIGHT_BOTTOM://右下
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP://左上、右上
                drawLeftTop(canvas, inset);
                drawRightTop(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM://左上、左下
                drawLeftTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_BOTTOM://左上、右下
                drawLeftTop(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM://右上、左下
                drawRightTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                break;
            case ShadowCorner.RIGHT_TOP | ShadowCorner.RIGHT_BOTTOM://右上、右下
                drawRightTop(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM://左下、右下
                drawLeftBottom(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM://左上、右上、左下
                drawLeftTop(canvas, inset);
                drawRightTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP | ShadowCorner.RIGHT_BOTTOM://左上、右上、右下
                drawLeftTop(canvas, inset);
                drawRightTop(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM://左上、左下、右下
                drawLeftTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM://右上、左下、右下
                drawRightTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            case ShadowCorner.ALL:
                drawLeftTop(canvas, inset);
                drawRightTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
            default://默认四个角
                drawLeftTop(canvas, inset);
                drawRightTop(canvas, inset);
                drawLeftBottom(canvas, inset);
                drawRightBottom(canvas, inset);
                break;
        }
    }

    private boolean hasRightTopCorner() {
        return mCornerType == ShadowCorner.RIGHT_TOP
                || mCornerType == ShadowCorner.ALL
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_TOP)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_BOTTOM);
    }

    private boolean hasRightBottomCorner() {
        return mCornerType == ShadowCorner.RIGHT_BOTTOM
                || mCornerType == ShadowCorner.ALL
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.LEFT_TOP)
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.RIGHT_TOP)
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP)
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_BOTTOM | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_TOP);
    }

    private boolean hasLeftTopCorner() {
        return mCornerType == ShadowCorner.LEFT_TOP
                || mCornerType == ShadowCorner.ALL
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM);
    }

    private boolean hasLeftBottomCorner() {
        return mCornerType == ShadowCorner.LEFT_BOTTOM
                || mCornerType == ShadowCorner.ALL
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM)
                || mCornerType == (ShadowCorner.LEFT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM)
                || mCornerType == (ShadowCorner.RIGHT_TOP | ShadowCorner.LEFT_BOTTOM | ShadowCorner.RIGHT_BOTTOM);
    }

    /** 画左上角阴影 */
    private void drawLeftTop(Canvas canvas, float inset) {
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.top + mCornerRadius);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画左上角阴影 */
    private void drawLeftBottom(Canvas canvas, float inset) {
        int saved = canvas.save();
        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - mCornerRadius);
        canvas.rotate(270f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画右上角阴影 */
    private void drawRightTop(Canvas canvas, float inset) {
        int saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.top + mCornerRadius);
        canvas.rotate(90f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.restoreToCount(saved);
    }

    /** 画右下角阴影 */
    private void drawRightBottom(Canvas canvas, float inset) {
        int saved = canvas.save();
        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - mCornerRadius);
        canvas.rotate(180f);
        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
        canvas.restoreToCount(saved);
    }

    private void buildShadowCorners() {
        RectF innerBounds = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        RectF outerBounds = new RectF(innerBounds);
        outerBounds.inset(-mShadowSize, -mShadowSize);
        if (mCornerShadowPath == null) {
            mCornerShadowPath = new Path();
        } else {
            mCornerShadowPath.reset();
        }
        mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        mCornerShadowPath.moveTo(-mCornerRadius, 0);
        mCornerShadowPath.rLineTo(-mShadowSize, 0);
        // outer arc
        mCornerShadowPath.arcTo(outerBounds, 180f, 90f, false);
        // inner arc
        mCornerShadowPath.arcTo(innerBounds, 270f, -90f, false);
        mCornerShadowPath.close();
        float startRatio = mCornerRadius / (mCornerRadius + mShadowSize);
        mCornerShadowPaint.setShader(new RadialGradient(0, 0, mCornerRadius + mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, startRatio, 1f},
                Shader.TileMode.CLAMP));

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.setShader(new LinearGradient(0, -mCornerRadius + mShadowSize, 0,
                -mCornerRadius - mShadowSize,
                new int[]{mShadowStartColor, mShadowStartColor, mShadowEndColor},
                new float[]{0f, .5f, 1f}, Shader.TileMode.CLAMP));
        mEdgeShadowPaint.setAntiAlias(false);
    }

    private void buildComponents(Rect bounds) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        final float verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER;
        mCardBounds.set(bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset,
                bounds.right - mRawMaxShadowSize, bounds.bottom - verticalOffset);
        if (mCornerRadius > mCardBounds.height() / 2) {
            mCornerRadius = mCardBounds.height() /2;
        }
        buildShadowCorners();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public void getMaxShadowAndCornerPadding(Rect into) {
        getPadding(into);
    }

    public void setShadowSize(float size) {
        setShadowSize(size, mRawMaxShadowSize);
    }

    public void setMaxShadowSize(float size) {
        setShadowSize(mRawShadowSize, size);
    }

    public float getShadowSize() {
        return mRawShadowSize;
    }

    public float getMaxShadowSize() {
        return mRawMaxShadowSize;
    }

    public float getMinWidth() {
        final float content = 2
                * Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow + mRawMaxShadowSize / 2);
        return content + (mRawMaxShadowSize + mInsetShadow) * 2;
    }

    public float getMinHeight() {
        final float content = 2 * Math.max(mRawMaxShadowSize, mCornerRadius + mInsetShadow
                + mRawMaxShadowSize * SHADOW_MULTIPLIER / 2);
        return content + (mRawMaxShadowSize * SHADOW_MULTIPLIER + mInsetShadow) * 2;
    }

    public void setBackgroundColor(@ColorInt int color) {
        setColor(ColorStateList.valueOf(color));
    }

    private void setColor(@Nullable ColorStateList color) {
        setBackground(color);
        invalidateSelf();
    }

    public ColorStateList getColor() {
        return mBackground;
    }

    /** 四个角阴影配置 */
    public interface ShadowCorner {

        /** 左上角圆角 */
        int LEFT_TOP = 1;
        /** 右上角圆角 */
        int RIGHT_TOP = 1 << 1;
        /** 左下角圆角 */
        int LEFT_BOTTOM = 1 << 2;
        /** 右上角圆角 */
        int RIGHT_BOTTOM = 1 << 3;
        /** 四个角都有圆角 */
        int ALL = LEFT_TOP | RIGHT_TOP | LEFT_BOTTOM | RIGHT_BOTTOM;
    }

    /** 四边阴影控制配置 */
    public interface ShadowSide {

        /** 左边有阴影 */
        int LEFT = 1;
        /** 上边有阴影 */
        int TOP = 1 << 1;
        /** 右边有阴影 */
        int RIGHT = 1 << 2;
        /** 下边有阴影 */
        int BOTTOM = 1 << 3;
        /** 四边都有阴影 */
        int ALL = LEFT | TOP | RIGHT | BOTTOM;
    }
}