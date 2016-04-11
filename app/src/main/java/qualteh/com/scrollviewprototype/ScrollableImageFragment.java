// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
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



public class ScrollableImageFragment extends Fragment implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener
{


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
    @Bind( R.id.vectorTest )ImageView img;
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

    public ScrollableImageFragment() {
        scaleDirection = -1;
        scale = 1.0F;
        lastScaleFactor = 0.0F;
        animationIsRunning = false;
        lastScaleTimestamp = System.currentTimeMillis();
    }

    public static ScrollableImageFragment newInstance()
    {
        return new ScrollableImageFragment();
    }

    //Overriden methods

    //Boundary methods
    private int calcScrollToX(int currentScroll) {
        if (currentScroll < leftLimit)
        {
            return (int)leftLimit;
        }
        if (currentScroll > rightLimit)
        {
            return (int)rightLimit;
        } else
        {
            return mainContainer.getScrollX();
        }
    }

    private int calcScrollToX(float scrollPosition) {
        scrollPosition = (float)mainContainer.getScrollX() + (mx - scrollPosition);
        if (scrollPosition < (float)leftLimit)
        {
            return (int)leftLimit;
        }
        if (scrollPosition > (float)rightLimit)
        {
            return (int)rightLimit;
        } else
        {
            return mainContainer.getScrollX();
        }
    }

    private int calcScrollToY(int currentScroll) {
        if (currentScroll < topLimit)
        {
            return (int)topLimit;
        }
        if (currentScroll > bottomLimit)
        {
            return (int)bottomLimit;
        } else
        {
            return mainContainer.getScrollY();
        }
    }

    private int calcScrollToY(float scrollPosition) {
        scrollPosition = (float)mainContainer.getScrollY() + (my - scrollPosition);
        if ((long)Math.round(scrollPosition) < topLimit)
        {
            return (int)topLimit;
        }
        if ((long)Math.round(scrollPosition) > bottomLimit)
        {
            return (int)bottomLimit;
        } else
        {
            return mainContainer.getScrollY();
        }
    }

    private boolean checkBoundsX(int currentScroll) {
        leftLimit = Math.round(((scale * (float)(SCREEN_WIDTH / 8))) * ((scale - 1.0F) * 4F) * -1F);
        rightLimit = Math.round(((float)leftLimit + (float)SCREEN_WIDTH * scale) - (float)getResources().getDisplayMetrics().widthPixels);

        return (long)Math.round(currentScroll) > leftLimit && (long)Math.round(currentScroll) < rightLimit;
    }

    private boolean checkBoundsX(float touchPosition) {
        touchPosition = (float)mainContainer.getScrollX() + (mx - touchPosition);
        leftLimit = Math.round(((scale * (float)(SCREEN_WIDTH / 8))) * ((scale - 1.0F) * 4F) * -1F);
        rightLimit = Math.round(((float)leftLimit + (float)SCREEN_WIDTH * scale) - (float)getResources().getDisplayMetrics().widthPixels);

        return (long)Math.round(touchPosition) > leftLimit && (long)Math.round(touchPosition) < rightLimit;
    }

    private boolean checkBoundsY(int currentScroll) {
        topLimit = Math.round(scale * (float)(SCREEN_HEIGHT / 8) * ((scale - 1.0F) * 4F) * -1F);
        bottomLimit = Math.round(((float)topLimit + (float)SCREEN_HEIGHT * scale) - (float)getResources().getDisplayMetrics().heightPixels);
        Log.d( "Height", currentScroll + " " + topLimit + " " + bottomLimit);
        return (long)Math.round(currentScroll) > topLimit && (long)Math.round(currentScroll) < bottomLimit;
    }

    private boolean checkBoundsY(float touchPosition) {
        touchPosition = (float)mainContainer.getScrollY() + (my - touchPosition);
        topLimit = Math.round(scale * (float)(SCREEN_HEIGHT / 8) * ((scale - 1.0F) * 4F) * -1F);
        bottomLimit = Math.round(((float)topLimit + (float)SCREEN_HEIGHT * scale) - (float)getResources().getDisplayMetrics().heightPixels);
        // Log.d( "Height", ( new StringBuilder() ).append( f ).append( " " ).append( topLimit ).toString() );
        return (long)Math.round(touchPosition) > topLimit && (long)Math.round(touchPosition) < bottomLimit;
    }

    //Animation methods
    private void connectionFailureAnimation() {
        firstTimeTextError.setVisibility(View.VISIBLE);
        AlphaAnimation alphaanimation = new AlphaAnimation(0.0F, 1.0F);
        alphaanimation.setRepeatMode(2);
        alphaanimation.setRepeatCount(1);
        alphaanimation.setFillAfter(true);
        alphaanimation.setStartOffset(2000L);
        alphaanimation.setDuration(1000L);
        firstTimeTextError.setAnimation(alphaanimation);
    }






    private void setDatabaseByResponse(boolean flag) {
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
        mMapData = dbhelper.getMapModel( sqlitedatabase );
        GraphicDrawer.drawMap(img, mFrameLayout, mPaint, mMapData, demoMachine, SCREEN_DENSITY);
    }

    public void demoButtonClicked() {
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

    public void handleTouchEvent(MotionEvent motionEvent) {
        switch ( motionEvent.getAction() ){
            case MotionEvent.ACTION_DOWN:
                mx = motionEvent.getX();
                my = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getPointerCount() < 2 && System.currentTimeMillis() - lastScaleTimestamp > 500L)
                {
                    float curX = motionEvent.getX();
                    float curY = motionEvent.getY();
                    if (checkBoundsX(curX))
                    {
                        mainContainer.scrollBy((int)(mx - curX), 0);
                    }
                    if (checkBoundsY(curY))
                    {
                        mainContainer.scrollBy(0, (int)(my - curY));
                    }
                    mx = curX;
                    my = curY;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                lastScaleTimestamp = System.currentTimeMillis();
                return;
            case MotionEvent.ACTION_UP:
                float curX = motionEvent.getX();
                float curY = motionEvent.getY();

                int i = mainContainer.getScrollX();
                int j = mainContainer.getScrollY();
                if (!checkBoundsX(curX))
                {
                    i = calcScrollToX(curX);
                }
                if (!checkBoundsY(curY))
                {
                    j = calcScrollToY(curY);
                }
                mainContainer.scrollTo(i, j);
                break;
            default:
                break;
        }
    }

    public void onClick(View view)
    {
        demoButtonClicked();
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate( bundle );
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewgroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.activity_main, viewgroup, false);
        ButterKnife.bind(this, view);
        Consts.SIZE_MULTIPLIER = ((double)getResources().getDisplayMetrics().widthPixels * 1.0D) / (double)((float)Consts.DIAGRAM_WIDTH * getResources().getDisplayMetrics().density + 20F * getResources().getDisplayMetrics().density);

        Consts.UI_X_FACTOR = ((double)Consts.DIAGRAM_WIDTH * Consts.SIZE_MULTIPLIER) / ((Consts.GeoCoordinates.MAX_X-Consts.GeoCoordinates.MIN_X)*1000000);
        Consts.UI_Y_FACTOR = ((double)Consts.DIAGRAM_HEIGHT * Consts.SIZE_MULTIPLIER) / ((Consts.GeoCoordinates.MAX_Y-Consts.GeoCoordinates.MIN_Y)*1000000);

        SCREEN_DENSITY = getResources().getDisplayMetrics().density;
        SCREEN_WIDTH = (int)Math.round((double)(Consts.DIAGRAM_WIDTH + 20) * Consts.SIZE_MULTIPLIER * SCREEN_DENSITY);
        SCREEN_HEIGHT = (int)Math.round((double)(Consts.DIAGRAM_HEIGHT + 10) * Consts.SIZE_MULTIPLIER * SCREEN_DENSITY);

        mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( ( int ) Math.round( ( double ) ( Consts.DIAGRAM_WIDTH + 20 ) * Consts.SIZE_MULTIPLIER * ( double ) scale * SCREEN_DENSITY ), ( int ) Math.round( ( double ) ( Consts.DIAGRAM_HEIGHT + 10 ) * Consts.SIZE_MULTIPLIER * ( double ) scale * SCREEN_DENSITY ) ) );
        mFrameLayout.invalidate();
        mFrameLayout.requestLayout();

        gestureDetector = new GestureDetector(getContext(), new GestureListener());
        scaleDetector = new ScaleGestureDetector(getActivity(), this);

        mPaint.setColor( Color.RED );
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
                canvas.drawCircle( getResources().getDisplayMetrics().density * 20f, getResources().getDisplayMetrics().density * 20f, getResources().getDisplayMetrics().density * 20f, paint );
            }

            public int getOpacity () {
                return 0;
            }

            public void setAlpha ( int i ) {
            }

            public void setColorFilter ( ColorFilter colorfilter ) {
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
        return view;
    }

    public void onPause()
    {
        super.onPause();
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float f;
        boolean canZoom;
        if (checkBoundsX(scaleGestureDetector.getFocusX()) && checkBoundsY( scaleGestureDetector.getFocusY() ))
        {
            canZoom = true;
        } else
        {
            canZoom = false;
        }

        f = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0.0F || Math.signum(f) == Math.signum(lastScaleFactor) && canZoom)
        {
            scale = scale * f;
            scale = Math.max(1.0F, Math.min( scale, 3F ) );

            mFrameLayout.setScaleX( scale );
            mFrameLayout.setScaleY( scale );
            mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( Math.round( ( float ) SCREEN_WIDTH * scale ), Math.round( ( float ) SCREEN_HEIGHT * scale ) ) );

            int scrollToX = calcScrollToX( scaleGestureDetector.getFocusX() );
            int scrollToY = calcScrollToY( scaleGestureDetector.getFocusY() );

            Log.d( "Focus On", scaleGestureDetector.getFocusX() + " " + scaleGestureDetector.getFocusY() + " " + scrollToX + " " + scrollToY );

            //mainContainer.scrollTo( scrollToX, scrollToY );

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

    public void onScaleEnd(ScaleGestureDetector scalegesturedetector) {
        lastScaleTimestamp = System.currentTimeMillis();
    }

    public void scaleButtonClicked() {
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

    void logBuildingCoords() {
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

        Log.d("WTF", s);
    }

    void logMainCoords() {
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
        Log.d( "WTF", s );
    }

    void logStorageCoords() {
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

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onDoubleTap(MotionEvent motionevent)
        {
            Log.d("Tap",""+mainContainer.getScrollX()+" "+mainContainer.getScrollY());
            return true;
        }

        public boolean onDown(MotionEvent motionevent)
        {
            return true;
        }

        @Override
        public boolean onFling ( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {

            double dist = Math.sqrt( Math.pow( ( e2.getX() - e1.getX() ), 2 ) - Math.pow( ( e2.getY() - e1.getY() ), 2 ) );



            if(e1.getPointerCount()<2 && System.currentTimeMillis() - lastScaleTimestamp > 500 && (e2.getEventTime() - e1.getEventTime() <500f ) ){



                int currentX = (int)(mainContainer.getScrollX()- (velocityX/Consts.SIZE_MULTIPLIER));
                int currentY = (int)(mainContainer.getScrollY()- (velocityY/Consts.SIZE_MULTIPLIER));

                Log.d("Pre Fling",""+currentX+" "+currentY + " "+velocityX+" "+velocityY);

                if(!checkBoundsX( currentX )){
                    currentX = calcScrollToX( currentX);
                }
                if(!checkBoundsY( currentY )){
                    currentY = calcScrollToY(currentY);
                }


                ObjectAnimator xFling = ObjectAnimator.ofInt( mainContainer, "scrollX", currentX );
                ObjectAnimator yFling = ObjectAnimator.ofInt( mainContainer, "scrollY", currentY );

                Log.d("Post Fling",""+currentX+" "+currentY);

                xFling.setInterpolator( new DecelerateInterpolator() );
                yFling.setInterpolator( new DecelerateInterpolator() );

                AnimatorSet animators = new AnimatorSet();
                animators.setDuration(1000);
                animators.playTogether(xFling, yFling);
                animators.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator arg0) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        // TODO Auto-generated method stub
                        Log.d("End",mainContainer.getScrollX()+" "+mainContainer.getScrollY());
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {
                        // TODO Auto-generated method stub

                    }
                });
                animators.start();
            }

            return false;
        }
    }
}
