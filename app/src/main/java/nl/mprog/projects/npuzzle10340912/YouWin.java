package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;


public class YouWin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_win);

        // Parse arguments
        Intent intent = getIntent();
        int imageResourceId = intent.getIntExtra(ImageSelection.MESSAGE_IMAGE_RESOURCE_ID, -1);
        int nMoves = intent.getIntExtra( GamePlay.MESSAGE_MOVE_COUNT, 0 );

        // Set Image in final screen
        ImageView imageView = (ImageView)findViewById( R.id.final_image );

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics( metrics );

        Bitmap finalBitmap = BitmapLoader.loadScaledBitmapFromResource( this, imageResourceId, metrics.widthPixels );

        imageView.setImageBitmap( finalBitmap );

        // Display amount of moves
        TextView textView = (TextView)findViewById( R.id.final_moves );
        textView.setText( "You finished in " + nMoves + " moves" );

        // Add click listener to return to main screen
        View finalScreen = (View)findViewById( R.id.you_win_activity );
        finalScreen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchToImageSelect();

            }
        });

        deleteDatabase( "nPuzzle10340912" );
    }

    public void switchToImageSelect() {

        Intent newActivity = new Intent( this, ImageSelection.class);
        startActivity( newActivity );

    }
}
