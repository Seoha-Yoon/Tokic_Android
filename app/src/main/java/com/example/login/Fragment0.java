package com.example.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Fragment0 extends Fragment {

    RadarChart radarChart;
    LineChart lineChart;

    public Fragment0(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fv =inflater.inflate(R.layout.fragment0,container,false);
        Button btn_mk_test = fv.findViewById(R.id.btn_mk_test);
        radarChart = fv.findViewById(R.id.radar_chart);
        lineChart = fv.findViewById(R.id.score_line_chart);

        btn_mk_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MockTestActivity.class);
                startActivity(intent);
            }
        });

        makeChart();
        scoreLineChart();

        return fv;
    }

    // 차트 데이터 생성
    private ArrayList<RadarEntry> dataValue(){
        ArrayList<RadarEntry> dataVals = new ArrayList<>();
        dataVals.add(new RadarEntry(90)); // 모법답변 유사도
        dataVals.add(new RadarEntry(66)); // 발음
        dataVals.add(new RadarEntry(50)); // 표현력
        dataVals.add(new RadarEntry(44)); // 문장의 적절성
        dataVals.add(new RadarEntry(70)); // 유창성

        return dataVals;
    }

    private List<Entry> scoreValue(){
        List<Entry> score = new ArrayList<>();
        score.add(new Entry(0f,90));
        score.add(new Entry(1f,50));
        score.add(new Entry(2f,70));
        score.add(new Entry(3f,80));

        return score;
    }

    private void makeChart(){
        RadarDataSet dataSet = new RadarDataSet(dataValue(), "DATA");
        dataSet.setColor(Color.BLUE);

        RadarData data = new RadarData();
        data.addDataSet(dataSet);
        String[] labels =  {"모범답변 유사도", "발음", "표현력", "문장의 적절성", "유창성"};
        XAxis xAxis = radarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);
        radarChart.setData(data);

        Description description = radarChart.getDescription();
        description.setEnabled(false);
    }

    private void scoreLineChart(){
        LineDataSet score = new LineDataSet(scoreValue(),"모의고사");
        score.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(score);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate(); // refresh

        // the labels that should be drawn on the XAxis
        final String[] labels = new String[] { "Test1", "Test2", "Test3", "Test4" };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        Description description = lineChart.getDescription();
        description.setEnabled(false);
    }

}
