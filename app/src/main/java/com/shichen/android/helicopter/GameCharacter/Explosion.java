package com.shichen.android.helicopter.GameCharacter;

/**
 * Created by hsctn on 2016-06-26.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.shichen.android.helicopter.GamePanel;

public class Explosion{
    private int x;
    private int y;
    private int width = 200;
    private int height = 200;
    private int row=0;
    private Animation animation = new Animation();
    private Bitmap[] image = new Bitmap[16];
    public Explosion(Bitmap res)
    {
        for(int i = 0; i<image.length; i++)
        {
            if(i%8==0&&i>0)row++;
            image[i] = Bitmap.createBitmap(res, (i-(8*row))*width, row*height, width, height);
        }
        Log.e( "Explosion: ","xixi" );
        animation.setPostures(image);
        animation.setRestTimeforFish(10);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Canvas canvas)
    {
        if(!animation.getIfhasDoneOnce())
        {
            canvas.drawBitmap(animation.getCurrentPosture(),this.x,this.y,null);
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

    public void resetExplosion(){
        animation.setIfDoneOnce(false);
        animation.setIfFirstFrame(true);
    }
}
