/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912.PuzzleGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.SquareImageView;

public class GameField extends BaseAdapter {
    public Context _context;
    public int _imageResourceId;

    private GameTile _tiles[];

    private static final int TILE_PADDING = 8;

    public GameField(Context context, int imageResourceId) {

        _context = context;
        _imageResourceId = imageResourceId;

    }

    public void scrambleField() {

        GameTile reverseTiles[] = new GameTile[_tiles.length];

        for( int i = 0; i < _tiles.length; i++ ) {
            int invIndex = reverseTiles.length -i -1;
            reverseTiles[ invIndex ] = _tiles[i];

            Point correctCoord = new Point();
            correctCoord.x = ( invIndex ) % (int)( Math.sqrt( _tiles.length ) );
            correctCoord.y =  invIndex / (int) Math.sqrt( _tiles.length );

            reverseTiles[ invIndex ]._coordinate = correctCoord;

        }

        _tiles = reverseTiles;

        // TODO If the amount of tiles is even, switch two tiles around
        if( (_tiles.length/2)*2 == _tiles.length ) {
            GameTile temp = _tiles[0];

        }

        notifyDataSetChanged();

    }

    public void createTiles( int gameSize, int desiredWidth ) {

        Bitmap fullImage = BitmapLoader.loadScaledBitmapFromResource(_context, _imageResourceId, desiredWidth);

        int totalTiles = gameSize*gameSize;
        _tiles = new GameTile[totalTiles];

        int tileWidth = fullImage.getWidth() / gameSize;
        int tileHeight = fullImage.getHeight() / gameSize;

        int count = 0;
        for( int y = 0; y < gameSize; y++ ) {
            int startY = y * tileHeight;
            for( int x = 0; x < gameSize; x++ ) {
                int startX = x * tileWidth;

                Bitmap newBitmap;

                if( count == gameSize*gameSize -1 ) {
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

                    newBitmap = Bitmap.createBitmap( tileWidth, tileHeight, conf );

                    _tiles[count] = new GameTile( newBitmap,x,y, count, true);
                    break;
                }

                newBitmap = Bitmap.createBitmap( fullImage, startX, startY, tileWidth, tileHeight, null, false );
                _tiles[count] = new GameTile( newBitmap, x, y, count, false );

                count++;
            }
        }
    }

    public void setSelected( int position, boolean selected ) {
        _tiles[position].setSelected( selected );
        notifyDataSetChanged();
    }

    public boolean isGameFinished() {
        for( int i = 0; i < _tiles.length; i++ ) {
            if( _tiles[i]._id != i ) return false;
        }

        return true;
    }

    public boolean switchTiles( int position1, int position2 ) {

        GameTile source = _tiles[ position1];
        GameTile destination = _tiles[ position2];

        // TODO remove print statement
        //System.out.println( source._coordinate.toString() + ",  " + destination._coordinate.toString() );

        if( !moveIsValid( source, destination) )
            return false;

        Point sourceCoordinate = source._coordinate;
        source._coordinate = destination._coordinate;
        destination._coordinate = sourceCoordinate;

        GameTile temp = _tiles[position1];
        _tiles[position1] = _tiles[position2];
        _tiles[position2] = temp;

        notifyDataSetChanged();

        return true;
    }

    public boolean moveIsValid( GameTile source, GameTile destination ) {

        if( source.equals( destination )) return false;
        if( !source.isAdjacent(destination)) return false;
        if( !destination._isEmpty ) return false;

        return true;
    }


    @Override
    public int getCount() {
        if( _tiles == null ) return 0;
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


        if (convertView == null) {
            squareImageView = new SquareImageView(_context);

            squareImageView.setLayoutParams( new GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT ));
            squareImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            squareImageView.setPadding( TILE_PADDING, TILE_PADDING, TILE_PADDING, TILE_PADDING );
        } else {

            squareImageView = (SquareImageView) convertView;

        }

        squareImageView.setBackgroundColor( _tiles[position]._color );
        squareImageView.setImageBitmap( _tiles[position]._bitmap );

        return squareImageView;
    }
}