package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by hsctn on 2016-06-25.
 */
public class Bigeyebluebird extends Bird implements Parcelable {
    private int speed;
    private int score;
    private Random random;
    private Animation animation;
    Bitmap[] postures;

    public Bigeyebluebird(Bitmap[] postures, int pos_x, int pos_y, int widthInSpriteSheet,
                         int heightInSpriteSheet, int numOfPostures) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.widthInSpriteSheet = widthInSpriteSheet;
        this.heightInSpriteSheet = heightInSpriteSheet;
        this.score = Fish.score;
        this.animation = new Animation();
        this.random = new Random();
        this.postures =postures;
        this.speed = Fish.score/100+7 + (int) (random.nextDouble() * 20);

        if (speed > 60) speed = 60; // bound bird's speed

        animation.setPostures(postures);
        animation.setRestTimeforFish(100 - speed);

    }

    @Override
    public void update() {
        pos_x -= speed;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pos_x);
        dest.writeInt(pos_y);
        dest.writeInt(speed);
    }

    public Bigeyebluebird(int pos_x, int pos_y, int speed){
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed = speed;
    }
    public static final Creator<Bigeyebluebird> CREATOR = new Creator<Bigeyebluebird>() {
        @Override
        public Bigeyebluebird createFromParcel(Parcel source) {
            int pos_x = source.readInt();
            int pos_y = source.readInt();
            int speed = source.readInt();
            return new Bigeyebluebird(pos_x, pos_y, speed);
        }

        @Override
        public Bigeyebluebird[] newArray(int size) {
            return new Bigeyebluebird[0];
        }
    };
}
