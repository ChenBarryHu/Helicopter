package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.shichen.android.helicopter.GamePanel;


/**
 * Created by hsctn on 2016-06-24.
 */

public class Fish extends GameObject {

    private Bitmap spritesheet;       // set of postures which are stored in Bitmap
    public static int score;


    private double gravityG = 5;
    private double liftingAccelerate = -10;
    private double realAcceleration = 0;
    private int width;
    private int height;
    // determine if we are pressing the screen
    private boolean ifScreenPressed;

    // determine if we are playing the game, the status of the player
    private boolean Ifcurrentlyplaying;
    // create a new animation object
    private boolean firstUpdate;
    private Animation animation;
    private long startTime;

    // player's constructor, pass in bitmap(which is a spritesheet),
    // and how many different image in that player
    // pos_x and pos_y represent the position of the player, in this, game, the x position
    // of the fish is fixed
    // the initial y position of the fish is the center of the screen(y axis)
    // heightInSpriteSheet and widthInSpriteSheet determines how much we should cut the bitmap of sprite sheet
    public Fish(Bitmap setOfPostures, int width, int height, int numOfPostures) {

        this.width = width;
        this.height = height;
        animation = new Animation();
        pos_x = 230;
        pos_y = GamePanel.HEIGHT / 2;

        Velocity_y = 0;
        score = 0;

        firstUpdate = true;     // used to calculate elapsed time keep reading the
                                // code and you will get it

        Bitmap[] postures = new Bitmap[numOfPostures];// numOfPostures means how many different
                                                      // images we are using to show the animation
        spritesheet = setOfPostures;

        for (int i = 0; i < postures.length; i++) {
                                    // createBitmap Returns an immutable bitmap from the specified subset of the
                                    // source bitmap, we cut the sprite sheet to seperate each individual image for animation
            postures[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setPostures(postures);         // give the image of animation to animation object
        animation.setRestTimeforFish(10);        // set the delay value of animation object to 10
    }




    public void setIfScreenPressed(boolean ifScreenPressed){ // set if the screen is being touched
        this.ifScreenPressed = ifScreenPressed;
    }

    public void update() {
        if (firstUpdate) {
            firstUpdate = false;
            startTime = System.nanoTime();
        }
        double elapsed = ((System.nanoTime() - startTime) / 10000000f) / 10; // attention: this is milisecond

        score++;
        startTime = System.nanoTime();    //update the startTime

        animation.update();

        if (ifScreenPressed) {
            realAcceleration = gravityG + liftingAccelerate;
            double dY = Velocity_y * elapsed + 0.5 * realAcceleration * elapsed * elapsed;
            pos_y = (int) (pos_y + dY);
            Velocity_y = (int) (Velocity_y + realAcceleration * elapsed);
        } else {
            realAcceleration = gravityG;
            double dY = Velocity_y * elapsed + 0.5 * realAcceleration * elapsed * elapsed;
            pos_y = (int) (pos_y + dY);
            Velocity_y = (int) (Velocity_y + realAcceleration * elapsed);
        }
                    // the folloeing code makes sure that the fish wouldn't get out of the screen
        if (pos_y < 0) {
            pos_y = 0;
            Velocity_y = 0;
        }
        if (pos_y > (GamePanel.HEIGHT - this.heightInSpriteSheet)) {
            pos_y = GamePanel.HEIGHT - this.heightInSpriteSheet;
            Velocity_y = 0;
        }
        Log.e("From fish:","The pos_y of the fish is "+pos_y);

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getCurrentPosture(), pos_x, pos_y, null);
        //Log.i("Draw fish", "pos_x is " + pos_x + " pos_y is " + pos_y);
    }

    public int getScore() {
        return score;
    }

    public boolean getIfcurrentlyplaying() {
        return Ifcurrentlyplaying;
    }

    public void setIfcurrentlyplaying(boolean ifcurrentlyplaying) {
        Ifcurrentlyplaying = ifcurrentlyplaying;
    }

    public void setFirstUpdate(boolean firstUpdate) {
        this.firstUpdate = firstUpdate;
    }

    public void resetScore() {
        score = 0;
    }

}
