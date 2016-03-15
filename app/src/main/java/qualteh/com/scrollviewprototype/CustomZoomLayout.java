package qualteh.com.scrollviewprototype;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Virgil Tanase on 11.03.2016.
 */
public class CustomZoomLayout extends FrameLayout {

    public CustomZoomLayout ( Context context ) {
        super( context );
    }

    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = getHeight() - getPaddingBottom()
                - getPaddingTop();

        return contentHeight;
    }


        @Override
        protected int computeVerticalScrollOffset() {
            int result = Math.max(0, super.computeVerticalScrollOffset());
            return result - getScrollVerticalMin();
        }
        @Override
        protected int computeHorizontalScrollRange() {
            final int count = getChildCount();
            final int contentWidth = getWidth() - getPaddingLeft()
                    - getPaddingRight();
            if (count == 0) {
                return contentWidth;
            }

            int scrollRange = getChildAt(0).getRight();
            final int scrollX = getScrollX();
            final int overscrollRight = Math.max(0, scrollRange - contentWidth);
            if (scrollX < 0) {
                scrollRange -= scrollX;
            } else if (scrollX > overscrollRight) {
                scrollRange += scrollX - overscrollRight;
            }
            return (int) (scrollRange * mScaleFactor + getScrollHorizontalMin());
        }

        @Override
        protected int computeHorizontalScrollOffset() {
            int result = Math.max(0, super.computeHorizontalScrollOffset());
            return result - getScrollHorizontalMin();
        }
        @Override
        protected int computeVerticalScrollExtent() {
            return getHeight() + getScrollVerticalMin();
        }

        @Override
        protected int computeHorizontalScrollExtent() {
            return getWidth() + getScrollHorizontalMin();
        }
    private int getScrollHorizontalMin() {
        if (this.getChildCount() < 1)
            return 0;
        View child = this.getChildAt(0);
        return (int) ((1 - mScaleFactor) * child.getPivotX());
    }

    private int getScrollVerticalMin() {
        if (this.getChildCount() < 1)
            return 0;
        View child = this.getChildAt(0);
        return (int) ((1 - mScaleFactor) * child.getPivotY());
    }
    private int getScrollVerticalRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = (int) Math.max(0, child.getHeight() * mScaleFactor
                    - (getHeight() - getPaddingBottom() - getPaddingTop())
                    + getScrollVerticalMin());
        }
        return scrollRange;
    }

    private int getScrollHorizontalRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = (int) Math.max(0, child.getWidth() * mScaleFactor
                    - (getWidth() - getPaddingRight() - getPaddingLeft())
                    + getScrollHorizontalMin());
        }
        return scrollRange;
    }


}
