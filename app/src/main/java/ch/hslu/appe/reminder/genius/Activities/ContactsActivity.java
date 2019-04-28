package ch.hslu.appe.reminder.genius.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.CustomerListAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Customer;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ReminderGeniusViewModel;

public class ContactsActivity extends AppCompatActivity {

    private ReminderGeniusViewModel reminderGeniusViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final CustomerListAdapter adapter = new CustomerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reminderGeniusViewModel = ViewModelProviders.of(this).get(ReminderGeniusViewModel.class);

        reminderGeniusViewModel.getAllCustomers().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(@Nullable final List<Customer> customers) {
                // Update the cached copy of the words in the adapter.
                adapter.setCustomers(customers);
            }
        });

        final Button button = findViewById(R.id.btn_addTestContacts);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("contacts", "Adding Test Customer to DB.");
                Customer customer = new Customer("Roman",
                        "Schraner",
                        "0774690331",
                        "roman.schraner@stud.hslu.ch",
                        "Cheerstrasse 13f",
                        "Luzern",
                        6014);
                reminderGeniusViewModel.insert(customer);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}