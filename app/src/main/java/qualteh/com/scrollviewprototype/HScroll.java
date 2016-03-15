package qualteh.com.scrollviewprototype;

/**
 * Created by Virgil Tanase on 10.03.2016.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HScroll extends HorizontalScrollView {

    public HScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HScroll(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public int computeHorizontalScrollRange () {
        return super.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset () {
        return super.computeHorizontalScrollOffset();
    }

    @Override
    public void computeScroll () {
        super.computeScroll();
    }
}