package ch.hslu.appe.reminder.genius.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.Fragment.DatepickerFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;

public class AddInstallationActivity extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    private InstallationViewModel installationViewModel;
    private Installation installation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_installation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);

        if (getIntent().hasExtra("installation.to.edit")) {
            getIntent().getParcelableExtra("installation.to.edit");
        } else {
            installation = Installation.builder().defaultInstallation();
        }

        populateTextFieldsFromInstallation();
        addCustomersToSpinner();
        addExpireDatePickerListener();
        addInstallationDatePickerListener();
        addNumberPickerListener();
    }

    private void populateTextFieldsFromInstallation() {
        ((TextView) findViewById(R.id.add_installation_product_details_text_view))
                .setText(installation.getProductDetails());
    }

    private void addNumberPickerListener() {
        //Get the widgets reference from XML layout
        final TextView tv = findViewById(R.id.textView6);
        NumberPicker np = findViewById(R.id.add_installation_service_intervall_number);

        //Set TextView text color
        //tv.setTextColor(Color.parseColor("#ffd32b3b"));

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener((picker, oldVal, newVal) -> {
            TextView installationDateTV = findViewById(R.id.add_installation_installation_date_text_view);

            tv.setText("Selected Number : " + newVal);
        });
    }

    private void addCustomersToSpinner() {
        Spinner spinner = findViewById(R.id.add_installation_customer_spinner);
        ArrayAdapter<Contact> adapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        contactViewModel.getAllContacts().observe(this, contacts -> adapter.addAll(contacts));

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
                newFragment.show(getSupportFragmentManager(), "date_picker_expire_date");
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
                newFragment.show(getSupportFragmentManager(), "date_picker_installation_date");
            }
        });
    }

    public void onTestClick() {

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
