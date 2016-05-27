package qualteh.com.scrollviewprototype.Dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import qualteh.com.scrollviewprototype.R;

/**
 * Created by Virgil Tanase on 25.04.2016.
 */
public class StorageDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private String stockString;
    private String capacityString;

    private GridLayout deliveryDialog;
    private GridLayout storageDialog;

    private Button livrareButton;
    private Button livrareFinalizareButton;

    @Nullable
    @Override
    public View onCreateView ( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.storage_dialog_view, container );
    }

    @Override
    public void onViewCreated ( View view, @Nullable Bundle savedInstanceState ) {

        TextView stockText    = (TextView) view.findViewById( R.id.stockTextView );
        TextView capacityText = (TextView) view.findViewById( R.id.capacityTextView );

        storageDialog = ( GridLayout ) view.findViewById( R.id.storage_dialog_layout );
        deliveryDialog = ( GridLayout ) view.findViewById( R.id.delivery_dialog_layout);

        livrareButton = (Button ) view.findViewById( R.id.buttonLivrare );
        livrareButton.setOnClickListener( this );
        livrareFinalizareButton = (Button ) view.findViewById( R.id.buttonLivrareOK );
        livrareFinalizareButton.setOnClickListener( this );

        stockText.setText( stockString );
        capacityText.setText( capacityString );


        super.onViewCreated( view, savedInstanceState );
    }

    public static StorageDialog newInstance()
    {
        return new StorageDialog();
    }

    public String getStockString () {
        return stockString;
    }

    public void setStockString ( String stockString ) {
        this.stockString = stockString;
    }

    public String getCapacityString () {
        return capacityString;
    }

    public void setCapacityString ( String capacityString ) {
        this.capacityString = capacityString;
    }

    @Override
    public void onClick ( View v ) {
        switch(v.getId()){
            case R.id.buttonLivrare:
                storageDialog.setVisibility( View.INVISIBLE );
                deliveryDialog.setVisibility( View.VISIBLE );
                break;
            case R.id.buttonLivrareOK:
                storageDialog.setVisibility( View.VISIBLE );
                deliveryDialog.setVisibility( View.INVISIBLE );
                break;
            default:
                break;
        }
    }

}