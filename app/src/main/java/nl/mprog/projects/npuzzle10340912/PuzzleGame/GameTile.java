/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912.PuzzleGame;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

public class GameTile {

    public Bitmap _bitmap;
    public Point _coordinate;
    public int _id;
    public boolean _isEmpty;
    public int _color = COLOR_UNSELECTED;

    public static final int COLOR_SELECTED = Color.rgb( 164, 198, 57 );
    public static final int COLOR_UNSELECTED = Color.BLACK;


    public GameTile( Bitmap bitmap, int x, int y, int id, boolean empty ) {
        _bitmap = bitmap;
        _coordinate = new Point( x, y );
        _id = id;

        _isEmpty = empty;

    }

    public boolean equals( GameTile tile ) {

        return _coordinate.x == tile._coordinate.x && _coordinate.y == tile._coordinate.y;

    }

    public boolean isAdjacent( GameTile tile ) {

        if((_coordinate.x == tile._coordinate.x &&
                Math.abs( _coordinate.y - tile._coordinate.y ) <= 1 )) return true;

        if((_coordinate.y == tile._coordinate.y &&
                Math.abs( _coordinate.x - tile._coordinate.x ) <= 1 )) return true;

        return false;

    }

    public void setSelected( boolean selected ) {
        if( selected ) _color = COLOR_SELECTED;
        else _color = COLOR_UNSELECTED;
    }


}
