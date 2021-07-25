package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class P1_mod_1 extends AppCompatActivity {
    Button btn_blank, btn_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod1);
        btn_blank = findViewById(R.id.btn_blank);
        btn_answer=findViewById(R.id.btn_answer);

        btn_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_mod_1_blank p1_mod_1_blank = new P1_mod_1_blank();

                transaction.replace(R.id.frame_fillblank, p1_mod_1_blank);
                transaction.commit();
            }
        });

        btn_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_mod_1_answer p1_mod_1_answer = new P1_mod_1_answer();

                transaction.replace(R.id.frame_fillblank, p1_mod_1_answer);
                transaction.commit();
            }
        });
    }
}