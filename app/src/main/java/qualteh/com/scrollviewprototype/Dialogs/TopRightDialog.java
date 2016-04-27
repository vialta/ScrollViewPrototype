package qualteh.com.scrollviewprototype.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Virgil Tanase on 25.04.2016.
 */
public class TopRightDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog ( Bundle savedInstanceState ) {
        return new AlertDialog.Builder( getActivity() )
                .setTitle( "TopRight" )
                .setPositiveButton(android.R.string.ok , null)
                .create();
    }
}
