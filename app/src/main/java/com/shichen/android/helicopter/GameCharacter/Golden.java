package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import java.util.Random;

/**
 * Created by hsctn on 2016-07-15.
 */
public class Golden extends Bonus{
    private int w;
    private int A;
    private Random random;

    private int acceleration_y;
    private Animation animation;
    Bitmap[] postures;
    public Golden(Bitmap[] postures){
        this.random = new Random();
        this.Velocity_x = -7;
        this.pos_y = 320;
        this.pos_x = 1300 +(int)(random.nextFloat()*10);
        this.w = 1;
        this.A = 0 -(int)(random.nextFloat()*500);
        this.Velocity_y = this.A * this.w;

        if((random.nextFloat()*2)<1) this.Velocity_y = -this.Velocity_y;
        this.acceleration_y = 0;
        this.animation = new Animation();
        this.postures = new Bitmap[7];
        this.postures = postures;

        animation.setPostures(postures);
        animation.setRestTimeforFish(30);
    }

    @Override
    public  void update(long elaspedTime){
        this.pos_x += (int)((elaspedTime/10.0) * this.Velocity_x);
        this.pos_y += (int)((elaspedTime/10.0) * ((float)(this.Velocity_y)/100));
        this.acceleration_y = -w*w*(this.pos_y - 320)/150;
        this.Velocity_y += this.acceleration_y * elaspedTime;
        //this.pos_x += -5;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas){
        Log.e("Golden draw called","!!!!!!!!!!!");
        canvas.drawBitmap(animation.getCurrentPosture(), pos_x, pos_y, null);
    }


    @Override
    public Rect getRectangle(){
        return new Rect(pos_x, pos_y, pos_x+65, pos_y+65);
    }
}
