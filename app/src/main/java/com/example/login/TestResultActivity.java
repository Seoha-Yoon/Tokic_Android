package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.VerticalBarChart;
import org.eazegraph.lib.models.BarModel;

public class TestResultActivity extends AppCompatActivity {

    VerticalBarChart barChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result_pg);

        barChart = (VerticalBarChart)findViewById(R.id.score_chart);

        setBarChart();

    }

    private void setBarChart() {

        barChart.setMaximumValue(100f);
        barChart.clearChart();

        // 점수 float 형
        // 모변 답변 유사도
        barChart.addBar(new BarModel(74f, 0xFFC6A9FF));
        // 발음
        barChart.addBar(new BarModel(83f, 0xFFFFCF86));
        // 유창성
        barChart.addBar(new BarModel(74f, 0xFF74C7E5));
        // 표현력
        barChart.addBar(new BarModel(56f, 0xFFA2E0C1));
        // 문장의 적절성
        barChart.addBar(new BarModel(96f, 0xFFD9A5B5));

        barChart.startAnimation();

    }




}
