package com.shichen.android.helicopter.GameCharacter;

/**
 * Created by hsctn on 2016-06-24.
 */

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] postures;        // this array stores the spritesheet of the fish
    private int currentPostureIndex;  // this integer represent which posture the fish is using
    private long restTimeforFish;     // during this amount of time, fish is having a rest
                                      // it keeps the same posture

    private long startswimmingTime;   // used to record times between two posture updates


    // these are for the flow of the program
    private boolean ifFirstFrame = true;
    private boolean ifDoneOnce = false;


    public void setPostures(Bitmap[] postures) {
        this.postures = postures;
        currentPostureIndex = 0;
    }


    public void setRestTimeforFish(long resttime) {
        restTimeforFish = resttime;
    }


    public void update() {
        if (ifFirstFrame) {
            startswimmingTime = System.nanoTime();
            ifFirstFrame = false;
        }
        // attention: the unit of elapsed is milisecond
        long timesBetweenTwoUpdates = (System.nanoTime() - startswimmingTime) / 1000000;
        if (timesBetweenTwoUpdates > restTimeforFish) {
            currentPostureIndex++;
            startswimmingTime = System.nanoTime();
        }
        if (currentPostureIndex == postures.length) {
            currentPostureIndex = 0;
            ifDoneOnce = true;
        }

    }


    public Bitmap getCurrentPosture() {      // return the bitmap image for current posture
        return postures[currentPostureIndex];
    }

    public int getCurrentPostureIndex() {
        return currentPostureIndex;
    }

    public boolean getIfhasDoneOnce() {
        return ifDoneOnce;
    }

    public void setIfDoneOnce(boolean ifDoneOnce) {
        this.ifDoneOnce = ifDoneOnce;
    }

    public void setIfFirstFrame(boolean ifFirstFrame) {
        this.ifFirstFrame = ifFirstFrame;
    }
}