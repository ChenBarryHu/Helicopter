package com.shichen.android.helicopter;

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.shichen.android.helicopter.GameCharacter.Fish;
import com.shichen.android.helicopter.GameCharacter.MonsterCommander;

public class  MainActivity extends  Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{


    private GoogleApiClient mGoogleApiClient;
    public FrameLayout game;
    public GamePanel gamePanel;
    Button endGameButton;
    Button SignIn;
    Button SignOut;
    Button Achievement;
    Button LB;
    private static int RC_SIGN_IN = 9001;

    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false; // set to true when you're in the middle of the
    // sign in flow, to know you should not attempt
    // to connect in onStart()


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
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        gamePanel.pauseGame();
        endGameButton.setText("resume game");
        SharedPreferences myPrefs = this.getSharedPreferences("mPrefs", MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt("bestscore", (int) GamePanel.bestScore);
        prefsEditor.commit();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, "CgkI8dbXyZAHEAIQBg", (int) GamePanel.bestScore);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();

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

        SignIn = new Button(this);
        SignIn.setWidth(300);
        SignIn.setText("Sign In");
        SignIn.setId(R.id.signin);

        SignOut = new Button(this);
        SignOut.setWidth(300);
        SignOut.setText("Sign Out");
        SignOut.setId(R.id.signout);
        SignOut.setVisibility(View.GONE);

        Achievement = new Button(this);
        Achievement.setWidth(300);
        Achievement.setText("Achievement");
        Achievement.setId(R.id.achievement);

        LB = new Button(this);
        LB.setWidth(300);
        LB.setText("Leader Board");
        LB.setId(R.id.leaderboard);


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
        OnClickListener ach_listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                        1);
            }
        };
        OnClickListener lb_listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        "CgkI8dbXyZAHEAIQBg"), 1);

            }
        };

        OnClickListener listenerSignIn = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(v.getId()== R.id.signin){
                    mSignInClicked = true;
                    mGoogleApiClient.connect();

                }
                if(v.getId()==R.id.signout){
                    mSignInClicked = false;
//                    Games.signOut(mGoogleApiClient);
                    // user explicitly signed out, so turn off auto sign in
                    mExplicitSignOut = true;
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        Games.signOut(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                    }

                }
                SignIn.setVisibility(View.VISIBLE);
                SignOut.setVisibility(View.GONE);

            }
        };

        endGameButton.setOnClickListener(listener);
        SignIn.setOnClickListener(listenerSignIn);
        SignOut.setOnClickListener(listenerSignIn);
        Achievement.setOnClickListener(ach_listener);
        LB.setOnClickListener(lb_listener);

        gameWidgets.addView(endGameButton);
        gameWidgets.addView(SignIn);
        gameWidgets.addView(SignOut);
        gameWidgets.addView(Achievement);
        gameWidgets.addView(LB);

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


    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
        findViewById(R.id.signin).setVisibility(View.GONE);
        findViewById(R.id.signout).setVisibility(View.VISIBLE);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error))){
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
        SignIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }

}
