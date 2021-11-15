package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class P1_mod_1 extends AppCompatActivity {
    Button btn_blank, btn_answer;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod1);
        btn_blank = findViewById(R.id.btn_blank);
        btn_answer=findViewById(R.id.btn_answer);
        btn_back=findViewById(R.id.btn_back);

        btn_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_blank.setBackgroundResource(R.drawable.clicked_radius_btn);
                btn_answer.setBackgroundResource(R.drawable.unclicked_radius_btn);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_mod_1_blank p1_mod_1_blank = new P1_mod_1_blank();

                transaction.replace(R.id.frame_fillblank, p1_mod_1_blank);
                transaction.commit();
            }
        });

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_answer.setBackgroundResource(R.drawable.clicked_radius_btn);
                btn_blank.setBackgroundResource(R.drawable.unclicked_radius_btn);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_mod_1_answer p1_mod_1_answer = new P1_mod_1_answer();

                transaction.replace(R.id.frame_fillblank, p1_mod_1_answer);
                transaction.commit();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_1.this, P1_main.class);
                startActivity(intent);
            }
        });
    }
}