/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by youbot on 9/13/14.
 */
public class ImageAdapter extends BaseAdapter {

    public Context _context;

    public ImageAdapter( Context context ) {

        _context = context;

    }

    public boolean switchItems( int position1, int position2 ) {

        if( position1 > this.getCount() || position2 > this.getCount() || position1 == position2 )
            return false;

        int temp = _images[position1];
        _images[position1] = _images[position2];
        _images[position2] = temp;

        notifyDataSetChanged();

        return true;
    }

    public void setSelected( int pos ) {
        SquareImageView squareImageView = new SquareImageView( _context );

        squareImageView.setSelected( true );
    }


    @Override
    public int getCount() {
        return _images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView squareImageView = new SquareImageView( _context );


        if (convertView == null) {  // if it's not recycled, initialize some attributes
            squareImageView = new SquareImageView(_context);

            squareImageView.setLayoutParams( new GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT ));
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setPadding(8, 8, 8, 8);
        } else {

            squareImageView = (SquareImageView) convertView;

        }

        squareImageView.setImageResource(_images[position]);

        return squareImageView;
    }

    private int _images[] = { R.drawable.npuzzle1, R.drawable.npuzzle2 };

    class SquareImageView extends ImageView {

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


}
