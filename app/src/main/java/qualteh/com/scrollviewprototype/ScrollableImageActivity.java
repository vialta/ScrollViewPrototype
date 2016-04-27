// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qualteh.com.scrollviewprototype.Dialogs.StorageDialog;

public class ScrollableImageActivity extends SingleFragmentActivity
{

    @Bind( R.id.buttonMenu ) Button topLeftButton;
    @Bind( R.id.buttonNotifications ) Button topRightButton;
    @Bind( R.id.mainLayout) CustomDrawerLayout mLeftSlider;
    @Bind( R.id.list_sliderLeftMenu )  ListView leftList;
    @Bind( R.id.list_sliderRightMenu) ListView rightList;

    private String[] leftMenuTitles;
    private String[] rightMenuTitles;


    private static Context mContext;
    private ScrollableImageFragment mFragment;
    private StorageDialog mStorageDialog;

    public static Context getActivityContext() {
        return mContext;
    }

    protected Fragment createFragment() {
        mFragment = ScrollableImageFragment.newInstance();
        return mFragment;
    }

    public Fragment createDialog(){
        mStorageDialog = StorageDialog.newInstance();
        return mStorageDialog;
    }

    public boolean dispatchTouchEvent(MotionEvent motionevent) {
        super.dispatchTouchEvent( motionevent );
        mFragment.scaleDetector.onTouchEvent(motionevent);
        mFragment.handleTouchEvent(motionevent);
        mFragment.gestureDetector.onTouchEvent(motionevent);
        return mFragment.gestureDetector.onTouchEvent(motionevent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        ButterKnife.bind(this);

        leftMenuTitles = getResources().getStringArray( R.array.leftMenu );
        rightMenuTitles = getResources().getStringArray( R.array.rightMenu );

        mLeftSlider.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );

        leftList.setAdapter( new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, leftMenuTitles  ) );
        leftList.setOnItemClickListener( new DrawerItemClickListener() );

        rightList.setAdapter( new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, rightMenuTitles  ) );
        rightList.setOnItemClickListener( new DrawerItemClickListener() );

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

    @OnClick(R.id.buttonMenu )
    public void onTopLeftButtonClick(View view){ mLeftSlider.openDrawer( GravityCompat.START );}

    @OnClick(R.id.buttonNotifications )
    public void onTopRightButtonClick(View view){mLeftSlider.openDrawer( GravityCompat.END );}

}
