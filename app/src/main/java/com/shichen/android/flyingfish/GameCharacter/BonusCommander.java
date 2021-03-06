package com.shichen.android.flyingfish.GameCharacter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.shichen.android.flyingfish.GamePanel;
import com.shichen.android.flyingfish.MainActivity;
import com.shichen.android.flyingfish.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hsctn on 2016-07-15.
 */
public class BonusCommander {


    static public ArrayList<Bonus> bonuses;
    // these are the monsters
//    static public ArrayList<Golden> goldens;
//    static public ArrayList<InverseGravity> silvens;
//    static public ArrayList<Powerdrink> powerdrinks;

    public static int backup_currentBonusVelocity_y;
    public static int backup_pos_x;
    public static int backup_pos_y;
    public static boolean hasBonusNow;


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
    public static int bonusIndex;

    private Bitmap[] arrayOfResForGolden;
    private Bitmap[] arrayOfResForSilven;


    public BonusCommander(Context context, Fish fish) {
        this.fish = fish;
        this.bonuses = new ArrayList<Bonus>();
//        this.goldens = new ArrayList<Golden>();
//        this.silvens = new ArrayList<InverseGravity>();
//        this.powerdrinks = new ArrayList<Powerdrink>();
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
        if (GamePanel.gravityInverseMode || GamePanel.unstoppableMode) {
            return 0;
        }
        double intermedia = random.nextDouble() * 100;
        if (intermedia <= 40) {
            return 0;
        } else if (intermedia <= 70) {
            return 1;
        } else {
            return 2;
        }
    }


    public void update() {
        if ((Fish.score - scoreWhenEatCorn) > 1) GamePanel.addpoints = false;
        if (GamePanel.gravityInverseMode) {
            if ((System.nanoTime() - startInverseTime) > 1000000000) {
                gravityInverseCount--;
                startInverseTime = System.nanoTime();
            }
            if (gravityInverseCount < 0) {
                gravityReinverse();
            }
        }


        if (GamePanel.unstoppableMode) {
            if ((System.nanoTime() - startUnstoppableTime) > 1000000000) {
                unstoppableCount--;
                startUnstoppableTime = System.nanoTime();
            }
            if (unstoppableCount < 0) {
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
        Log.e("BonusCommander called", "eachUpdateLoopTime is " + eachUpdateLoopTime);

        if (GamePanel.unstoppableMode) {
            for (int i = 0; i < bonuses.size(); i++) {
                int thisBonusIndex = bonuses.get(i).bonusindex;
                bonuses.get(i).update(eachUpdateLoopTime);
                if (collisionUnstoppableMode(bonuses.get(i), fish)) {
                    if(thisBonusIndex == 0) {
                        Log.e("collision appears", "!!!!!!!");
                        Fish.score += 30;
                        GamePanel.addpoints = true;
                        this.scoreWhenEatCorn = Fish.score;
                        bonuses.remove(i);
                        MainActivity.unLockgolden();
                        break;
                    }
                }
                if (bonuses.get(i).getPos_x() < -70) {
                    bonuses.remove(i);
                }
            }

        } else {
            for (int i = 0; i < bonuses.size(); i++) {
                int thisBonusIndex = bonuses.get(i).bonusindex;
                bonuses.get(i).update(eachUpdateLoopTime);
                if (collision(bonuses.get(i), fish)) {
                    if(thisBonusIndex==0) {
                        Log.e("collision appears", "!!!!!!!");
                        if (GamePanel.gravityInverseMode) {
                            Fish.score += 15;
                        } else {
                            Fish.score += 5;
                        }
                        MainActivity.unLockgolden();
                        GamePanel.addpoints = true;
                        this.scoreWhenEatCorn = Fish.score;
                        bonuses.remove(i);
                        break;
                    }else if (thisBonusIndex==1){
                        Log.e("collision appears", "!!!!!!!");
                        gravityInverse();
                        MainActivity.unLockReverseGravity();
                        bonuses.remove(i);
                        break;
                    }else{
                        Log.e("collision appears", "!!!!!!!");
                        enterUnstoppableMode();
                        MainActivity.unLockPowerDrink();
                        bonuses.remove(i);
                        break;
                    }
                }
                if (bonuses.get(i).getPos_x() < -70) {
                    bonuses.remove(i);
                }
            }
        }


        int timeForBonusOut = 5000 + (int) (random.nextFloat() * 4000);
        if (elaspedTime > timeForBonusOut) {
            int bonusSelector = selectBonus();
            if (bonusSelector == 0) {
                bonuses.add(new Golden(arrayOfResForGolden));
            } else if (bonusSelector == 1) {
                bonuses.add(new InverseGravity(arrayOfResForSilven));
            } else {
                bonuses.add(new Powerdrink(resForPowerDrink));
            }
            bonusStartAppearTime = System.nanoTime();
        }
    }


    public void draw(Canvas canvas) {
        for (Bonus bonus : bonuses) {
            bonus.draw(canvas);
        }

    }


    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    public boolean collisionUnstoppableMode(GameObject a, Fish b) {
        if ((Rect.intersects(a.getRectangle(), b.getBiggerUnstoppableRectangle())) ||
                (Rect.intersects(a.getRectangle(), b.getSmallerUnstoppableRectangle()))) {
            return true;
        }
        return false;
    }


    public void setIfBonusOccurInThisRound(boolean ifBonusOccurInThisRound) {
        this.ifBonusOccurInThisRound = ifBonusOccurInThisRound;
    }

    public void gravityInverse() {
        fish.gravityInverse();
        startInverseTime = System.nanoTime();
        GamePanel.gravityInverseMode = true;
    }

    public void gravityReinverse() {
        fish.gravityReinverse();
        GamePanel.gravityInverseMode = false;
        this.gravityInverseCount = 20;
    }

    public void enterUnstoppableMode() {
        startUnstoppableTime = System.nanoTime();
        GamePanel.unstoppableMode = true;
        fish.enterUnstoppableMode();
    }

    public void leaveUnstoppableMode() {
        GamePanel.unstoppableMode = false;
        this.unstoppableCount = 10;
        fish.leaveUnstoppableMode();
    }

    public void resume() {
        this.drawGraphTime = System.nanoTime();
    }

    public static void backup() {
        hasBonusNow = (bonuses.size()==1);
        backup_currentBonusVelocity_y = bonuses.get(0).Velocity_y;
        backup_pos_x = bonuses.get(0).pos_x;
        backup_pos_y = bonuses.get(0).pos_y;
    }
}


