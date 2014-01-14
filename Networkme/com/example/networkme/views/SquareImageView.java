package example.networkme.views;

// credits to: http://stackoverflow.com/questions/15261088/gridview-with-two-columns-and-auto-resized-images
/*
 * This view will make sure that the content is kept to a square
 * design meaning x and y coordinates will be the same dimension
 * very good for regularising display
 */
import android.content.Context;
import android.util.AttributeSet;

import com.loopj.android.image.SmartImageView;

public class SquareImageView extends SmartImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); 

    }
}