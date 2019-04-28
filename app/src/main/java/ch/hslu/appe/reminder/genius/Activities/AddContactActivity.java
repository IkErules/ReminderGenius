package ch.hslu.appe.reminder.genius.Activities;

import android.app.SearchableInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ch.hslu.appe.reminder.genius.AsyncTasks.LoadContactDataAsync;
import ch.hslu.appe.reminder.genius.R;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addSearchListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addSearchListener() {
        SearchView searchView = findViewById(R.id.search_view_contact_name);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadContactDataAsync loadingData = new LoadContactDataAsync(AddContactActivity.this);
                loadingData.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
