package com.xmrlab.android.mux;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import com.xmrlab.android.R;

/**
 * Resolves the MUX palette from the current theme (muxBg, muxPanel, ... in attrs_mux.xml),
 * so views follow whichever palette the activity theme carries (cyan or amber).
 */
public final class MuxColors {

    public final int bg;
    public final int panel;
    public final int primary;
    public final int dim;
    public final int accent;
    public final int danger;
    public final int text;
    public final int border;

    private MuxColors(int bg, int panel, int primary, int dim, int accent,
                      int danger, int text, int border) {
        this.bg = bg;
        this.panel = panel;
        this.primary = primary;
        this.dim = dim;
        this.accent = accent;
        this.danger = danger;
        this.text = text;
        this.border = border;
    }

    public static MuxColors resolve(Context c) {
        return new MuxColors(
                attr(c, R.attr.muxBg, 0xFF040A12),
                attr(c, R.attr.muxPanel, 0xFF08141F),
                attr(c, R.attr.muxPrimary, 0xFF19E3FF),
                attr(c, R.attr.muxDim, 0xFF3C8A99),
                attr(c, R.attr.muxAccent, 0xFFFFB000),
                attr(c, R.attr.muxDanger, 0xFFFF5252),
                attr(c, R.attr.muxText, 0xFFD6F8FF),
                attr(c, R.attr.muxBorder, 0x8019E3FF));
    }

    /** Same colour with a different alpha (0..1). */
    public static int withAlpha(int color, float alpha) {
        return ColorUtils.setAlphaComponent(color, Math.round(255f * alpha));
    }

    private static int attr(Context c, int attrId, int fallback) {
        TypedValue tv = new TypedValue();
        if (c.getTheme().resolveAttribute(attrId, tv, true)) {
            if (tv.type >= TypedValue.TYPE_FIRST_COLOR_INT
                    && tv.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                return tv.data;
            }
            if (tv.resourceId != 0) {
                try {
                    return ContextCompat.getColor(c, tv.resourceId);
                } catch (Resources.NotFoundException ignored) {
                    // fall through to the default
                }
            }
        }
        return fallback;
    }
}
