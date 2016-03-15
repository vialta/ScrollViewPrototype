package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Virgil Tanase on 10.03.2016.
 */





public class VScroll extends ScrollView {

    public VScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VScroll(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public int computeVerticalScrollRange () {
        return super.computeVerticalScrollRange();
    }

    @Override
    public void computeScroll () {
        super.computeScroll();
    }

    @Override
    public int computeVerticalScrollOffset () {
        return super.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent () {
        return super.computeVerticalScrollExtent();
    }
}

