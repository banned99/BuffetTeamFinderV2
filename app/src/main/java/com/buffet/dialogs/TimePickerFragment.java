package com.buffet.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by icespw on 10/26/2016 AD.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static int HOUR;
    public static int MINUTE;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int HOUR = c.get(Calendar.HOUR_OF_DAY);
        int MINUTE = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, HOUR, MINUTE,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        TimePickerFragment.HOUR = hourOfDay;
        TimePickerFragment.MINUTE = minute;
        String minutee = String.format("%1$02d", minute);
        CreateDealDialog.TIME.setText(""+HOUR+":"+minutee);
//        Toast.makeText(view.getContext().getApplicationContext(), "Hour: " + hourOfDay + "\nminute: " +minutee, Toast.LENGTH_LONG).show();

    }
}
