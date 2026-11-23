package com.xmrlab.android.ota;

import android.content.Context;

import com.xmrlab.android.engine.EngineType;

public final class QemuDownloader {

    private QemuDownloader() {}

    public static final class Bundle {
        public final RemoteManifest.Asset qemu;
        public final RemoteManifest.Asset kernel;
        public final RemoteManifest.Asset initrd;
        public final RemoteManifest.Asset libslirp;
        public final RemoteManifest.Asset rootfs;

        Bundle(RemoteManifest.Asset qemu, RemoteManifest.Asset kernel, RemoteManifest.Asset initrd,
               RemoteManifest.Asset libslirp, RemoteManifest.Asset rootfs) {
            this.qemu = qemu;
            this.kernel = kernel;
            this.initrd = initrd;
            this.libslirp = libslirp;
            this.rootfs = rootfs;
        }
    }

    public static Bundle resolve(Context context) {
        RemoteManifest manifest = ManifestService.fetch(context);
        boolean umlOnly = EngineType.isUml(new com.xmrlab.android.utils.Core(context));
        boolean usable = manifest != null && manifest.rootless != null
                && (umlOnly ? manifest.rootless.isCompleteForUml() : manifest.rootless.isComplete());
        if (usable) {
            RemoteManifest.RootlessAssets r = manifest.rootless;
            return new Bundle(r.qemu, r.kernel, r.initrd, r.libslirp, r.rootfs);
        }
        return new Bundle(
                null,
                new RemoteManifest.Asset(ApiEndpoints.FALLBACK_ROOTLESS_KERNEL, "", 0),
                new RemoteManifest.Asset(ApiEndpoints.FALLBACK_ROOTLESS_INITRD, "", 0),
                null,
                new RemoteManifest.Asset(ApiEndpoints.FALLBACK_ROOTLESS_ROOTFS, "", 0));
    }
}
