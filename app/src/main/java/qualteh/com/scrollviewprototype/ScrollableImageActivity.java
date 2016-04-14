// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScrollableImageActivity extends SingleFragmentActivity
{

    private static Context mContext;
    private ScrollableImageFragment mFragment;

    public ScrollableImageActivity()
    {
    }

    public static Context getActivityContext()
    {
        return mContext;
    }

    protected Fragment createFragment()
    {
        mFragment = ScrollableImageFragment.newInstance();
        return mFragment;
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent)
    {
        super.dispatchTouchEvent( motionevent );
        mFragment.scaleDetector.onTouchEvent(motionevent);
        mFragment.handleTouchEvent(motionevent);
        mFragment.gestureDetector.onTouchEvent(motionevent);
        return mFragment.gestureDetector.onTouchEvent(motionevent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mContext = getApplicationContext();
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById( R.id.contentContainer );
        if (fragment == null)
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.contentContainer, fragment).commit();
            return;
        } else
        {
            mFragment = (ScrollableImageFragment)fragment;
            return;
        }
    }

    public void onDemoButtonClick(View view)
    {
        mFragment.demoButtonClicked();
    }
}
