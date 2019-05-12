package ch.hslu.appe.reminder.genius.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.List;

import ch.hslu.appe.reminder.genius.Adapter.ShowInstallationImageAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.Fragment.ServiceExpiringFragment;
import ch.hslu.appe.reminder.genius.Fragment.SettingsFragment;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationImageViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

public class ShowInstallationActivity extends AppCompatActivity {

    public static final String SHOW_INSTALLATION = "installation.to.show";

    private Bundle savedInstanceState;
    private SharedPreferences sharedPreferences;

    private Installation installation;
    private Contact contact;
    private ProductCategory productCategory;

    private RecyclerView recyclerView;

    private ShowInstallationImageAdapter adapter;

    private ContactViewModel contactViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private InstallationViewModel installationViewModel;
    private InstallationImageViewModel installationImageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_installation);

        this.savedInstanceState = savedInstanceState;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        this.productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        this.installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);
        this.installationImageViewModel = ViewModelProviders.of(this).get(InstallationImageViewModel.class);

        this.recyclerView = findViewById(R.id.show_installation_image_recycler_view);
        this.recyclerView.setHasFixedSize(true);

        Intent caller = getIntent();
        this.installation = caller.getParcelableExtra(SHOW_INSTALLATION);
        Log.d("ShowInstallationActivity", "Got Installation to display: " + this.installation.toString());

        this.setContactFromInstallation();
        this.setProductCategoryFromInstallation();

        this.observeImages();

        this.showInstallation();

        //this.registerListeners();
    }

    private void observeImages() {
        recyclerView = findViewById(R.id.show_installation_image_recycler_view);
        adapter = new ShowInstallationImageAdapter(this, this.installationImageViewModel);
        recyclerView.setAdapter(adapter);
        // Make RecyclerView Horizontal
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        this.installationImageViewModel.getImagesForInstallation(this.installation.getInstallationId()).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable final List<Image> images) {
                // Update the cached copy of the words in the adapter.
                adapter.setImages(images);
            }
        });
    }

    private void setProductCategoryFromInstallation() {
        this.productCategoryViewModel.getProductCategoryById(this.installation.getProductCategoryId()).observe(this, new Observer<ProductCategory>() {
            @Override
            public void onChanged(@Nullable final ProductCategory productCategoryFromDb) {
                // Update the cached copy
                productCategory = productCategoryFromDb;
                notifyProductCategoryChanged();
            }
        });
    }

    private void notifyProductCategoryChanged() {
        if (this.isExpiringSoonFragmentDue()) {
            this.showExpiringServiceFragment();
        }

        ((TextView) findViewById(R.id.show_installation_productcategory_name_text_view)).setText(this.productCategory.getCategoryName());
        ((TextView) findViewById(R.id.show_installation_productcategory_description_text_view)).setText(this.productCategory.getDescription());
    }

    private void setContactFromInstallation() {
        this.contactViewModel.getContactById(this.installation.getContactId()).observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(@Nullable final Contact contactFromDb) {
                // Update the cached copy
                contact = contactFromDb;
                notifyContactChanged();
            }
        });
    }

    private void notifyContactChanged() {
        if (this.isExpiringSoonFragmentDue()) {
            this.showExpiringServiceFragment();
        }

        ((TextView) findViewById(R.id.show_installation_contact_text_view)).setText(this.contact.getFormattedAddressWithName());
        ((TextView) findViewById(R.id.show_installation_mail_text_view)).setText(this.contact.getMail());
        ((TextView) findViewById(R.id.show_installation_phone_text_view)).setText(this.contact.getPhone());
    }

    private void showInstallation() {
        if (this.isExpiringSoonFragmentDue()) {
            this.showExpiringServiceFragment();
        }

        ((TextView) findViewById(R.id.show_installation_name_text_view)).setText(getResources().getString(R.string.show_installation_installation_text) + ": " + this.installation.getInstallationId());
        ((TextView) findViewById(R.id.show_installation_notes_text_view)).setText(this.installation.getNotes());
        ((TextView) findViewById(R.id.show_installation_expiredate_text_view)).setText(getResources().getString(R.string.show_installation_next_service_text) + ": " + this.installation.getFriendlyExpireDateAsString());

        ((CheckBox) findViewById(R.id.show_installation_notify_customer_mail_check_box)).setActivated(this.installation.getNotifyCustomerMail());
        ((CheckBox) findViewById(R.id.show_installation_notify_customer_sms_check_box)).setActivated(this.installation.getNotifyCustomerSms());
        ((CheckBox) findViewById(R.id.show_installation_notify_technician_mail_check_box)).setActivated(this.installation.getNotifyCreatorMail());
        ((CheckBox) findViewById(R.id.show_installation_notify_technician_sms_check_box)).setActivated(this.installation.getNotifyCreatorSms());

    }

    private void showExpiringServiceFragment() {
        if ((findViewById(R.id.show_installation_fragment_frame) != null) &
                (this.contact != null) &
                (this.installation != null) &
                (this.productCategory != null)) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ServiceExpiringFragment serviceExpiringFragment = ServiceExpiringFragment.newInstance(this.installation, this.contact, this.productCategory);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.show_installation_fragment_frame, serviceExpiringFragment).commit();
        }
    }

    private Boolean isExpiringSoonFragmentDue() {
        Boolean displayNotification = this.sharedPreferences.getBoolean(SettingsFragment.SHOW_NOTIFICATIONS, true);
        int durationInMonths = this.sharedPreferences.getInt(SettingsFragment.NOTIFICATION_DURATION_MONTHS, 0);

        if ((displayNotification) & (this.installation.getExpireDate().isBefore((LocalDate.now().plusMonths(durationInMonths))))) {
            Log.d("ShowInstallationActivity", "Installation is expiring soon, fragment should be displayed.");
            return true;
        } else {
            Log.d("ShowInstallationActivity", "Installation is not expiring soon, fragment should not be displayed.");
            return false;
        }
    }

    /*
    private void registerListeners() {
        TextView address = (TextView) findViewById(R.id.show_contact_address_text_view);
        address.setOnClickListener((View v) -> {
            //onContactAddressClicked();
        });

        TextView mail = (TextView) findViewById(R.id.show_contact_mail_text_view);
        mail.setOnClickListener((View v) -> {
            //onContactMailClicked();
        });
        TextView phone = (TextView) findViewById(R.id.show_contact_phone_text_view);
        phone.setOnClickListener((View v) -> {
            //onContactPhoneClicked();
        });
    } */

}
