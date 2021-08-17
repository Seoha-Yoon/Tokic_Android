package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class P1_mod_2_fail extends AppCompatActivity {

    ImageButton btn_back, btn_tryagain, btn_solution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod2_fail);

        btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_fail.this, P1_mod_2.class);
                startActivity(intent);
            }
        });

        btn_tryagain = findViewById(R.id.btn_tryagain);
        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_fail.this, P1_mod_2.class);
                startActivity(intent);
            }
        });

    }
}