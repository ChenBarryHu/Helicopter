package com.shichen.android.flyingfish.GameCharacter;

import android.graphics.Rect;

/**
 * Created by hsctn on 2016-06-24.
 */
public abstract class GameObject {
    public int pos_x;               // the x, y position of the game object
    public int pos_y;
    protected int Velocity_x;          // the velocity in x axis and y axis of the object
    protected int Velocity_y;
    protected int widthInSpriteSheet;  // Every game object need a image to represent it at a moment
    protected int heightInSpriteSheet; // widthInSpriteSheet x heightInSpriteSheet are the sizes of that image

                      // followings are getter and setter functions
    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public void setVelocity_x(int velocity_x) {
        Velocity_x = velocity_x;
    }

    public void setVelocity_y(int velocity_y) {
        Velocity_y = velocity_y;
    }

    public int getWidthInSpriteSheet() {
        return widthInSpriteSheet;
    }

    public int getHeightInSpriteSheet() {
        return heightInSpriteSheet;
    }

    public Rect getRectangle()
    {
        return new Rect(pos_x, pos_y, pos_x+widthInSpriteSheet, pos_y+heightInSpriteSheet);
    }

}
