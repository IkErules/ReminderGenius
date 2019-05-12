package ch.hslu.appe.reminder.genius.Activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import ch.hslu.appe.reminder.genius.Adapter.InstallationAdapter;
import ch.hslu.appe.reminder.genius.BroadCastReceiver.BootBroadcastReceiver;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.Fragment.SettingsFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;
import ch.hslu.appe.reminder.genius.Worker.NotificationWorker;

import static ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity.SHOW_INSTALLATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private InstallationViewModel installationViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private ContactViewModel contactViewModel;

    private RecyclerView recyclerView;
    private InstallationAdapter adapter;

    private SharedPreferences sharedPreferences;

    private SeekBar filterByExpireDateSeekBar;
    private TextView filterByExpireDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerTitle(GravityCompat.START, "TEST Title");
        drawer.setDrawerTitle(GravityCompat.END, "TEST Title");
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddInstallationActivity.class);
            startActivity(intent);
        });

        this.installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);
        this.productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        observeInstallation();
        setUpRecyclerView();

        this.setBroadCastReceiver();

      /* to set up some Products
        productCategoryViewModel.insert(new ProductCategory("Boiler", 1, "Standard Boiler für Warmwasser." ));
        productCategoryViewModel.insert(new ProductCategory("Heizung", 2, "Standard Heizung." ));
        productCategoryViewModel.insert(new ProductCategory("Solaranlage", 3, "Standard Solaranlage, mit einer Grösse von 2x3m pro Panel." ));
        */

        this.filterByExpireDateSeekBar = (SeekBar) findViewById(R.id.main_filter_by_expire_date_seekbar);
        this.filterByExpireDateTextView = (TextView) findViewById(R.id.main_filter_by_expire_date_text_view);
        this.filterByExpireDateTextView.setText(String.format(getApplicationContext().getResources().getString(R.string.main_text_view_months_filter), Integer.toString(this.sharedPreferences.getInt(SettingsFragment.NOTIFICATION_DURATION_MONTHS, 0))));
        this.filterByExpireDateSeekBar.setProgress(this.sharedPreferences.getInt(SettingsFragment.NOTIFICATION_DURATION_MONTHS, 0));
        this.setSeekBarListener();
        this.registerButtonListener();

    }

    private void registerButtonListener() {
        Button showAllInstallationsBtn = (Button) findViewById(R.id.main_show_all_installations_button);
        showAllInstallationsBtn.setOnClickListener((View v) -> {
            LocalDate expireDate = LocalDate.now().plusMonths(100);
            adapter.getFilterByExpireDate().filter(expireDate.toString());
        });
    }

    private void setSeekBarListener() {
        this.filterByExpireDateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                Log.i("MainActivity", "Changing seekbar's progress to: " + progress);
                LocalDate expireDate = LocalDate.now().plusMonths(progressValue);
                adapter.getFilterByExpireDate().filter(expireDate.toString());
                TextView seekBarText = (TextView) findViewById(R.id.main_filter_by_expire_date_text_view);
                seekBarText.setText(String.format(getApplicationContext().getResources().getString(R.string.main_text_view_months_filter), Integer.toString(progressValue)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("MainActivity", "Start Tracking Touch Seekbar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("MainActivity", "Stop Tracking Touch Seekbar");
            }
        });
    }

    private void setBroadCastReceiver() {
        Boolean displayNotification = this.sharedPreferences.getBoolean(SettingsFragment.SHOW_NOTIFICATIONS, true);
        if (!displayNotification) {
            Log.i("MainAcitivity", "Disabling BroadCastReceiver");
            BootBroadcastReceiver.disableBroadcastReceiver(this);
        } else {
            Log.i("MainAcitivity", "Enabling BroadCastReceiver");
            BootBroadcastReceiver.enableBroadcastReceiver(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Remove all Notifications on App Resume
        NotificationManager notificationManager = (NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent setPrefs = new Intent(this, AppPreferencesActivity.class);
            startActivity(setPrefs);
            return true;
        } else if (id == R.id.main_action_show_notification) {
            this.showTestNotifications();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showTestNotifications() {
        int limit = 2;
        int notificationCounter = 0;
        boolean limitReached = false;

        List<Installation> installations = adapter.getInstallations();
        for (Installation installation : installations) {
            if (notificationCounter >= limit) {
                break;
            }

            Log.d("NotificationWorker", "Creating notification for Installation: " + installation.toString());
            Intent showInstallationIntent = new Intent(getApplicationContext(), ShowInstallationActivity.class);
            showInstallationIntent.putExtra(SHOW_INSTALLATION, installation);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), installation.getInstallationId(), showInstallationIntent, 0);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("TEST-Notifications", "Default", NotificationManager.IMPORTANCE_DEFAULT);
                Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            }

            contactViewModel.getContactById(installation.getContactId()).observe(this, contact ->
                    Objects.requireNonNull(notificationManager).notify(installation.getInstallationId(), this.createNotification(
                            getApplicationContext().getResources().getString(R.string.notification_title),
                            "Kunde: " + contact.getLastName() + " " + installation.getFriendlyExpireDateAsString(),
                            "Installation: " + Integer.toString(installation.getInstallationId()) + "\n" +
                                    "Fälligkeitsdatum: " + installation.getFriendlyExpireDateAsString() + "\n" +
                                    "Produkt: " + Integer.toString(installation.getProductCategoryId()) + "\n" +
                                    "Kontakt: " + contact.getFormattedAddressWithName(),
                            pendingIntent))
                    );

            notificationCounter += 1;
        }
    }

    private Notification createNotification(String notificationTitle, String notificationText, String longText, PendingIntent onClickIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), "TEST-Notifications")
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_installation) {
            Intent intent = new Intent(this, InstallationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new InstallationAdapter.SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void observeInstallation() {
        recyclerView = findViewById(R.id.default_recycler_view);
        adapter = new InstallationAdapter(this, installationViewModel, productCategoryViewModel, contactViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.installationViewModel.getAllInstallations().observe(this, installations -> {
            // Update the cached copy of the words in the adapter.
            adapter.setInstallations(installations);
            adapter.getFilterByExpireDate().filter(LocalDate.now().plusMonths(this.filterByExpireDateSeekBar.getProgress()).toString());
        });
    }
}
