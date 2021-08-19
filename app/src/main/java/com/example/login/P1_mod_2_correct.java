package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class P1_mod_2_correct extends AppCompatActivity {
    Button btn_check;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod2_correct);

        btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_correct.this, P1_mod_2.class);
                startActivity(intent);
            }
        });

        btn_check=findViewById(R.id.btn_check_mod2);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2_correct.this, P1_mod_3.class);
                startActivity(intent);
            }
        });

    }
}