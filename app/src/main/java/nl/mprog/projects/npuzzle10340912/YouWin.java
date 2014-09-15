package nl.mprog.projects.npuzzle10340912;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class YouWin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_win);

        // Parse arguments
        Intent intent = getIntent();
        int imageResourceId = Integer.parseInt( intent.getStringExtra( ImageSelection.MESSAGE_IMAGE_RESOURCE_ID ));

        // Set Image in final screen
        ImageView imageView = (ImageView)findViewById(R.id.final_image);
        imageView.setImageResource( imageResourceId );

        View finalScreen = (View)findViewById( R.id.you_win_activity );
        finalScreen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchToImageSelect();

            }
        });
    }

    public void switchToImageSelect() {

        Intent newActivity = new Intent( this, ImageSelection.class);
        startActivity( newActivity );

    }
}
