/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912.Utils;

import android.content.Context;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);

    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
    {
        super.onSizeChanged(w, w, oldw, oldh);
    }


}