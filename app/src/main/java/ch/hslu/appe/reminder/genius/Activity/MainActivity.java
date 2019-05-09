package ch.hslu.appe.reminder.genius.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.Menu;

import java.util.concurrent.TimeUnit;

import ch.hslu.appe.reminder.genius.Adapter.InstallationAdapter;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.Worker.NotificationWorker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private InstallationViewModel installationViewModel;
    private RecyclerView recyclerView;
    private InstallationAdapter adapter;

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
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddInstallationActivity.class);
            startActivity(intent);
        });

        observeInstallation();
        setUpRecyclerView();

        this.scheduleNotification();

        /** to set up some Products
         ProductCategoryViewModel productViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
         productViewModel.insert(new ProductCategory("Product 1", 1, "Default description" ));
         productViewModel.insert(new ProductCategory("Product 2", 2, "Default description 2" ));
         productViewModel.insert(new ProductCategory("Product 3", 3, "Default description, but pretty long to check if there is a linebreak in AddInstallationView" ));
         */

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
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
        installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);

        recyclerView = findViewById(R.id.default_recycler_view);
        adapter = new InstallationAdapter(this, installationViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.installationViewModel.getAllInstallations().observe(this, installations -> {
            // Update the cached copy of the words in the adapter.
            adapter.setInstallations(installations);
        });
    }

    // Notification will run every 15 Minutes from now on.
    private void scheduleNotification() {
        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(false).build();
        Data installationData = new Data.Builder().putString("test-property", "test-value").build();

        // Minimum Schedule Interval: 15 Minutes
        PeriodicWorkRequest notificationRequestPeriodic = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15,
                TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(installationData)
                .build();

        WorkManager.getInstance().enqueue(notificationRequestPeriodic);
    }

    private void cancelNotification() {
        WorkManager instance = WorkManager.getInstance();
        instance.cancelAllWork();
    }
}
