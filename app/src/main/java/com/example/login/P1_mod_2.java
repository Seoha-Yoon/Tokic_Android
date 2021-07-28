package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P1_mod_2 extends AppCompatActivity {

    Button sen1,sen2,sen3;
    String txt1,txt2,txt3;

    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod2);

        btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2.this, P1_main.class);
                startActivity(intent);
            }
        });


        ArrayList<String> l = new ArrayList<>();

        sen1 = (Button)findViewById(R.id.sentence1);
        sen2 = (Button)findViewById(R.id.sentence2);
        sen3 = (Button)findViewById(R.id.sentence3);

        txt1 = "저는 시간 있을 때 영화관에 가요.";
        txt2 = "저의 취미는 영화보기에요.";
        txt3 = "집에서도 영화를 자주 봐요.";

        l.add(txt1);
        l.add(txt2);
        l.add(txt3);

        Collections.shuffle(l);

        sen1.setText(l.get(0));
        sen2.setText(l.get(1));
        sen3.setText(l.get(2));




    }
}