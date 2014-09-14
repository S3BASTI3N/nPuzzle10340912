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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by youbot on 9/14/14.
 */
public class GameTileAdapter extends BaseAdapter {
    public Context _context;
    public int _imageIndex;

    private Bitmap _tiles[];

    public GameTileAdapter( Context context, int imageIndex ) {

        _context = context;
        _imageIndex = imageIndex;

    }

    public void createTiles( int gameSize ) {
        Bitmap fullImage = BitmapFactory.decodeResource( _context.getResources(), _images[_imageIndex] );

        int totalTiles = gameSize*gameSize;
        _tiles = new Bitmap[totalTiles];

        int tileWidth = fullImage.getWidth() / gameSize;
        int tileHeight = fullImage.getHeight() / gameSize;

        int count = 0;
        for( int y = 0; y < gameSize; y++ ) {
            for( int x = 0; x < gameSize; x++ ) {
                int startX = x * tileWidth;
                int startY = y * tileHeight;



                if( count == gameSize*gameSize -1 ) {
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

                    _tiles[count] = Bitmap.createBitmap( tileWidth, tileHeight, conf );
                    break;
                }

                _tiles[count] = Bitmap.createBitmap( fullImage, startX, startY, tileWidth, tileHeight, null, false );

                count++;

            }
        }


    }

    public boolean switchItems( int position1, int position2 ) {

        if( position1 > this.getCount() || position2 > this.getCount() || position1 == position2 )
            return false;

        Bitmap temp = _tiles[position1];
        _tiles[position1] = _tiles[position2];
        _tiles[position2] = temp;

        notifyDataSetChanged();

        return true;
    }


    @Override
    public int getCount() {
        //if( _tiles == null ) return 0;
        return _tiles.length;
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

        squareImageView.setImageBitmap( _tiles[position] );


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