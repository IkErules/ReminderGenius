package ch.hslu.appe.reminder.genius.Worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Objects;

import ch.hslu.appe.reminder.genius.Activity.MainActivity;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;

public class NotificationWorker extends Worker {

    // Interval for showing a notification (in months)
    private final static int INTERVAL = 24;
    public final static String INSTALLATIONS_EXPIRING_SOON = "installations.expiring.soon";

    private InstallationViewModel installationViewModel;
    private InstallationDao installationDao;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // trigger instant notification
        this.triggerNotification("TEST Notification", "Test Content", 1);
        return Result.success();
    }

    private void triggerNotification(String title, String text, int id) {
        int expiringInstallations = getInputData().getInt(this.INSTALLATIONS_EXPIRING_SOON, 0);
        Log.d("NotificationWorker", "Starting Task, received String: " + Integer.toString(expiringInstallations));

        if (expiringInstallations > 0) {
            Log.d("NotificationWorker", "Amount of soon expiring Instllations: " + Integer.toString(expiringInstallations));
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("111", id);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                    .setContentTitle(title)
                    .setContentText("Installations expiring soon: " + Integer.toString(expiringInstallations))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);

            Objects.requireNonNull(notificationManager).notify(id, notification.build());
        } else {
            Log.d("NotificationWorker", "No Installations expiring soon.");
        }
    }
}
