package com.example.thelibrariansapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.thelibrariansapp.activity.ManageBooksActivity;

public class NotAvaiableDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Libri non disponibili")
                .setMessage("Uno o più libri che hai nel carrello sono terminati.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Chiudi il dialog e vai alla ManageBooksActivity
                        dismiss();
                    }
                });
        return builder.create();
    }
}
