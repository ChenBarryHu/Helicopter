package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shichen.android.helicopter.GamePanel;

import java.util.Random;

/**
 * Created by hsctn on 2016-07-15.
 */
public class Golden extends Bonus{
    private int w;
    private int A;
    private Random random;
    private long startBonusTime;
    private boolean ifFirstUpdate;
    private int acceleration_y;
    private Animation animation;
    Bitmap[] postures;
    public Golden(Bitmap[] postures){
        this.Velocity_x = -1;
        this.pos_y = 320;
        this.pos_x = GamePanel.HEIGHT+5+(int)(random.nextFloat()*10);
        this.w = 1;
        this.A = 240 -(int)(random.nextFloat()*30);
        this.Velocity_y = this.A * this.w;
        this.acceleration_y = 0;
        this.ifFirstUpdate = true;
        this.animation = new Animation();
        this.random = new Random();
        this.postures = new Bitmap[7];
        this.postures = postures;

        animation.setPostures(postures);
        animation.setRestTimeforFish(30);
    }
    @Override
    public  void update(long elaspedTime){
        this.pos_x -= elaspedTime * this.Velocity_x;
        this.pos_y += elaspedTime * this.Velocity_y;
        this.acceleration_y = -w*w*(this.pos_y - 320);
        this.Velocity_y -= this.acceleration_y * elaspedTime;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getCurrentPosture(), pos_x, pos_y, null);
    }


    @Override
    public Rect getRectangle(){
        return new Rect(pos_x, pos_y, 65, 65);
    }

}
