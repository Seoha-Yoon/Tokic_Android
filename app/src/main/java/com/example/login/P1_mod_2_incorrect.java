package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class P1_mod_2_incorrect extends AppCompatActivity {

    ImageButton btn_tryagain,btn_solution,btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod2_incorrect);

        btn_tryagain = (ImageButton)findViewById(R.id.btn_tryagain);
        btn_solution=(ImageButton)findViewById(R.id.btn_solution);
        btn_back=(ImageButton)findViewById(R.id.btn_back);

        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_incorrect.this, P1_mod_2.class);
                startActivity(intent);
            }
        });

        btn_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_incorrect.this, P1_mod_2.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_incorrect.this, P1_module.class);
                startActivity(intent);
            }
        });

    }
}