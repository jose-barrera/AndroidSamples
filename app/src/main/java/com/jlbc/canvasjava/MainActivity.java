package com.jlbc.canvasjava;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Defines the size of the bitmap
        int widthInPixels = 800;
        int heightInPixels = 600;

        // Creates an ImageView instance to display the bitmap
        ImageView imageView = new ImageView(this);
        // Creates a Bitmap instance of specified size
        Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels, Bitmap.Config.ARGB_8888);
        // Creates a Canvas instance to use the drawing methods onto the bitmap
        Canvas canvas = new Canvas(bitmap);
        // Creates a Paint instance to use as paintbrush or pencil
        Paint paint = new Paint();

        // Paints blue the canvas background
        canvas.drawColor(Color.argb(255,0,0,255));
        // Sets a size and white color for the text and paints it
        paint.setTextSize(50f);
        paint.setColor(Color.argb(255,255,255,255));
        canvas.drawText("This is a cool example!!", 30f, 549f, paint);
        // Depending the orientation of the phone, display a different shape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            paint.setColor(Color.argb(255,212,207,62));
            canvas.drawCircle(400f, 250f, 100f, paint);
        } else {
            paint.setColor(Color.argb(255,255,0,0));
            canvas.drawRoundRect(350f, 200f, 500f, 400f, 10f, 10f, paint);
        }
        // Sets the bitmap into ImageView
        imageView.setImageBitmap(bitmap);
        // Sets the ImageView background to green
        imageView.setBackgroundColor(Color.GREEN);
        // Sets the ImageView as the view to display into activity
        setContentView(imageView);
    }
}

/*
 *  Reference: Horton (2018). Android Programming for Beginners
 */