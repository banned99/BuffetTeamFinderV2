package com.buffet.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.buffet.activities.ChooseBranchActivity;
import com.buffet.fragments.ChooseBranchFragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class CreateDealDialog extends DialogFragment{

    Communicator communicator;
    public static TextView TIME, DATE;
    View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ArrayList<String> options = new ArrayList<String>();
        for (int i = 0; i < ChooseBranchFragment.max_person; i++){
            options.add(""+i);
        }

        view = inflater.inflate(R.layout.create_deal_dialog, null);

        DATE = (TextView) view.findViewById(R.id.date_choose);
        TIME = (TextView) view.findViewById(R.id.time_choose);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, options);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button dateButton = (Button) view.findViewById(R.id.choose_date_button);
        Button timeButton = (Button) view.findViewById(R.id.choose_time_button);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(manager, "date");
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(manager, "time");
            }
        });


        builder.setTitle("New Deal");
        builder.setView(view)
                .setPositiveButton("OK", null)
                .setNegativeButton("CANCEL", null);


        return builder.create();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                performOkButtonAction();
            }
        });
    }

    private void performOkButtonAction() {
        try {
            communicator.onDialogMessage();
            dismiss();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Please fill all the blanks", Toast.LENGTH_LONG).show();
        }
    }


    public interface Communicator {
        void onDialogMessage();
    }
}


