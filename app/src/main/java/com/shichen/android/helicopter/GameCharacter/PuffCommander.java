package com.shichen.android.helicopter.GameCharacter;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by hsctn on 2016-06-25.
 */
public class PuffCommander {
    public ArrayList<WaterPuff> puff;
    private Fish fish;
    public long puffStartAppearTime;
    private long elaspedTime;
    private boolean ifFirstPuffInThisRound = true;

    public PuffCommander(Fish fish) {
        puff = new ArrayList<WaterPuff>();
        this.fish = fish;
    }

    public void update() {
        if (ifFirstPuffInThisRound) {
            puffStartAppearTime = System.nanoTime();
            ifFirstPuffInThisRound = false;
        }
        elaspedTime = (System.nanoTime() - puffStartAppearTime) / 1000000;

        if (elaspedTime > 200) {
            puff.add(new WaterPuff(fish.getPos_x()+30, fish.getPos_y() + 10));
            puffStartAppearTime = System.nanoTime();
            for (int i = 0; i < puff.size(); i++) {
                puff.get(i).update();
                if (puff.get(i).getPos_x() - fish.getPos_x()< -10) {
                    puff.remove(i);
                }
            }
        }
    }


    public void draw(Canvas canvas) {
        for(WaterPuff wp: puff)
        {
            wp.draw(canvas);
        }
    }

    public void setIfFirstPuffInThisRound(boolean ifFirstPuffInThisRound) {
        this.ifFirstPuffInThisRound = ifFirstPuffInThisRound;
    }
}
