/****************************************************
 *
 * Author: Sébastien Negrijn
 * uvaID:  10340912
 * email:  sebastiennegrijn@hotmail.com
 *
 ****************************************************/

package nl.mprog.projects.npuzzle10340912.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/*
 *
 *  Implemented according to:
 *  http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 *
 */

public class BitmapLoader {

    public static Bitmap loadScaledBitmapFromResource( Context context, int resourceId, int width ) {

        // Get dimensions of original image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource( context.getResources(), resourceId, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        // calculate the desired dimensions
        int outWidth = width;
        int outHeight =  (int)(imageHeight / ((double)(imageWidth) / (double)(width)));

        // calculate the sample size;
        options.inSampleSize = 1;

        int halfWidth = imageWidth / 2;
        int halfHeight = imageHeight / 2;

        if ( imageWidth > outWidth || imageHeight > outHeight ) {
            while ((halfHeight / options.inSampleSize) > outHeight
                    && (halfWidth / options.inSampleSize) > outWidth) {
                options.inSampleSize *= 2;
            }
        }

        options.inJustDecodeBounds = false;

        // Resize bitmap;
        Bitmap map = BitmapFactory.decodeResource( context.getResources(), resourceId, options );
        return Bitmap.createScaledBitmap( map, outWidth, outHeight, false );

    }
}
