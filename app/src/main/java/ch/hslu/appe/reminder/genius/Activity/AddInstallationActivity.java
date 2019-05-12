package ch.hslu.appe.reminder.genius.Activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.InstallationImage;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.Fragment.DatepickerFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ImageViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationImageViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

import static java.time.format.DateTimeFormatter.*;

public class AddInstallationActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String INSTALLATION_TO_EDIT = "installation.to.edit";
    private ContactViewModel contactViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private InstallationViewModel installationViewModel;
    private ImageViewModel imageViewModel;
    private InstallationImageViewModel installationImageViewModel;
    private Installation installation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_installation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        installationImageViewModel = ViewModelProviders.of(this).get(InstallationImageViewModel.class);

        productCategoryViewModel.insert(new ProductCategory("Test-Category", 1, "Test Kategorie mit Beschreibung."));

        if (getIntent().hasExtra(INSTALLATION_TO_EDIT)) {
            installation = getIntent().getParcelableExtra(INSTALLATION_TO_EDIT);

        } else {
            installation = Installation.builder().defaultInstallation();
        }

        addProductToSpinner();
        addCustomersToSpinner();
        addExpireDatePickerListener();
        addInstallationDatePickerListener();
        addNumberPickerListener();
        addNotesTextViewListener();
        populateTextFieldsFromInstallation();
    }

    private void addTestImages(int installationId) {
        addImage(R.drawable.eagle, "eagle.jpg", "Test Eagle Image", installationId);
        addImage(R.drawable.bear, "bear.jpg", "Test Bear Image", installationId);
        addImage(R.drawable.bonobo, "bonobo.jpg", "Test Bonobo Image", installationId);
        addImage(R.drawable.horse, "horse.jpg", "Test Horse Image", installationId);
    }

    private void addImage(int resourceId, String fileName, String description, int installationId) {
        this.saveToInternalStorage((Bitmap) BitmapFactory.decodeResource(this.getResources(), resourceId), fileName);
        File directory = getDir("installationImages", Context.MODE_PRIVATE);
        String path = (new File(directory, fileName)).toString();

        int imageId = imageViewModel.insert(new Image(path, description));

        this.createInstallationImageRelation(installationId, imageId);
    }

    private void createInstallationImageRelation(int installationId, int imageId) {
        installationImageViewModel.insert(new InstallationImage(installationId, imageId));
    }

    private void saveImagesToInstallation(int installationId) {
        if (installationId != 0) {
            Log.d("AddInstallationActivity", "Added Installation: " + installationId);
            addTestImages(installationId);
        } else {
            Log.w("AddInstallationActivity", "Installation not yet ready.");
        }
    }

    private void addNotesTextViewListener() {
        TextView notesTextView = findViewById(R.id.add_installation_notes_text_view);
        notesTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus == false) {
                installation.setNotes(String.valueOf(notesTextView.getText()));
            }
        });
    }

    private void populateTextFieldsFromInstallation() {
        ((TextView) findViewById(R.id.add_installation_product_details_text_view))
                .setText(installation.getProductDetails());
        ((NumberPicker) findViewById(R.id.add_installation_service_intervall_number))
                .setValue(installation.getServiceInterval());
        ((TextView) findViewById(R.id.add_installation_installation_date_text_view))
                .setText(installation.getInstallationDate().format(ofPattern(DATE_FORMAT)));
        ((TextView) findViewById(R.id.add_installation_expire_date_text_view))
                .setText(installation.getExpireDate().format(ofPattern(DATE_FORMAT)));
        ((TextView) findViewById(R.id.add_installation_notes_text_view))
                .setText(installation.getNotes());
    }

    private void setInstallationFromTextFields() {
        CharSequence installationDate = ((TextView) findViewById(R.id.add_installation_installation_date_text_view))
                .getText();
        installation.setInstallationDate(convertToLocalDate(String.valueOf(installationDate)));
        CharSequence expireDate= ((TextView) findViewById(R.id.add_installation_expire_date_text_view))
                .getText();
        installation.setExpireDate(convertToLocalDate(String.valueOf(expireDate)));
        CharSequence notes = ((TextView) findViewById(R.id.add_installation_notes_text_view))
                .getText();
        installation.setNotes(String.valueOf(notes));
    }

    private void addNumberPickerListener() {
        //Get the widgets reference from XML layout
        final TextView tv = findViewById(R.id.textView6);
        NumberPicker np = findViewById(R.id.add_installation_service_intervall_number);

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener((picker, oldVal, newVal) -> {
            installation.setServiceInterval(newVal);
            installation.setExpireDate(installation.getInstallationDate().plusYears(newVal));
            updateExpirationDateView();
        });
    }

    private void addProductToSpinner() {
        Spinner spinner = findViewById(R.id.add_installation_product_spinner);

        HashMap<Integer, ProductCategory> productMapper = new HashMap<>();
        ArrayAdapter<String> productAdapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        productCategoryViewModel.getAllProductsCategory().observe(this, products -> {
            Collections.sort(products);
            for (int i = 0; i < products.size(); i++)
            {
                productMapper.put(i, products.get(i));
                productAdapter.add(products.get(i).getCategoryName());
            }
            if (installation.getProductCategoryId() != -1) {
                productMapper.entrySet().forEach(product -> {
                    if (product.getValue().getProductCategoryId() == installation.getProductCategoryId()) {
                        spinner.setSelection(product.getKey());
                    }
                });
            }
        });

        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(productAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductCategory selectedProduct = productMapper.get(position);
                installation.setProductCategoryId(selectedProduct.getProductCategoryId());
                installation.setServiceInterval(selectedProduct.getDefaultServiceInterval());
                installation.setExpireDate(installation.getInstallationDate()
                        .plusYears(selectedProduct.getDefaultServiceInterval()));
                ((TextView) findViewById(R.id.add_installation_product_details_text_view))
                        .setText(selectedProduct.getDescription());
                ((NumberPicker) findViewById(R.id.add_installation_service_intervall_number))
                        .setValue(selectedProduct.getDefaultServiceInterval());
                updateExpirationDateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addCustomersToSpinner() {
        Spinner spinner = findViewById(R.id.add_installation_customer_spinner);

        HashMap<Integer, Contact> contactIdMapper = new HashMap<>();
        ArrayAdapter<String> contactAdapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);

        contactViewModel.getAllContacts().observe(this, contacts -> {
            Collections.sort(contacts);
            for (int i = 0; i < contacts.size(); i++)
            {
                contactIdMapper.put(i, contacts.get(i));
                contactAdapter.add(contacts.get(i).toStringShort());
            }
            if (installation.getContactId() != -1) {
                contactIdMapper.entrySet().forEach(customer -> {
                    if (customer.getValue().getContactId() == installation.getContactId()) {
                        spinner.setSelection(customer.getKey());
                    }
                });
            }
        });

        contactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(contactAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                installation.setContactId(contactIdMapper.get(position).getContactId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addExpireDatePickerListener() {
        TextView expireDate = findViewById(R.id.add_installation_expire_date_text_view);
        expireDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                LocalDate initDate = convertToLocalDate(String.valueOf(expireDate.getText()));
                DatepickerFragment newFragment = new DatepickerFragment(initDate);
                newFragment.setDateSetListener((view, year, month, day) -> {
                    LocalDate choosenDate = LocalDate.of(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    expireDate.setText(choosenDate.format(ofPattern(DATE_FORMAT)));
                    installation.setExpireDate(choosenDate);
                });
                newFragment.show(getSupportFragmentManager(), "date_picker_expire_date");
            } else {
                LocalDate inputDate = convertToLocalDate(String.valueOf(expireDate.getText()));
                expireDate.setText(inputDate.format(ofPattern(DATE_FORMAT)));
                installation.setExpireDate(inputDate);
            }
        });
    }

    private LocalDate convertToLocalDate(String toConvert) {
        LocalDate converted = LocalDate.now();
        try {
            converted = LocalDate.parse(toConvert, ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException ex) {
            Log.e("AddInstallationActivity", String.format("Error while parsing date %s to LocalDate %s", toConvert, ex.getMessage()));
        }
        return converted;
    }

    private void addInstallationDatePickerListener() {
        TextView installationDatePicker = findViewById(R.id.add_installation_installation_date_text_view);
        installationDatePicker.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                DatepickerFragment newFragment = new DatepickerFragment();
                newFragment.setDateSetListener((view, year, month, day) -> {
                    LocalDate choosenDate = LocalDate.of(view.getYear(), view.getMonth(), view.getDayOfMonth());
                    installationDatePicker.setText(choosenDate.format(ofPattern(DATE_FORMAT)));
                    installation.setInstallationDate(choosenDate);
                });
                newFragment.show(getSupportFragmentManager(), "date_picker_installation_date");
            }
        });
    }

    private void updateExpirationDateView() {
        TextView textView = findViewById(R.id.add_installation_expire_date_text_view);
        textView.setText(installation.getExpireDate().format(ofPattern(DATE_FORMAT)));
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

        if (id == R.id.action_save) {
            setInstallationFromTextFields();
            int installationId = installationViewModel.insert(installation);
            this.saveImagesToInstallation(installationId);

            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        // path to /data/data/yourapp/app_data/installationImages
        File directory = cw.getDir("installationImages", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
}
