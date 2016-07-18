package com.shichen.android.helicopter.GameCharacter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.shichen.android.helicopter.GamePanel;
import com.shichen.android.helicopter.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hsctn on 2016-07-15.
 */
public class BonusCommander {


    // these are the monsters
    public ArrayList<Golden> goldens;
    public ArrayList<InverseGravity> silvens;
    public ArrayList<Powerdrink> powerdrinks;


    // this variable is used to determine when to create monsters
    public long bonusStartAppearTime;
    static public long drawGraphTime;


    private long elaspedTime;
    private long eachUpdateLoopTime;
    private Fish fish;
    private boolean ifBonusOccurInThisRound = false;
    private Context context;
    private Random random;
    private int bonusSelector;  // true represent big yellow bird, false represent colorful bird
    private int numOfPosForGolden = 7;
    private int numOfPosForSilven = 7;
    private Bitmap resForGolden;
    private Bitmap resForSilven;
    private Bitmap resForPowerDrink;
    private int scoreWhenEatCorn;
    public int gravityInverseCount = 20;
    public int unstoppableCount = 10;
    private long startInverseTime;
    private long startUnstoppableTime;

    private Bitmap[] arrayOfResForGolden;
    private Bitmap[] arrayOfResForSilven;


    public BonusCommander(Context context, Fish fish) {
        this.fish = fish;
        this.goldens = new ArrayList<Golden>();
        this.silvens = new ArrayList<InverseGravity>();
        this.powerdrinks = new ArrayList<Powerdrink>();
        this.random = new Random();
        this.context = context;
        resForGolden = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden);
        resForSilven = BitmapFactory.decodeResource(context.getResources(), R.drawable.silven);
        resForPowerDrink = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerdrink2);
        arrayOfResForGolden = new Bitmap[numOfPosForGolden];
        arrayOfResForSilven = new Bitmap[numOfPosForSilven];
        for (int i = 0; i < numOfPosForGolden; i++) {
            // createBitmap Returns an immutable bitmap from the specified subset of the
            // source bitmap, we cut the sprite sheet to seperate each individual image for animation
            arrayOfResForGolden[i] = Bitmap.createBitmap(resForGolden, i * 65, 0, 65, 65);
            arrayOfResForSilven[i] = Bitmap.createBitmap(resForSilven, i * 65, 0, 65, 65);
        }
        this.scoreWhenEatCorn = 0;
    }

    private int selectBonus() {
        if(GamePanel.gravityInverseMode || GamePanel.unstoppableMode){ return 0;}
        double intermedia = random.nextDouble() * 100;
        if (intermedia <= 50) {
            return 2;
        } else if(intermedia <= 90){
            return 1;
        } else {
            return 0;
        }
    }


    public void update() {
        if((Fish.score - scoreWhenEatCorn)> 1) GamePanel.addpoints =false;
        if(GamePanel.gravityInverseMode){
            if((System.nanoTime() - startInverseTime)>1000000000){
                gravityInverseCount--;
                startInverseTime = System.nanoTime();
            }
            if(gravityInverseCount<0){
                gravityReinverse();
            }
        }


        if(GamePanel.unstoppableMode){
            if((System.nanoTime() - startUnstoppableTime)>1000000000){
                unstoppableCount--;
                startUnstoppableTime = System.nanoTime();
            }
            if(unstoppableCount<0){
                leaveUnstoppableMode();
            }
        }


        if (!ifBonusOccurInThisRound) {
            bonusStartAppearTime = System.nanoTime();
            drawGraphTime = bonusStartAppearTime;
            ifBonusOccurInThisRound = true;
        }
        elaspedTime = (System.nanoTime() - bonusStartAppearTime) / 1000000;
        eachUpdateLoopTime = (System.nanoTime() - drawGraphTime) / 1000000;
        drawGraphTime = System.nanoTime();
        Log.e("BonusCommander called", "eachUpdateLoopTime is "+eachUpdateLoopTime);

        if(GamePanel.unstoppableMode) {
            for (int i = 0; i < goldens.size(); i++) {
                goldens.get(i).update(eachUpdateLoopTime);
                if (collisionUnstoppableMode(goldens.get(i), fish)) {
                    Log.e("collision appears", "!!!!!!!");
                    Fish.score += 30;
                    GamePanel.addpoints = true;
                    this.scoreWhenEatCorn = Fish.score;
                    goldens.remove(i);
                    break;
                }
                if (goldens.get(i).getPos_x() < -70) {
                    goldens.remove(i);
                }
            }
//            for (int i = 0; i < silvens.size(); i++) {
//                silvens.get(i).update(eachUpdateLoopTime);
//                if (collisionUnstoppableMode(silvens.get(i), fish)) {
//                    Log.e("collision appears", "!!!!!!!");
//                    gravityInverse();
//                    silvens.remove(i);
//                    break;
//                }
//                if (silvens.get(i).getPos_x() < -70) {
//                    silvens.remove(i);
//                }
//            }
//            for (int i = 0; i < powerdrinks.size(); i++) {
//                powerdrinks.get(i).update(eachUpdateLoopTime);
//                if (collisionUnstoppableMode(powerdrinks.get(i), fish)) {
//                    Log.e("collision appears", "!!!!!!!");
//                    enterUnstoppableMode();
//                    powerdrinks.remove(i);
//                    break;
//                }
//                if (powerdrinks.get(i).getPos_x() < -70) {
//                    powerdrinks.remove(i);
//                }
//            }
        }else{
            for (int i = 0; i < goldens.size(); i++) {
                goldens.get(i).update(eachUpdateLoopTime);
                if (collision(goldens.get(i), fish)) {
                    Log.e("collision appears", "!!!!!!!");
                    if (GamePanel.gravityInverseMode) {
                        Fish.score += 15;
                    } else {
                        Fish.score += 5;
                    }
                    GamePanel.addpoints = true;
                    this.scoreWhenEatCorn = Fish.score;
                    goldens.remove(i);
                    break;
                }
                if (goldens.get(i).getPos_x() < -70) {
                    goldens.remove(i);
                }
            }
            for (int i = 0; i < silvens.size(); i++) {
                silvens.get(i).update(eachUpdateLoopTime);
                if (collision(silvens.get(i), fish)) {
                    Log.e("collision appears", "!!!!!!!");
                    gravityInverse();
                    silvens.remove(i);
                    break;
                }
                if (silvens.get(i).getPos_x() < -70) {
                    silvens.remove(i);
                }
            }
            for (int i = 0; i < powerdrinks.size(); i++) {
                powerdrinks.get(i).update(eachUpdateLoopTime);
                if (collision(powerdrinks.get(i), fish)) {
                    Log.e("collision appears", "!!!!!!!");
                    enterUnstoppableMode();
                    powerdrinks.remove(i);
                    break;
                }
                if (powerdrinks.get(i).getPos_x() < -70) {
                    powerdrinks.remove(i);
                }
            }

        }





        int timeForBonusOut = 5000+(int)(random.nextFloat()*4000);
        if (elaspedTime > timeForBonusOut) {
            int bonusSelector = selectBonus();
            if (bonusSelector == 0) {
                goldens.add(new Golden(arrayOfResForGolden));
            } else if(bonusSelector == 1){
                silvens.add(new InverseGravity(arrayOfResForSilven));
            }else{
                powerdrinks.add(new Powerdrink(resForPowerDrink));
            }
            bonusStartAppearTime = System.nanoTime();
        }
    }


    public void draw(Canvas canvas) {
        for (Golden golden : goldens) {
            golden.draw(canvas);
        }
        for (InverseGravity silven : silvens) {
            silven.draw(canvas);
        }
        for (Powerdrink powerdrink : powerdrinks) {
            powerdrink.draw(canvas);
        }
    }


    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    public boolean collisionUnstoppableMode(GameObject a, Fish b) {
        if ((Rect.intersects(a.getRectangle(), b.getBiggerUnstoppableRectangle()))||
                (Rect.intersects(a.getRectangle(), b.getSmallerUnstoppableRectangle()))) {
            return true;
        }
        return false;
    }


    public void setIfBonusOccurInThisRound(boolean ifBonusOccurInThisRound) {
        this.ifBonusOccurInThisRound = ifBonusOccurInThisRound;
    }

    public void gravityInverse(){
        fish.gravityInverse();
        startInverseTime = System.nanoTime();
        GamePanel.gravityInverseMode = true;
    }

    public void gravityReinverse(){
        fish.gravityInverse();
        GamePanel.gravityInverseMode = false;
        this.gravityInverseCount = 20;
    }

    public void enterUnstoppableMode(){
        startUnstoppableTime = System.nanoTime();
        GamePanel.unstoppableMode = true;
        fish.enterUnstoppableMode();
    }

    public void leaveUnstoppableMode(){
        GamePanel.unstoppableMode = false;
        this.unstoppableCount = 10;
        fish.leaveUnstoppableMode();
    }

    public void resume(){this.drawGraphTime = System.nanoTime();}
}


