package com.shichen.android.helicopter.GameCharacter;

/**
 * Created by hsctn on 2016-06-26.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.shichen.android.helicopter.GamePanel;

public class Explosion {
    private int x;
    private int y;
    private int width;
    private int height;
    private int row=0;
    private Animation animation = new Animation();
    private Bitmap spritesheet;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i = 0; i<image.length; i++)
        {
            if(i%8==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(spritesheet, (i-(8*row))*width, row*height, width, height);
        }
        animation.setPostures(image);
        animation.setRestTimeforFish(10);



    }
    public void draw(Canvas canvas)
    {
        if(!animation.getIfhasDoneOnce())
        {
            canvas.drawBitmap(animation.getCurrentPosture(),x,y,null);
        }

    }
    public void update()
    {
        if(!animation.getIfhasDoneOnce())
        {
            animation.update();
        }
        if(animation.getIfhasDoneOnce()){
            GamePanel.explosionFinish = true;
        }
    }
    public int getHeight(){return height;}
}
