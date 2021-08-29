package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestResultActivity extends AppCompatActivity {

    TextView similarity, pronunciation, fluency, expressiveness, relevance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result_pg);

        similarity = findViewById(R.id.sc_similarity);
        pronunciation = findViewById(R.id.sc_pronunciation);
        fluency = findViewById(R.id.sc_fluency);
        expressiveness = findViewById(R.id.sc_expressiveness);
        relevance = findViewById(R.id.sc_relevance);

        // 점수로 바꾸기
        similarity.setText("0");
        pronunciation.setText("0");
        fluency.setText("0");
        expressiveness.setText("0");
        relevance.setText("0");
    }


}
