package ch.hslu.appe.reminder.genius.Worker;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import ch.hslu.appe.reminder.genius.Activity.MainActivity;
import ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.Repository.InstallationRepository;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

import static ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity.SHOW_INSTALLATION;

public class NotificationWorker extends Worker {

    public final static String INSTALLATIONS_EXPIRING_SOON = "installations.expiring.soon";
    private final static String NOTIFICATION_CHANNEL = "installations.expiring.soon.notification";

    private InstallationViewModel installationViewModel;
    private InstallationDao installationDao;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        this.installationDao = ReminderGeniusRoomDb.getDatabase(context).installationDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        // trigger instant notification
        this.triggerNotification();
        return Result.success();
    }

    private void triggerNotification() {
        int expiringInstallationId = getInputData().getInt(this.INSTALLATIONS_EXPIRING_SOON, 0);
        Log.d("NotificationWorker", "Starting Task, received InstallationID: " + Integer.toString(expiringInstallationId));

        if (expiringInstallationId != 0) {
            Installation installation = this.installationDao.findSingleInstallationById(expiringInstallationId);

            Log.d("NotificationWorker", "Creating notification for Installation: " + installation.toString());
            Intent showInstallationIntent = new Intent(getApplicationContext(), ShowInstallationActivity.class);
            showInstallationIntent.putExtra(SHOW_INSTALLATION, installation);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), expiringInstallationId, showInstallationIntent, 0);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, "Default", NotificationManager.IMPORTANCE_DEFAULT);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            }

            Objects.requireNonNull(notificationManager).notify(installation.getInstallationId(), this.createNotification(
                    getApplicationContext().getResources().getString(R.string.notification_title),
                    getApplicationContext().getResources().getString(R.string.notification_text) + ": " + Integer.toString(installation.getInstallationId()),
                    "Installation: " + Integer.toString(installation.getInstallationId()) + "\n" +
                            "Fälligkeitsdatum: " + installation.getExpireDate().toString() + "\n" +
                            "Produkt: " + Integer.toString(installation.getProductCategoryId()) + "\n" +
                            "Kontakt: " + Integer.toString(installation.getContactId()),
                    pendingIntent));
        } else {
            Log.d("NotificationWorker", "Received invalid InstallationId: " + Integer.toString(expiringInstallationId));
        }
    }

    private Notification createNotification(String notificationTitle, String notificationText, String expandedText, PendingIntent onClickIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(onClickIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(expandedText))
                .setOngoing(false)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
    }
}
