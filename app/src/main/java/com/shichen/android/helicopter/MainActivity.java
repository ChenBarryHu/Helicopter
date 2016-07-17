package com.shichen.android.helicopter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shichen.android.helicopter.GameCharacter.Bird;

public class MainActivity extends Activity {
    FrameLayout game;
    GamePanel gameView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // set the screen to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // make the surface view the layout of this activity


        game = new FrameLayout(this);
        gameView = new GamePanel(this);
        LinearLayout gameWidgets = new LinearLayout(this);


        final Button endGameButton = new Button(this);
        endGameButton.setWidth(300);
        endGameButton.setText("Stop Game");


        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);


        OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!gamePanel.thread.ifPauseGame) {
                    gamePanel.pauseGame();
                    endGameButton.setText("resume game");
                } else {
                    gamePanel.fish.resume();
                    gamePanel.resumeGame();
                    gamePanel.bonusCommander.resume();
                    endGameButton.setText("stop game");
                }
            }
        };

        endGameButton.setOnClickListener(listener);
        gameWidgets.addView(endGameButton);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        gamePanel.pauseGame();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ggsmd", 1);
        outState.putParcelable("bird", bird);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int ggsmd = savedInstanceState.getInt("ggsmd");
        Bird bird = savedInstanceState.getParcelable("bird");
        |||||||6d 03 b57...Now we add pause button, but the lifecycle functions are not added, and
        if we pause and resume the game, the thread just broke
        setContentView(this.gamePanel);


        game = new FrameLayout(this);
        GamePanel gameView = new GamePanel(this);
        LinearLayout gameWidgets = new LinearLayout(this);


        final Button endGameButton = new Button(this);
        endGameButton.setWidth(300);
        endGameButton.setText("Start Game");


        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);


        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!gamePanel.thread.ifPauseGame) {
                    gamePanel.pauseGame();
                    endGameButton.setText("resume game");
                } else {
                    gamePanel.fish.resume();
                    gamePanel.resumeGame();
                    gamePanel.bonusCommander.resume();
                    endGameButton.setText("stop game");
                }
//                /** Instantiating PopupMenu class */
//                PopupMenu popup = new PopupMenu(getBaseContext(), v);
//
//                /** Adding menu items to the popumenu */
//                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
//
//                /** Defining menu item click listener for the popup menu */
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//
//                /** Showing the popup menu */
//                popup.show();
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

    @Override
    public void onPause() {
        super.onPause();
        gamePanel.pauseGame();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ggsmd", 1);
        outState.putParcelable("bird", bird);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int ggsmd = savedInstanceState.getInt("ggsmd");
        Bird bird = savedInstanceState.getParcelable("bird");
        =======
        setContentView(new GamePanel(this));
        >>>>>>>parent of 6d 03 b57...Now we add pause button, but the lifecycle functions are
        not added, and if we pause and resume the game, the thread just broke
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
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shichen.android.helicopter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shichen.android.helicopter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
