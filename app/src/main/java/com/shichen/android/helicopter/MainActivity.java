package com.shichen.android.helicopter;

import android.app.Activity;
import android.content.SharedPreferences;
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

    public static final String PREFS_NAME = "MyPrefsFile";
    public FrameLayout game;
    public GamePanel gamePanel;
    private SharedPreferences scorePrefs;
    //static public long bestScore;

    @Override
    protected void onPause() {
        super.onPause();
        scorePrefs = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor ed = scorePrefs.edit();
        ed.putLong("BESTSCORE", GamePanel.bestScore);
        ed.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scorePrefs = getSharedPreferences(PREFS_NAME,0);
        long bestScore = scorePrefs.getLong("BESTSCORE",0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set the screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);





        game = new FrameLayout(this);
        gamePanel = new GamePanel (this);
        LinearLayout gameWidgets = new LinearLayout (this);




        final Button endGameButton = new Button(this);
        endGameButton.setWidth(300);
        endGameButton.setText("Stop Game");


        game.addView(gamePanel);
        game.addView(gameWidgets);

        setContentView(game);

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

    @Override
    public void onResume() {
        super.onResume();
    }
//




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
