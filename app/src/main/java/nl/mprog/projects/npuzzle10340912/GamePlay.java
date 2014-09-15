package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import nl.mprog.projects.npuzzle10340912.PuzzleGame.GameField;


public class GamePlay extends ActionBarActivity {

    private int _imageResourceId;
    private int _gameWidth = 3;

    private int _firstPosition = NO_POSITION;
    private int _nMoves = 0;
    private boolean _locked = true;

    private static final int NO_POSITION = -1;
    public static final String MESSAGE_MOVE_COUNT = "MESSAGE_MOVE_COUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView( R.layout.play_game );

        // Parse arguments
        Intent intent = getIntent();
        _imageResourceId = Integer.parseInt( intent.getStringExtra( ImageSelection.MESSAGE_IMAGE_RESOURCE_ID ));

        // Initialise image grid
        final GridView gridView = (GridView)findViewById( R.id.play_field );
        GameField gameField = new GameField( this, _imageResourceId );

        // Get screen width
        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );
        gameField.createTiles( _gameWidth, screenMetrics.widthPixels );

        gridView.setNumColumns(_gameWidth);
        gridView.setStretchMode( GridView.STRETCH_COLUMN_WIDTH );

        gridView.setAdapter(gameField);

        updateMovesDisplay();




        // Add click listener for actions;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if( !_locked ) {
                    GameField gameField = (GameField) parent.getAdapter();
                    if( _firstPosition == position ) {
                        gameField.setSelected( position, false );
                        _firstPosition = NO_POSITION;
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
    }

    public void switchToCongratulate() {

        Intent newActivity = new Intent( this, YouWin.class );

        newActivity.putExtra( ImageSelection.MESSAGE_IMAGE_RESOURCE_ID, _imageResourceId );
        newActivity.putExtra( MESSAGE_MOVE_COUNT, _nMoves );

        startActivity( newActivity );

    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler startGame = new Handler();

        startGame.postDelayed( new Runnable() {
            @Override
            public void run() {
                GridView grid = (GridView)findViewById ( R.id.play_field );
                GameField gameField = (GameField)grid.getAdapter();

                gameField.scrambleField();
                _locked = false;
            }
        }, 3000  );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.image_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovesDisplay() {

        TextView movesField = (TextView)findViewById( R.id.n_moves );

        movesField.setText("Moves: " + _nMoves);

    }
}
