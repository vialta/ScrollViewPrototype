package qualteh.com.scrollviewprototype;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Virgil Tanase on 10.03.2016.
 */
public class ScrollableImageFragment extends Fragment implements View.OnClickListener, ScaleGestureDetector.OnScaleGestureListener{

    public ScaleGestureDetector scaleDetector;

    // Maximum and Minimum Zoom
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 3.0f;

    private static final int SCREEN_WIDTH  = 3200;
    private static final int SCREEN_HEIGHT = 1800;

    private float mx, my;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    private final Paint mPaint = new Paint(  );

    @Bind( R.id.uiContainer ) FrameLayout mFrameLayout;
    @Bind( R.id.contentContainer ) FrameLayout mainContainer;
    @Bind(R.id.vectorTest) ImageView img;

    private Button b2;

    public GestureDetector gestureDetector;

    public static ScrollableImageFragment newInstance () {
        return new ScrollableImageFragment();
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Nullable
    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate( R.layout.activity_main,container,false );
        ButterKnife.bind( this, view );
        mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( Math.round( SCREEN_WIDTH * scale ), Math.round( SCREEN_HEIGHT * scale ) ) );
        mFrameLayout.invalidate();
        mFrameLayout.requestLayout();

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        Button b1 = (Button)container.findViewById( R.id.button1 );
        b2 = (Button)container.findViewById( R.id.button2 );
        b1.setOnClickListener( this );
        b2.setOnClickListener( this );

        scaleDetector = new ScaleGestureDetector( getActivity(), this );

        mPaint.setColor( Color.RED );
        mPaint.setStrokeWidth( 20f );
        mPaint.setStrokeJoin( Paint.Join.ROUND);
        GraphicDrawer.drawRandomDrawable( img, mFrameLayout, mPaint );
        GraphicDrawer.drawBuildings(mFrameLayout);
        return view;
    }

    @Override
    public void onClick ( View v ) {
        mPaint.setColor( Color.RED );
        mPaint.setStrokeJoin( Paint.Join.ROUND );

        GraphicDrawer.drawRandomDrawable( img, mFrameLayout, mPaint );
    }

    @Override
    public void onScaleEnd ( ScaleGestureDetector detector ) {
        if ( ! checkBoundsX( detector.getFocusX() ) ) {
            mainContainer.scrollTo( calcScrollToX( detector.getFocusX() ), mainContainer.getScrollY() );
        }
        if ( ! checkBoundsY( detector.getFocusY() ) ) {
            mainContainer.scrollTo( mainContainer.getScrollX(), calcScrollToY( detector.getFocusY() ) );
        }
    }

    @Override
    public boolean onScaleBegin ( ScaleGestureDetector detector ) {
        return true;
    }

    @Override
    public boolean onScale ( ScaleGestureDetector detector ) {

        float scaleFactor = scaleDetector.getScaleFactor();
        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        if ( ! checkBoundsX( detector.getFocusX() ) ) {
            mainContainer.scrollTo( calcScrollToX( detector.getFocusX() ), mainContainer.getScrollY() );
        }
        if ( ! checkBoundsY( detector.getFocusY() ) ) {
            mainContainer.scrollTo( mainContainer.getScrollX(), calcScrollToY( detector.getFocusY() ) );
        }
        mFrameLayout.setScaleX( scale );
        mFrameLayout.setScaleY( scale );
        mFrameLayout.setLayoutParams( new FrameLayout.LayoutParams( Math.round( SCREEN_WIDTH * scale ), Math.round( SCREEN_HEIGHT * scale ) ) );

        b2.setX( SCREEN_WIDTH - b2.getWidth() );
        b2.setY( SCREEN_HEIGHT - b2.getHeight() );

        mFrameLayout.invalidate();
        mFrameLayout.requestLayout();

        Log.d("Scale ",detector.getFocusX()+" "+detector.getFocusY());

        return true;
    }

    private boolean checkBoundsX ( float curX ){
        float xValue = mainContainer.getScrollX()+(mx - curX);
        //container width
        int w = SCREEN_WIDTH;
        return ( Math.round( xValue ) > Math.round( ( w * ( scale - 1 ) / 2 * scale ) * ( - 1 ) ) ) && Math.round( xValue ) < Math.round( - w / 2 * scale * scale + 3 * w / 2 * scale - 3 * w / 5 );
    }

    private boolean checkBoundsY (float curY){
        float yValue = mainContainer.getScrollY()+(my - curY);
        //height
        int h = SCREEN_HEIGHT;
        return Math.round( yValue ) > Math.round( ( h * ( scale - 1 ) / 2 * scale ) * ( - 1 ) ) && Math.round( yValue ) < Math.round( - h / 2 * scale * scale + 3 * h / 2 * scale - 3 * h / 5 );
    }

    private int calcScrollToX ( float curX ) {
        float xValue = mainContainer.getScrollX()+(mx - curX);
        if(xValue<Math.round(( SCREEN_WIDTH * ( scale - 1 ) / 2 * scale ) * (-1)) ){
            return Math.round(( SCREEN_WIDTH * ( scale - 1 ) / 2 * scale ) * (-1));
        }
        if(xValue>Math.round(  -SCREEN_WIDTH/2 *scale*scale + 3*SCREEN_WIDTH /2 *scale - 3*SCREEN_WIDTH/5)){
            return Math.round( -SCREEN_WIDTH/2 *scale*scale + 3*SCREEN_WIDTH /2 *scale - 3*SCREEN_WIDTH/5);
        }
        return 0;
    }

    private int calcScrollToY(float curY){
        float yValue = mainContainer.getScrollY()+(my-curY);
        if(Math.round(yValue)<Math.round( ( SCREEN_HEIGHT * ( scale - 1 ) / 2 * scale ) * ( - 1 ) )){
            return Math.round(( SCREEN_HEIGHT * ( scale - 1 ) / 2 * scale ) * (-1));
        }
        if(Math.round(yValue)>Math.round( -SCREEN_HEIGHT /2 *scale*scale + 3*SCREEN_HEIGHT /2 *scale - 3*SCREEN_HEIGHT/5 )){
            return Math.round( -SCREEN_HEIGHT /2 *scale*scale + 3*SCREEN_HEIGHT /2 *scale - 3*SCREEN_HEIGHT/5 );
        }
        return 0;
    }

    public void handleTouchEvent(MotionEvent event){
        float curX, curY;

            switch ( event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                    //if(event.getPointerCount()<2) {
                        mx = event.getX();
                        my = event.getY();
                   // }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(event.getPointerCount()<2) {
                        curX = event.getX();
                        curY = event.getY();
                        if ( checkBoundsX( curX ) ) {
                            mainContainer.scrollBy( ( int ) ( mx - curX ), 0 );
                        }
                        if ( checkBoundsY( curY ) ) {
                            mainContainer.scrollBy( 0, ( int ) ( my - curY ) );
                        }

                        mx = curX;
                        my = curY;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    curX = event.getX();
                    curY = event.getY();

                    if ( ! checkBoundsX( curX ) ) {
                        mainContainer.scrollTo( calcScrollToX( curX ), mainContainer.getScrollY() );
                    }
                    if ( ! checkBoundsY( curY ) ) {
                        mainContainer.scrollTo( mainContainer.getScrollX(), calcScrollToY( curY ) );
                    }

                    break;

        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }

}