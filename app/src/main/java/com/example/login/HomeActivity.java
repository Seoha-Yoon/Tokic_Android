package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    Button btn_test, btn_part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_test = (Button)findViewById(R.id.test_start_button);
        btn_part = (Button)findViewById(R.id.prac_start_button);

        btn_test.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MockTestActivity.class);
            startActivity(intent);
        });

        btn_part.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}