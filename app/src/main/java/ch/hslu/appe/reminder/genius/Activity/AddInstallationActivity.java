package ch.hslu.appe.reminder.genius.Activity;

import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Bundle;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ch.hslu.appe.reminder.genius.Adapter.ShowInstallationImageAdapter;
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

    static final int REQUEST_IMAGE_CAPTURE = 569;
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String INSTALLATION_TO_EDIT = "installation.to.edit";
    private ContactViewModel contactViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private InstallationViewModel installationViewModel;
    private ImageViewModel imageViewModel;
    private InstallationImageViewModel installationImageViewModel;
    private Installation installation;

    private boolean hasToSetDefaultServiceIntervalFromProduct = true;

    private String currentPhotoPath;
    private List<Image> tempImages;

    private RecyclerView recyclerView;
    private ShowInstallationImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_installation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        installationImageViewModel = ViewModelProviders.of(this).get(InstallationImageViewModel.class);

        recyclerView = findViewById(R.id.add_installation_image_recycler_view);
        recyclerView.setHasFixedSize(false);
        tempImages = new ArrayList<>();

        setInstallationFromIntent();
        addProductToSpinner();
        addCustomersToSpinner();
        addInstallationDatePickerListener();
        addNumberPickerListener();
        populateTextFieldsFromInstallation();
        observeImages();
    }

    private void setInstallationFromIntent() {
        if (getIntent().hasExtra(INSTALLATION_TO_EDIT)) {
            installation = getIntent().getParcelableExtra(INSTALLATION_TO_EDIT);

            installationImageViewModel.getImagesForInstallation(installation.installationId)
                    .observe(this, imageIds ->
                            imageViewModel.getImagesById(imageIds.stream()
                            .map(Image::getImageId)
                            .toArray(Integer[]::new))
                            .observe(this, images -> {
                                tempImages.addAll(images);
                                updateAdapter();
                            }));
            hasToSetDefaultServiceIntervalFromProduct = false;
        } else {
            installation = Installation.builder().defaultInstallation();
            hasToSetDefaultServiceIntervalFromProduct = true;
        }
    }

    private void observeImages() {
        recyclerView = findViewById(R.id.add_installation_image_recycler_view);
        adapter = new ShowInstallationImageAdapter(this);
        recyclerView.setAdapter(adapter);
        // Make RecyclerView Horizontal
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void updateAdapter() {
        TextView imageLabel = findViewById(R.id.add_installation_pictures_text_view);
        if (tempImages.size() != 0) {
            imageLabel.setVisibility(View.VISIBLE);
        } else {
            imageLabel.setVisibility(View.INVISIBLE);
        }
        adapter.setImages(tempImages);
    }

    private void saveTempImages(int installationId) {
        tempImages.forEach(image -> {
            int imageId = imageViewModel.insert(image);
            installationImageViewModel.insert(new InstallationImage(installationId, imageId));
        });
    }

    private void saveImagesToInstallation(int installationId) {
        if (installationId != 0) {
            Log.d("AddInstallationActivity", "Added Installation: " + installationId);
            saveTempImages(installationId);
        } else {
            Log.w("AddInstallationActivity", "Installation not yet ready.");
        }
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
        ((CheckBox) findViewById(R.id.add_installation_notify_customer_mail_check_box))
                .setChecked(installation.getNotifyCustomerMail());
        ((CheckBox) findViewById(R.id.add_installation_notify_customer_sms_check_box))
                .setChecked(installation.getNotifyCustomerSms());
        ((CheckBox) findViewById(R.id.add_installation_notify_technician_mail_check_box))
                .setChecked(installation.getNotifyCreatorMail());
        ((CheckBox) findViewById(R.id.add_installation_notify_technician_sms_check_box))
                .setChecked(installation.getNotifyCreatorSms());
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
        installation.setNotifyCustomerMail(
                ((CheckBox) findViewById(R.id.add_installation_notify_customer_mail_check_box)).isChecked());
        installation.setNotifyCustomerSms(
                ((CheckBox) findViewById(R.id.add_installation_notify_customer_sms_check_box)).isChecked());
        installation.setNotifyCreatorMail(
                ((CheckBox) findViewById(R.id.add_installation_notify_technician_mail_check_box)).isChecked());
        installation.setNotifyCreatorSms(
                ((CheckBox) findViewById(R.id.add_installation_notify_technician_sms_check_box)).isChecked());
    }

    private void addNumberPickerListener() {
        //Get the widgets reference from XML layout
        NumberPicker np = findViewById(R.id.add_installation_service_intervall_number);

        //Populate NumberPicker values from minimum and maximum value range
        //Set the minimum value of NumberPicker
        np.setMinValue(1);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        np.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener((picker, oldVal, newVal) -> {
            installation.setServiceInterval(newVal);
            installation.setExpireDate(installation.getInstallationDate().plusYears(newVal));
            updateExpirationDateViewFromInstallation();
        });
    }

    private void addProductToSpinner() {
        Spinner spinner = findViewById(R.id.add_installation_product_spinner);

        HashMap<Integer, ProductCategory> productMapper = new HashMap<>();
        ArrayAdapter<String> productAdapter= new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        boolean comesFromStart = false;

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
                if (hasToSetDefaultServiceIntervalFromProduct) {
                    ProductCategory selectedProduct = productMapper.get(position);
                    installation.setProductCategoryId(selectedProduct.getProductCategoryId());
                    installation.setProductDetails(selectedProduct.getDescription());
                    installation.setServiceInterval(selectedProduct.getDefaultServiceInterval());
                    installation.setExpireDate(installation.getInstallationDate()
                            .plusYears(selectedProduct.getDefaultServiceInterval()));

                    ((TextView) findViewById(R.id.add_installation_product_details_text_view))
                            .setText(selectedProduct.getDescription());
                    ((NumberPicker) findViewById(R.id.add_installation_service_intervall_number))
                            .setValue(selectedProduct.getDefaultServiceInterval());

                    updateExpirationDateViewFromInstallation();
                }

                hasToSetDefaultServiceIntervalFromProduct = true;
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

    private void updateExpirationDateViewFromInstallation() {
        TextView textView = findViewById(R.id.add_installation_expire_date_text_view);
        textView.setText(installation.getExpireDate().format(ofPattern(DATE_FORMAT)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu_with_picture, menu);
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
        } else if (id == R.id.action_add_picture) {
            dispatchTakePictureIntent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        // path to /data/data/yourapp/app_data/installationImages
        //File directory = cw.getDir("installationImages", Context.MODE_PRIVATE);
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        //directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                directory);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Image newImage = new Image(currentPhotoPath, "Image Description");
            tempImages.add(newImage);
            adapter.setImages(tempImages);
            updateAdapter();
        }
    }

    /** Loading pics from DB and display them on View
    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, boptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, boptions);
        imageView.setImageBitmap(bitmap);
    } */
}
