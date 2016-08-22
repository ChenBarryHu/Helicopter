package com.shichen.android.flyingfish;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by hsctn on 2016-06-23.
 */


public class GameLoopEngine extends Thread {
    private int FPS = 40;                      // how many frame we want per second
    private double framecap = 1.0 / FPS;       // this is the time of each frame of this game


    private SurfaceHolder surfaceHolder;       // surfaceholder and gamePanel object
    final private GamePanel gamePanel;               //    use surfaceholder to get canvas, use gamePanel to
    //    control game

    private boolean ifRunning;       //used to control the  status of the game by setting the value of it true or false
    public static boolean ifPauseGame;
    public static Canvas canvas;   // we use Canvas to draw something

    static public double thisTime;
    public double lastTime;


    public GameLoopEngine(SurfaceHolder surfaceHolder, GamePanel gamePanel) { // constructor: we need a
        // surfaceHolder and gamePanel to construct this object
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }


    @Override
    public void run() {              // because GameLoopEngine extends thread, so should override run() method

        ifRunning = true;


        thisTime = System.nanoTime() / 1000000000.0f;  // this is for first loop
        // thisTime and lastTime are uesd to calculate passedTime
        double passedTime;                                    // this is the running time for each game Engine loop
        double unprocessedTime = 0;                           // this is used to determine when to update




        // the following three variables are used for debugging
        // they are used for printing frequency of while loop and update
        double fpsTimeCumulator = 0;
        int fpsCountCumulator = 0;
        //long whileloopcount = 0;

        gamePanel.thread.drawOnceBackGround();
        while (ifRunning) {   // when ifRunning is true, the loop will keep ifRunning
            lastTime = thisTime;
            thisTime = System.nanoTime() / 1000000000.0f;
            passedTime = thisTime - lastTime;
            unprocessedTime += passedTime;
            fpsTimeCumulator += passedTime;
            //whileloopcount++;
            //Log.e("gameEngine","pointD");
            //Log.i("Fish :", "the score of fish is "+ Fish.score);
            if(ifPauseGame) {
                //Log.e("gameEngine","pointC");
                if(unprocessedTime > framecap){
                    unprocessedTime -= framecap;
                    fpsCountCumulator++;
                    if (fpsTimeCumulator > 1) {            // print out the frequency of the engine
                        Log.e("Frequency of the Engine"," fps = " + fpsCountCumulator);
                        fpsCountCumulator = 0;
                        fpsTimeCumulator -= 1.0f;
                    }
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            if(unprocessedTime > framecap) {       // when unprocessedTime > framecap, we need to refresh
                unprocessedTime -= framecap;        // we subtract the framecap for next refesh
                fpsCountCumulator++;                // every time we refresh, we add 1 to fpsCountCumulator
                //               which is a variable used for debugging


                // to draw with canvas, we need three steps:
                //    1. first, lockCanvas, that means instantiate canvas
                //    2. second, draw
                //    3. unlock the canvas and post

                // attention: the surfaceHolder below is actually passed from
                //      GamePanel, that means we draw something in the surfaceView
                //      (which is GamePanel)
                canvas = null;
                Log.e("gameEngine","pointA");
                try {
                    canvas = this.surfaceHolder.lockCanvas();
//                    final float scaleFactorX = (float) gamePanel.getWidth() / (200 * 1.f);     // getWidth(), getHeight() gets
//                    final float scaleFactorY = (float) gamePanel.getHeight() / (HEIGHT * 1.f); // the screenwidth and screenheight
//                    canvas.scale(scaleFactorX, scaleFactorY);  // scale the  canvas
                    synchronized (surfaceHolder) {

                        Log.e("gameEngine","pointB");
                        canvas.drawBitmap(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.smallblue), 0, 0, null);
                        this.gamePanel.update();           // we call the methods in gamePanel
                        this.gamePanel.draw(canvas);

                    }
                } catch (Exception e) {
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                if (fpsTimeCumulator > 1) {            // print out the frequency of the engine
                    Log.e("Frequency of the Engine"," fps = " + fpsCountCumulator);
                    fpsCountCumulator = 0;
                    fpsTimeCumulator -= 1.0f;
                }
            }
        }
    }


    public void setIfRunning(boolean ifrunning) {   // this is used to set the status of the game to running or stopped
        this.ifRunning = ifrunning;
    }

    public void setIfPauseGame(boolean ifPauseGame) {
        this.ifPauseGame = ifPauseGame;
    }

//    public boolean isIfPauseGame() {
//        return ifPauseGame;
//    }

    public void drawOnceBackGround(){
        canvas = null;
        try {
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {

                //Log.e("gameEngine","pointB");
                this.gamePanel.draw(canvas);
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
