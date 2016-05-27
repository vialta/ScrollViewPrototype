// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package qualteh.com.scrollviewprototype;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.List;
import qualteh.com.scrollviewprototype.Model.Building;
import qualteh.com.scrollviewprototype.Model.Commission;
import qualteh.com.scrollviewprototype.Model.MapModel;
import qualteh.com.scrollviewprototype.Model.Storage;
import qualteh.com.scrollviewprototype.Dialogs.StorageDialog;

// Referenced classes of package qualteh.com.scrollviewprototype:
//            DemoMachine, ScrollableImageActivity

class GraphicDrawer {

    public static void drawMap( ImageView imageView, final FrameLayout frameLayout, final Paint paint, final MapModel mapModel, DemoMachine demoMachine, final Fragment fragment) {

        paint.setStrokeJoin( Paint.Join.ROUND);
        imageView.setBackgroundColor( 0xff444444 );
        Object obj = mapModel.getMainCoordinates().getCoordinates();
        demoMachine = new DemoMachine();


        final FrameLayout frame = frameLayout;
        final MapModel mMapData = mapModel;
        final List<Building> buildings = mapModel.getBuildings();

        final List<Double> points = mapModel.getMainCoordinates().getCoordinates();
        final List<Storage> storages = mapModel.getStorage();
        final List<Commission> commissions = mapModel.getCommissions();

        String s="";
        for(int i=0;i<points.size();i++){
            s+=points.get( i ).toString();
        }

        imageView.setImageDrawable( new Drawable() {
            @Override
            public void draw ( Canvas canvas ) {
                paint.setStrokeWidth( 2 );
                paint.setColor( Color.parseColor( mMapData.getMainCoordinates().getStrokeColor() ) );
                for ( int i = 0 ; i < points.size() - 2 ; i += 2 ) {
                    canvas.drawLine(   Math.round(points.get( i )) ,  Math.round( points.get( i + 1 )), Math.round( points.get( i + 2 )), Math.round( points.get( i + 3 )), paint );
                }

                canvas.drawLine( Math.round(points.get( points.size() - 2 ) ), Math.round(points.get( points.size() - 1 ) ), Math.round(points.get( 0 ) ), Math.round(points.get( 1 ) ), paint );
                Button button;
                for ( Iterator iterator = buildings.iterator() ; iterator.hasNext() ; frame.addView( button ) ) {
                    Building building = ( Building ) iterator.next();
                    paint.setStrokeWidth( building.getStrokeWidth().intValue() );
                    paint.setColor( Color.parseColor( building.getStrokeColor() ) );
                    for ( int j = 0 ; j < building.getCoordinates().size() - 2 ; j += 2 ) {
                        canvas.drawLine( ( building.getCoordinates().get( j ) ).intValue(), ( building.getCoordinates().get( j + 1 ) ).intValue(), ( building.getCoordinates().get( j + 2 ) ).intValue(), ( building.getCoordinates().get( j + 3 ) ).intValue(), paint );
                    }

                    canvas.drawLine( ( building.getCoordinates().get( building.getCoordinates().size() - 2 ) ).intValue(), ( building.getCoordinates().get( building.getCoordinates().size() - 1 ) ).intValue(), ( building.getCoordinates().get( 0 ) ).intValue(), ( building.getCoordinates().get( 1 ) ).intValue(), paint );
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
                for ( Iterator iterator = storages.iterator() ; iterator.hasNext() ; frame.addView( button1 ) ) {
                    final Storage storage = ( Storage ) iterator.next();
                    int fillValue = storage.getStock();
                    paint.setStrokeWidth( storage.getStrokeWidth() );
                    int color,r,g,b;
                    if(storage.getStock() >  storage.getCapacity()){
                        paint.setColor ( Color.YELLOW );
                        color = Color.YELLOW;
                    }
                    else{
                        color = Integer.parseInt( String.valueOf( Color.parseColor( storage.getStrokeColor() ) ) );
                        r = (color>>16) & 0xFF;
                        g = (color>>8) & 0xFF;
                        b = (color>>0) & 0xFF;
                        paint.setColor( Color.rgb( r,g,b ) );

                    }

                    for ( int k = 0 ; k < storage.getCoordinates().size() - 2 ; k += 2 ) {
                        canvas.drawLine( (  storage.getCoordinates().get( k ) ).intValue(), (  storage.getCoordinates().get( k + 1 ) ).intValue(), (  storage.getCoordinates().get( k + 2 ) ).intValue(), (  storage.getCoordinates().get( k + 3 ) ).intValue(), paint );
                    }
                    canvas.drawLine( (  storage.getCoordinates().get( storage.getCoordinates().size() - 2 ) ).intValue(), (  storage.getCoordinates().get( storage.getCoordinates().size() - 1 ) ).intValue(), (  storage.getCoordinates().get( 0 ) ).intValue(), (  storage.getCoordinates().get( 1 ) ).intValue(), paint );

                    Double minWidthStorage  = storage.getCoordinates().get( 0 );
                    Double maxWidthStorage  = storage.getCoordinates().get( 0 );
                    Double minHeightStorage = storage.getCoordinates().get( 1 );
                    Double maxHeightStorage = storage.getCoordinates().get( 1 );

                    for(int k=0; k<storage.getCoordinates().size(); k+=2 ){
                        if(minWidthStorage>storage.getCoordinates().get( k )){
                            minWidthStorage = storage.getCoordinates().get( k );
                        }
                        if(maxWidthStorage<storage.getCoordinates().get( k )){
                            maxWidthStorage = storage.getCoordinates().get(k);
                        }
                        if(minHeightStorage>storage.getCoordinates().get( k+1 )){
                            minHeightStorage = storage.getCoordinates().get( k+1 );
                        }
                        if(maxHeightStorage<storage.getCoordinates().get( k+1 )){
                            maxHeightStorage = storage.getCoordinates().get( k+1 );
                        }
                    }

                    double topHeightStorage = maxHeightStorage - ((maxHeightStorage - minHeightStorage)/100 * fillValue);

                    r = (color>>16) & 0xFF;
                    g = (color>>8) & 0xFF;
                    b = (color>>0) & 0xFF;

                    double redFactor   = storage.getStock() * (250.0/storage.getCapacity());
                    double greenFactor = storage.getStock() * (170.0/storage.getCapacity());
                    double blueFactor  = storage.getStock() * (175.0/storage.getCapacity());

                    r -= redFactor;
                    g -= greenFactor;
                    b += blueFactor;

                    paint.setColor( Color.rgb( r,g,b ) );

                    canvas.drawRect( Math.round( minWidthStorage ) *1.0f-1, Math.round( topHeightStorage ) *1.0f-1, Math.round( maxWidthStorage ) *1.0f-1, Math.round( maxHeightStorage ) *1.0f-1, paint );

                    button1 = new Button( ScrollableImageActivity.getActivityContext() );
                    FrameLayout.LayoutParams layoutparams1 = new FrameLayout.LayoutParams( GraphicDrawer.scaleX( storage.getCoordinates() ), GraphicDrawer.scaleY( storage.getCoordinates() ) );
                    layoutparams1.setMargins( GraphicDrawer.minX( storage.getCoordinates() ), GraphicDrawer.minY( storage.getCoordinates() ), 0, 0 );
                    button1.setLayoutParams( layoutparams1 );
                    button1.setTag( storage.getId() );
                    button1.setText( storage.getId() );
                    button1.setBackgroundColor( Color.TRANSPARENT );
                    button1.setTextSize( ( float ) Math.sqrt( GraphicDrawer.scaleX( storage.getCoordinates() ) ) / 2.0F );
                    button1.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick ( View v ) {
                            FragmentManager fragmentManager = fragment.getFragmentManager();
                            StorageDialog storageDialog = new StorageDialog();
                            storageDialog.setStockString( String.valueOf( storage.getStock() ) );
                            storageDialog.setCapacityString( String.valueOf( storage.getCapacity() ) );
                            Log.d("Test",storage.getStock()+" "+storage.getCapacity());
                            storageDialog.show(fragmentManager, "Storage "+storage.getId());
                        }
                    });
                }

                for ( Iterator iterator =  commissions.iterator() ; iterator.hasNext() ;  ) {
                    Commission commission = ( Commission ) iterator.next();

                    paint.setStrokeWidth( commission.getStrokeWidth() );
                    paint.setColor( Color.parseColor( commission.getStrokeColor() ) );
                    for ( int k = 0 ; k < commission.getCoordinates().size() - 2 ; k += 2 ) {
                        canvas.drawLine( ( commission.getCoordinates().get( k ) ).intValue(), ( commission.getCoordinates().get( k + 1 ) ).intValue(), ( commission.getCoordinates().get( k + 2 ) ).intValue(), ( commission.getCoordinates().get( k + 3 ) ).intValue(), paint );
                    }
                    canvas.drawLine( ( commission.getCoordinates().get( commission.getCoordinates().size() - 2 ) ).intValue(), ( commission.getCoordinates().get( commission.getCoordinates().size() - 1 ) ).intValue(), ( commission.getCoordinates().get( 0 ) ).intValue(), ( commission.getCoordinates().get( 1 ) ).intValue(), paint );
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

    private static int maxX(List list) {
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

    private static int maxY(List list) {
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

    private static int minX(List list) {int j = ((Double)list.get(0)).intValue();
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

    private static int minY(List list) {
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

    private static int scaleX(List list) {
        int i = minX(list);
        return maxX(list) - i;
    }

    private static int scaleY(List list) {
        int i = minY(list);
        return maxY(list) - i;
    }

}