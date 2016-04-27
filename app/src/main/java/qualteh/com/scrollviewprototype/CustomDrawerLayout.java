package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;

/**
 * Created by Virgil Tanase on 25.04.2016.
 */
public class CustomDrawerLayout extends DrawerLayout {
    public CustomDrawerLayout ( Context context, AttributeSet attrs ) {
        super( context, attrs );
    }

    @Override
    protected void onMeasure ( int widthMeasureSpec, int heightMeasureSpec ) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure( widthMeasureSpec, heightMeasureSpec );
    }
}
