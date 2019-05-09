package ch.hslu.appe.reminder.genius.Worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import ch.hslu.appe.reminder.genius.Activity.MainActivity;
import ch.hslu.appe.reminder.genius.Adapter.ContactListAdapter;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.Repository.InstallationRepository;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;

public class NotificationWorker extends Worker,Lifecycle {

    // Interval for showing a notification (in months)
    private final static int INTERVAL = 24;

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

    private void observeInstallations(LocalDate expireDate) {
        this.installationDao.findInstallationsByExpireDate(expireDate).observe(getApplicationContext().get, new Observer<List<Installation>>() {
            @Override
            public void onChanged(@Nullable final List<Installation> installations) {
                // Update the cached copy of the words in the adapter.
                if (installations.size() > 0) {
                    Log.i("NotificationWorker", "Notification has to be sent, because there are triggerable Installations.");
                } else {
                    Log.d("NotificationWorker", "No triggerable Installations available.");
                }
            }
        });
    }

    private void triggerNotification(String title, String text, int id) {
        String installationData = getInputData().getString("test-property");
        Log.d("NotificationWorker", "Starting Task, received String: " + installationData);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("111", id);

        LocalDate currentDate = LocalDate.now();
        LocalDate dateToCompare = currentDate.plusMonths(this.INTERVAL);

        this.installationDao = ReminderGeniusRoomDb.getDatabase(getApplicationContext()).installationDao();
        this.installationDao.findInstallationsByExpireDate(dateToCompare);

        InstallationViewModel installationViewModel = new InstallationViewModel()

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        Objects.requireNonNull(notificationManager).notify(id, notification.build());
    }
}
