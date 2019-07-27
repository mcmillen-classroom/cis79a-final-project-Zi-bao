package com.e.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    //image
    private ImageView mario, turtle, coin;
    //private Drawable mario;

    // size
    private int marioSize;

    //position
    private float marioX, marioY;
    private float turtleX, turtleY;
    private float coinX, coinY;

    //score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;

    //class
    private Timer timer;
    private Handler handler = new Handler();

    //status
    private boolean start_flg = false;
    private boolean action_flg = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        mario = findViewById(R.id.mario);
        turtle = findViewById(R.id.turtle);
        coin = findViewById(R.id.coin);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);



    }

    public void changePos() {

        //add timeCount
        timeCount += 20;

        //coin drop
        coinY += 19;



        float coinCenterX = coinX + coin.getWidth() / 2;
        float coinCenterY = coinY + coin.getHeight() / 2;

        if (hitBox(coinCenterX, coinCenterY)) {
            coinY = frameHeight +100;
            score += 10;
        }

        if (coinY > frameHeight) {
            coinY = -100;
            coinX = (float) Math.floor(Math.random() *(frameWidth - coin.getWidth()));
        }
        coin.setX(coinX);
        coin.setY(coinY);

        //pink
        //turtle
        turtleY += 25;

        float turtleCenterX = turtleX + turtle.getWidth() / 2;
        float turtleCenterY = turtleY + turtle.getHeight() / 2;

        if (hitBox(turtleCenterX, turtleCenterY)){
            turtleY = frameHeight + 100;

            //change FrameWidth
            frameWidth = frameWidth * 80/100;
            changeFrameWidth(frameWidth);

            if (frameWidth <= marioSize){
                endGame();
            }
        }

        if (turtleY > frameHeight) {
            turtleY = -100;
            turtleX = (float) Math.floor(Math.random() * (frameWidth - turtle.getWidth()));
        }

        turtle.setX(turtleX);
        turtle.setY(turtleY);

        //move mario
        if(action_flg) {
            //touching
            marioX += 14;
            //mario.setImageDrawable(mario);
        } else {
            //releasing
            marioX -= 14;
        }

        //check mario position
        if (marioX < 0) {
            marioX = 0;
        }
        if (frameWidth - marioSize < marioX) {
            marioX = frameWidth - marioSize;
        }
        mario.setX(marioX);

        scoreLabel.setText("SCORE : " + score);
    }

    public boolean hitBox(float x, float y) {
        if(marioX <= x && x <= marioX + marioSize &&
                marioY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void endGame() {
        //timer stops
        timer.cancel();
        timer = null;
        start_flg = false;

        //before
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        mario.setVisibility(View.INVISIBLE);
        turtle.setVisibility(View.INVISIBLE);
        coin.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg =true;
            } else if (event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);

        if(frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            marioSize = mario.getHeight();
            marioX = mario.getX();
            marioY = mario.getY();
        }

        frameWidth = initialFrameWidth;

        mario.setX(0.0f);
        turtle.setY(3000.0f);
        coin.setY(3000.0f);

        turtleY = turtle.getY();
        coinY = coin.getY();

        mario.setVisibility(View.VISIBLE);
        turtle.setVisibility(View.VISIBLE);
        coin.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0 ");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        },0,20);

    }



    public void quitGame(View view) {

    }

}



