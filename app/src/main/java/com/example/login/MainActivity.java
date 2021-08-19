package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.google.android.material.chip.Chip;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    //private FirebaseAuth mFirebaseAuth;
    HorizontalScrollView scroll;
    Button btn_levelup,btn_home;
    Chip btn_result, btn_P1, btn_P2,btn_P3,btn_P4,btn_P5,btn_P6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scroll = findViewById(R.id.ScrollView);
        scroll.setHorizontalScrollBarEnabled(true);

        btn_home=(Button)findViewById(R.id.btn_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_result = (Chip)findViewById(R.id.btn_result); // = Fragment0
        btn_P1 = (Chip)findViewById(R.id.btn_P1);
        btn_P2 = (Chip)findViewById(R.id.btn_P2);
        btn_P3 = (Chip)findViewById(R.id.btn_P3);
        btn_P4 = (Chip)findViewById(R.id.btn_P4);
        btn_P5 = (Chip)findViewById(R.id.btn_P5);
        btn_P6 = (Chip)findViewById(R.id.btn_P6);
        btn_levelup = (Button)findViewById(R.id.btn_levelup); // = Fragment7

        //진단평가 클릭 후 첫 화면 설정
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment0 fragment0 = new Fragment0();

        transaction.replace(R.id.frame, fragment0);
        transaction.commit();

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment0 fragment0 = new Fragment0();

                transaction.replace(R.id.frame, fragment0);
                transaction.commit();
            }
        });


        btn_P1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment1 fragment1 = new Fragment1();

                transaction.replace(R.id.frame, fragment1);
                transaction.commit();


            }
        });

        btn_P2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment2 fragment2 = new Fragment2();

                transaction.replace(R.id.frame, fragment2);
                transaction.commit();
            }
        });

        btn_P3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment3 fragment3 = new Fragment3();

                transaction.replace(R.id.frame, fragment3);
                transaction.commit();
            }
        });

        btn_P4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment4 fragment4 = new Fragment4();

                transaction.replace(R.id.frame, fragment4);
                transaction.commit();
            }
        });

        btn_P5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment5 fragment5 = new Fragment5();

                transaction.replace(R.id.frame, fragment5);
                transaction.commit();
            }
        });

        btn_P6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment6 fragment6 = new Fragment6();

                transaction.replace(R.id.frame, fragment6);
                transaction.commit();
            }
        });

        btn_levelup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment7 fragment7 = new Fragment7();

                transaction.replace(R.id.frame, fragment7);
                transaction.commit();
            }
        });





    }


}