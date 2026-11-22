package com.zalexdev.stryker.mux;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import java.util.Locale;

import com.zalexdev.stryker.R;

/**
 * MUX HUD panel: translucent fill, 1dp border, top-left and bottom-right corners cut,
 * bright L-shaped ticks on the other two corners, optional small title with an amber marker.
 *
 * Usage:
 * <pre>
 * &lt;com.zalexdev.stryker.mux.MuxPanel
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content"
 *     app:muxTitle="STATUS"&gt;
 *     ... children ...
 * &lt;/com.zalexdev.stryker.mux.MuxPanel&gt;
 * </pre>
 * If no padding is given the panel uses 10dp / 8dp. The title adds 18dp on top of the top padding.
 */
public class MuxPanel extends FrameLayout {

    private final Path shape = new Path();
    private final Path ticks = new Path();
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF marker = new RectF();

    private final float density;
    private final float strokeW;
    private final float tickLen;
    private final float tickW;
    private float cut;

    private String title;
    private int userPadTop;
    private MuxColors colors;

    public MuxPanel(Context context) {
        this(context, null);
    }

    public MuxPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MuxPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = getResources().getDisplayMetrics().density;
        strokeW = density;
        tickLen = 10f * density;
        tickW = 2f * density;
        cut = 12f * density;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MuxPanel, defStyleAttr, 0);
        try {
            title = a.getString(R.styleable.MuxPanel_muxTitle);
            cut = a.getDimension(R.styleable.MuxPanel_muxCut, cut);
        } finally {
            a.recycle();
        }

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeW);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeWidth(tickW);
        titlePaint.setTypeface(Typeface.MONOSPACE);
        titlePaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10f, getResources().getDisplayMetrics()));
        titlePaint.setLetterSpacing(0.08f);

        int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = getPaddingRight();
        int b = getPaddingBottom();
        if (l == 0 && t == 0 && r == 0 && b == 0) {
            l = r = Math.round(10f * density);
            t = b = Math.round(8f * density);
        }
        setPadding(l, t, r, b);

        setWillNotDraw(false);
        refreshColors();
    }

    /** Sets or clears the title. */
    public void setTitle(String newTitle) {
        title = newTitle;
        super.setPadding(getPaddingLeft(), userPadTop + titleExtra(),
                getPaddingRight(), getPaddingBottom());
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        userPadTop = top;
        super.setPadding(left, top + titleExtra(), right, bottom);
    }

    private int titleExtra() {
        return (title != null && !title.isEmpty()) ? Math.round(18f * density) : 0;
    }

    private void refreshColors() {
        colors = MuxColors.resolve(getContext());
        fillPaint.setColor(MuxColors.withAlpha(colors.panel, 0.82f));
        strokePaint.setColor(colors.border);
        tickPaint.setColor(colors.primary);
        markerPaint.setColor(colors.accent);
        titlePaint.setColor(colors.primary);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshColors();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        buildPaths(w, h);
    }

    private void buildPaths(int w, int h) {
        float half = strokeW / 2f;
        float c = Math.min(cut, Math.min(w, h) / 2f);

        shape.reset();
        shape.moveTo(half + c, half);
        shape.lineTo(w - half, half);
        shape.lineTo(w - half, h - half - c);
        shape.lineTo(w - half - c, h - half);
        shape.lineTo(half, h - half);
        shape.lineTo(half, half + c);
        shape.close();

        float tw = tickW / 2f;
        ticks.reset();
        // top-right
        ticks.moveTo(w - tickLen, tw);
        ticks.lineTo(w - tw, tw);
        ticks.lineTo(w - tw, tickLen);
        // bottom-left
        ticks.moveTo(tw, h - tickLen);
        ticks.lineTo(tw, h - tw);
        ticks.lineTo(tickLen, h - tw);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(shape, fillPaint);
        canvas.drawPath(shape, strokePaint);
        canvas.drawPath(ticks, tickPaint);

        if (title != null && !title.isEmpty()) {
            float rowCenter = userPadTop + 9f * density;
            float left = getPaddingLeft();
            marker.set(left, rowCenter - 5f * density, left + 4f * density, rowCenter + 5f * density);
            canvas.drawRect(marker, markerPaint);

            Paint.FontMetrics fm = titlePaint.getFontMetrics();
            float baseline = rowCenter - (fm.ascent + fm.descent) / 2f;
            canvas.drawText(title.toUpperCase(Locale.ROOT), left + 10f * density, baseline, titlePaint);
        }
    }
}
