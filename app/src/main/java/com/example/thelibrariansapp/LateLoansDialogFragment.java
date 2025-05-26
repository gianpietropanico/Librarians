package com.example.thelibrariansapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.thelibrariansapp.activity.ManageBooksActivity;

public class LateLoansDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Prestiti in Ritardo")
                .setMessage("Hai dei prestiti che sono in ritardo.")
                .setPositiveButton("Gestisci Prestiti", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Chiudi il dialog e vai alla ManageBooksActivity
                        dismiss();
                        // Passa il contesto all'activity
                        startActivity(new Intent(getActivity(), ManageBooksActivity.class));
                    }
                })
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss(); // Chiudi il dialog
                    }
                });
        return builder.create();
    }
}
