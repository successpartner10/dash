package com.successpartner.dashcam;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Local (on-device) notifications for the camera device — e.g. "Person detected".
 * Mirrors the JS `Native.notify(title, body)` bridge so the web app can raise
 * notifications even when the screen is off (Android 13+ requires POST_NOTIFICATIONS,
 * which the app requests at runtime via the WebView permissions flow).
 */
public final class NotificationHelper {

    private static final String CHANNEL_ID = "events";
    private static int counter = 2000;

    private NotificationHelper() {}

    public static void notify(Context ctx, String title, String body) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Events", NotificationManager.IMPORTANCE_HIGH);
            ch.setDescription("Motion, impact and detection alerts");
            nm.createNotificationChannel(ch);
        }

        Intent launch = new Intent(ctx, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                ctx, 0, launch, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification.Builder b = (Build.VERSION.SDK_INT >= 26)
                ? new Notification.Builder(ctx, CHANNEL_ID)
                : new Notification.Builder(ctx);

        Notification n = b.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        nm.notify(counter++, n);
    }
}
