package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Canvas;

/**
 * Created by hsctn on 2016-06-25.
 */
public abstract class Bird extends GameObject {
    public abstract void update();
    public abstract void draw(Canvas canvas);

}
