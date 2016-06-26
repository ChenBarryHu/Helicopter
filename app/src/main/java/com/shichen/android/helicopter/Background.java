package com.shichen.android.helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hsctn on 2016-06-23.
 */
                        // this class is used to store the bitmap
                        // resource of the background picture
public class Background {
    private Bitmap image;

                            // x and y determine the position of the left-top corner of the picture
                            // dx determine how much distance shift leftward every refreshment
    private int x, y, dx;

                            // constructor: get the resource (bitmap)
                            // of the background picture
    public Background(Bitmap res){
        image = res;
        x=0;
        y=0;
        dx = GamePanel.MOVESPEED;
    }

                           // update the position of the background picture
    public void update(){
        x += dx;
        if(x<-GamePanel.WIDTH){
            x=0;
        }
    }

                            // this method will be called in Game Enging loop
    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        if(x<0){
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y,null);
        }

    }
}
