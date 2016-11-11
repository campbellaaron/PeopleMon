package com.aaroncampbell.peoplemon.Components;

import android.graphics.Bitmap;

/**
 * Created by aaroncampbell on 11/11/16.
 */

public class Utils {
    public static Bitmap scale(Bitmap image) {
        image = Bitmap.createScaledBitmap(image, 100, 100, true);
        return image;
    }

    public void encodeImage() {

    }
}
