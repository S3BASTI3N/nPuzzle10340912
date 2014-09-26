/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912.Utils;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class GameState implements Parcelable {

    private boolean _restored = false;

    private int _resourceId;
    private int _moves;
    private int _tileOrder[];
    private int _gameDifficulty;
    private Context _context;

    DBConnect _dbConnect;

    public GameState( Context context ) {
        _dbConnect = new DBConnect( context );

        //context.deleteDatabase( "nPuzzle10340912" );

        if( _dbConnect.isRestored() ) {

            Log.d( "msg", "restored game state");

            _restored = true;

            _resourceId = _dbConnect.getResourceId();
            _tileOrder  = _dbConnect.getTileOrder();
            _moves = _dbConnect.getMoves();
            _gameDifficulty = _dbConnect.getGameDifficulty();

            Log.d( "msg", "stored image resource: " + _resourceId );

        }

    }

    public GameState( Context context, int resourceId, int moves, int tileOrder[], int gameDifficulty ) {

        _context = context;
        _resourceId = resourceId;
        _moves = moves;
        _tileOrder = tileOrder;
        _gameDifficulty = gameDifficulty;

        _dbConnect = new DBConnect( context );
        _dbConnect.updateState( this );

    }

    public int getResourceId() { return _resourceId; }

    public int getMoves() { return _moves; }

    public int[] getTileOrder() { return _tileOrder; }

    public int getGameDifficulty() { return _gameDifficulty; }

    public boolean isRestored() {
        return _restored;
    }

    private GameState( Parcel in ) {

        _resourceId = in.readInt();
        _moves = in.readInt();
        _tileOrder = in.createIntArray();
        _gameDifficulty = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {

        destination.writeInt( _resourceId );
        destination.writeInt( _moves );
        destination.writeIntArray( _tileOrder );
        destination.writeInt( _gameDifficulty );

    }

    public static final Creator<GameState> CREATOR = new Creator<GameState>() {
        @Override
        public GameState createFromParcel(Parcel source) {
            return new GameState( source );
        }

        @Override
        public GameState[] newArray(int size) {
            return new GameState[size];
        }
    };

    private class DBConnect extends SQLiteOpenHelper {

        private static final String DB_NAME = "nPuzzle10340912";

        private SQLiteDatabase _readableDatabase;
        private SQLiteDatabase _writableDatabase;

        private boolean _restored = true;

        // Game settings
        private final String DATABASE_CREATE_SETTINGS = "create table "


                + "game_settings( "
                + "id           INT     PRIMARY     KEY     NOT NULL, "
                + "difficulty   INT, "
                + "resource_id  INT, "
                + "moves        INT ); ";

        // Game state (tile settings)
        private final String DATABASE_CREATE_STATE = "create table "
                + "game_state( "
                + "id           INT     PRIMARY     KEY     NOT NULL, "
                + "position     INT );";

        private final String DATABASE_INIT_POP_SETTING = "insert into "
                + "game_settings ( id ) "
                + "values "
                + "( 0 ); ";

        private final String DATABASE_INIT_POP_STATE =

                "insert into game_state ( id ) values "
                + "(  0 ),"
                + "(  1 ),"
                + "(  2 ),"
                + "(  3 ),"
                + "(  4 ),"
                + "(  5 ),"
                + "(  6 ),"
                + "(  7 ),"
                + "(  8 ),"
                + "(  9 ),"
                + "( 10 ),"
                + "( 11 ),"
                + "( 12 ),"
                + "( 13 ),"
                + "( 14 ),"
                + "( 15 ),"
                + "( 16 ),"
                + "( 17 ),"
                + "( 18 ),"
                + "( 19 ),"
                + "( 20 ),"
                + "( 21 ),"
                + "( 22 ),"
                + "( 23 ),"
                + "( 24 )";


        public DBConnect( Context context ) {
            super( context, DB_NAME, null, 1 );

            _readableDatabase = getReadableDatabase();
            _writableDatabase = getWritableDatabase();

            //context.deleteDatabase( "nPuzzle10340912" );



            Cursor c = getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    Log.d( "msg", "Table name: " + c.getString(0));
                    c.moveToNext();
                }
            }

            checkForRestore();

        }

        public void checkForRestore() {
            Cursor cursor = _writableDatabase.rawQuery( "SELECT resource_id FROM game_settings", null );
            cursor.moveToFirst();

            if( cursor.getInt(0) == 0 ) _restored = false;

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL( DATABASE_CREATE_SETTINGS );
            db.execSQL(DATABASE_CREATE_STATE);

            db.execSQL(DATABASE_INIT_POP_SETTING);
            db.execSQL(DATABASE_INIT_POP_STATE);


            _restored = false;

            Log.d( "msg", "Created database" );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public boolean isRestored() {
            return _restored;
        }

        public void updateState( GameState state ) {

            _writableDatabase.execSQL("UPDATE game_settings SET difficulty = " + state.getGameDifficulty());
            _writableDatabase.execSQL("UPDATE game_settings SET resource_id = " + state.getResourceId());
            _writableDatabase.execSQL("UPDATE game_settings SET moves = " + state.getMoves());
            _writableDatabase.execSQL("UPDATE game_settings SET difficulty = " + state.getGameDifficulty());

            int tileOrder[] = state.getTileOrder();
            Log.d( "msg", "Storing " + tileOrder.length + " tiles" );

            for (int i = 0; i < tileOrder.length; i++) {
                _writableDatabase.execSQL("UPDATE game_state SET position = " + tileOrder[i] +
                        " where id = " + i);

                Log.d( "msg", "UPDATE game_state SET position = " + tileOrder[i] +
                        " where id = " + i);
            }

            Log.d("msg", "updated db");
        }


        public int getResourceId() {
            Cursor cursor = _readableDatabase.rawQuery("SELECT resource_id FROM game_settings", null);
            cursor.moveToFirst();
            return cursor.getInt( 0 );
        }

        public int getMoves() {
            Cursor cursor = _readableDatabase.rawQuery("SELECT moves FROM game_settings", null);
            cursor.moveToFirst();
            return cursor.getInt( 0 );
        }

        public int[] getTileOrder() {
            Cursor cursor = _readableDatabase.rawQuery("SELECT position FROM game_state ORDER BY id ASC", null);
            cursor.moveToFirst();

            int result[] = new int[cursor.getCount()];

            Log.d( "msg", "Getting order: " + cursor.toString() );


            for( int i = 0; i < result.length; i++ ) {
                result[i] = cursor.getInt( 0 );
                cursor.moveToNext();


            }

            return result;

        }

        public int getGameDifficulty() {
            Cursor cursor = _readableDatabase.rawQuery("SELECT difficulty FROM game_settings", null);
            cursor.moveToFirst();

            return cursor.getInt( 0 );
        }
    }
}
