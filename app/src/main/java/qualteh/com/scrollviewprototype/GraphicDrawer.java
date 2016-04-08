// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import qualteh.com.scrollviewprototype.Model.Building;
import qualteh.com.scrollviewprototype.Model.MainCoordinates;
import qualteh.com.scrollviewprototype.Model.MapModel;
import qualteh.com.scrollviewprototype.Model.Storage;

// Referenced classes of package qualteh.com.scrollviewprototype:
//            DemoMachine, ScrollableImageActivity

class GraphicDrawer
{

    GraphicDrawer()
    {
    }

    public static void drawMap(ImageView imageView, final FrameLayout frameLayout, final Paint paint, final MapModel mapModel, DemoMachine demoMachine, double d)
    {
        paint.setStrokeJoin( Paint.Join.ROUND);
        imageView.setBackgroundColor( 0xff444444 );
        Object obj = mapModel.getMainCoordinates().getCoordinates();
        demoMachine = new DemoMachine();
//        for (obj = ((List) (obj)).iterator(); ((Iterator) (obj)).hasNext(); demoMachine.add( Integer.valueOf(((Double)((Iterator) (obj)).next()).intValue()))
//                ) { }

        final FrameLayout frame = frameLayout;
        final MapModel mMapData = mapModel;
        final List buildings = mapModel.getBuildings();

        final List<Double> points = mapModel.getMainCoordinates().getCoordinates();
        final List<Storage> storages = mapModel.getStorage();

        String s="";
        for(int i=0;i<points.size();i++){
            s+=points.get( i ).toString();
        }
        Log.d("I'mScrewed",s);

        imageView.setImageDrawable( new Drawable() {
            @Override
            public void draw ( Canvas canvas ) {
                paint.setStrokeWidth( mMapData.getMainCoordinates().getStrokeWidth().intValue() );
                paint.setColor( Color.parseColor( mMapData.getMainCoordinates().getStrokeColor() ) );
                for ( int i = 0 ; i < points.size() - 2 ; i += 2 ) {
                    Log.d( "Main Coords Draw", ( new StringBuilder() ).append( points.get( i ) ).append( " " ).append( points.get( i + 1 ) ).toString() );
                    canvas.drawLine(   Math.round(points.get( i )) ,  Math.round( points.get( i + 1 )), Math.round( points.get( i + 2 )), Math.round( points.get( i + 3 )), paint );
                }

                canvas.drawLine( Math.round(points.get( points.size() - 2 ) ), Math.round(points.get( points.size() - 1 ) ), Math.round(points.get( 0 ) ), Math.round(points.get( 1 ) ), paint );
                Button button;
                for ( Iterator iterator = buildings.iterator() ; iterator.hasNext() ; frame.addView( button ) ) {
                    Building building = ( Building ) iterator.next();
                    paint.setStrokeWidth( building.getStrokeWidth().intValue() );
                    paint.setColor( Color.parseColor( building.getStrokeColor() ) );
                    for ( int j = 0 ; j < building.getCoordinates().size() - 2 ; j += 2 ) {
                        canvas.drawLine( ( ( Double ) building.getCoordinates().get( j ) ).intValue(), ( ( Double ) building.getCoordinates().get( j + 1 ) ).intValue(), ( ( Double ) building.getCoordinates().get( j + 2 ) ).intValue(), ( ( Double ) building.getCoordinates().get( j + 3 ) ).intValue(), paint );
                    }

                    canvas.drawLine( ( ( Double ) building.getCoordinates().get( building.getCoordinates().size() - 2 ) ).intValue(), ( ( Double ) building.getCoordinates().get( building.getCoordinates().size() - 1 ) ).intValue(), ( ( Double ) building.getCoordinates().get( 0 ) ).intValue(), ( ( Double ) building.getCoordinates().get( 1 ) ).intValue(), paint );
                    button = new Button( ScrollableImageActivity.getActivityContext() );
                    FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams( GraphicDrawer.scaleX( building.getCoordinates() ), GraphicDrawer.scaleY( building.getCoordinates() ) );
                    layoutparams.setMargins( GraphicDrawer.minX( building.getCoordinates() ), GraphicDrawer.minY( building.getCoordinates() ), 0, 0 );
                    button.setLayoutParams( layoutparams );
                    button.setTag( building.getName() );
                    button.setText( building.getName() );
                    button.setEnabled( false );
                    button.setTextSize( ( float ) Math.sqrt( GraphicDrawer.scaleX( building.getCoordinates() ) ) / 2.0F );
                    button.getBackground().setAlpha( 64 );
                }

                Button button1;
                for ( Iterator iterator1 = storages.iterator() ; iterator1.hasNext() ; frame.addView( button1 ) ) {
                    Storage storage = ( Storage ) iterator1.next();
                    paint.setStrokeWidth( storage.getStrokeWidth().intValue() );
                    paint.setColor( Color.parseColor( storage.getStrokeColor() ) );
                    for ( int k = 0 ; k < storage.getCoordinates().size() - 2 ; k += 2 ) {
                        canvas.drawLine( ( ( Double ) storage.getCoordinates().get( k ) ).intValue(), ( ( Double ) storage.getCoordinates().get( k + 1 ) ).intValue(), ( ( Double ) storage.getCoordinates().get( k + 2 ) ).intValue(), ( ( Double ) storage.getCoordinates().get( k + 3 ) ).intValue(), paint );
                    }

                    canvas.drawLine( ( ( Double ) storage.getCoordinates().get( storage.getCoordinates().size() - 2 ) ).intValue(), ( ( Double ) storage.getCoordinates().get( storage.getCoordinates().size() - 1 ) ).intValue(), ( ( Double ) storage.getCoordinates().get( 0 ) ).intValue(), ( ( Double ) storage.getCoordinates().get( 1 ) ).intValue(), paint );
                    button1 = new Button( ScrollableImageActivity.getActivityContext() );
                    FrameLayout.LayoutParams layoutparams1 = new FrameLayout.LayoutParams( GraphicDrawer.scaleX( storage.getCoordinates() ), GraphicDrawer.scaleY( storage.getCoordinates() ) );
                    layoutparams1.setMargins( GraphicDrawer.minX( storage.getCoordinates() ), GraphicDrawer.minY( storage.getCoordinates() ), 0, 0 );
                    button1.setLayoutParams( layoutparams1 );
                    button1.setTag( storage.getId() );
                    button1.setText( storage.getId() );
                    button1.setTextSize( ( float ) Math.sqrt( GraphicDrawer.scaleX( storage.getCoordinates() ) ) / 2.0F );
                    final Button finalButton = button1;
                    button1.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View v ) {
                            Toast.makeText( ScrollableImageActivity.getActivityContext(), ( CharSequence ) finalButton.getTag(), Toast.LENGTH_SHORT ).show();
                        }
                    });
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
                return 0;
            }
        } );
    }

    private static int maxX(List list)
    {
        int j = ((Double)list.get(0)).intValue();
        for (int i = 0; i < list.size();)
        {
            int k = j;
            if (j < ((Double)list.get(i)).intValue())
            {
                k = ((Double)list.get(i)).intValue();
            }
            i += 2;
            j = k;
        }

        return j;
    }

    private static int maxY(List list)
    {
        int j = ((Double)list.get(1)).intValue();
        for (int i = 1; i < list.size();)
        {
            int k = j;
            if (j < ((Double)list.get(i)).intValue())
            {
                k = ((Double)list.get(i)).intValue();
            }
            i += 2;
            j = k;
        }

        return j;
    }

    private static int minX(List list)
    {
        int j = ((Double)list.get(0)).intValue();
        for (int i = 0; i < list.size();)
        {
            int k = j;
            if (j > ((Double)list.get(i)).intValue())
            {
                k = ((Double)list.get(i)).intValue();
            }
            i += 2;
            j = k;
        }

        return j;
    }

    private static int minY(List list)
    {
        int j = ((Double)list.get(1)).intValue();
        for (int i = 1; i < list.size();)
        {
            int k = j;
            if (j > ((Double)list.get(i)).intValue())
            {
                k = ((Double)list.get(i)).intValue();
            }
            i += 2;
            j = k;
        }

        return j;
    }

    private static int scaleX(List list)
    {
        int i = minX(list);
        return maxX(list) - i;
    }

    private static int scaleY(List list)
    {
        int i = minY(list);
        return maxY(list) - i;
    }




}
