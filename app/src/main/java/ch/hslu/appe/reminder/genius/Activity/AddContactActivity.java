package ch.hslu.appe.reminder.genius.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import ch.hslu.appe.reminder.genius.AsyncTask.LoadContactDataAsync;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.Model.SearchContact;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;

public class AddContactActivity extends AppCompatActivity {

    public static final int PICK_CONTACT_REQUEST = 59;
    public static final String EDIT_CONTACT = "contact.to.edit";
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent caller = getIntent();
        Contact contact = caller.getParcelableExtra(EDIT_CONTACT);
        // TODO: Process Contact and Display

        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        addSearchListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Contact contact = createContactFromTextFields();
            Log.i("AddContactActivity", "Adding Contact to DB, contact: " + contact.toString());
            this.contactViewModel.insert(contact);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSearchListener() {
        SearchView searchView = findViewById(R.id.add_contact_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadContactDataAsync loadingData = new LoadContactDataAsync(AddContactActivity.this, AddContactActivity.this);
                loadingData.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("search.contact.to.import")) {
                    SearchContact selectedContact = data.getParcelableExtra("search.contact.to.import");
                    Log.d("AddContactActivity", String.format("Selected contact: %s", selectedContact.toString()));

                    setTextFields(selectedContact);
                    resetSearchView();
                }
            }
        }
    }

    private void setTextFields(SearchContact searchContact) {
        TextView firstname = findViewById(R.id.add_contact_firstname_text_view);
        firstname.setText(searchContact.getFirstname());

        TextView name = findViewById(R.id.add_contact_name_text_view);
        name.setText(searchContact.getName());

        TextView address = findViewById(R.id.add_contact_address_text_view);
        address.setText(searchContact.getStreet());

        TextView zip = findViewById(R.id.add_contact_zip_text_view);
        zip.setText(searchContact.getZipString());

        TextView city = findViewById(R.id.add_contact_city_text_view);
        city.setText(searchContact.getCity());

        TextView canton = findViewById(R.id.add_contact_canton_text_view);
        canton.setText(searchContact.getCanton());

        TextView phone = findViewById(R.id.add_contact_phone_text_view);
        phone.setText(searchContact.getPhone());
    }

    private Contact createContactFromTextFields() {
        // TODO: Cleanup Code and fill all fields!
        String firstname = ((TextView) findViewById(R.id.add_contact_firstname_text_view)).getText().toString();
        String lastname = ((TextView) findViewById(R.id.add_contact_name_text_view)).getText().toString();
        String street = ((TextView) findViewById(R.id.add_contact_address_text_view)).getText().toString();
        int zip = Integer.parseInt(((TextView) findViewById(R.id.add_contact_zip_text_view)).getText().toString());
        String city = ((TextView) findViewById(R.id.add_contact_city_text_view)).getText().toString();
        String canton = ((TextView) findViewById(R.id.add_contact_canton_text_view)).getText().toString();
        String phone = ((TextView) findViewById(R.id.add_contact_phone_text_view)).getText().toString();
        String mail = ((TextView) findViewById(R.id.add_contact_email_text_view)).getText().toString();

        Contact contact = new Contact(firstname, lastname, "", "", phone, mail, street, city, zip, canton, "");
        return contact;
    }

    private void resetSearchView() {
        SearchView searchView = findViewById(R.id.add_contact_search_view);
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}
