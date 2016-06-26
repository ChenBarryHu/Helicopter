package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by hsctn on 2016-06-25.
 */
public class ColorfulBird extends Bird{
    private int speed;
    private int score;
    private Random random;
    private Animation animation;
    Bitmap[] postures;
    Bitmap spriteSheet;

    public ColorfulBird(Bitmap res, int pos_x, int pos_y ,int widthInSpriteSheet,
                        int heightInSpriteSheet, int numOfPostures)
    {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.widthInSpriteSheet = widthInSpriteSheet;
        this.heightInSpriteSheet = heightInSpriteSheet;
        this.animation = new Animation();
        this.score = Fish.score;
        this.spriteSheet = res;
        this.random = new Random();
        this.postures = new Bitmap[numOfPostures];
        this.speed = Fish.score/100+7 + (int) (random.nextDouble()*20); // We need to change here later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        if(speed>20)speed = 20; // bound bird's speed

        int color = selectColor();
        for(int i = 0; i<numOfPostures;i++)
        {
            postures[i] = Bitmap.createBitmap(spriteSheet, i * widthInSpriteSheet,
                    heightInSpriteSheet*color, widthInSpriteSheet, heightInSpriteSheet);
        }

        animation.setPostures(postures);
        animation.setRestTimeforFish(100-speed);

    }

    private int selectColor(){
        return (int)(random.nextDouble()*8);
    }

    @Override
    public void update()
    {
        score = Fish.score;
        pos_x-=speed;
        animation.update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getCurrentPosture(),pos_x,pos_y,null);
        }catch(Exception e){}
    }

//    @Override
//    public int getWidth()
//    {
//        //offset slightly for more realistic collision detection
//        return widthInSpriteSheet-10;
//    }
}
