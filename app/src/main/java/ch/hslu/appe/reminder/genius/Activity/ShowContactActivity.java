package ch.hslu.appe.reminder.genius.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.R;

public class ShowContactActivity extends AppCompatActivity {

    public static final String SHOW_CONTACT = "contact.to.show";

    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent caller = getIntent();
        this.contact = caller.getParcelableExtra(SHOW_CONTACT);
        Log.d("ShowContactActivity", "Got Contact to display: " + this.contact.toString());

        this.showContact();

        this.registerListeners();
    }

    private void showContact() {
        ((TextView) findViewById(R.id.show_contact_name_text_view)).setText(this.contact.getFirstName() + " " + this.contact.getLastName());
        ((TextView) findViewById(R.id.show_contact_address_text_view)).setText(this.contact.getFormattedAddress());
        ((TextView) findViewById(R.id.show_contact_mail_text_view)).setText(this.contact.getMail());
        ((TextView) findViewById(R.id.show_contact_phone_text_view)).setText(this.contact.getPhone());
    }

    private void registerListeners() {
        TextView address = (TextView) findViewById(R.id.show_contact_address_text_view);
        address.setOnClickListener((View v) -> {
            onContactAddressClicked();
        });

        TextView mail = (TextView) findViewById(R.id.show_contact_mail_text_view);
        mail.setOnClickListener((View v) -> {
            onContactMailClicked();
        });
        TextView phone = (TextView) findViewById(R.id.show_contact_phone_text_view);
        phone.setOnClickListener((View v) -> {
            onContactPhoneClicked();
        });
    }

    private void onContactAddressClicked() {
        // Create Google Maps intent: https://developer.android.com/guide/components/intents-common#Maps
        Uri geoLocation = Uri.parse("geo:0,0?q=" +
                Uri.encode(this.contact.getStreet()) + ", " +
                Uri.encode(Integer.toString(this.contact.getZip())) + " " +
                Uri.encode(this.contact.getCity()) +  ", " +
                Uri.encode(this.contact.getCanton())
        );
        Log.d("ShowContactActivity", "Parsed URI for Maps: " +  geoLocation.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void onContactMailClicked() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, this.contact.getMail());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void onContactPhoneClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + this.contact.getPhone()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

