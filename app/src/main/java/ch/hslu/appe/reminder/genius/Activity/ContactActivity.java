package ch.hslu.appe.reminder.genius.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.ContactListAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;

public class ContactActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.observeContacts();
        this.registerListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void observeContacts() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ContactListAdapter adapter = new ContactListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        /** TODO: Hat nach Rebase nicht mehr funktioniert :-/ */
        this.contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                // Update the cached copy of the words in the adapter.
                adapter.setContacts(contacts);
            }
        });
    }

    private void registerListeners() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ContactActivity", "Adding Contact, FAB Clicked.");
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button button = (Button) findViewById(R.id.contact_add_contact_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.i("contact", "Adding Test Contact to DB.");
                Contact contact = new Contact("Roman",
                        "Schraner",
                        "Person",
                        "",
                        "0774690331",
                        "roman.schraner@stud.hslu.ch",
                        "Cheerstrasse 13f",
                        "Luzern",
                        6014,
                        "Luzern",
                        "CH");
                contactViewModel.insert(contact);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact_setting_clear_db) {
            Toast.makeText(this, R.string.contact_toast_clear_db, Toast.LENGTH_LONG).show();

            contactViewModel.deleteAllContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}