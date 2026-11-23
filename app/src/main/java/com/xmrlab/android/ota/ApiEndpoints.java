package com.xmrlab.android.ota;

public final class ApiEndpoints {

    public static final String GITHUB_REPO = "https://github.com/mahfuz0712/XMR-Lab";

    public static final String MANIFEST_URL =
            "https://raw.githubusercontent.com/mahfuz0712/XMR-Lab/main/stryker_manifest.json";

    private static final String ASSET_BASE =
            "https://github.com/mahfuz0712/XMR-Lab/releases/download/xmr-assets-651/";

    public static final String FALLBACK_CHROOT_64 =
            ASSET_BASE + "chroot64-debian.tar.gz";
    public static final String FALLBACK_ROOTLESS_KERNEL = ASSET_BASE + "Image";
    public static final String FALLBACK_ROOTLESS_INITRD = ASSET_BASE + "initrd.img";
    public static final String FALLBACK_ROOTLESS_ROOTFS = ASSET_BASE + "rootfs.imgz";

    public static final String PREFS = "xmr_lab_ota";

    private ApiEndpoints() {
    }
}
