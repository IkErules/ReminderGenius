package ch.hslu.appe.reminder.genius.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    }

    private void showContact() {
        // TODO: Display Contact
        ((TextView) findViewById(R.id.show_contact_name_text_view)).setText(this.contact.getFirstName() + " " + this.contact.getLastName());
        ((TextView) findViewById(R.id.show_contact_street_text_view)).setText(this.contact.getStreet());
        ((TextView) findViewById(R.id.show_contact_zip_text_view)).setText(Integer.toString(this.contact.getZip()));
        ((TextView) findViewById(R.id.show_contact_city_text_view)).setText(this.contact.getCity());
        ((TextView) findViewById(R.id.show_contact_mail_text_view)).setText(this.contact.getMail());
        ((TextView) findViewById(R.id.show_contact_phone_text_view)).setText(this.contact.getPhone());

    }
}
