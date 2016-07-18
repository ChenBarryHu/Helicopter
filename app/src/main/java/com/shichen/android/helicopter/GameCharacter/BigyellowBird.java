package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by hsctn on 2016-06-25.
 */
public class BigyellowBird extends Bird implements Serializable {

    private int score;
    private Random random;
    private Animation animation;
    Bitmap[] postures;

    public BigyellowBird(Bitmap[] postures, int pos_x, int pos_y, int numOfPostures) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.widthInSpriteSheet = 179;
        this.heightInSpriteSheet = 158;
        this.score = Fish.score;
        this.animation = new Animation();
        this.random = new Random();
        this.postures = new Bitmap[numOfPostures];
        this.postures = postures;

        this.Velocity_x = Fish.score/100+7 + (int) (random.nextDouble() * 20);
        if(this.Velocity_x>=90){
            this.Velocity_x = 90;
        }
        animation.setPostures(postures);
        animation.setRestTimeforFish(100 - Velocity_x);

    }

    @Override
    public void update() {
        pos_x -= Velocity_x;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas) {
            canvas.drawBitmap(animation.getCurrentPosture(), pos_x, pos_y, null);
    }

    @Override
    public Rect getRectangle(){
        return new Rect(pos_x+24, pos_y+23, pos_x+117, pos_y+112);
    }
}