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
import android.graphics.Rect;
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
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
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



public class ScrollableImageFragment extends Fragment implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener {

    private AnimatorSet animators = new AnimatorSet();
    private static final float MAX_ZOOM = 4F;
    private static final float MIN_ZOOM = 1F;
    private static double SCREEN_DENSITY;

    private boolean animationIsRunning;
    private DemoMachine demoMachine;
    @Bind( R.id.errorText )TextView firstTimeTextError;
    public GestureDetector gestureDetector;
    @Bind( R.id.vectorTest )ImageView img;
    private float lastScaleFactor;

    private int previousScrollX;
    private int previousScrollY;

    private long lastScaleTimestamp;
    private long startTouchTimestamp;

    @Bind( R.id.uiContainer )FrameLayout mFrameLayout;
    private MapModel mMapData;
    private final Paint mPaint = new Paint();

    Rect scrollBounds = new Rect();
    private ImageView machineView;
    @Bind( R.id.contentContainer )FrameLayout mainContainer;
    private float mx;
    private float my;
    private float startX;
    private float startY;

    private float scale;
    public ScaleGestureDetector scaleDetector;

    public ScrollableImageFragment() {
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
                paint.setColor( Color.RED );
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
                Log.d("Response", String.valueOf( response.body().getCommissions().size() ) );
                mMapData = ( MapModel ) response.body();
                mMapData.getMainCoordinates().adjustCoordinates( getResources().getDisplayMetrics().density );
                mMapData.adjustCoordinates( getResources().getDisplayMetrics().density );
                setDatabaseByResponse( true );
            }

            @Override
            public void onFailure ( Call<MapModel> call, Throwable throwable ) {
                Log.d("Error",throwable.getMessage());
                setDatabaseByResponse( false );
            }
        } );
        animators.addListener( new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart ( Animator arg0 ) {
                previousScrollX = mainContainer.getScrollX();
                previousScrollY = mainContainer.getScrollY();
            }

            @Override
            public void onAnimationRepeat ( Animator arg0 ) {
            }

            @Override
            public void onAnimationEnd ( Animator arg0 ) {
            }

            @Override
            public void onAnimationCancel ( Animator arg0 ) {
            }
        } );
        mainContainer.getViewTreeObserver().addOnScrollChangedListener( new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged () {
                if(mFrameLayout.getGlobalVisibleRect( scrollBounds )){
                    if(scrollBounds.left>0 || scrollBounds.right < getResources().getDisplayMetrics().widthPixels || scrollBounds.top>Math.round( 24*getResources().getDisplayMetrics().density+1) || scrollBounds.bottom< getResources().getDisplayMetrics().heightPixels){
                        if(animators.isRunning()) {
                            mainContainer.scrollTo( previousScrollX, previousScrollY );
                            animators.cancel();
                        }
                    }
                    else{
                        previousScrollX = mainContainer.getScrollX();
                        previousScrollY = mainContainer.getScrollY();
                    }
                }
            }
        } );
        return view;
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
        firstTimeTextError.setAnimation( alphaanimation );
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
        GraphicDrawer.drawMap(img, mFrameLayout, mPaint, mMapData, demoMachine, this);
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

    public void handleTouchEvent(MotionEvent motionEvent) {
        switch ( motionEvent.getAction() ){
            case MotionEvent.ACTION_DOWN:
                mx = motionEvent.getX();
                my = motionEvent.getY();
                startX = mx;
                startY = my;
                startTouchTimestamp = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getPointerCount() < 2 && System.currentTimeMillis() - lastScaleTimestamp > 500L)
                {
                    float curX = motionEvent.getX();
                    float curY = motionEvent.getY();

                    mainContainer.getHitRect( scrollBounds );
                    mainContainer.scrollBy( ( int ) ( mx - curX ), 0 );
                    mainContainer.scrollBy( 0, ( int ) ( my - curY ) );

                    if(mFrameLayout.getGlobalVisibleRect( scrollBounds )){
                        if(scrollBounds.left>0 || scrollBounds.right < getResources().getDisplayMetrics().widthPixels){
                            mainContainer.scrollBy( (int)(mx-curX)*(-1),0 );
                        }
                        if(scrollBounds.top>Math.round( 24*getResources().getDisplayMetrics().density+1) || scrollBounds.bottom< getResources().getDisplayMetrics().heightPixels  ) {
                            mainContainer.scrollBy(0, (int)(my - curY)*(-1));
                        }
                    }

                    previousScrollX = mainContainer.getScrollX();
                    previousScrollY = mainContainer.getScrollY();

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

                if(mFrameLayout.getGlobalVisibleRect( scrollBounds )) {
                    if(scrollBounds.left >0){
                        mainContainer.scrollBy( scrollBounds.left, 0 );
                    }
                    if(scrollBounds.right <getResources().getDisplayMetrics().widthPixels){
                        mainContainer.scrollBy( scrollBounds.right-getResources().getDisplayMetrics().widthPixels ,0 );
                    }
                    if(scrollBounds.top > Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) ){
                        mainContainer.scrollBy( 0 , scrollBounds.top-Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) );
                    }
                    if(scrollBounds.bottom < getResources().getDisplayMetrics().heightPixels){
                        mainContainer.scrollBy( 0 , scrollBounds.bottom-getResources().getDisplayMetrics().heightPixels );
                    }
                }
                long timeMillisDiff = System.currentTimeMillis()-startTouchTimestamp;
                if(timeMillisDiff<500 && motionEvent.getPointerCount()<2 && System.currentTimeMillis() - lastScaleTimestamp > 500){

                    ObjectAnimator xFling = ObjectAnimator.ofInt( mainContainer, "scrollX", mainContainer.getScrollX()-(int)(curX-startX) );
                    ObjectAnimator yFling = ObjectAnimator.ofInt( mainContainer, "scrollY", mainContainer.getScrollY()-(int)(curY-startY) );

                    xFling.setInterpolator( new DecelerateInterpolator() );
                    yFling.setInterpolator( new DecelerateInterpolator() );

                    animators = new AnimatorSet();

                    animators.setDuration( 1000 );
                    animators.playTogether( xFling,yFling );
                    if(!animators.isRunning()){
                        animators.start();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onPause()
    {
        super.onPause();
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        float factor;
        boolean canZoom=false;

        if(mFrameLayout.getGlobalVisibleRect( scrollBounds )) {
            if ( !(scrollBounds.left > 0 || scrollBounds.right < getResources().getDisplayMetrics().widthPixels || scrollBounds.top > Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) || scrollBounds.bottom < getResources().getDisplayMetrics().heightPixels) ) {
                canZoom = true;

            }
        }
        factor = scaleDetector.getScaleFactor();

        if(canZoom){
            if ((lastScaleFactor == 0.0F || Math.signum(factor) == Math.signum(lastScaleFactor))) {
                scale = scale * factor;
                scale = Math.max(MIN_ZOOM, Math.min( scale, MAX_ZOOM ) );

                mFrameLayout.setScaleX( scale );
                mFrameLayout.setScaleY( scale );

                mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( Math.round( ( float ) mFrameLayout.getWidth() ), Math.round( ( float ) mFrameLayout.getHeight() ) ) );

                mFrameLayout.invalidate();
                mFrameLayout.requestLayout();
                mainContainer.getHitRect( scrollBounds );
                if(mFrameLayout.getGlobalVisibleRect( scrollBounds )) {
                    if ( (scrollBounds.left > 0 || scrollBounds.right < getResources().getDisplayMetrics().widthPixels || scrollBounds.top > Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) || scrollBounds.bottom < getResources().getDisplayMetrics().heightPixels) ) {
                        if(scale>1) {
                            if ( scrollBounds.left > 0 ) {
                                mainContainer.setPivotX( getResources().getDisplayMetrics().widthPixels );
                            }
                            if ( scrollBounds.right < getResources().getDisplayMetrics().widthPixels ) {
                                mainContainer.setPivotX( 0 );
                            }
                            if ( scrollBounds.top > Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) ) {
                                mainContainer.setPivotY( getResources().getDisplayMetrics().heightPixels );
                            }
                            if ( scrollBounds.bottom < getResources().getDisplayMetrics().heightPixels ) {
                                mainContainer.setPivotY( 0 );
                            }
                        }
                        mFrameLayout.setScaleX( scale );
                        mFrameLayout.setScaleY( scale );
                        if ( scrollBounds.left > 0 ) {
                            mainContainer.scrollBy( scrollBounds.left,0 );
                        }
                        if ( scrollBounds.right < getResources().getDisplayMetrics().widthPixels ) {
                            mainContainer.scrollBy( scrollBounds.right-getResources().getDisplayMetrics().widthPixels,0 );
                        }
                        if ( scrollBounds.top > Math.round( 24 * getResources().getDisplayMetrics().density + 1 ) ) {
                            mainContainer.scrollBy(0,scrollBounds.top-Math.round( 24 * getResources().getDisplayMetrics().density + 1 ));
                        }
                        if ( scrollBounds.bottom < getResources().getDisplayMetrics().heightPixels ) {
                            mainContainer.scrollBy(0,scrollBounds.bottom-getResources().getDisplayMetrics().heightPixels);
                        }
                    }
                }
                lastScaleFactor = factor;
            }
            else {
                lastScaleFactor = 0.0F;
            }
        }
       return true;
    }

    public boolean onScaleBegin(ScaleGestureDetector scalegesturedetector) {
        if(animators.isRunning()){
            animators.cancel();
        }

        float newX = scaleDetector.getFocusX();
        float newY = scaleDetector.getFocusY();

        mFrameLayout.setTranslationX( mFrameLayout.getTranslationX() + (mFrameLayout.getPivotX() - newX) * (1-mFrameLayout.getScaleX()) );
        mFrameLayout.setTranslationY( mFrameLayout.getTranslationY() + ( mFrameLayout.getPivotY() - newY ) * ( 1 - mFrameLayout.getScaleY() ) );

        mFrameLayout.setPivotX( newX );
        mFrameLayout.setPivotY( newY );

        return true;
    }

    public void onScaleEnd(ScaleGestureDetector scalegesturedetector) {
        lastScaleTimestamp = System.currentTimeMillis();
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
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onDoubleTap(MotionEvent motionevent)
        {
            return true;
        }

        public boolean onDown(MotionEvent motionevent)
        {
            return true;
        }


    }
}