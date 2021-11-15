package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;

public class P1_main extends AppCompatActivity {
    ScrollView scroll;
    Button btn_module, btn_intro;
    ImageButton btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_main);
        scroll = findViewById(R.id.Scroll_P1);

        btn_module=findViewById(R.id.btn_goto_module);
        btn_intro=findViewById(R.id.btn_goto_intro);
        btn_back=findViewById(R.id.btn_back);

        btn_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_module.setBackgroundColor(Color.parseColor("#7EB0BD"));
                btn_module.setTextColor(Color.WHITE);
                btn_intro.setBackgroundColor(Color.WHITE);
                btn_intro.setTextColor(Color.BLACK);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_module p1_module = new P1_module();

                transaction.replace(R.id.frame_P1, p1_module);
                transaction.commit();


            }
        });

        btn_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_intro.setBackgroundColor(Color.parseColor("#7EB0BD"));
                btn_intro.setTextColor(Color.WHITE);
                btn_module.setBackgroundColor(Color.WHITE);
                btn_module.setTextColor(Color.BLACK);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                P1_intro p1_introduction = new P1_intro();

                transaction.replace(R.id.frame_P1, p1_introduction);
                transaction.commit();


            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_main.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}