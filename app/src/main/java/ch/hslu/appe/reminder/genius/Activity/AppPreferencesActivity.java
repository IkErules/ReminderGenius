package ch.hslu.appe.reminder.genius.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ch.hslu.appe.reminder.genius.Fragment.SettingsFragment;
import ch.hslu.appe.reminder.genius.R;

public class AppPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }
}
