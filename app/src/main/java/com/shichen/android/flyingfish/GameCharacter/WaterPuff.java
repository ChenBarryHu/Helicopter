package com.shichen.android.flyingfish.GameCharacter;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by hsctn on 2016-06-25.
 */

public class WaterPuff extends GameObject {
    public int radius;

    public WaterPuff(int pos_x, int pos_y) {
        radius = 5;
        super.pos_x = pos_x;
        super.pos_y = pos_y;
    }

    public void update() {
        pos_x -= 10;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(0xEA80FC);
        paint.setAlpha(225);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(pos_x - radius + 35, pos_y + 25, radius, paint);
        //Log.e("Puff :", "The puff is being drawn");
    }

}
