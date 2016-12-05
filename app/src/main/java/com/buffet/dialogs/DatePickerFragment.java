package com.buffet.dialogs;

import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by icespw on 10/26/2016 AD.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static int YEAR;
    public static int MONTH;
    public static int DAY;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        DatePickerFragment.DAY = dayOfMonth;
        DatePickerFragment.MONTH = month;
        DatePickerFragment.YEAR = year;

        CreateDealDialog.DATE.setText(""+year+"-"+(month + 1)+"-"+dayOfMonth);
//        CreateDealDialog.TIME.setText(""+HOUR+":"+MINUTE);
//        Toast.makeText(view.getContext().getApplicationContext(), "Hour: " + hourOfDay + "\nminute: " +minute, Toast.LENGTH_LONG).show();

    }
}
