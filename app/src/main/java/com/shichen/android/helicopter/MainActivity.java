package com.shichen.android.helicopter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shichen.android.helicopter.GameCharacter.Bird;

public class  MainActivity extends  Activity  {

    public FrameLayout game;
    GamePanel gamePanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set the screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.gamePanel = new GamePanel(this);
        // make the surface view the layout of this activity
        setContentView(this.gamePanel);



        game = new FrameLayout(this);
        GamePanel gameView = new GamePanel (this);
        LinearLayout gameWidgets = new LinearLayout (this);




        final Button endGameButton = new Button(this);
        endGameButton.setWidth(300);
        endGameButton.setText("Stop Game");


        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);






        //dgdfgdfg
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(GamePanel.resetModeStart) return;
                if(!gamePanel.thread.ifPauseGame) {
                    gamePanel.pauseGame();
                    endGameButton.setText("resume game");
                }else{
                    gamePanel.fish.resume();
                    gamePanel.resumeGame();
                    //GameLoopEngine.thisTime = System.nanoTime();
                    gamePanel.bonusCommander.resume();
                    endGameButton.setText("stop game");
                }
            }
        };

        endGameButton.setOnClickListener(listener);
        gameWidgets.addView(endGameButton);
    }

//    public void onClick(View v){
//
//    }

//    public void showPopup() {
//        // Anchor popoup with layout to "center" menu
//
//        PopupMenu popup = new PopupMenu(this, game);
//        popup.setOnMenuItemClickListener(this);
//        popup.getMenuInflater().inflate(R.layout.menu, popup.getMenu());
//        popup.show();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        if(gamePanel.thread.()){
////            return;
////        }else{
////
////        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //gamePanel.pauseGame();
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ggsmd", 1);
        //outState.putParcelable("bird", bird);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int ggsmd = savedInstanceState.getInt("ggsmd");
        Bird bird = savedInstanceState.getParcelable("bird");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
