package ch.hslu.appe.reminder.genius.Worker;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import ch.hslu.appe.reminder.genius.Activity.MainActivity;
import ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity;
import ch.hslu.appe.reminder.genius.DB.Dao.ContactDao;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.DB.Dao.ProductCategoryDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;
import ch.hslu.appe.reminder.genius.Fragment.SettingsFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.Repository.InstallationRepository;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

import static ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity.SHOW_INSTALLATION;

public class NotificationWorker extends Worker {

    public static final String INSTALLATIONS_EXPIRING_SOON = "installations.expiring.soon";
    private static final String NOTIFICATION_CHANNEL = "installations.expiring.soon.notification";
    private static final String NOTIFICATION_GROUP_KEY_SERVICE_EXPIRE = "\"installations.expiring.soon.notification.group";
    private static final String UNIQUE_WORKER_NAME = "notification.worker";

    private InstallationDao installationDao;
    private ContactDao contactDao;
    private ProductCategoryDao productCategoryDao;

    private Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        this.context = context;
        this.installationDao = ReminderGeniusRoomDb.getDatabase(context).installationDao();
        this.contactDao = ReminderGeniusRoomDb.getDatabase(context).contactDao();
        this.productCategoryDao = ReminderGeniusRoomDb.getDatabase(context).productCategoryDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        // trigger instant notification
        this.triggerNotification();
        return Result.success();
    }

    public static void enqueueSelf(int repeatIntervalDays) {
        WorkManager.getInstance().enqueueUniquePeriodicWork(UNIQUE_WORKER_NAME, ExistingPeriodicWorkPolicy.REPLACE, getOwnWorkRequest(repeatIntervalDays));
    }

    private static PeriodicWorkRequest getOwnWorkRequest(int repeatIntervalDays) {
        return new PeriodicWorkRequest.Builder(
                NotificationWorker.class,
                repeatIntervalDays,
                TimeUnit.DAYS,
                60,
                TimeUnit.MINUTES
        ).build();
    }

    private void triggerNotification() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        int durationInMonths = sharedPreferences.getInt(SettingsFragment.NOTIFICATION_DURATION_MONTHS, 0);

        List<Installation> installations = this.installationDao.findInstallationsByExpireDateSync(LocalDate.now().plusMonths(durationInMonths));

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,
                    getApplicationContext().getResources().getString(R.string.notification_category_text),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getApplicationContext().getResources().getString(R.string.notification_category_description_text));
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        for (Installation installation : installations) {
            Log.d("NotificationWorker", "Creating Notification for expiring Notification: " + installation.toString());

            Contact contact = this.contactDao.findSingleContactById(installation.getContactId());
            ProductCategory productCategory = this.productCategoryDao.findSingleProductCategoryById(installation.getProductCategoryId());

            Intent showInstallationIntent = new Intent(getApplicationContext(), ShowInstallationActivity.class);
            showInstallationIntent.putExtra(SHOW_INSTALLATION, installation);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), installation.getInstallationId(), showInstallationIntent, 0);

            Objects.requireNonNull(notificationManager).notify(installation.getInstallationId(), this.createNotification(
                    getApplicationContext().getResources().getString(R.string.notification_title),
                    String.format(getApplicationContext().getResources().getString(R.string.notification_text), productCategory.getCategoryName(), contact.getFirstName() + " " + contact.getLastName()),
                    contact.getFirstName() + " " + contact.getLastName() + "\n" +
                            getApplicationContext().getResources().getString(R.string.notification_due_date_text) + ": " + installation.getFriendlyExpireDateAsString() + "\n" +
                            getApplicationContext().getResources().getString(R.string.notification_product_text) + ": " + productCategory.getCategoryName(),
                    pendingIntent));
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
                // .setGroup(NOTIFICATION_GROUP_KEY_SERVICE_EXPIRE)
                .build();
    }
}
