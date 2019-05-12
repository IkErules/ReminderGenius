package ch.hslu.appe.reminder.genius.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ch.hslu.appe.reminder.genius.Adapter.InstallationAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

public class InstallationActivity extends AppCompatActivity {
    private InstallationViewModel installationViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private ContactViewModel contactViewModel;

    private RecyclerView recyclerView;
    private InstallationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.registerListeners();
        this.observeInstallations();
        this.setUpRecyclerView();

    }

    private void observeInstallations() {
        this.installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);
        this.productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        recyclerView = findViewById(R.id.installation_recycler_view);
        adapter = new InstallationAdapter(this, installationViewModel, productCategoryViewModel, contactViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.installationViewModel.getAllInstallations().observe(this, new Observer<List<Installation>>() {
            @Override
            public void onChanged(@Nullable final List<Installation> installations) {
                // Update the cached copy of the words in the adapter.
                adapter.setInstallations(installations);
            }
        });
    }

    private void registerListeners() {
        FloatingActionButton fab = findViewById(R.id.installation_fab);
        fab.setOnClickListener(view -> {
            Log.d("InstallationActivity", "Adding Installation, FAB Clicked.");
            Intent intent = new Intent(getApplicationContext(), AddInstallationActivity.class);
            startActivity(intent);
        });
    }

    private void setUpRecyclerView() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new InstallationAdapter.SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_installation_menu, menu);

        this.addSearchListener(menu);

        return true;
    }

    private void addSearchListener(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.installation_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Set Icon on Keyboard
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(getString(R.string.installation_search_desc));

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
}
