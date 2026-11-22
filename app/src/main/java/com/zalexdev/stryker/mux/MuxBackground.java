package com.zalexdev.stryker.mux;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

/**
 * MUX screen background: solid dark base, a faint 28dp grid, and two slowly rotating
 * arcs (one rotation per 40 s) in the top-right area. Put the screen content inside it.
 *
 * The grid is one tiny tiled bitmap, so drawing stays cheap, and the animation only runs
 * while the view is visible.
 */
public class MuxBackground extends FrameLayout {

    private static final long PERIOD_MS = 40_000L;
    private static final float MIN_STEP_DEG = 0.5f;

    private final Paint gridPaint = new Paint();
    private final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arcRect = new RectF();
    private final float density;

    private MuxColors colors;
    private float angle;
    private float pageShift;
    private float lastDrawnAngle = -1f;
    private ValueAnimator animator;

    public MuxBackground(Context context) {
        this(context, null);
    }

    public MuxBackground(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MuxBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = getResources().getDisplayMetrics().density;
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(density);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(3f * density);
        setWillNotDraw(false);
        refreshColors();
    }

    private void refreshColors() {
        colors = MuxColors.resolve(getContext());
        int tile = Math.max(8, Math.round(28f * density));
        Bitmap bmp = Bitmap.createBitmap(tile, tile, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        Paint line = new Paint();
        line.setColor(MuxColors.withAlpha(colors.primary, 0.05f));
        c.drawRect(0f, 0f, tile, 1f, line);
        c.drawRect(0f, 0f, 1f, tile, line);
        gridPaint.setShader(new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshColors();
        if (isShown()) {
            startAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (isVisible) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    /** Extra turn of the arcs (in pages), e.g. driven by a ViewPager. */
    public void setPageShift(float pages) {
        pageShift = pages;
        invalidate();
    }

    private void startAnimation() {
        if (animator != null || !isAttachedToWindow()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 26 && !ValueAnimator.areAnimatorsEnabled()) {
            return;
        }
        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(PERIOD_MS);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(a -> {
            float value = (float) a.getAnimatedValue();
            if (Math.abs(value - lastDrawnAngle) >= MIN_STEP_DEG) {
                angle = value;
                lastDrawnAngle = value;
                invalidate();
            }
        });
        animator.start();
    }

    private void stopAnimation() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        canvas.drawColor(colors.bg);
        canvas.drawRect(0f, 0f, w, h, gridPaint);

        float cx = w * 0.82f;
        float cy = h * 0.18f;
        float r = Math.min(w, h) * 0.35f;

        ringPaint.setColor(MuxColors.withAlpha(colors.primary, 0.07f));
        canvas.drawCircle(cx, cy, r * 0.8f, ringPaint);

        arcRect.set(cx - r, cy - r, cx + r, cy + r);
        canvas.save();
        canvas.rotate(angle + pageShift * 45f, cx, cy);
        arcPaint.setColor(MuxColors.withAlpha(colors.primary, 0.14f));
        canvas.drawArc(arcRect, 0f, 80f, false, arcPaint);
        arcPaint.setColor(MuxColors.withAlpha(colors.accent, 0.12f));
        canvas.drawArc(arcRect, 140f, 50f, false, arcPaint);
        canvas.restore();
    }
}
