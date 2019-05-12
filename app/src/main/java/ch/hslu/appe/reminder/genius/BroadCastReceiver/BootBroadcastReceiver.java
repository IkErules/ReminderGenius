package ch.hslu.appe.reminder.genius.BroadCastReceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.Fragment.SettingsFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.Worker.NotificationWorker;

import static ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity.SHOW_INSTALLATION;


public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            Log.d("BootBroadcastReceiver", "Received Boot Complete Event.");

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

            int frequencyInDays = sharedPreferences.getInt(SettingsFragment.NOTIFICATION_FREQUENCY_DAYS, 0);

            NotificationWorker.enqueueSelf(frequencyInDays);

            /*this.showTestNotifications(context);

            Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(false).build();
            Data installationData = new Data.Builder().putInt(NotificationWorker.INSTALLATIONS_EXPIRING_SOON, 1).build();

            // Minimum Schedule Interval: 15 Minutes
            PeriodicWorkRequest notificationRequestPeriodic = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15,
                    TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setInputData(installationData)
                    .build();

            WorkManager.getInstance().enqueue(notificationRequestPeriodic);*/
        }
    }

    private void showTestNotifications(Context context) {
        int limit = 2;
        int notificationCounter = 0;
        boolean limitReached = false;

            Log.d("NotificationWorker", "Creating notification for Installation: 1");
            Intent showInstallationIntent = new Intent(context, ShowInstallationActivity.class);
            showInstallationIntent.putExtra(SHOW_INSTALLATION, "test");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, showInstallationIntent, 0);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("TEST-Notifications", "Default", NotificationManager.IMPORTANCE_DEFAULT);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            }

            Objects.requireNonNull(notificationManager).notify(1, this.createNotification(context,
                    context.getResources().getString(R.string.notification_title),
                    context.getResources().getString(R.string.notification_text) + ": " + Integer.toString(1),
                    "Installation: " + Integer.toString(1) + "\n" +
                            "Fälligkeitsdatum: 1 \n" +
                            "Produkt: 1 \n" +
                            "Kontakt: 1",
                    pendingIntent));
    }

    private Notification createNotification(Context context, String notificationTitle, String notificationText, String longText, PendingIntent onClickIntent) {
        return new NotificationCompat.Builder(context, "TEST-Notifications")
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(onClickIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(longText))
                .setOngoing(false)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
    }

    public static void enableBroadcastReceiver(Context context) {
        Log.d("BootBroadcastReceiver", "Enabling Broadcast Receiver.");
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disableBroadcastReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, BootBroadcastReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
