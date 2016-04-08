// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import qualteh.com.scrollviewprototype.API.ApiInterface;
import qualteh.com.scrollviewprototype.Data.DbHelper;
import qualteh.com.scrollviewprototype.Model.Building;
import qualteh.com.scrollviewprototype.Model.MapModel;
import qualteh.com.scrollviewprototype.Model.Storage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Referenced classes of package qualteh.com.scrollviewprototype:
//            GraphicDrawer, DemoMachine, Consts

public class ScrollableImageFragment extends Fragment
    implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener
{
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {


        public boolean onDoubleTap(MotionEvent motionevent)
        {
            return true;
        }

        public boolean onDown(MotionEvent motionevent)
        {
            return true;
        }


    }


    private OverScroller scroller;

    private static final float MAX_ZOOM = 3F;
    private static final float MIN_ZOOM = 1F;
    private static double SCREEN_DENSITY;
    private static int SCREEN_HEIGHT;
    private static int SCREEN_WIDTH;
    private boolean animationIsRunning;
    private long bottomLimit;
    private DemoMachine demoMachine;
    @Bind( R.id.errorText )TextView firstTimeTextError;
    public GestureDetector gestureDetector;
    @Bind( R.id.vectorTest )ImageView contentPainter;
    private float lastScaleFactor;
    private long lastScaleTimestamp;
    private long leftLimit;
    @Bind( R.id.uiContainer )FrameLayout mFrameLayout;
    private MapModel mMapData;
    private final Paint mPaint = new Paint();

    private ImageView machineView;
    @Bind( R.id.contentContainer )FrameLayout mainContainer;
    private float mx;
    private float my;
    private long rightLimit;
    private float scale;
    public ScaleGestureDetector scaleDetector;
    private int scaleDirection;
    private long topLimit;

    public ScrollableImageFragment()
    {
        scaleDirection = -1;
        scale = 1.0F;
        lastScaleFactor = 0.0F;
        animationIsRunning = false;
        lastScaleTimestamp = System.currentTimeMillis();
    }

    public void onClick(View view)
    {
        demoButtonClicked();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewgroup, Bundle bundle)
    {
        View view = layoutInflater.inflate(R.layout.activity_main, viewgroup, false);
        ButterKnife.bind(this, view);

        scroller = new OverScroller( getContext() );

        Consts.SIZE_MULTIPLIER = ((double)getResources().getDisplayMetrics().widthPixels * 1.0D) / (double)((float)Consts.DIAGRAM_WIDTH * getResources().getDisplayMetrics().density + 20F * getResources().getDisplayMetrics().density);
        Log.d("TEST", (new StringBuilder()).append("").append(Consts.SIZE_MULTIPLIER).append(" ").append(" ").append(getResources().getDisplayMetrics().widthPixels).append(" ").append(Consts.DIAGRAM_WIDTH).append(" ").append(20).toString());
        Consts.UI_X_FACTOR = ((double)Consts.DIAGRAM_WIDTH * Consts.SIZE_MULTIPLIER) / 3607.9999999998336D;
        Consts.UI_Y_FACTOR = ((double)Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER) / 2988.9999999994643D;
        Log.d("TEST", (new StringBuilder()).append("").append(Consts.SIZE_MULTIPLIER).append(" ").append(Consts.UI_X_FACTOR).append(" ").append(getResources().getDisplayMetrics().widthPixels).append(" ").append(getResources().getDisplayMetrics().heightPixels).toString());
        SCREEN_DENSITY = getResources().getDisplayMetrics().density;
        SCREEN_WIDTH = (int)Math.round((double)(Consts.DIAGRAM_WIDTH + 20) * Consts.SIZE_MULTIPLIER * SCREEN_DENSITY);
        SCREEN_HEIGHT = (int)Math.round((double)(Consts.DIAGRAM_HEIGHT + 10) * Consts.SIZE_MULTIPLIER * SCREEN_DENSITY);
        Log.d( "SCREEN PRE", ( new StringBuilder() ).append( Math.round( ( double ) ( Consts.DIAGRAM_WIDTH + 20 ) * Consts.SIZE_MULTIPLIER * SCREEN_DENSITY ) ).append( " " ).append( SCREEN_WIDTH ).append( " " ).append( Consts.DIAGRAM_WIDTH ).append( " " ).append( 20 ).append( " " ).append( Consts.SIZE_MULTIPLIER ).append( " " ).append( SCREEN_DENSITY ).append( " " ).toString() );
        contentPainter.setLayoutParams( new FrameLayout.LayoutParams( ( int ) Math.round( ( double ) ( Consts.DIAGRAM_WIDTH + 20 ) * Consts.SIZE_MULTIPLIER * ( double ) scale * SCREEN_DENSITY ), ( int ) Math.round( ( double ) ( Consts.DIAGRAM_HEIGHT + 10 ) * Consts.SIZE_MULTIPLIER * ( double ) scale * SCREEN_DENSITY ) ) );
        contentPainter.invalidate();
        contentPainter.requestLayout();
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        scaleDetector = new ScaleGestureDetector(getActivity(), this);
        mPaint.setColor( 0xffff0000 );
        mPaint.setStrokeWidth( 20F );
        mPaint.setStrokeJoin( Paint.Join.ROUND);
        demoMachine = new DemoMachine();
        demoMachine.newRandomCoordinate();
        demoMachine.getMachinePosition().updateUIPositionByDensity(getResources().getDisplayMetrics().density);
        machineView = new ImageView(getContext());
        machineView.setX( demoMachine.getMachinePosition().getUiX() );
        machineView.setY( demoMachine.getMachinePosition().getUiY() );
        machineView.setOnClickListener( this );
        machineView.setImageDrawable( new Drawable() {
            public void draw ( Canvas canvas ) {
                Paint paint = new Paint();
                paint.setColor( 0xffff0000 );
                paint.setStyle( Paint.Style.FILL );
                canvas.drawCircle( 50F, 50F, 50F, paint );
            }

            public int getOpacity () {
                return 0;
            }

            public void setAlpha ( int i ) {
            }

            public void setColorFilter ( ColorFilter colorfilter ) {
            }


            {

            }
        } );
        mFrameLayout.addView( machineView );
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "http://vtanase.com/Prototyping/" )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
        ApiInterface api = retrofit.create( ApiInterface.class );
        Call<MapModel> call = api.callVersion();
        call.enqueue( new Callback<MapModel>() {
            @Override
            public void onResponse ( Call<MapModel> call, Response<MapModel> response ) {
                mMapData = ( MapModel ) response.body();
                mMapData.getMainCoordinates().adjustCoordinates( getResources().getDisplayMetrics().density );
                mMapData.adjustCoordinates( getResources().getDisplayMetrics().density );
                Log.d( "Response", response.message().toString() );
                setDatabaseByResponse( true );
            }

            @Override
            public void onFailure ( Call<MapModel> call, Throwable throwable ) {
                Log.d( "Failure", throwable.getMessage() );
                setDatabaseByResponse( false );
            }
        } );
        vScroll = ( ScrollView ) view.findViewById( R.id.vScroll );
        hScroll = (HorizontalScrollView ) view.findViewById( R.id.hScroll );

        OverScrollDecoratorHelper.setUpOverScroll(vScroll);
        OverScrollDecoratorHelper.setUpOverScroll(hScroll);

        return view;
    }

    public void onPause()
    {
        super.onPause();
    }

    private int calcScrollToX(float f)
    {
        f = (float)mainContainer.getScrollX() + (mx - f);
        if (f < (float)leftLimit)
        {
            return (int)leftLimit;
        }
        if (f > (float)rightLimit)
        {
            return (int)rightLimit;
        } else
        {
            return 0;
        }
    }

    private int calcScrollToY(float f)
    {
        f = (float)mainContainer.getScrollY() + (my - f);
        if ((long)Math.round(f) < topLimit)
        {
            Log.d("calcScrollToY", (new StringBuilder()).append(f).append(" ").append(topLimit).toString());
            return (int)topLimit;
        }
        if ((long)Math.round(f) > bottomLimit)
        {
            Log.d("calcScrollToY", (new StringBuilder()).append(f).append(" ").append(bottomLimit).toString());
            return (int)bottomLimit;
        } else
        {
            return 0;
        }
    }

    private boolean checkBoundsX(float f)
    {
        f = (float)mainContainer.getScrollX() + (mx - f);
        leftLimit = Math.round( ( ( scale * ( float ) ( SCREEN_WIDTH / 4 ) ) / 2.0F ) * ( ( scale - 1.0F ) * 4F ) * - 1F );
        rightLimit = Math.round(((float)leftLimit + (float)SCREEN_WIDTH * scale) - (float)getResources().getDisplayMetrics().widthPixels);
        return (long)Math.round(f) > leftLimit && (long)Math.round(f) < rightLimit;
    }

    private boolean checkBoundsY(float f)
    {
        f = (float)mainContainer.getScrollY() + (my - f);
        topLimit = Math.round(scale * (float)(SCREEN_HEIGHT / 8) * ((scale - 1.0F) * 4F) * -1F);
        bottomLimit = Math.round(((float)topLimit + (float)SCREEN_HEIGHT * scale) - (float)getResources().getDisplayMetrics().heightPixels);
        Log.d( "Height", ( new StringBuilder() ).append( f ).append( " " ).append( topLimit ).toString() );
        return (long)Math.round(f) > topLimit && (long)Math.round(f) < bottomLimit;
    }

    private void connectionFailureAnimation()
    {
        firstTimeTextError.setVisibility(View.VISIBLE);
        AlphaAnimation alphaanimation = new AlphaAnimation(0.0F, 1.0F);
        alphaanimation.setRepeatMode(2);
        alphaanimation.setRepeatCount(1);
        alphaanimation.setFillAfter(true);
        alphaanimation.setStartOffset(2000L);
        alphaanimation.setDuration(1000L);
        firstTimeTextError.setAnimation(alphaanimation);
    }

    public static ScrollableImageFragment newInstance()
    {
        return new ScrollableImageFragment();
    }

    private void setDatabaseByResponse(boolean flag)
    {
        SharedPreferences sharedpreferences;
        DbHelper dbhelper;
        boolean isFirstTime;
        sharedpreferences = getContext().getSharedPreferences( "qualteh.com.scrollviewprototype", 0 );
        isFirstTime = sharedpreferences.getBoolean("PREFS_FIRST_TIME", true);
        dbhelper = new DbHelper(getContext());
        final SQLiteDatabase sqlitedatabase = getActivity().openOrCreateDatabase(dbhelper.getDatabaseName(), 0, null);
        if (!isFirstTime){
            if (flag && !dbhelper.isCurrentVersion(sqlitedatabase, mMapData.getVersion()))
            {
                dbhelper.updateMapModel(sqlitedatabase, mMapData, mMapData.getId());
            }
        }
        else{
            if (!flag)
            {
                connectionFailureAnimation();
                return;
            }
            dbhelper.onCreate( sqlitedatabase );
            dbhelper.addMapModel( sqlitedatabase, mMapData );
            sharedpreferences.edit().putBoolean("PREFS_FIRST_TIME", false).apply();
        }
        mMapData = dbhelper.getMapModel(sqlitedatabase);
        GraphicDrawer.drawMap( contentPainter, mFrameLayout, mPaint, mMapData, demoMachine, SCREEN_DENSITY );
    }

    public void demoButtonClicked()
    {
        if (!animationIsRunning)
        {
            demoMachine.newRandomCoordinate();
            demoMachine.getMachinePosition().updateUIPositionByDensity(getResources().getDisplayMetrics().density);
            TranslateAnimation translateanimation = new TranslateAnimation(0.0F, (float)demoMachine.getMachinePosition().getUiX() - machineView.getX(), 0.0F, (float)demoMachine.getMachinePosition().getUiY() - machineView.getY());
            translateanimation.setDuration(1000L);
            translateanimation.setFillBefore(false);
            translateanimation.setFillEnabled(true);
            translateanimation.setFillAfter(false);
            translateanimation.setAnimationListener(new Animation.AnimationListener() {

                final ScrollableImageFragment scrollableImageFragment;

                public void onAnimationEnd(Animation animation)
                {
                    machineView.setX(demoMachine.getMachinePosition().getUiX());
                    machineView.setY(demoMachine.getMachinePosition().getUiY());
                    animationIsRunning = false;
                }

                public void onAnimationRepeat(Animation animation)
                {
                }

                public void onAnimationStart(Animation animation)
                {
                    animationIsRunning = true;
                }

            
            {
                scrollableImageFragment = ScrollableImageFragment.this;
            }
            });
            machineView.startAnimation(translateanimation);
        }
    }

    public View getMachineView()
    {
        return machineView;
    }

    public void handleTouchEvent(MotionEvent event)
    {
        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                curX = event.getX();
                curY = event.getY();

                vScroll.smoothScrollBy( ( int ) ( mx - curX ), ( int ) ( my - curY ) );
                hScroll.smoothScrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }


//        switch ( motionEvent.getAction() ){
//            case MotionEvent.ACTION_DOWN:
//                mx = motionEvent.getX();
//                my = motionEvent.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (motionEvent.getPointerCount() < 2 && System.currentTimeMillis() - lastScaleTimestamp > 500L)
//                {
//                    float curX = motionEvent.getX();
//                    float curY = motionEvent.getY();
//                    if (checkBoundsX(curX))
//                    {
//                        mainContainer.scrollBy((int)(mx - curX), 0);
//                    }
//                    if (checkBoundsY(curY))
//                    {
//                        mainContainer.scrollBy(0, (int)(my - curY));
//                    }
//                    mx = curX;
//                    my = curY;
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                lastScaleTimestamp = System.currentTimeMillis();
//                return;
//            case MotionEvent.ACTION_UP:
//                float curX = motionEvent.getX();
//                float curY = motionEvent.getY();
//                Log.d("TAG", (new StringBuilder()).append("UP ").append((float)mainContainer.getScrollY() + (my - curY)).append(" ").append(leftLimit).append(" ").append(checkBoundsX(curX)).append(" ").append(checkBoundsY(curY)).toString());
//                int i = mainContainer.getScrollX();
//                int j = mainContainer.getScrollY();
//                if (!checkBoundsX(curX))
//                {
//                    Log.d("TAG", "Scroll X UP");
//                    i = calcScrollToX(curX);
//                }
//                if (!checkBoundsY(curY))
//                {
//                    Log.d("TAG", "Scroll Y UP");
//                    j = calcScrollToY(curY);
//                }
//                mainContainer.scrollTo(i, j);
//                break;
//            default:
//                break;
//        }
    }

    void logBuildingCoords()
    {
        String s = "[";
        for (int i = 0; i < mMapData.getBuildings().size(); i++)
        {
            int j = 0;
            while (j < ((Building)mMapData.getBuildings().get(i)).getCoordinates().size()) 
            {
                s = (new StringBuilder()).append(s).append(((Building)mMapData.getBuildings().get(i)).getCoordinates().get(j)).toString();
                if (j % 2 == 1)
                {
                    s = (new StringBuilder()).append(s).append("][").toString();
                } else
                {
                    s = (new StringBuilder()).append(s).append(" , ").toString();
                }
                j++;
            }
            s = (new StringBuilder()).append(s).append("/").toString();
        }

        Log.d( "WTF", s );
    }

    void logMainCoords()
    {
        String s = "[";
        int i = 0;
        while (i < mMapData.getMainCoordinates().getCoordinates().size()) 
        {
            s = (new StringBuilder()).append(s).append(mMapData.getMainCoordinates().getCoordinates().get(i)).toString();
            if (i % 2 == 1)
            {
                s = (new StringBuilder()).append(s).append("][").toString();
            } else
            {
                s = (new StringBuilder()).append(s).append(" , ").toString();
            }
            i++;
        }
        Log.d("WTF", s);
    }

    void logStorageCoords()
    {
        String s = "[";
        for (int i = 0; i < mMapData.getStorage().size(); i++)
        {
            int j = 0;
            while (j < ((Storage)mMapData.getStorage().get(i)).getCoordinates().size()) 
            {
                s = (new StringBuilder()).append(s).append(((Storage)mMapData.getStorage().get(i)).getCoordinates().get(j)).toString();
                if (j % 2 == 1)
                {
                    s = (new StringBuilder()).append(s).append("][").toString();
                } else
                {
                    s = (new StringBuilder()).append(s).append(" , ").toString();
                }
                j++;
            }
            s = (new StringBuilder()).append(s).append("/").toString();
        }

        Log.d( "WTF", s );
    }



    public boolean onScale(ScaleGestureDetector scalegesturedetector)
    {
        float f;
        boolean flag;
        if (checkBoundsX(scalegesturedetector.getFocusX()) && checkBoundsY(scalegesturedetector.getFocusY()))
        {
            flag = true;
        } else
        {
            flag = false;
        }
        Log.d("SCALE ", String.valueOf(flag));
        f = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0.0F || Math.signum(f) == Math.signum(lastScaleFactor))
        {
            scale = scale * f;
            scale = Math.max(1.0F, Math.min(scale, 3F));
            Log.d("Pre Scale ", (new StringBuilder()).append(scale).append(" ").append(Math.round(SCREEN_WIDTH)).toString());

            mFrameLayout.setScaleX(scale);
            mFrameLayout.setScaleY(scale);
            mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(Math.round((float)SCREEN_WIDTH * scale), Math.round((float)SCREEN_HEIGHT * scale)));
            Log.d("After Scale ", (new StringBuilder()).append(scale).append(" ").append(Math.round((float)SCREEN_WIDTH * scale)).toString());
            mFrameLayout.invalidate();
            mFrameLayout.requestLayout();
            lastScaleFactor = f;
            return true;
        } else
        {
            lastScaleFactor = 0.0F;
            return true;
        }
    }

    public boolean onScaleBegin(ScaleGestureDetector scalegesturedetector)
    {
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scalegesturedetector)
    {
        Log.d("TAG", "SE");
        lastScaleTimestamp = System.currentTimeMillis();
    }

    public void scaleButtonClicked()
    {
        if (scale > 2.75F || scale < 1.25F)
        {
            scaleDirection = scaleDirection * -1;
        }
        scale = scale + 0.25F * (float)scaleDirection;
        mFrameLayout.setScaleX(scale);
        mFrameLayout.setScaleY(scale);
        mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(Math.round((float)SCREEN_WIDTH * scale), Math.round((float)SCREEN_HEIGHT * scale)));
        mFrameLayout.invalidate();
        mFrameLayout.requestLayout();
    }

    public void setMachineView(ImageView imageview)
    {
        machineView = imageview;
    }



/*
    static MapModel access$102(ScrollableImageFragment scrollableimagefragment, MapModel mapmodel)
    {
        scrollableimagefragment.mMapData = mapmodel;
        return mapmodel;
    }

*/



/*
    static boolean access$302(ScrollableImageFragment scrollableimagefragment, boolean flag)
    {
        scrollableimagefragment.animationIsRunning = flag;
        return flag;
    }

*/


}
