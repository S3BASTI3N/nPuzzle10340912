package nl.mprog.projects.npuzzle10340912;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.LinkedList;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.GameState;
import nl.mprog.projects.npuzzle10340912.Utils.SquareImageView;


public class ImageSelection extends ActionBarActivity {


    static final String MESSAGE_IMAGE_RESOURCE_ID = "MESSAGE_IMAGE_RESOURCE_ID";

    private GameState _gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_selection);

        GridView gridView = (GridView)findViewById( R.id.image_grid );

        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );

        _gameState = new GameState( this );
        if( _gameState.isRestored() ) restoreState( screenMetrics.widthPixels );

        BaseAdapter imageAdapter = new ImageAdapter( this, screenMetrics.widthPixels );

        gridView.setAdapter( imageAdapter );

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                switchToGamePlay(position);

            }
        });

        ImageView previousGame = (ImageView)findViewById( R.id.restore_game_image);
        previousGame.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGamePlay(-1);
            }
        });
    }

    private void restoreState( int width ) {

        if( width == 0 ) width = 800;

        // Set text
        TextView restoreGameText = (TextView)findViewById( R.id.restore_game_text );
        restoreGameText.setText( "Continue previous game");

        // Set scaled image
        ImageView restoreGameImage = (ImageView)findViewById( R.id.restore_game_image );
        restoreGameImage.setImageBitmap(
                BitmapLoader.loadScaledBitmapFromResource( this, _gameState.getResourceId(), width ));



    }

    private void switchToGamePlay( int imageIndex ) {

        Intent newActivity = new Intent( this, GamePlay.class );

        // TODO: convert to int
        String selectedImageResourceId = "-1";
        if( imageIndex != -1 ) selectedImageResourceId = IMAGES()[imageIndex] + "";

        newActivity.putExtra( MESSAGE_IMAGE_RESOURCE_ID, selectedImageResourceId );

        startActivity( newActivity );

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


    public class ImageAdapter extends BaseAdapter {

        public Context _context;
        public int _screenWidth;

        public ImageAdapter( Context context, int screenWidth ) {

            _context = context;
            _screenWidth = screenWidth;

        }

        @Override
        public int getCount() {
            return ImageSelection.IMAGES().length;
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
                squareImageView.setPadding(8, 8, 8, 8);
            } else {

                squareImageView = (SquareImageView) convertView;

            }

            squareImageView.setImageBitmap( BitmapLoader.loadScaledBitmapFromResource(_context, ImageSelection.IMAGES()[position], _screenWidth / 3));

            return squareImageView;
        }
    }

    public static int[] IMAGES() {

        R.drawable drawableResources = new R.drawable();
        Class<R.drawable> c = R.drawable.class;
        Field[] fields = c.getDeclaredFields();

        LinkedList<Integer> items = new LinkedList<Integer>();

        for( int i = 0; i < fields.length; i++ ) {

            try {
                int resourceId = fields[i].getInt( drawableResources );
                String itemName = fields[i].getName();
                if( itemName.startsWith( "npuzzle" )) {
                    items.add( resourceId );
                }

            } catch( Exception e ) {

            }
        }

        int[] itemArray = new int[items.size()];

        for( int i = 0; i < items.size();i++ ) {
            itemArray[i] = items.get(i);
        }

        return itemArray;

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Try to restore previous state of game
        try {
            _gameState = savedInstanceState.getParcelable( "GameState" );

            Log.d("msg", "Restored game state");

        } catch( Exception e ) {
            Log.d( "msg", "No saved game state" );
        }

    }

}
