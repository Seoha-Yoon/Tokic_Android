package com.example.login;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Queue;

public class MockTestBase extends AppCompatActivity {

    private Button nextProb;

    // countdown
    TextView textViewCountDown;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private long timeLeftInMillis;
    CountDownTimer countDownTimer;

    // manage fragment
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Part1ProbFragment fragmentPart1 = new Part1ProbFragment();
    private Part2ProbFragment fragmentPart2 = new Part2ProbFragment();
    private Part3ProbFragment fragmentPart3 = new Part3ProbFragment();
    private Part4ProbFragment fragmentPart4 = new Part4ProbFragment();
    private Part5ProbFragment fragmentPart5 = new Part5ProbFragment();
    private Part6ProbFragment fragmentPart6 = new Part6ProbFragment();

    private Fragment[] fragments = {fragmentPart1, fragmentPart2, fragmentPart3, fragmentPart4, fragmentPart5, fragmentPart6};
    private int num = 0;

    // voice recording
    MediaRecorder recorder;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mocktest_probs);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.prob_frame, fragments[num]).commitAllowingStateLoss();
        num++;

        // for Countdown
        textViewCountDown = findViewById(R.id.tv_countdown);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

        nextProb = findViewById(R.id.btn_nextprob);

        nextProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num<6) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.prob_frame, fragments[num]).commitAllowingStateLoss();
                    num++;
                }else{
                    Intent intent = new Intent(MockTestBase.this, MockTestActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                finish();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);

        textViewCountDown.setText(timeFormatted);
    }
}
