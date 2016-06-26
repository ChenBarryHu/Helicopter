package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hsctn on 2016-06-25.
 */
public class TopBat extends GameObject {
    Bitmap pictureForBat;
    long createdTime;
    int balancedPosition;
    int velocity_y = 160;
    int maxHeight = 0;
    int minHeight = 1040;
    boolean ifFirstCreated = true;




    public TopBat(Bitmap image, int pos_x) {
        super.pos_x = pos_x;
        super.pos_y = -35;
        this.widthInSpriteSheet = 200;
        this.heightInSpriteSheet = 75;
        this.balancedPosition = 100;
        this.pictureForBat = image;
    }

//    public void update() {
//        if(ifFirstCreated){
//            this.createdTime = System.nanoTime();
//            ifFirstCreated = false;
//        }
//        double passedTime = (System.nanoTime() - createdTime) / 1000000000f;
//        createdTime = System.nanoTime();
//        if ((passedTime * velocity_y + pos_y < maxHeight) || (passedTime * velocity_y + pos_y) > minHeight) {
//            velocity_y = -velocity_y;
//        }
//        pos_y = pos_y + (int)(passedTime * velocity_y);
//    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(pictureForBat, pos_x, pos_y, null);
        } catch (Exception e) {
        }
    }

    public void resetBat(){
        this.pos_y = balancedPosition;
        this.ifFirstCreated = true;
        this.velocity_y = 160;
    }
}
