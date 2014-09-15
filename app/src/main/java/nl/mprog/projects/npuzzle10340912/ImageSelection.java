package nl.mprog.projects.npuzzle10340912;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.SquareImageView;


public class ImageSelection extends ActionBarActivity {

    public static int IMAGES[] = { R.drawable.npuzzle1, R.drawable.npuzzle2 };
    static final String MESSAGE_IMAGE_RESOURCE_ID = "MESSAGE_IMAGE_RESOURCE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image_selection);

        GridView gridView = (GridView)findViewById( R.id.image_grid );

        DisplayMetrics screenMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( screenMetrics );

        BaseAdapter imageAdapter = new ImageAdapter( this, screenMetrics.widthPixels );

        gridView.setAdapter( imageAdapter );

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                switchToGamePlay(position);

            }
        });

    }

    private void switchToGamePlay( int imageIndex ) {

        Intent newActivity = new Intent( this, GamePlay.class );
        String selectedImageResourceId = IMAGES[imageIndex] + "";

        newActivity.putExtra( MESSAGE_IMAGE_RESOURCE_ID, selectedImageResourceId );

        startActivity( newActivity );

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


    public class ImageAdapter extends BaseAdapter {

        public Context _context;
        public int _screenWidth;

        public ImageAdapter( Context context, int screenWidth ) {

            _context = context;
            _screenWidth = screenWidth;

        }

        @Override
        public int getCount() {
            return ImageSelection.IMAGES.length;
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

            squareImageView.setImageBitmap( BitmapLoader.loadScaledBitmapFromResource(_context, ImageSelection.IMAGES[position], _screenWidth / 3));

            return squareImageView;
        }
    }



}
