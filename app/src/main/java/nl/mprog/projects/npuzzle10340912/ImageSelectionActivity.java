/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.GameState;
import nl.mprog.projects.npuzzle10340912.Utils.SquareImageView;


public class ImageSelectionActivity extends ActionBarActivity {


    static final String MESSAGE_IMAGE_RESOURCE_ID = "MESSAGE_IMAGE_RESOURCE_ID";

    private GameState _gameState;

    private int _width;

    private int[] _images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _images = getImages();

        Log.d( "ImageSelection", "Creating ImageSelection" );



        setContentView(R.layout.image_selection_layout);

        GridView gridView = (GridView)findViewById( R.id.image_grid );

        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );
        _width = screenMetrics.widthPixels;

        BaseAdapter imageAdapter = new ImageAdapter( this, _width );

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

    @Override
    public void onResume() {
        super.onResume();

        _gameState = new GameState( this );

        if( _gameState.isValid() ) {

            Log.d( "ImageSelection", "Setting up previous game" );

            restoreState(_width);

        } else {

            Log.d( "ImageSelection", "Not showing previous game" );
            TextView restoreGameText = (TextView)findViewById( R.id.restore_game_text );
            restoreGameText.setVisibility( TextView.GONE );

            ImageView restoreGameImage = (ImageView)findViewById( R.id.restore_game_image );
            restoreGameImage.setVisibility(ImageView.GONE);
            restoreGameImage.setImageDrawable( null );

        }
    }

    private void restoreState( int width ) {
        if( width == 0 ) width = 800;

        // Set text
        TextView restoreGameText = (TextView)findViewById( R.id.restore_game_text );
        restoreGameText.setText( "Continue previous game");
        restoreGameText.invalidate();
        restoreGameText.setVisibility( ImageView.VISIBLE );

        // Set scaled image
        ImageView restoreGameImage = (ImageView)findViewById( R.id.restore_game_image );
        restoreGameImage.setImageBitmap(
                BitmapLoader.loadScaledBitmapFromResource( this, _gameState.getResourceId(), width ));

        restoreGameImage.invalidate();
        restoreGameImage.setVisibility( ImageView.VISIBLE );
    }

    private void switchToGamePlay( int imageIndex ) {

        Intent newActivity = new Intent( this, GamePlayActivity.class );

        int resourceId = -1;
        if( imageIndex != -1 ) resourceId = _images[imageIndex];

        newActivity.putExtra( MESSAGE_IMAGE_RESOURCE_ID, resourceId );

        startActivity( newActivity );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            return _images.length;
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

            squareImageView.setImageBitmap( BitmapLoader.loadScaledBitmapFromResource(_context, _images[position], _screenWidth / 3));

            return squareImageView;
        }
    }

    public int[] getImages() {

        ArrayList<Integer> imageIdsList = new ArrayList<Integer>();

        int id = getResources().getIdentifier("npuzzle0", "drawable", getPackageName());
        for( int i = 1; id != 0; i++ ) {
            imageIdsList.add( id );
            id = getResources().getIdentifier("npuzzle"+i, "drawable", getPackageName());

        }

        int imageIdsArray[] = new int[imageIdsList.size()];

        for( int i = 0; i < imageIdsArray.length; i++ ) {
            imageIdsArray[i] = imageIdsList.get(i);
        }

        return imageIdsArray;

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Try to restore previous state of game
        try {
            _gameState = savedInstanceState.getParcelable( "GameState" );

            Log.d("ImageSelection", "Restored game state");

        } catch( Exception e ) {
            Log.d( "ImageSelection", "No saved game state" );
        }

    }

}
