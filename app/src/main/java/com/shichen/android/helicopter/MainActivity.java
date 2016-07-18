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

import com.shichen.android.helicopter.GameCharacter.Fish;
import com.shichen.android.helicopter.GameCharacter.MonsterCommander;

public class  MainActivity extends  Activity  {

    public FrameLayout game;
    public GamePanel gamePanel;
    Button endGameButton;
//    static public int[] backup_bird_pos_x;
//    static public int[] backup_bird_pos_y;
//    static public int[] backup_bird_speed;
//    static public int backup_currentBonusVelocity_y;
//    static public int backup_bonus_pos_x;
//    static public int backup_bonus_pos_y;
//    static public int backup_background_x;
//    static public int backup_fish_position_y;
//    static public int backup_fish_speed_y;

    @Override
    protected void onPause() {
        super.onPause();
        gamePanel.pauseGame();
        endGameButton.setText("resume game");
        SharedPreferences myPrefs = this.getSharedPreferences("mPrefs", MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt("bestscore", (int) GamePanel.bestScore);
        prefsEditor.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set the screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences myPrefs1 = this.getSharedPreferences("mPrefs", MODE_PRIVATE);
        int bestscore = myPrefs1.getInt("bestscore", 0);
        game = new FrameLayout(this);
        gamePanel = new GamePanel (this, bestscore);
        LinearLayout gameWidgets = new LinearLayout (this);




        endGameButton = new Button(this);
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




    @Override
    public void onResume() {
        super.onResume();
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MonsterCommander.backup();
        //BonusCommander.backup();
        outState.putIntArray("bird_pos_x",MonsterCommander.backup_bird_pos_x);
        outState.putIntArray("bird_pos_y",MonsterCommander.backup_bird_pos_y);
        outState.putIntArray("bird_speed",MonsterCommander.backup_bird_speed);
//        outState.putInt("bonus_velocity_y",BonusCommander.backup_currentBonusVelocity_y);
//        outState.putInt("bonus_pos_x",BonusCommander.backup_pos_x);
//        outState.putInt("bonus_pos_y",BonusCommander.backup_pos_y);
        outState.putInt("bg_pos_x",Background.x);
        //outState.putInt("fish_pos_x", Fish.backup_position_x);
        outState.putInt("fish_pos_y", Fish.backup_position_y);
        outState.putInt("fish_velocity_y", Fish.backup_speed_y);


        //outState.putInt("ggsmd", 1);
        //outState.putParcelable("bird", bird);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        backup_bird_pos_x= savedInstanceState.getIntArray("bird_pos_x");
//        backup_bird_pos_y=savedInstanceState.getIntArray("bird_pos_y");
//        backup_bird_speed=savedInstanceState.getIntArray("bird_speed");
//        backup_currentBonusVelocity_y=savedInstanceState.getInt("bonus_velocity_y");
//        backup_bonus_pos_x=savedInstanceState.getInt("bonus_pos_x");
//        backup_bonus_pos_y=savedInstanceState.getInt("bonus_pos_y");
//        backup_background_x=savedInstanceState.getInt("bg_pos_x");
//        backup_fish_position_y=savedInstanceState.getInt("fish_pos_y");
//        backup_fish_speed_y=savedInstanceState.getInt("fish_velocity_y");
//        Toast.makeText(this, "this is my Toast message!!! =)",
//                Toast.LENGTH_LONG).show();
//        this.gamePanel.fish.pos_y = backup_fish_position_y;
//        this.gamePanel.fish.backup_speed_y = backup_fish_position_y;
//        this.gamePanel.fish.restoreVelocity();
//        this.gamePanel.fish.resume();
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
