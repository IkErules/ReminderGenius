package ch.hslu.appe.reminder.genius.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.Adapter.ContactListAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;

public class ContactActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        observeContacts();
        registerListeners();
        setUpRecyclerView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void observeContacts() {
        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        recyclerView = findViewById(R.id.default_recycler_view);
        adapter = new ContactListAdapter(this, contactViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        fab.setOnClickListener(view -> {
            Log.d("ContactActivity", "Adding Contact, FAB Clicked.");
            Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
            startActivity(intent);
        });

        Button button = findViewById(R.id.contact_add_contact_button);
        button.setOnClickListener(view -> {
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_contact_menu, menu);

        this.addSearchListener(menu);

        return true;
    }

    private void addSearchListener(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.contact_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Set Icon on Keyboard
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(getString(R.string.contact_search_desc));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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

    private void setUpRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new ContactListAdapter.SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}