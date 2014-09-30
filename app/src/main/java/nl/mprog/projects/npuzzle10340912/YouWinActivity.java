/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import nl.mprog.projects.npuzzle10340912.Utils.BitmapLoader;
import nl.mprog.projects.npuzzle10340912.Utils.GameState;


public class YouWinActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_win_layout);

        // Delete previous game
        GameState state = new GameState( this );
        state.invalidate();

        // Parse arguments
        Intent intent = getIntent();
        int imageResourceId = intent.getIntExtra(ImageSelectionActivity.MESSAGE_IMAGE_RESOURCE_ID, -1);
        int nMoves = intent.getIntExtra( GamePlayActivity.MESSAGE_MOVE_COUNT, 0 );

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
        View finalScreen = findViewById( R.id.you_win_activity );
        finalScreen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchToImageSelect();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void switchToImageSelect() {

        finish();

    }
}
