package ch.hslu.appe.reminder.genius.Fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ch.hslu.appe.reminder.genius.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public static final String SHOW_NOTIFICATIONS = "preferences_display_notifications";
    public static final String NOTIFICATION_DURATION_MONTHS = "preferences_display_notifications_months";
    public static final String NOTIFICATION_FREQUENCY_DAYS = "preferences_display_notifications_frequency_days";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}