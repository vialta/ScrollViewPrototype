package qualteh.com.scrollviewprototype;

/**
 * Created by Virgil Tanase on 10.03.2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;

public class ScrollableImageActivity extends SingleFragmentActivity {

    private ScrollableImageFragment mFragment;

    private static Context mContext;



    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        ScrollableImageActivity.mContext=getApplicationContext();
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.contentContainer );
        Log.d( "Fragment ", String.valueOf( fragment ) );
        if(fragment==null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add( R.id.contentContainer,fragment )
                    .commit();
        }
        else{
            mFragment= ( ScrollableImageFragment ) fragment;
        }
    }

    @Override
    protected Fragment createFragment () {
        mFragment= ScrollableImageFragment.newInstance();
        return mFragment;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent( event );
        mFragment.scaleDetector.onTouchEvent( event );
        mFragment.gestureDetector.onTouchEvent( event );
        mFragment.handleTouchEvent( event );
        return mFragment.gestureDetector.onTouchEvent( event );
    }

    public static Context getActivityContext() {
        return ScrollableImageActivity.mContext;
    }
}