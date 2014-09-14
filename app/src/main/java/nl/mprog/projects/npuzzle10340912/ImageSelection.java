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


public class ImageSelection extends ActionBarActivity {

    private int _firstItem = -1;
    private int _secondItem = -1;

    static final String MESSAGE_IMAGE_INDEX = "MESSAGE_IMAGE_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selection);

        GridView gridView = (GridView)findViewById( R.id.image_grid );
        final BaseAdapter imageAdapter = new ImageAdapter( this );

        gridView.setAdapter( imageAdapter );

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                switchToGamePlay(position);

            }
        });

    }

    private void switchToGamePlay( int imageIndex ) {

        Intent newActivity = new Intent( this, GamePlay.class );
        String selectedImageIndex = imageIndex + "";

        newActivity.putExtra( MESSAGE_IMAGE_INDEX, selectedImageIndex );

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
}
