package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static qualteh.com.scrollviewprototype.ScrollableImageActivity.getActivityContext;

/**
 * Created by Virgil Tanase on 17.03.2016.
 */
class GraphicDrawer {


    public static void drawRandomDrawable ( ImageView img, FrameLayout frame ) {
        Paint paint = new Paint(  );
        paint.setColor( Color.BLACK );
        drawRandomDrawable( img, frame, paint );
    }

    public static void drawRandomDrawable (ImageView img, FrameLayout frame, final Paint paint ) {

        paint.setStrokeWidth( 20f );
        paint.setStrokeJoin( Paint.Join.ROUND );


        img.setBackgroundColor( Color.DKGRAY );

        final List<Float> points = new ArrayList<>(  );

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

        final List<List<Float>> floatList = drawBuildings( frame );

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

    public static List<List<Float>> drawBuildings (FrameLayout frame) {
        final List<Button> imageViewsList = new ArrayList<>();
        final List<List<Float>> pointStringsList = new ArrayList<>(  );

        List<Float> pointStringsA = new ArrayList<>(  );

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
                new Button( getActivityContext() ) );
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams( 100, 100 );
        lp.setMargins( 100, 100, 0, 0 );
        imageViewsList.get( 0 ).setLayoutParams( lp );
        imageViewsList.get( 0 ).setTag( "Image1" );
        imageViewsList.get( 0 ).setText( "Image1" );


        imageViewsList.get( 0 ).setContentDescription( "Image1" );
        imageViewsList.get( 0 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                Toast.makeText( getActivityContext(), ( CharSequence ) imageViewsList.get( 0 ).getTag(), Toast.LENGTH_SHORT ).show();
            }
        } );
        frame.addView( imageViewsList.get( 0 ) );

        List<Float> pointStringsB = new ArrayList<>(  );
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
                new Button( getActivityContext() ) );
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(100,100);
        lp2.setMargins( 300, 300, 0, 0 );
        imageViewsList.get( 1 ).setLayoutParams( lp2 );
        imageViewsList.get( 1 ).setTag( "Image2" );

        imageViewsList.get( 1 ).setText("Image2" );
        imageViewsList.get( 1 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                Toast.makeText( getActivityContext(), ( CharSequence ) imageViewsList.get( 1 ).getTag(), Toast.LENGTH_SHORT ).show();
            }
        } );
        frame.addView( imageViewsList.get( 1 ) );

        List<Float> pointStringsC = new ArrayList<>(  );
        pointStringsC.add( 1300f );
        pointStringsC.add( 1300f );
        pointStringsC.add( 1400f );
        pointStringsC.add( 1300f );
        pointStringsC.add( 1400f );
        pointStringsC.add( 1400f );
        pointStringsC.add( 1300f );
        pointStringsC.add( 1400f );
        pointStringsList.add( pointStringsC );
        imageViewsList.add(
                new Button( getActivityContext() ) );
        FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(100,100);
        lp3.setMargins( 1300, 1300, 0, 0 );
        imageViewsList.get( 2 ).setLayoutParams( lp3 );
        imageViewsList.get( 2 ).setTag( "Image3" );

        imageViewsList.get( 2 ).setText("Image3" );
        imageViewsList.get( 2 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View v ) {
                Toast.makeText( getActivityContext(), ( CharSequence ) imageViewsList.get( 2 ).getTag(), Toast.LENGTH_SHORT ).show();
            }
        } );
        frame.addView( imageViewsList.get( 2 ) );

        return pointStringsList;
    }

    public void drawDeposits (final Paint paint) {
    }

}
