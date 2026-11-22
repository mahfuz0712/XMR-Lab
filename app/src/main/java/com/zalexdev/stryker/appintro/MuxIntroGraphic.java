package com.zalexdev.stryker.appintro;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class MuxIntroGraphic extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private static final int CYAN = 0xFF18C7E8;
    private static final int ORANGE = 0xFFFF9A52;
    private static final int TEXT = 0xFFEAF7FF;
    private static final int DIM = 0x665A9AA8;

    private float rotation;
    private float pulse;
    private ValueAnimator animator;

    public MuxIntroGraphic(Context context) {
        super(context);
        init();
    }

    public MuxIntroGraphic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MuxIntroGraphic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(9000L);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(animation -> {
            rotation = (float) animation.getAnimatedValue();
            pulse = (float) Math.sin(Math.toRadians(rotation * 2f));
            invalidate();
        });

        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) * 0.36f;

        // Outer HUD ring
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.2f);
        paint.setColor(DIM);
        canvas.drawCircle(cx, cy, radius * 1.35f, paint);

        // Inner rings
        paint.setColor(0x5532CDE8);
        canvas.drawCircle(cx, cy, radius, paint);
        canvas.drawCircle(cx, cy, radius * 0.72f, paint);

        // Rotating cyan arc
        rect.set(
                cx - radius * 1.35f,
                cy - radius * 1.35f,
                cx + radius * 1.35f,
                cy + radius * 1.35f
        );

        paint.setColor(CYAN);
        paint.setStrokeWidth(3f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rect, rotation, 72f, false, paint);

        // Orange secondary arc
        paint.setColor(ORANGE);
        paint.setStrokeWidth(2.5f);
        canvas.drawArc(rect, -rotation * 0.7f, 34f, false, paint);

        // Crosshair
        paint.setStrokeWidth(1f);
        paint.setColor(0x6658DDF0);
        canvas.drawLine(cx - radius * 1.5f, cy, cx + radius * 1.5f, cy, paint);
        canvas.drawLine(cx, cy - radius * 1.5f, cx, cy + radius * 1.5f, paint);

        // Center core
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0x2218C7E8);
        canvas.drawCircle(cx, cy, radius * (0.48f + pulse * 0.025f), paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.5f);
        paint.setColor(CYAN);
        canvas.drawCircle(cx, cy, radius * 0.48f, paint);

        // centre label
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(TEXT);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(android.graphics.Typeface.create(
                "sans-serif-medium",
                android.graphics.Typeface.BOLD
        ));
        paint.setTextSize(radius * 0.43f);
        canvas.drawText("XMR", cx, cy + radius * 0.15f, paint);

        // Small status marker
        paint.setColor(ORANGE);
        canvas.drawCircle(cx + radius * 0.95f, cy - radius * 0.95f, 3.5f, paint);

        // Bottom status line
        paint.setColor(CYAN);
        paint.setTextSize(Math.max(8f, radius * 0.12f));
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("SYSTEM INITIALIZATION", cx, cy + radius * 1.72f, paint);
    }
}
