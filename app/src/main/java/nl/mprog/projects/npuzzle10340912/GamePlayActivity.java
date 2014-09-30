/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import nl.mprog.projects.npuzzle10340912.PuzzleGame.GameFieldAdapter;
import nl.mprog.projects.npuzzle10340912.Utils.GameState;


public class GamePlayActivity extends ActionBarActivity {

    private int _imageResourceId;
    private int _gameWidth = 4;

    private int _firstPosition = NO_POSITION;
    private int _nMoves = 0;
    private boolean _locked = true;
    private GameFieldAdapter _gameFieldAdapter;
    private boolean _restart = false;

    private GameState _state;
    private Handler _startGame;
    private Runnable _startGameRunnable;

    private static final int NO_POSITION = -1;
    public static final String MESSAGE_MOVE_COUNT = "MESSAGE_MOVE_COUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.play_game_layout);

        // Parse arguments
        Intent intent = getIntent();
        _imageResourceId = intent.getIntExtra( ImageSelectionActivity.MESSAGE_IMAGE_RESOURCE_ID, -1 );


        _gameWidth = intent.getIntExtra( "gameWidth", 4 );

        _state = new GameState( this );

        if( _imageResourceId == -1 ) {
            Log.d( "GamePlay", "Setting imageResource and gamewith from db" );
            _imageResourceId = _state.getResourceId();
            _gameWidth = _state.getGameDifficulty();


        }

        Log.d( "GamePlay", "Game width is set to: " + _gameWidth );
        Log.d( "GamePlay", "Game width in db is: " + _state.getGameDifficulty());

        // Initialise image grid
        final GridView gridView = (GridView)findViewById( R.id.play_field );
        _gameFieldAdapter = new GameFieldAdapter( this, _imageResourceId );

        // Get screen width
        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );
        _gameFieldAdapter.createTiles( _gameWidth, screenMetrics.widthPixels );

        gridView.setNumColumns(_gameWidth);
        gridView.setStretchMode( GridView.STRETCH_COLUMN_WIDTH );

        gridView.setAdapter(_gameFieldAdapter);

        updateMovesDisplay();


        // Add click listener for actions;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if( !_locked ) {
                    GameFieldAdapter gameFieldAdapter = (GameFieldAdapter) parent.getAdapter();
                    if( _firstPosition == position ) {
                        gameFieldAdapter.setSelected( position, false );
                        _firstPosition = NO_POSITION;
                        return;
                    }
                    if (_firstPosition == NO_POSITION) {
                        _firstPosition = position;

                        gameFieldAdapter.setSelected(position, true);

                        return;
                    }

                    gameFieldAdapter.setSelected(_firstPosition, false);
                    if (!gameFieldAdapter.switchTiles(_firstPosition, position)) {
                        Toast.makeText(parent.getContext(), "Invalid move", Toast.LENGTH_SHORT).show();
                    } else {
                        _nMoves++;
                        updateMovesDisplay();
                    }

                    _firstPosition = -1;

                    if( gameFieldAdapter.isGameFinished() ) {
                        switchToCongratulate();
                    }
                }

            }
        });

        _startGame = new Handler();
        _startGameRunnable = new Runnable() {
            @Override
            public void run() {
                initialiseField();
            }
        };

        _startGame.postDelayed( _startGameRunnable, 3000);

    }

    public void initialiseField() {
        GridView grid = (GridView) findViewById(R.id.play_field);
        GameFieldAdapter gameFieldAdapter = (GameFieldAdapter) grid.getAdapter();

        if( _state.isValid() ) {
            Log.d( "GamePlay", "Restoring tile order" );
            _gameFieldAdapter.setTileOrder( _state.getTileOrder() );
        } else {
            Log.d( "GamePlay", "Scrambling the field" );
            gameFieldAdapter.scrambleField();
        }

        TextView textView = (TextView) findViewById(R.id.play_game_preview);
        textView.setVisibility(View.INVISIBLE);

        _locked = false;
    }

    public void switchToCongratulate() {

        finish();

        Intent newActivity = new Intent( this, YouWinActivity.class );

        newActivity.putExtra( ImageSelectionActivity.MESSAGE_IMAGE_RESOURCE_ID, _imageResourceId );
        newActivity.putExtra( MESSAGE_MOVE_COUNT, _nMoves );

        startActivity( newActivity );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_game_menu, menu);

        int menuItemId = 0;
        if( _gameWidth == 3 ) menuItemId = R.id.easy;
        if( _gameWidth == 4 ) menuItemId = R.id.normal;
        if( _gameWidth == 5 ) menuItemId = R.id.hard;

        if( menuItemId != 0 ) {
            MenuItem menuItem = menu.findItem( menuItemId );

            menuItem.setChecked( true );

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Switch gamemode to easy
        if( id == R.id.easy && _gameWidth != 3 ) {

            changeDifficulty( 3 );

            return true;
        }

        // Switch gamemode to normal
        if( id == R.id.normal && _gameWidth != 4 ) {

            changeDifficulty( 4 );

            return true;
        }

        // Switch gamemode to hard
        if( id == R.id.hard && _gameWidth != 5 ) {

            changeDifficulty( 5 );

            return true;
        }

        // Restart game
        if( id == R.id.restart ) {
            _restart = true;
            finish();

            _state.invalidate();

            Intent intent = getIntent();
            intent.putExtra( ImageSelectionActivity.MESSAGE_IMAGE_RESOURCE_ID, _imageResourceId );
            intent.putExtra( "gameWidth", _gameWidth );

            startActivity(intent);
            return true;
        }

        // Quit game
        if( id == R.id.quit ) {

            _state.invalidate();
            _imageResourceId = 0;
            _nMoves = 0;

            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    public void changeDifficulty( int difficulty ) {

        _restart = true;
        finish();

        Intent intent = getIntent();
        intent.putExtra( ImageSelectionActivity.MESSAGE_IMAGE_RESOURCE_ID, _imageResourceId );
        intent.putExtra( "gameWidth", difficulty );
        _state.invalidate();

        startActivity(intent);

    }

    public void updateMovesDisplay() {

        TextView movesField = (TextView)findViewById( R.id.n_moves );

        movesField.setText("Moves: " + _nMoves);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if( _locked ) {
            _startGame.removeCallbacks(_startGameRunnable);
            initialiseField();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        finish();

        if( ! _restart ) {
            _state = new GameState(this, _imageResourceId, _nMoves, _gameFieldAdapter.getTileOrder(), _gameWidth);
        }

        Log.d( "GamePlay", "Pausing this activity" );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        GameState gameState = new GameState( this,  _imageResourceId, _nMoves, _gameFieldAdapter.getTileOrder(), _gameWidth );

        savedInstanceState.putParcelable( "GameState", gameState );

        Log.d( "GamePlay", "Saved instance" );
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Try to restore previous state of game
        try {
            GameState gameState;
            gameState = savedInstanceState.getParcelable( "GameState" );

            this._nMoves = gameState.getMoves();
            this._imageResourceId = gameState.getResourceId();

            Log.d( "GamePlay", "Restored game state" );


        } catch( Exception e ) {
            Log.d( "GamePlay", "No saved game state" );
        }

    }
}
