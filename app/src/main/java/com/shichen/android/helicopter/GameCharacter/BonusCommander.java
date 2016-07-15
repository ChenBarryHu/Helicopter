package com.shichen.android.helicopter.GameCharacter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shichen.android.helicopter.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by hsctn on 2016-07-15.
 */
public class BonusCommander {


    // these are the monsters
    public ArrayList<Bonus> bonuses;


    // this variable is used to determine when to create monsters
    public long bonusStartAppearTime;


    private long elaspedTime;
    private Fish fish;
    private boolean ifBonusOccurInThisRound = false;
    private Context context;
    private Random random;
    private int bonusSelector;  // true represent big yellow bird, false represent colorful bird
    private int numOfPosForGolden = 7;
    private int numOfPosForSilven = 7;
    private Bitmap resForGolden;
    private Bitmap resForSilven;

    private Bitmap[] arrayOfResForGolden;
    private Bitmap[] arrayOfResForSilven;


    public BonusCommander(Context context, Fish fish) {
        this.fish = fish;
        this.bonuses = new ArrayList<Bonus>();
        this.random = new Random();
        this.context = context;
        resForGolden = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden);
        resForSilven = BitmapFactory.decodeResource(context.getResources(), R.drawable.silven);
        arrayOfResForGolden = new Bitmap[numOfPosForGolden];
        arrayOfResForSilven = new Bitmap[numOfPosForSilven];
        for (int i = 0; i < numOfPosForGolden; i++) {
            // createBitmap Returns an immutable bitmap from the specified subset of the
            // source bitmap, we cut the sprite sheet to seperate each individual image for animation
            arrayOfResForGolden[i] = Bitmap.createBitmap(resForGolden, i * 65, 0, 65, 65);
            arrayOfResForSilven[i] = Bitmap.createBitmap(resForSilven, i * 65, 0, 65, 65);
        }
//            this.resForBigYellow[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigyellowup);
//            this.resForBigYellow[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigyellowdown);
//            this.resForColorfulBird = BitmapFactory.decodeResource(context.getResources(), R.drawable.birds);
//            this.resForBigEyeBlue[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe1);
//            this.resForBigEyeBlue[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe2);
//            this.resForBigEyeBlue[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe3);
//            this.resForBigEyeBlue[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe4);
//            this.resForBigEyeBlue[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe5);
//            this.resForBigEyeBlue[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe6);
//            this.resForBigEyeBlue[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe7);
//            this.resForBigEyeBlue[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe8);
    }

    private int selectBonus() {
        double intermedia = random.nextDouble() * 100;
        if (intermedia <= 50) {
            return 0;
        } else {
            return 1;
        }
    }


    public void update() {
        if (!ifBonusOccurInThisRound) {
            bonusStartAppearTime = System.nanoTime();
            ifBonusOccurInThisRound = true;
        }
        elaspedTime = (System.nanoTime() - bonusStartAppearTime) / 1000000;



        for (int i = 0; i < bonuses.size(); i++) {
            bonuses.get(i).update(elaspedTime);
            if (collision(bonuses.get(i), fish)) {
                bonuses.remove(i);
                break;
            }
            if (bonuses.get(i).getPos_x() < -70) {
                bonuses.remove(i);
            }
        }

        int timeForBonusOut = (int) (2000 + random.nextFloat() * 3000 - Fish.score * 10);
        if (timeForBonusOut < 500) {
            timeForBonusOut = 500;
        }
        if (elaspedTime > timeForBonusOut) {
            int bonusSelector = selectBonus();
            if (bonusSelector == 0) {
                bonuses.add(new Golden(arrayOfResForGolden));
            } else {
                bonuses.add(new Golden(arrayOfResForGolden));
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

    public void setIfBonusOccurInThisRound(boolean ifBonusOccurInThisRound) {
        this.ifBonusOccurInThisRound = ifBonusOccurInThisRound;
    }
}


