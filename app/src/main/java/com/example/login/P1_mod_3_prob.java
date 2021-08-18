package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class P1_mod_3_prob extends AppCompatActivity {

    Button btn_submit;
    ImageButton btn_back, btn_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod3_prob);

        btn_back=findViewById(R.id.btn_back_mod3_prob);
        btn_record=findViewById(R.id.recordmic_mod3_prob);
        btn_submit=findViewById(R.id.btn_submit_mod3_prob);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_3_prob.this, P1_mod_3.class);
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_3_prob.this, P1_mod_3_result.class);
                startActivity(intent);
            }
        });



    }
}