package com.shichen.android.helicopter.GameCharacter;

/**
 * Created by hsctn on 2016-06-25.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shichen.android.helicopter.GamePanel;
import com.shichen.android.helicopter.R;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by hsctn on 2016-06-25.
 */
public class MonsterCommander {

    // these are the monsters
    public ArrayList<Bird> monsters;
    public ArrayList<TopBat> topBats;
    public ArrayList<BotBat> botBats;

    // this variable is used to determine when to create monsters
    public long monsterStartAppearTime;


    public int score;
    private long elaspedTime;
    private Fish fish;
    private boolean ifMonsterOccurInThisRound = false;
    private Context context;
    private Random random;
    private int birdSelector;  // true represent big yellow bird, false represent colorful bird
    private final int widthForBigYellow = 179;
    private final int heightForBigYellow = 158;
    private final int widthForColorfulBird = 64;
    private final int heightForColorfulBird = 64;
    private final int widthForBigEyeBlue = 146;
    private final int heightForBigEyeBlue = 100;
    public Bitmap resForTopBat;
    private Bitmap resForBotBat;
    private Bitmap[] resForBigYellow = new Bitmap[2];
    private Bitmap resForColorfulBird;
    private Bitmap[] resForBigEyeBlue = new Bitmap[8];

    public MonsterCommander(Context context, Fish fish) {
        this.fish = fish;
        this.monsters = new ArrayList<Bird>();
        this.botBats = new ArrayList<BotBat>();
        this.topBats = new ArrayList<TopBat>();
        this.random = new Random();
        this.score = Fish.score;
        this.context = context;
        this.resForBigYellow[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigyellowup);
        this.resForBigYellow[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bigyellowdown);
        this.resForColorfulBird = BitmapFactory.decodeResource(context.getResources(), R.drawable.birds);
        this.resForBigEyeBlue[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe1);
        this.resForBigEyeBlue[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe2);
        this.resForBigEyeBlue[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe3);
        this.resForBigEyeBlue[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe4);
        this.resForBigEyeBlue[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe5);
        this.resForBigEyeBlue[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe6);
        this.resForBigEyeBlue[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe7);
        this.resForBigEyeBlue[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bbe8);
        this.resForTopBat = BitmapFactory.decodeResource(context.getResources(), R.drawable.topbat);
        this.resForBotBat = BitmapFactory.decodeResource(context.getResources(), R.drawable.botbat);
    }

    private int selectBird() {
        double intermedia = random.nextDouble() * 100;
        if (intermedia > 0 && intermedia <= 40) {
            return 0;
        } else if (intermedia >= 70) {
            return 1;
        } else {
            return 2;
        }
    }

    public void initializeBats() {
        for (int i = 0; i * 140 < 1280; i++) {
            topBats.add(new TopBat(resForTopBat, 140 * i));
        }
        for (int i = 0; i * 140 < 1280; i++) {
            botBats.add(new BotBat(resForBotBat, 140 * i));
        }
    }


    public void update() {
        if (!ifMonsterOccurInThisRound) {
            if (topBats.size() < 9 || botBats.size() < 9) {
                fish.setIfcurrentlyplaying(false);
                return;
            }
            monsterStartAppearTime = System.nanoTime();
            ifMonsterOccurInThisRound = true;
        }
        elaspedTime = (System.nanoTime() - monsterStartAppearTime) / 1000000;


        if (fish.pos_y < 30 || fish.pos_y > 580) {
            fish.setIfcurrentlyplaying(false);
        }
//        for(int i = 0; i<botBats.size(); i++){
//            //botBats.get(i).update();
//            //topBats.get(i).update();
//
//            if(collision(topBats.get(i), fish)) {
//                fish.setIfcurrentlyplaying(false);
//                fish.setFirstUpdate(true);
//                fish.setVelocity_y(0);
//                fish.setPos_y(GamePanel.HEIGHT/2);
//                break;
//            }
//            if(collision(botBats.get(i), fish)) {
//                fish.setIfcurrentlyplaying(false);
//                fish.setFirstUpdate(true);
//                fish.setVelocity_y(0);
//                fish.setPos_y(GamePanel.HEIGHT/2);
//                break;
//            }
//
//        }
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).update();
            if (collision(monsters.get(i), fish)) {
                monsters.remove(i);
                fish.setIfcurrentlyplaying(false);
                break;
            }
            if (monsters.get(i).getPos_x() < -180) {
                monsters.remove(i);
            }
        }

        int timeForMonsterOut = (int) (2000 + random.nextFloat() * 3000- Fish.score *10);
        if(timeForMonsterOut < 500){
            timeForMonsterOut = 500;
        }
        if (elaspedTime > timeForMonsterOut) {
            birdSelector = selectBird();
            if (birdSelector == 0) {
                monsters.add(new BigyellowBird(resForBigYellow, GamePanel.WIDTH + 10,
                        (int) (random.nextDouble() * (GamePanel.HEIGHT - heightForBigYellow))
                        , widthForBigYellow, heightForBigYellow, 2));
                monsterStartAppearTime = System.nanoTime();
            } else if (birdSelector == 1) {
                monsters.add(new ColorfulBird(resForColorfulBird, GamePanel.WIDTH + 10,
                        (int) (random.nextDouble() * (GamePanel.HEIGHT - heightForColorfulBird)),
                        widthForColorfulBird, heightForColorfulBird, 4));
            } else {
                monsters.add(new Bigeyebluebird(resForBigEyeBlue, GamePanel.WIDTH + 10,
                        (int) (random.nextDouble() * (GamePanel.HEIGHT - heightForBigEyeBlue))
                        , widthForBigEyeBlue, heightForBigEyeBlue, 2));
                monsterStartAppearTime = System.nanoTime();
            }
            monsterStartAppearTime = System.nanoTime();
        }

    }


    public void draw(Canvas canvas) {
        for (Bird bird : monsters) {
            bird.draw(canvas);
        }
        for (TopBat bat : topBats) {
            bat.draw(canvas);
        }
        for (BotBat bat : botBats) {
            bat.draw(canvas);
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    public void setIfMonsterOccurInThisRound(boolean ifMonsterOccurInThisRound) {
        this.ifMonsterOccurInThisRound = ifMonsterOccurInThisRound;
    }
}
