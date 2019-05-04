package ch.hslu.appe.reminder.genius.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatepickerFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            (view, year, month, day) -> Toast.makeText(getActivity(), "selected date is " + view.getYear() +
                    " / " + (view.getMonth()+1) +
                    " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();

    public void setDateSetListener(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;
    }
}
