package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class P1_main extends AppCompatActivity {
    ScrollView scroll;
    Button btn_module, btn_intro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_main);
        scroll = findViewById(R.id.Scroll_P1);

        btn_module=findViewById(R.id.btn_goto_module);
        btn_intro=findViewById(R.id.btn_goto_intro);

        btn_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_module.setBackgroundColor(Color.GRAY);
                btn_intro.setBackgroundColor(Color.WHITE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_module p1_module = new P1_module();

                transaction.replace(R.id.frame_P1, p1_module);
                transaction.commit();


            }
        });

        btn_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_intro.setBackgroundColor(Color.GRAY);
                btn_module.setBackgroundColor(Color.WHITE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_intro p1_introduction = new P1_intro();

                transaction.replace(R.id.frame_P1, p1_introduction);
                transaction.commit();


            }
        });

    }
}