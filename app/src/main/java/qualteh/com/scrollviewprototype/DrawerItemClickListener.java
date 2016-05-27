package qualteh.com.scrollviewprototype;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by Virgil Tanase on 25.04.2016.
 */
public class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {

    @Override
    public void onItemClick ( AdapterView<?> parent, View view, int position, long id ) {
        Toast.makeText( ScrollableImageActivity.getActivityContext(), String.valueOf( parent.getItemAtPosition( position ).toString() ), Toast.LENGTH_SHORT ).show();
    }

}