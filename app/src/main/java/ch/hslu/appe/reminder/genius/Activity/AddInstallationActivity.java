package ch.hslu.appe.reminder.genius.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ch.hslu.appe.reminder.genius.Fragment.DatepickerFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;

public class AddInstallationActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_installation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addCustomersToSpinner();
        addExpireDatePickerListener();
        addInstallationDatePickerListener();
    }

    private void addCustomersToSpinner() {

        Spinner spinner = findViewById(R.id.add_installation_customer_spinner);
        ArrayAdapter<CharSequence> adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        contactViewModel.getAllContacts().observe(this, contacts -> contacts.forEach(c -> adapter.add(c.toStringShort())));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void addExpireDatePickerListener() {
        TextView expireDate = findViewById(R.id.add_installation_expire_date_text_view);
        expireDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatepickerFragment newFragment = new DatepickerFragment();
                newFragment.setDateSetListener((view, year, month, day) -> {
                    LocalDate choosenDate = LocalDate.of(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    expireDate.setText(choosenDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                });
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    private void addInstallationDatePickerListener() {
        TextView installationDate = findViewById(R.id.add_installation_installation_date_text_view);
        installationDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatepickerFragment newFragment = new DatepickerFragment();
                newFragment.setDateSetListener((view, year, month, day) -> {
                    LocalDate choosenDate = LocalDate.of(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    installationDate.setText(choosenDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                });
                newFragment.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // Do some action here
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
