package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;


public class GamePlay extends ActionBarActivity {

    private int _imageIndex;
    private int _gameWidth = 3;

    private int _firstPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.play_game );

        // Parse arguments
        Intent intent = getIntent();
        _imageIndex = Integer.parseInt( intent.getStringExtra( ImageSelection.MESSAGE_IMAGE_INDEX ));


        GridView gridView = (GridView)findViewById( R.id.play_field );
        GameTileAdapter gameTileAdapter = new GameTileAdapter( this, _imageIndex );

        gameTileAdapter.createTiles( _gameWidth );

        gridView.setNumColumns(_gameWidth);
        gridView.setStretchMode( GridView.STRETCH_COLUMN_WIDTH );

        gridView.setAdapter( gameTileAdapter );

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (_firstPosition == -1) {
                    _firstPosition = position;

                    return;
                }

                GameTileAdapter gameTileAdapter = (GameTileAdapter) parent.getAdapter();
                if (!gameTileAdapter.switchItems(_firstPosition, position)) {
                    Toast.makeText(parent.getContext(), "Invalid move", Toast.LENGTH_SHORT).show();
                }

                _firstPosition = -1;

            }
        });







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
}
