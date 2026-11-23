package com.xmrlab.android.wifi.attack;

import com.xmrlab.android.R;

import static com.xmrlab.android.wifi.attack.AttackMetric.ATTEMPTS;
import static com.xmrlab.android.wifi.attack.AttackMetric.CANDIDATE;
import static com.xmrlab.android.wifi.attack.AttackMetric.CAPTURED;
import static com.xmrlab.android.wifi.attack.AttackMetric.CHANNEL;
import static com.xmrlab.android.wifi.attack.AttackMetric.CLIENTS;
import static com.xmrlab.android.wifi.attack.AttackMetric.CRACKED;
import static com.xmrlab.android.wifi.attack.AttackMetric.ELAPSED;
import static com.xmrlab.android.wifi.attack.AttackMetric.ETA;
import static com.xmrlab.android.wifi.attack.AttackMetric.FRAMES;
import static com.xmrlab.android.wifi.attack.AttackMetric.IFACE;
import static com.xmrlab.android.wifi.attack.AttackMetric.LEFT;
import static com.xmrlab.android.wifi.attack.AttackMetric.LOCK;
import static com.xmrlab.android.wifi.attack.AttackMetric.NETWORKS;
import static com.xmrlab.android.wifi.attack.AttackMetric.PIN;
import static com.xmrlab.android.wifi.attack.AttackMetric.PROGRESS;
import static com.xmrlab.android.wifi.attack.AttackMetric.RATE;
import static com.xmrlab.android.wifi.attack.AttackMetric.STATE;
import static com.xmrlab.android.wifi.attack.AttackMetric.TARGET;
import static com.xmrlab.android.wifi.attack.AttackMetric.TIMEOUT;
import static com.xmrlab.android.wifi.attack.AttackMetric.TRIED;
import static com.xmrlab.android.wifi.attack.AttackStage.ASSOC;
import static com.xmrlab.android.wifi.attack.AttackStage.CAPTURE;
import static com.xmrlab.android.wifi.attack.AttackStage.DEAUTH;
import static com.xmrlab.android.wifi.attack.AttackStage.EAPOL;
import static com.xmrlab.android.wifi.attack.AttackStage.INJECT;
import static com.xmrlab.android.wifi.attack.AttackStage.MONITOR;
import static com.xmrlab.android.wifi.attack.AttackStage.PINS;
import static com.xmrlab.android.wifi.attack.AttackStage.PIXIE;
import static com.xmrlab.android.wifi.attack.AttackStage.RADIO;
import static com.xmrlab.android.wifi.attack.AttackStage.SAVE;
import static com.xmrlab.android.wifi.attack.AttackStage.SWEEP;
import static com.xmrlab.android.wifi.attack.AttackStage.TRY;
import static com.xmrlab.android.wifi.attack.AttackStage.WORDLIST;
import static com.xmrlab.android.wifi.attack.AttackStage.WPS;

public enum AttackKind {

    PIXIE_DUST("Pixie Dust", R.drawable.autopixie, R.color.accent_wifi,
            new AttackStage[] { RADIO, ASSOC, WPS, PIXIE },
            new AttackMetric[] { ELAPSED, STATE, ATTEMPTS, LOCK },
            null),

    WPS_PIN_BRUTE("WPS pin bruteforce", R.drawable.fiber_pin, R.color.accent_wifi,
            new AttackStage[] { RADIO, ASSOC, WPS },
            new AttackMetric[] { ELAPSED, STATE, PIN, TRIED },
            null),

    WPS_PIN_ONE("WPS pin check", R.drawable.fiber_pin, R.color.accent_wifi,
            new AttackStage[] { RADIO, ASSOC, WPS },
            new AttackMetric[] { ELAPSED, STATE, PIN, LOCK },
            null),

    WPS_PIN_NULL("Null WPS pin", R.drawable.unlock, R.color.accent_wifi,
            new AttackStage[] { RADIO, ASSOC, WPS },
            new AttackMetric[] { ELAPSED, STATE, LOCK, ATTEMPTS },
            null),

    WPS_PIN_LIST("WPS pin list", R.drawable.number, R.color.accent_wifi,
            new AttackStage[] { PINS, RADIO, ASSOC, WPS },
            new AttackMetric[] { ELAPSED, STATE, PIN, PROGRESS, LEFT, LOCK },
            null),

    PSK_BRUTE("Passphrase bruteforce", R.drawable.key, R.color.accent_handshakes,
            new AttackStage[] { WORDLIST, TRY },
            new AttackMetric[] { ELAPSED, CANDIDATE, PROGRESS, RATE, ETA },
            "Association attempts per minute"),

    HANDSHAKE("Handshake capture", R.drawable.handshake_interface, R.color.accent_handshakes,
            new AttackStage[] { MONITOR, CAPTURE, AttackStage.TARGET, DEAUTH, EAPOL, SAVE },
            new AttackMetric[] { ELAPSED, CHANNEL, CLIENTS, STATE, FRAMES, RATE },
            null),

    DEAUTH_ONE("Deauthentication", R.drawable.deauth, R.color.red,
            new AttackStage[] { MONITOR, INJECT },
            new AttackMetric[] { ELAPSED, CHANNEL, FRAMES, RATE, STATE, IFACE },
            null),

    MASS_PIXIE("Pixie Dust sweep", R.drawable.autopixie, R.color.accent_wifi,
            new AttackStage[] { RADIO, SWEEP, ASSOC, WPS },
            new AttackMetric[] { ELAPSED, PROGRESS, TARGET, CRACKED, TIMEOUT, STATE },
            null),

    MASS_HANDSHAKE("Mass handshake capture", R.drawable.handshake_interface, R.color.accent_handshakes,
            new AttackStage[] { MONITOR, CAPTURE, DEAUTH, EAPOL, SAVE },
            new AttackMetric[] { ELAPSED, NETWORKS, CLIENTS, CAPTURED, STATE },
            null),

    MASS_DEAUTH("Broadcast deauthentication", R.drawable.deauth, R.color.red,
            new AttackStage[] { MONITOR, INJECT },
            new AttackMetric[] { ELAPSED, FRAMES, RATE, IFACE },
            null);

    public final String title;
    public final int icon;
    public final int accentRes;
    public final AttackStage[] stages;
    public final AttackMetric[] metrics;

    public final String rateLabel;

    AttackKind(String title, int icon, int accentRes,
               AttackStage[] stages, AttackMetric[] metrics, String rateLabel) {
        this.title = title;
        this.icon = icon;
        this.accentRes = accentRes;
        this.stages = stages;
        this.metrics = metrics;
        this.rateLabel = rateLabel;
    }
}
