package com.xmrlab.android;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xmrlab.android.logger.LogEntry;
import com.xmrlab.android.logger.LogStore;
import com.xmrlab.android.ota.NotificationCenter;
import com.xmrlab.android.ota.UpdateScheduler;
import com.xmrlab.android.utils.Utils;

public class XmrLabApp extends com.xmrlab.android.terminal.App {

    @Override
    public void onCreate() {
        super.onCreate();
        LogStore store = LogStore.init(this);
        store.add(LogEntry.INFO, "session", "==== XMR Lab " + BuildConfig.VERSION_NAME
                + " session start ====");
        String abi = Build.SUPPORTED_ABIS.length > 0 ? Build.SUPPORTED_ABIS[0] : "unknown";
        store.add(LogEntry.INFO, "session", "Device: " + Build.MANUFACTURER + " " + Build.MODEL
                + " · Android " + Build.VERSION.RELEASE
                + " (" + abi + ")");
        com.stryker.terminal.bridge.StrykerLog.install((level, tag, message, error) -> {
            LogStore s = LogStore.peek();
            if (s == null) return;
            String text = message;
            if (error != null) {
                text = (text == null || text.isEmpty() ? "" : text + System.lineSeparator())
                        + android.util.Log.getStackTraceString(error);
            }
            s.add(level, tag, text);
        });

        NotificationCenter.ensureChannel(this);
        UpdateScheduler.schedule(this);
        applyEdgeToEdgeInsets();
        com.xmrlab.android.engine.GuestShellFactory.install();
        com.xmrlab.android.terminal.backend.TerminalSession.setShellChooser(() -> {
            try {
                com.xmrlab.android.utils.Core core = new com.xmrlab.android.utils.Core(this);
                if (!com.xmrlab.android.engine.EngineType.isChosen(core)) return null;
                return com.xmrlab.android.engine.EngineType.isRootless(core) ? "ssh:guest" : null;
            } catch (Throwable t) {
                return null;
            }
        });
    }

    private void applyEdgeToEdgeInsets() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Utils.applySystemBarInsets(activity);
            }

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle state) {}

            @Override
            public void onActivityResumed(@NonNull Activity activity) {}

            @Override
            public void onActivityPaused(@NonNull Activity activity) {}

            @Override
            public void onActivityStopped(@NonNull Activity activity) {}

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle b) {}

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {}
        });
    }
}
