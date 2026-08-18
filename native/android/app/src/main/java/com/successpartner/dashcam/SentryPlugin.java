package com.successpartner.dashcam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * JS bridge for sentry mode:
 *   Sentry.start()  -> start the foreground service
 *   Sentry.stop()   -> stop it
 *   Sentry.keepAwake() -> hold the screen on while the app is in the foreground
 *   Sentry.ignoreBatteryOptimizations() -> prompt to exempt from Doze
 */
@CapacitorPlugin(name = "Sentry")
public class SentryPlugin extends Plugin {

    @PluginMethod
    public void start(PluginCall call) {
        try {
            SentryService.start(getContext());
            call.resolve();
        } catch (Throwable t) {
            call.reject("Failed to start sentry service: " + t.getMessage(), t);
        }
    }

    @PluginMethod
    public void stop(PluginCall call) {
        try {
            SentryService.stop(getContext());
            call.resolve();
        } catch (Throwable t) {
            call.reject("Failed to stop sentry service: " + t.getMessage(), t);
        }
    }

    @PluginMethod
    public void keepAwake(PluginCall call) {
        try {
            getBridge().getActivity().getWindow()
                    .addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            call.resolve();
        } catch (Throwable t) {
            call.reject(t.getMessage(), t);
        }
    }

    @PluginMethod
    public void ignoreBatteryOptimizations(PluginCall call) {
        try {
            PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(getContext().getPackageName())) {
                Intent i = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Uri.parse("package:" + getContext().getPackageName()));
                getActivity().startActivity(i);
            }
            call.resolve();
        } catch (Throwable t) {
            call.reject(t.getMessage(), t);
        }
    }
}
