package com.xmrlab.android.hid.backchannel;

public final class BackChannelMarker {

    private BackChannelMarker() {}

    public static final String PROFILE_NAME = "Back-Channel Bridge (HID + ACM)";
    public static final String VID = "0x1d50";
    public static final String PID = "0x6018";
    public static final String VID_BARE = "1D50";
    public static final String PID_BARE = "6018";
    public static final String MANUFACTURER = "XMR Lab";
    public static final String PRODUCT = "XMR Lab Back-Channel";
    public static final String SERIAL = "XMRLABBC1";

    public static final String HIDTOGO_PROFILE_NAME = "HID-to-Go (KBD + Mouse + Channel)";
    public static final String HIDTOGO_VID = "0x1d50";
    public static final String HIDTOGO_PID = "0x6019";
    public static final String HIDTOGO_VID_BARE = "1D50";
    public static final String HIDTOGO_PID_BARE = "6019";
    public static final String HIDTOGO_PRODUCT = "XMR Lab HID-to-Go";
    public static final String HIDTOGO_SERIAL = "XMRLABHTG1";
}
