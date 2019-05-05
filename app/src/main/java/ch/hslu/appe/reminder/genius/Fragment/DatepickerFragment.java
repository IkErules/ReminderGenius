package ch.hslu.appe.reminder.genius.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;

public class DatepickerFragment extends DialogFragment {

    private LocalDate initDate;

    public DatepickerFragment() {
        this(LocalDate.now());
    }

    public DatepickerFragment(LocalDate initDate) {
        this.initDate = initDate;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = initDate.getYear();
        int month = initDate.getMonthValue();
        int day = initDate.getDayOfMonth();

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
