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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.SquareImageView;

public class GameFieldAdapter extends BaseAdapter {
    public Context _context;
    public int _imageResourceId;

    private GameTile _tiles[];

    private static final int TILE_PADDING = 8;

    public GameFieldAdapter(Context context, int imageResourceId) {

        _context = context;
        _imageResourceId = imageResourceId;

    }

    public void scrambleField() {

        GameTile reverseTiles[] = new GameTile[_tiles.length];

        int gameWidth = (int)Math.sqrt( _tiles.length );

        // Scramble the filed and restore the coordinates
        for( int i = 0; i < _tiles.length; i++ ) {
            int invIndex = reverseTiles.length -i -1;
            reverseTiles[ invIndex ] = _tiles[i];

            Point correctCoord = new Point();
            correctCoord.x = ( invIndex ) % ( gameWidth );
            correctCoord.y =  invIndex / gameWidth;

            reverseTiles[ invIndex ]._coordinate = correctCoord;

        }

        _tiles = reverseTiles;

        // if the amount of tiles is even, witch two tiles around
        if( (_tiles.length/2)*2 == _tiles.length ) {
            switchTiles( 0, 1 );

        }

        notifyDataSetChanged();

    }

    public void createTiles( int gameSize, int desiredWidth ) {

        Bitmap fullImage = BitmapLoader.loadScaledBitmapFromResource(_context, _imageResourceId, desiredWidth);

        int totalTiles = gameSize*gameSize;
        _tiles = new GameTile[totalTiles];

        int tileWidth = fullImage.getWidth() / gameSize;
        int tileHeight = fullImage.getHeight() / gameSize;

        // Create all tiles by grabbing a piece of the full image and storing that piece in a GameTile
        int count = 0;
        for( int y = 0; y < gameSize; y++ ) {
            int startY = y * tileHeight;
            for( int x = 0; x < gameSize; x++ ) {
                int startX = x * tileWidth;

                Bitmap newBitmap;

                // Last tile should be empty
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

    // Check if the order of the tiles is correct by looping the ids
    public boolean isGameFinished() {
        for( int i = 0; i < _tiles.length; i++ ) {
            if( _tiles[i]._id != i ) return false;
        }

        return true;
    }

    public boolean switchTiles( int position1, int position2 ) {

        GameTile source = _tiles[ position1];
        GameTile destination = _tiles[ position2];

        if( !moveIsValid( source, destination) ) return false;

        Point sourceCoordinate = source._coordinate;
        source._coordinate = destination._coordinate;
        destination._coordinate = sourceCoordinate;

        GameTile temp = _tiles[position1];
        _tiles[position1] = _tiles[position2];
        _tiles[position2] = temp;

        notifyDataSetChanged();

        return true;
    }

    public int[] getTileOrder() {
        int order[] = new int[_tiles.length];

        int length = _tiles.length;
        for( int i = 0; i< length; i++ ) {
            order[i] = _tiles[i]._id;
        }

        return order;
    }

    public void setTileOrder( int tileOrder[] ) {

        Log.d( "GameField", "Setting new tile order" );

        GameTile newOrder[] = new GameTile[_tiles.length];

        for( int i = 0; i < newOrder.length; i++ ) {
            newOrder[i] = _tiles[tileOrder[i]];
        }


        _tiles = newOrder;

        int gameWidth = (int)Math.sqrt( (double)_tiles.length );
        int x = 0, y = 0;

        // Restore the coordinates of the tiles
        for (GameTile _tile : _tiles) {
            if (x == gameWidth) y++;
            if (x == gameWidth) x = 0;

            _tile._coordinate = new Point(x, y);

            x++;
        }
        notifyDataSetChanged();
    }

    public boolean moveIsValid( GameTile source, GameTile destination ) {

        // Return false when the two tiles are the same
        if( source.equals( destination )) return false;

        // Return false when the two tiles aren't adjacent to each other
        if( !source.isAdjacent(destination)) return false;

        // Return false when the destination tile isn't the empty tile
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

        SquareImageView squareImageView;

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