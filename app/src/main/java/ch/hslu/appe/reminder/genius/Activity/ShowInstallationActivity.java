package ch.hslu.appe.reminder.genius.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;


import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.reminder.genius.Adapter.ShowInstallationViewPagerAdapter;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

public class ShowInstallationActivity extends AppCompatActivity {

    public static final String SHOW_INSTALLATION = "installation.to.show";

    private Installation installation;
    private Contact contact;
    private ProductCategory productCategory;

    private ViewPager2 installationImageViewPager;

    private ContactViewModel contactViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private InstallationViewModel installationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_installation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        this.productCategoryViewModel = ViewModelProviders.of(this).get(ProductCategoryViewModel.class);
        this.installationViewModel = ViewModelProviders.of(this).get(InstallationViewModel.class);

        this.installationImageViewPager = findViewById(R.id.show_installation_image_view_pager);

        List<Drawable> list = new ArrayList<>();
        list.add(getDrawable(R.drawable.eagle));
        list.add(getDrawable(R.drawable.bear));
        list.add(getDrawable(R.drawable.bonobo));
        list.add(getDrawable(R.drawable.horse));

        this.installationImageViewPager.setAdapter(new ShowInstallationViewPagerAdapter(this, list, this.installationImageViewPager));

        Intent caller = getIntent();
        this.installation = caller.getParcelableExtra(SHOW_INSTALLATION);
        Log.d("ShowInstallationActivity", "Got Installation to display: " + this.installation.toString());


        this.setContactFromInstallation();
        this.setProductCategoryFromInstallation();

        this.showInstallation();

        //this.registerListeners();
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
        ((TextView) findViewById(R.id.show_installation_productcategory_name_text_view)).setText(this.productCategory.getCategoryName());
        ((TextView) findViewById(R.id.show_installation_productcategory_description_text_view)).setText(this.productCategory.getDescription());
    }

    private void setContactFromInstallation() {
        this.contactViewModel.getContactById(this.installation.getProductCategoryId()).observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(@Nullable final Contact contactFromDb) {
                // Update the cached copy
                contact = contactFromDb;
                notifyContactChanged();
            }
        });
    }

    private void notifyContactChanged() {
        ((TextView) findViewById(R.id.show_installation_contact_text_view)).setText(this.contact.getFormattedAddressWithName());
        ((TextView) findViewById(R.id.show_installation_mail_text_view)).setText(this.contact.getMail());
        ((TextView) findViewById(R.id.show_installation_phone_text_view)).setText(this.contact.getPhone());
    }

    private void showInstallation() {
        ((TextView) findViewById(R.id.show_installation_name_text_view)).setText("Installation " + this.installation.getInstallationId());
        ((TextView) findViewById(R.id.show_installation_notes_text_view)).setText(this.installation.getNotes());
        ((TextView) findViewById(R.id.show_installation_expiredate_text_view)).setText("Nächster Service: " + this.installation.getExpireDate().toString());

        ((CheckBox) findViewById(R.id.show_installation_notify_customer_mail_check_box)).setActivated(this.installation.getNotifyCustomerMail());
        ((CheckBox) findViewById(R.id.show_installation_notify_customer_sms_check_box)).setActivated(this.installation.getNotifyCustomerSms());
        ((CheckBox) findViewById(R.id.show_installation_notify_technician_mail_check_box)).setActivated(this.installation.getNotifyCreatorMail());
        ((CheckBox) findViewById(R.id.show_installation_notify_technician_sms_check_box)).setActivated(this.installation.getNotifyCreatorSms());

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
