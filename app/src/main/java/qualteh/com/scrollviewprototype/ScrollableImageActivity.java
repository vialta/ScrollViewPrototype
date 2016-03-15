package qualteh.com.scrollviewprototype;

/**
 * Created by Virgil Tanase on 10.03.2016.
 */
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrollableImageActivity extends Activity implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener{


    private final boolean DEBUG_SCROLLS=true;


    //ScalingFactor i.e. Amount of Zoom
    static float mScaleFactor = 1.0f;

    // Maximum and Minimum Zoom
    private static float MIN_ZOOM = 1.0f;
    private static float MAX_ZOOM = 3.0f;

    //Different Operation to be used
    private final int NONE_OPERATION=0;
    private final int ZOOM_OPERATION=2;
    private float mWidth= 1280;
    private float mHeight=800;

    private float mx, my;
    final Paint mPaint = new Paint(  );
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    private FrameLayout mFrameLayout;
    private FrameLayout mainContainer;

    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    private float mStartWidth;
    private float mStartHeight;

    private float mScale =1f;
    private ScaleGestureDetector scaleDetector;
    GestureDetector gestureDetector;

    int startingWidth,startingHeight;

    protected Fragment createFragment () {
        return ScrollableImageFragment.newInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mFrameLayout = ( FrameLayout ) findViewById( R.id.uiContainer );
        mainContainer = (FrameLayout) findViewById( R.id.mainContainer );


//            vScroll = (ScrollView) findViewById(R.id.vScroll);
//            hScroll = (HorizontalScrollView) findViewById(R.id.hScroll);



        Button b1 = (Button)findViewById( R.id.button1 );
        Button b2 = (Button)findViewById( R.id.button2 );
        b1.setOnClickListener( this );
        b2.setOnClickListener( this );

       scaleDetector = new ScaleGestureDetector( getApplicationContext(), this );





        mPaint.setColor( Color.RED );
        mPaint.setStrokeWidth( 20f );
        mPaint.setStrokeJoin( Paint.Join.ROUND);
        drawRandomDrawable( mPaint );




        drawBuildings();
        DisplayMetrics dp = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( dp );




    }

    @Override
    public void onClick ( View v ) {
        mPaint.setColor( Color.RED );
        mPaint.setStrokeJoin( Paint.Join.ROUND );
        drawRandomDrawable( mPaint );

    }


    //TODO RXJAVA THIS!!! IT IS SO WORTH IT



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curX, curY;

        if(event.getPointerCount()<2) {
            switch ( event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                    mx = event.getX();
                    my = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    curX = event.getX();
                    curY = event.getY();
//                    if(DEBUG_SCROLLS){
//                        vScroll.scrollBy( ( int ) ( mx - curX ), ( int ) ( my - curY ) );
//                        hScroll.scrollBy( ( int ) ( mx - curX ), ( int ) ( my - curY ) );
//                    }
                    mx = curX;
                    my = curY;
                    break;
                case MotionEvent.ACTION_UP:
                    curX = event.getX();
                    curY = event.getY();
//                    if(DEBUG_SCROLLS){
//                        vScroll.scrollBy( ( int ) ( mx - curX ), ( int ) ( my - curY ) );
//                        hScroll.scrollBy( ( int ) ( mx - curX ), ( int ) ( my - curY ) );
//                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    startX = event.getX() - prevDx;
                    startY = event.getY() - prevDy;
                    break;
            }
        }
        scaleDetector.onTouchEvent( event );

        if (event.getPointerCount()>2 && scale >= MIN_ZOOM ) {
//            float maxDx = (hScroll.getChildAt( 0 ).getWidth() -  (hScroll.getChildAt( 0 ).getWidth() / scale)) / 2 * scale;
//            float maxDy = (hScroll.getChildAt( 0 ).getHeight() - (hScroll.getChildAt( 0 ).getHeight() / scale))/ 2 * scale;
//            dx = Math.min(Math.max(dx, -maxDx), maxDx);
//            dy = Math.min(Math.max(dy, -maxDy), maxDy);
            applyScaleAndTranslation();
        }

        return true;
    }

    @Override
    public void onScaleEnd ( ScaleGestureDetector detector ) {


    }

    @Override
    public boolean onScaleBegin ( ScaleGestureDetector detector ) {
        return true;
    }

    @Override
    public boolean onScale ( ScaleGestureDetector detector ) {

//        float scale = 1-detector.getScaleFactor();
//
//        float prevScale =mScale;
//        mScale+=scale;
//        if(mScale<0.1f)
//            mScale=0.1f;
//        if(mScale>10f)
//            mScale=10f;
//
//        ScaleAnimation scaleAnimation = new ScaleAnimation( 1f/prevScale, 1f/mScale, 1f/prevScale, 1f/mScale, detector.getFocusX(), detector.getFocusY() );
//        scaleAnimation.setDuration( 0 );
//        scaleAnimation.setFillAfter( true );
//
//        mFrameLayout.startAnimation( scaleAnimation );

        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }


        Log.d("Container Scale ",mFrameLayout.getScaleX()+" "+mFrameLayout.getScaleY());
        Log.d("Container Size ",mFrameLayout.getWidth()+" "+mFrameLayout.getHeight() );

        mFrameLayout.setScaleX( scale );
        mFrameLayout.setScaleY( scale );
        mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( Math.round( 3200 * scale ), Math.round ( 1800 * scale)) );

        mFrameLayout.invalidate();
        mFrameLayout.requestLayout();

        if(DEBUG_SCROLLS){
//            hScroll.getChildAt( 0 ).setScaleX( scale );
//            hScroll.getChildAt( 0 ).setScaleY( scale );

//            hScroll.getChildAt( 0 ).getLayoutParams().width = ( int ) (hScroll.getChildAt( 0 ).getWidth() * scale);
//            vScroll.getChildAt( 0 ).getLayoutParams().height = ( int ) (hScroll.getChildAt( 0 ).getHeight() * scale);

        }


        return true;
    }

    private void drawRandomDrawable ( final Paint paint) {
        paint.setStrokeWidth( 20f );
        paint.setStrokeJoin( Paint.Join.ROUND );
        ImageView img = ( ImageView ) findViewById( R.id.vectorTest );
        final List<Float> points = new ArrayList<Float>(  );

        //5 points = 10 values
        points.add (  300f );
        points.add (  100f );
        points.add (   50f );
        points.add (  600f );
        points.add (  300f );
        points.add ( 1100f );
        points.add ( 1500f );
        points.add ( 1100f );
        points.add( 1500f );
        points.add( 100f );




        final List<List<Float>> floatList = drawBuildings(  );

        img.setImageDrawable( new Drawable() {
            @Override
            public void draw ( Canvas canvas ) {
                //paint.setStrokeWidth( 20f );
                paint.setStrokeJoin( Paint.Join.ROUND );
                paint.setColor( Color.RED );
                for ( int i = 0 ; i < points.size() - 2 ; i += 2 ) {
                    canvas.drawLine( points.get( i ), points.get( i + 1 ), points.get( i + 2 ), points.get( i + 3 ), paint );
                }
                canvas.drawLine( points.get( points.size() - 2 ), points.get( points.size() - 1 ), points.get( 0 ), points.get( 1 ), paint );
                paint.setStrokeWidth( 5f );
                paint.setColor( Color.BLUE );
                for(int j=0;j<floatList.size();j++) {
                    for ( int i = 0 ; i < floatList.get( j ).size() - 2 ; i += 2 ) {
                        canvas.drawLine( floatList.get( j ).get( i ), floatList.get( j ).get( i + 1 ), floatList.get( j ).get( i + 2 ), floatList.get( j ).get( i + 3 ), paint );
                    }
                    canvas.drawLine(  floatList.get( j ).get( floatList.get( j ).size() - 2 ),  floatList.get( j ).get( floatList.get( j ).size() - 1 ),  floatList.get( j ).get( 0 ),  floatList.get( j ).get( 1 ), paint );
                }
            }

            @Override
            public void setAlpha ( int alpha ) {

            }

            @Override
            public void setColorFilter ( ColorFilter colorFilter ) {

            }

            @Override
            public int getOpacity () {
                return PixelFormat.OPAQUE;
            }
        } );
    }

    private List<List<Float>> drawBuildings () {

        final List<Button> imageViewsList = new ArrayList<Button>();
        final List<List<Float>> pointStringsList = new ArrayList<List<Float>>(  );
        List<Float> pointStringsA = new ArrayList<Float>(  );

        pointStringsA.add( 100f );
        pointStringsA.add( 100f );
        pointStringsA.add( 200f );
        pointStringsA.add( 100f );
        pointStringsA.add( 200f );
        pointStringsA.add( 200f );
        pointStringsA.add( 100f );
        pointStringsA.add( 200f );
        pointStringsList.add( pointStringsA );
        imageViewsList.add(
                new Button( ScrollableImageActivity.this ) );
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(100,100);
        lp.setMargins( 100, 100, 0, 0 );
        imageViewsList.get( 0 ).setLayoutParams( lp );
        imageViewsList.get( 0 ).setTag( "Image1" );
        imageViewsList.get( 0 ).setText("Image1");


        imageViewsList.get( 0 ).setContentDescription( "Image1" );
        imageViewsList.get( 0 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                Toast.makeText( getApplicationContext(), ( CharSequence ) imageViewsList.get( 0 ).getTag(), Toast.LENGTH_SHORT ).show();
            }
        } );
        mFrameLayout.addView( imageViewsList.get( 0 ) );

        List<Float> pointStringsB = new ArrayList<Float>(  );
        pointStringsB.add( 300f );
        pointStringsB.add( 300f );
        pointStringsB.add( 400f );
        pointStringsB.add( 300f );
        pointStringsB.add( 400f );
        pointStringsB.add( 400f );
        pointStringsB.add( 300f );
        pointStringsB.add( 400f );
        pointStringsList.add( pointStringsB );
        imageViewsList.add(
                new Button( ScrollableImageActivity.this ) );
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(100,100);
        lp2.setMargins( 300, 300, 0, 0 );
        imageViewsList.get( 1 ).setLayoutParams( lp2 );
        imageViewsList.get( 1 ).setTag( "Image2" );

        imageViewsList.get( 1 ).setText("Image2" );
        imageViewsList.get( 1 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                Toast.makeText( getApplicationContext(), ( CharSequence ) imageViewsList.get( 1 ).getTag(), Toast.LENGTH_SHORT ).show();
            }
        } );
        mFrameLayout.addView( imageViewsList.get( 1 ) );
        return pointStringsList;
    }

    private void drawDeposits (final Paint paint) {

    }

    private void applyScaleAndTranslation() {
        Log.d( "SCALE ", "applyScaleAndTranslation" );



    }

}