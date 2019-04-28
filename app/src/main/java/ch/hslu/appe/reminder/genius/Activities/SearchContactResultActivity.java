package ch.hslu.appe.reminder.genius.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.reminder.genius.Adapters.SearchContactAdapter;
import ch.hslu.appe.reminder.genius.Models.SearchContact;
import ch.hslu.appe.reminder.genius.R;

public class SearchContactResultActivity extends AppCompatActivity {

    private List<SearchContact> searchContactsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchContactAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_contact_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        ArrayList<SearchContact> contacts = extras.getParcelableArrayList("contacts");
        searchContactsList.addAll(contacts);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new SearchContactAdapter(searchContactsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }
}
