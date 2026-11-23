package com.xmrlab.android.appintro;

import android.content.Context;
import android.util.AttributeSet;

import com.xmrlab.android.mux.MuxBackground;

/**
 * Onboarding background. It is now the MUX HUD background (dark base, faint grid, slowly
 * rotating arcs). The class name and the three methods below are kept so AppIntroActivity
 * and the layouts need no changes.
 */
public class IntroBackgroundView extends MuxBackground {

    public IntroBackgroundView(Context context) {
        super(context);
    }

    public IntroBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntroBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Called by the pager while swiping; the arcs turn a little with each page. */
    public void setPageOffset(float position) {
        setPageShift(position);
    }

    /** Kept for compatibility. The MUX background is always dark. */
    public void setDarkTheme(Boolean dark) {
        // intentionally empty
    }

    /** Kept for compatibility. */
    public void replayReveal() {
        // intentionally empty
    }
}
