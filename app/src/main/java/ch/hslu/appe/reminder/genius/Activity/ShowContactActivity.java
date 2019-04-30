package ch.hslu.appe.reminder.genius.Activity;

import androidx.appcompat.app.AppCompatActivity;

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

        Intent caller = getIntent();
        this.contact = caller.getParcelableExtra(SHOW_CONTACT);
        Log.d("ShowContactActivity", "Got Contact to display: " + this.contact.toString());

        this.showContact();
    }

    private void showContact() {
        // TODO: Display Contact
        ((TextView) findViewById(R.id.show_contact_firstname_text_view)).setText(this.contact.getFirstName());
        ((TextView) findViewById(R.id.show_contact_lastname_text_view)).setText(this.contact.getLastName());

    }
}
