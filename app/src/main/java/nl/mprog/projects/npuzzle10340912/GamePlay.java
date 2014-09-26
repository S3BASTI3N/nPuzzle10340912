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

import org.w3c.dom.Text;

import nl.mprog.projects.npuzzle10340912.PuzzleGame.GameField;
import nl.mprog.projects.npuzzle10340912.Utils.GameState;


public class GamePlay extends ActionBarActivity {

    private int _imageResourceId;
    private int _gameWidth = 4;

    private int _firstPosition = NO_POSITION;
    private int _nMoves = 0;
    private boolean _locked = true;
    GameField _gameField;

    GameState _state;

    private static final int NO_POSITION = -1;
    public static final String MESSAGE_MOVE_COUNT = "MESSAGE_MOVE_COUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.play_game);

        // Parse arguments
        Intent intent = getIntent();
        _imageResourceId = Integer.parseInt( intent.getStringExtra( ImageSelection.MESSAGE_IMAGE_RESOURCE_ID ));

        _state = new GameState( this );
        _gameWidth = intent.getIntExtra( "gameWidth", 4 );

        if( _imageResourceId == -1 ) {
            _imageResourceId = _state.getResourceId();
            _gameWidth = _state.getGameDifficulty();


        }

        // Initialise image grid
        final GridView gridView = (GridView)findViewById( R.id.play_field );
        _gameField = new GameField( this, _imageResourceId );

        // Get screen width
        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );
        _gameField.createTiles( _gameWidth, screenMetrics.widthPixels );

        gridView.setNumColumns(_gameWidth);
        gridView.setStretchMode( GridView.STRETCH_COLUMN_WIDTH );

        gridView.setAdapter(_gameField);

        updateMovesDisplay();




        // Add click listener for actions;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if( !_locked ) {
                    GameField gameField = (GameField) parent.getAdapter();
                    if( _firstPosition == position ) {
                        gameField.setSelected( position, false );
                        _firstPosition = NO_POSITION;
                        return;
                    }
                    if (_firstPosition == NO_POSITION) {
                        _firstPosition = position;

                        gameField.setSelected(position, true);

                        return;
                    }

                    gameField.setSelected(_firstPosition, false);
                    if (!gameField.switchTiles(_firstPosition, position)) {
                        Toast.makeText(parent.getContext(), "Invalid move", Toast.LENGTH_SHORT).show();
                    } else {
                        _nMoves++;
                        updateMovesDisplay();
                    }

                    _firstPosition = -1;

                    if( gameField.isGameFinished() ) {
                        switchToCongratulate();
                    }
                }

            }
        });

        Handler startGame = new Handler();

        startGame.postDelayed(new Runnable() {
            @Override
            public void run() {
                GridView grid = (GridView) findViewById(R.id.play_field);
                GameField gameField = (GameField) grid.getAdapter();

                if( _state.isRestored() ) {
                    _gameField.setTileOrder( _state.getTileOrder() );
                } else {
                    gameField.scrambleField();
                }

                TextView textView = (TextView) findViewById(R.id.play_game_preview);
                textView.setVisibility(View.INVISIBLE);

                _locked = false;
            }
        }, 3000);

    }

    public void switchToCongratulate() {

        Intent newActivity = new Intent( this, YouWin.class );

        newActivity.putExtra( ImageSelection.MESSAGE_IMAGE_RESOURCE_ID, _imageResourceId );
        newActivity.putExtra( MESSAGE_MOVE_COUNT, _nMoves );

        startActivity( newActivity );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.play_game, menu);
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
            finish();

            Intent intent = getIntent();
            intent.putExtra( "gameWidth", 3 );
            deleteDatabase( "nPuzzle10340912");
            startActivity(intent);
            return true;
        }

        // Switch gamemode to normal
        if( id == R.id.normal && _gameWidth != 4 ) {
            finish();

            Intent intent = getIntent();
            intent.putExtra( "gameWidth", 4 );
            deleteDatabase( "nPuzzle10340912");

            startActivity(intent);
            return true;
        }

        // Switch gamemode to hard
        if( id == R.id.hard && _gameWidth != 5 ) {
            finish();

            Intent intent = getIntent();
            intent.putExtra( "gameWidth", 5 );
            deleteDatabase( "nPuzzle10340912");

            startActivity(intent);
            return true;
        }

        // Restart game
        if( id == R.id.restart ) {
            finish();

            Intent intent = getIntent();
            intent.putExtra( "gameWidth", _gameWidth );

            startActivity(intent);
            return true;
        }

        // Quit game
        if( id == R.id.quit ) {
            Intent intent = new Intent( this, ImageSelection.class );
            deleteDatabase( "nPuzzle10340912" );

            finish();

            startActivity( intent );

        }

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovesDisplay() {

        TextView movesField = (TextView)findViewById( R.id.n_moves );

        movesField.setText("Moves: " + _nMoves);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // TODO: replace 0 with game difficulty
        GameState gameState = new GameState( this,  _imageResourceId, _nMoves, _gameField.getTileOrder(), _gameWidth );

        savedInstanceState.putParcelable( "GameState", gameState );

        Log.d( "msg", "Saved instance" );
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

            Log.d( "msg", "Restored game state" );


        } catch( Exception e ) {
            Log.d( "msg", "No saved game state" );
        }

    }
}
