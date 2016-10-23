package com.buffet.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ggwp.caliver.banned.buffetteamfinderv2.R;

/**
 * Created by YaYaTripleSix on 23-Oct-16.
 */

public class CreateDealDialog extends DialogFragment {

    Communicator communicator;
    EditText restaurant, branch, time, promotion, amount;

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

        setCancelable(false);

        View view = inflater.inflate(R.layout.create_deal_dialog, null);
        restaurant = (EditText) view.findViewById(R.id.new_restaurant);
        branch = (EditText) view.findViewById(R.id.new_branch);
        time = (EditText) view.findViewById(R.id.new_time);
        promotion = (EditText) view.findViewById(R.id.new_promotion);
        amount = (EditText) view.findViewById(R.id.new_amount);

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
            communicator.onDialogMessage(restaurant.getText().toString(), branch.getText().toString(), time.getText().toString(), promotion.getText().toString(), Integer.parseInt(amount.getText().toString()));
            dismiss();

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Please fill all the blanks", Toast.LENGTH_LONG).show();
        }
    }
    public interface Communicator {
        void onDialogMessage(String restaurant, String branch, String time, String promotion, int amount);
    }
}


