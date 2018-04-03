package com.example.nam.recipeshopper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;

public class AddRecipeDialogFragment extends AppCompatDialogFragment {
    private static final String TAG = "AddRecipeDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_addrecipe, null))
                // Add action buttons
                .setPositiveButton("Add recipe from URL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d(TAG, "onClick: add recipe clicked");
                        mListener.onDialogPositiveClick(AddRecipeDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int d) {
                        Log.d(TAG, "onClick: cancel clicked");
                        mListener.onDialogNegativeClick(AddRecipeDialogFragment.this);
                        AddRecipeDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddRecipeDialogListener {
        public void onDialogPositiveClick(AppCompatDialogFragment dialog);
        public void onDialogNegativeClick(AppCompatDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddRecipeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the AddRecipeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AddRecipeDialogListener so we can send events to the host
            mListener = (AddRecipeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement AddRecipeDialogListener");
        }
    }
}
