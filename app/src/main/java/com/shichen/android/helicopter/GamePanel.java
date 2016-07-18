package com.shichen.android.helicopter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shichen.android.helicopter.GameCharacter.BonusCommander;
import com.shichen.android.helicopter.GameCharacter.Explosion;
import com.shichen.android.helicopter.GameCharacter.Fish;
import com.shichen.android.helicopter.GameCharacter.MonsterCommander;
import com.shichen.android.helicopter.GameCharacter.PuffCommander;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by hsctn on 2016-06-23.
 */
// SurfaceHolder.Callback interface has three abstract void methed:
//  surfaceChanged, surfaceCreated, surfaceDestroyed
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    // ********************* variable part******************************//
    // these are constant for background
    public static final int WIDTH = 1280;   // these are the actual width and
    public static final int HEIGHT = 640;   // height of the background picture
    public static final int MOVESPEED = -5; // this is the distance background move left
    // every time we update



    // these are the objects we are gonna manipulate to run the game
    public GameLoopEngine thread;          // we will need a thread to run our engine
    private Background bg;                  // this is a background object,provide bitmap, position, update method and draw  method for app background
    final public Fish fish;                      // we will create our player character which is a cute little fish!!!
    private PuffCommander puffCommander;
    private MonsterCommander monsterCommander;
    final public BonusCommander bonusCommander;
    private Explosion explosion;
    ExecutorService threadPoolExecutor;
    Future longRunningTaskFuture;



    // these are the sentinals we use to control the flow of the game
    private boolean newGameWellPrepared = true;
    static public boolean resetModeStart = false;
    private boolean fishNeedDisappear = false;
    public  static boolean explosionStart =false;
    public  static boolean explosionFinish = false;
    private boolean justOpened = true;
    public static boolean addpoints = false;
    public static boolean gravityInverseMode = false;
    public static boolean unstoppableMode = false;
    public boolean ifPauseGame=false;



    // bestScore record the score of the game, it will also be used to change the easyness of the game
    static public long bestScore;



    // this variable is used to give a interval when the little fish die
    private long startResetTime =0;




    //*********************************************************************************//


    public GamePanel(Context context, int bestscore) {
        super(context);
        getHolder().addCallback(this);      // add the callback to the surfaceholder
        // the code above means that the surfaceview hold its holder and
        // we need the callbacks(surfaceChanged, surfaceCreated, surfaceDestroyed)
        setFocusable(true);      // make gamePanel focusable so it can handle events
        if(thread==null){
            thread = new GameLoopEngine(getHolder(), this);
            thread.setIfRunning(true);
        }
        this.bestScore = (long)(bestscore);

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.gb));// initialize the background object, give it the image resource which is a bitmap
        fish = new Fish(BitmapFactory.decodeResource(getResources(), R.drawable.swimfish),BitmapFactory.decodeResource(getResources(), R.drawable.unstoppableswimminginmage));
        bonusCommander = new BonusCommander(getContext(), fish);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {   // This is called immediately after the surface is first created.

        explosionStart =false;
        explosionFinish = false;
        puffCommander = new PuffCommander(fish);
        monsterCommander = new MonsterCommander(getContext(), fish);
        explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion));
        if(thread==null){
            thread = new GameLoopEngine(getHolder(), this);
            thread.setIfRunning(true);
        }
        if (thread.getState() == Thread.State.NEW)  {
            thread.start();
        }

    }

    // This is called immediately after any structural
    // changes (format or size) have been made to the surface.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // This is called immediately before a surface is being destroyed.
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        int counter = 0;
        while (counter < 1000) {
            counter++;
            try {
                if(!(thread==null)) {
                    thread.setIfRunning(false);
                    //longRunningTaskFuture.cancel(true);; // wait until that thread finish
                    thread.join();
                    thread = null;   //  set it to null, so that the garbage collector can collect it
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override                                       // this is the central control part of the game
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!fish.getIfcurrentlyplaying()&&newGameWellPrepared) {
                resetModeStart = false;
                fish.setIfcurrentlyplaying(true);
                fish.setIfScreenPressed(true);
                newGameWellPrepared = false;
            } else if(fish.getIfcurrentlyplaying()){
                fish.setIfScreenPressed(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            fish.setIfScreenPressed(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e("GamePanel draw called","point F");
        final float scaleFactorX = (float) getWidth() / (WIDTH * 1.f);     // getWidth(), getHeight() gets
        final float scaleFactorY = (float) getHeight() / (HEIGHT * 1.f); // the screenwidth and screenheight
        super.draw(canvas);
        if (canvas != null) {
            final int savedState = canvas.save();      // I don't know what does it means
            canvas.scale(scaleFactorX, scaleFactorY);  // scale the  canvas
            //Log.e("draw"," background drawn!!!!!!!!!!!!!!!!!!!!!!!!");
            bg.draw(canvas);                           // draw the background and fish!!!!
            if(!fishNeedDisappear) {
                fish.draw(canvas);
            }
            if(!unstoppableMode) {
                puffCommander.draw(canvas);
            }
            monsterCommander.draw(canvas);
            Log.e("Commander draw called","Commander");
            bonusCommander.draw(canvas);
            if(explosionStart&& (!explosionFinish))
            {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);         // I don't know what does it means
        }

    }


    // this function is called in Mainthread ruuning
    // loop to update the background and fish
    public void update() {

        Log.e("GamePanel update called","point E");
        if (fish.getIfcurrentlyplaying()) {
            bg.update();
            fish.update();
            puffCommander.update();
            monsterCommander.update();
            Log.e("Commander update called","Commander");
            bonusCommander.update();


            //Log.e("From game panel:","The pos_y of fish is "+ fish.pos_y);
        }else{
            if(!resetModeStart)
            {
                resetModeStart = true;
                newGameWellPrepared = false;
                explosionStart = true;
                explosionFinish = false;
                startResetTime = System.nanoTime();
                fishNeedDisappear = true;
                explosion.setX(fish.getPos_x()-60);
                explosion.setY(fish.getPos_y()-70);

            }
            if(!newGameWellPrepared) {
                explosion.update();

                if (justOpened) {
                    explosionStart = false;
                    explosionFinish = true;
                    newGame();
                    return;
                }
                long resetElapsed = (System.nanoTime() - startResetTime) / 1000000;
                if (resetElapsed > 2500 && explosionFinish) {
                    explosionStart = false;
                    newGame();
                }
            }
        }
    }






    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Score: " + fish.getScore(), 10, HEIGHT - 60, paint);
        canvas.drawText("BEST: " + bestScore, WIDTH - 215, HEIGHT - 60, paint);

        Paint colorpaint = new Paint();
        colorpaint.setColor(getResources().getColor(R.color.add5scorescolor));
        colorpaint.setTextSize(50);
        colorpaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        if(addpoints){
            if(gravityInverseMode) {
                canvas.drawText("Great Job, +15!!!!", WIDTH / 2 - 200, 150, colorpaint);
            }else if(unstoppableMode){
                canvas.drawText("Great Job, +30!!!!", WIDTH / 2 - 200, 150, colorpaint);
            }else{
                canvas.drawText("Great Job, +5!!!!", WIDTH / 2 - 200, 150, colorpaint);
            }
        }
        if(gravityInverseMode){
            colorpaint.setColor(Color.WHITE);
            canvas.drawText("Inverse Gravity Mode remaining: " +bonusCommander.gravityInverseCount+" s", WIDTH/2-400, 90, colorpaint);
            canvas.drawText("TRIPLE BONUS!!!!! ", WIDTH/2-200, 500, colorpaint);
        }
        if(unstoppableMode){
            colorpaint.setColor(Color.WHITE);
            canvas.drawText("UNSTOPPABLE", WIDTH/2-200, 90, colorpaint);
            canvas.drawText("SIX TIMES BONUS!!!!! ", WIDTH/2-270, 500, colorpaint);
        }
        if(!fish.getIfcurrentlyplaying() && newGameWellPrepared)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2-130, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 -110, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 -90, paint1);
        }
    }


    public void newGame()
    {
        // clear the puff, monster and bat from last round
        monsterCommander.monsters.clear();
        bonusCommander.bonuses.clear();
        puffCommander.puff.clear();


        // reset the status of monstercommander, fish, and puffCommander
        monsterCommander.setIfMonsterOccurInThisRound(false);
        bonusCommander.setIfBonusOccurInThisRound(false);
        fish.setFirstUpdate(true);
        puffCommander.setIfFirstPuffInThisRound(true);
        explosion.resetExplosion();
        if(!justOpened) {
            if (fish.getScore() > bestScore) {
                bestScore = fish.getScore();
            }
        }
        if(gravityInverseMode){
            bonusCommander.gravityReinverse();
        }
        if(unstoppableMode){
            bonusCommander.leaveUnstoppableMode();
        }
        fish.resetScore();
        fish.setVelocity_y(0);
        fish.setPos_y(HEIGHT/2);
        fishNeedDisappear = false;
        newGameWellPrepared = true;
        addpoints = false;
        if(justOpened) {
            justOpened = false;
        }
    }


    public void pauseGame(){
        if(resetModeStart){
            return;
        }else{
            thread.setIfPauseGame(true);
        }
    }

    public void resumeGame(){
        thread.setIfPauseGame(false);
    }


    public static void setBestScore(long bestScore) {
        GamePanel.bestScore = bestScore;
    }
}
