package com.shichen.android.flyingfish.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by hsctn on 2016-06-25.
 */
public class ColorfulBird extends Bird implements Serializable {
    private int score;
    private Random random;
    private Animation animation;
    Bitmap[] postures;
    Bitmap spriteSheet;

    public ColorfulBird(Bitmap res, int pos_x, int pos_y, int numOfPostures)
    {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.widthInSpriteSheet = 64;
        this.heightInSpriteSheet = 64;
        this.animation = new Animation();
        this.score = Fish.score;
        this.spriteSheet = res;
        this.random = new Random();
        this.postures = new Bitmap[numOfPostures];
        this.Velocity_x = Fish.score/100+7 + (int) (random.nextDouble()*20); // We need to change here later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        if(Velocity_x>80)Velocity_x = 80;
        int color = selectColor();
        for(int i = 0; i<numOfPostures;i++)
        {
            postures[i] = Bitmap.createBitmap(spriteSheet, i * this.widthInSpriteSheet,
                    heightInSpriteSheet*color, this.widthInSpriteSheet, heightInSpriteSheet);
        }

        animation.setPostures(postures);
        animation.setRestTimeforFish(100-Velocity_x);

    }

    private int selectColor(){
        return (int)(random.nextDouble()*8);
    }

    @Override
    public void update()
    {
        pos_x-=Velocity_x;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
            canvas.drawBitmap(animation.getCurrentPosture(),pos_x,pos_y,null);
    }


}
