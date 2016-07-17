package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shichen.android.helicopter.GamePanel;


/**
 * Created by hsctn on 2016-06-24.
 */

public class Fish extends GameObject {

    private Bitmap spritesheet;       // set of postures which are stored in Bitmap
    private Bitmap unstoppableSpriteSheet;
    private int numOfPostures;
    public static int score;



    private double gravityG = 6;
    private double liftingAccelerate = -10;
    private double realAcceleration = 0;
    private int width;
    private int height;
    private int unStoppableWidth;
    private int unStoppableHeight;
    // determine if we are pressing the screen
    private boolean ifScreenPressed;

    // determine if we are playing the game, the status of the player
    private boolean Ifcurrentlyplaying = false;
    // create a new animation object
    private boolean firstUpdate;
    private Animation animation;
    private Animation unstoppableAnimation;
    private long startTime;
    private float timeForScore;

    // Fish's constructor, pass in bitmap(which is a spritesheet),
    // and how many different image in that bitmap
    // pos_x and pos_y represent the position of the player, in this game, the x position
    // of the fish is fixed
    // the initial y position of the fish is the center of the screen(y axis)
    // heightInSpriteSheet and widthInSpriteSheet determines how much we should cut the bitmap of sprite sheet
    public Fish(Bitmap setOfPostures, Bitmap unstoppableSpriteSheet) {

        this.width = 64;
        this.height = 64;
        this.numOfPostures = 6;
        this.unStoppableHeight=512;
        this.unStoppableWidth=512;
        this.animation = new Animation();
        this.unstoppableAnimation = new Animation();
        pos_x = 230;
        pos_y = GamePanel.HEIGHT / 2;

        Velocity_y = 0;
        this.score = 0;
        timeForScore = 0;

        firstUpdate = true;     // used to calculate elapsed time keep reading the
        // code and you will get it

        Bitmap[] postures = new Bitmap[numOfPostures];// numOfPostures means how many different
        // images we are using to show the animation
        Bitmap[] unstoppablePostures = new Bitmap[numOfPostures];
        this.spritesheet = setOfPostures;
        this.unstoppableSpriteSheet = unstoppableSpriteSheet;

        for (int i = 0; i < postures.length; i++) {
            // createBitmap Returns an immutable bitmap from the specified subset of the
            // source bitmap, we cut the sprite sheet to seperate each individual image for animation
            postures[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
            unstoppablePostures[i] = Bitmap.createBitmap(unstoppableSpriteSheet, i * unStoppableWidth, 0, unStoppableWidth, unStoppableHeight);
        }

        animation.setPostures(postures);         // give the image of animation to animation object
        animation.setRestTimeforFish(10);        // set the delay value of animation object to 10
        unstoppableAnimation.setPostures(unstoppablePostures);
        unstoppableAnimation.setRestTimeforFish(10);
    }


    public void setIfScreenPressed(boolean ifScreenPressed) { // set if the screen is being touched
        this.ifScreenPressed = ifScreenPressed;
    }


    public void update() {
        if (firstUpdate) {
            firstUpdate = false;
            startTime = System.nanoTime();
        }
        double elapsed = ((System.nanoTime() - startTime) / 10000000f) / 10; // attention: this is milisecond
        timeForScore += elapsed;

        if(timeForScore>= 10) {
            if(GamePanel.gravityInverseMode){
                score+=3;
            }else if(GamePanel.unstoppableMode){
                score+=6;
            }else{
                score++;
            }
            timeForScore-=10;
        }

        startTime = System.nanoTime();    //update the startTime

        if(GamePanel.unstoppableMode) {
            unstoppableAnimation.update();
        }else{
            animation.update();
        }

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


        if(GamePanel.unstoppableMode){
            if (pos_y > (GamePanel.HEIGHT - 365)) {
                pos_y = GamePanel.HEIGHT - 365;
                Velocity_y = 0;
            }
            if(pos_y<-89){
                pos_y = -89;
                Velocity_y = 0;
            }
        }else {
            if (pos_y < 0) {
                pos_y = 0;
                Velocity_y = 0;
            }
            if (pos_y > (GamePanel.HEIGHT - this.heightInSpriteSheet)) {
                pos_y = GamePanel.HEIGHT - this.heightInSpriteSheet;
                Velocity_y = 0;
            }
        }
    }

    public void draw(Canvas canvas) {
        if(GamePanel.unstoppableMode) {
            canvas.drawBitmap(unstoppableAnimation.getCurrentPosture(), pos_x, pos_y, null);
            //Log.i("Draw fish", "pos_x is " + pos_x + " pos_y is " + pos_y);
        }else{
            canvas.drawBitmap(animation.getCurrentPosture(), pos_x, pos_y, null);
        }
    }

    public int getScore() {
        return score;
    }


    // these functions control the flow of the program
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


    public void gravityInverse(){
        gravityG = -gravityG;
        //setGravityG(-getGravityG());
        liftingAccelerate = -liftingAccelerate;
        //setLiftingAccelerate(-getLiftingAccelerate());
    }

    public Rect getBiggerUnstoppableRectangle(){
        return new Rect(pos_x+181, pos_y + 110, pos_x + 337, pos_y + 349);
    }
    public Rect getSmallerUnstoppableRectangle(){
        return new Rect(pos_x+342, pos_y + 272, pos_x + 504, pos_y + 340);
    }

    public void enterUnstoppableMode(){
        this.pos_x = 0;
        this.pos_y = 200;
        this.setVelocity_y(0);
    }
    public void leaveUnstoppableMode(){
        this.pos_x = 230;
        this.pos_y = GamePanel.HEIGHT / 2;
        this.setVelocity_y(0);
    }
    @Override
    public Rect getRectangle(){
            return new Rect(pos_x, pos_y + 11, pos_x + widthInSpriteSheet, pos_y + 42);
    }
}
