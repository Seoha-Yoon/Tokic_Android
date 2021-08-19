package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class P1_mod_3 extends AppCompatActivity {

    Button btn_start;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod3);

        btn_back=findViewById(R.id.btn_back_mod3);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_3.this, P1_main.class);
                startActivity(intent);
            }
        });

        btn_start=findViewById(R.id.btn_start_mod3);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_3.this, P1_mod_3_prob.class);
                startActivity(intent);
            }
        });

    }
}