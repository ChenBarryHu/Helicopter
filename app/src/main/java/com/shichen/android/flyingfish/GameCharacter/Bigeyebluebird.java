package com.shichen.android.flyingfish.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by hsctn on 2016-06-25.
 */
public class Bigeyebluebird extends Bird implements Serializable {
    private int score;
    private Random random;
    private Animation animation;
    Bitmap[] postures;

    public Bigeyebluebird(Bitmap[] postures, int pos_x, int pos_y, int numOfPostures) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.widthInSpriteSheet = 146;
        this.heightInSpriteSheet = 100;
        this.score = Fish.score;
        this.animation = new Animation();
        this.random = new Random();
        this.postures =postures;
        this.Velocity_x = Fish.score/100+7 + (int) (random.nextDouble() * 20);

        if (Velocity_x > 60) Velocity_x = 60; // bound bird's speed

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
        return new Rect(pos_x+36, pos_y+13, pos_x+84, pos_y+70);
    }
}
