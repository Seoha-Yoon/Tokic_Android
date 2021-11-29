package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.StorageReference;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.VerticalBarChart;
import org.eazegraph.lib.models.BarModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit ;


public class TestResultActivity extends AppCompatActivity {

    VerticalBarChart barChart;

    private ProgressBar pgsBar;
    // upload video
    private String idByANDROID_ID;
    boolean success=false;

    // POST
    private static final Object BASE_URL = "http://15.165.31.148:5000/post";
    private static final String test_or_verify = "test";
    private ArrayList<Float> array_int = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result_pg);
        //pgsBar = (ProgressBar) findViewById(R.id.pBar);

        barChart = (VerticalBarChart)findViewById(R.id.score_chart);

        // 서버
        // 안드로이드폰 ID
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        Button calculate=findViewById(R.id.btn_score_calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreCalculation();
              //  pgsBar.setVisibility(v.VISIBLE);
            }
        });

        Button show=findViewById(R.id.btn_score_show);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(success){
                    setBarChart();
                }
                else{
                    Toast.makeText(TestResultActivity.this, "점수가 산정되지 않았습니다.\n 조금만 기다려주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void setBarChart() {

        barChart.setMaximumValue(100);
        barChart.clearChart();

        // 점수 float 형
        // 모변 답변 유사도
        barChart.addBar(new BarModel(array_int.get(0), 0xFFC6A9FF));
        // 발음
        barChart.addBar(new BarModel(array_int.get(1), 0xFFFFCF86));
        // 유창성
        barChart.addBar(new BarModel(array_int.get(2), 0xFF74C7E5));
        // 표현력
        barChart.addBar(new BarModel(array_int.get(3), 0xFFA2E0C1));
        // 문장의 적절성
        barChart.addBar(new BarModel(array_int.get(4), 0xFFD9A5B5));

        barChart.startAnimation();

    }



    private void scoreCalculation(){

        Toast.makeText(TestResultActivity.this, "점수 산정 시작 : 3분 정도 소요", Toast.LENGTH_SHORT).show();

        // flask 통신
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.HOURS)
                .readTimeout(10, TimeUnit.HOURS)
                .build();


        String start_time = Part1Prob.getTime;

        // POST
        RequestBody formbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("android_id",idByANDROID_ID)
                .addFormDataPart("test_or_verify",test_or_verify)
                .addFormDataPart("part1_url", "User/"+idByANDROID_ID+'/' + "No1"+"_"+idByANDROID_ID+start_time+"_test.mp3")
                .addFormDataPart("part2_url", "User/"+idByANDROID_ID+'/' + "No2"+"_"+idByANDROID_ID+start_time+"_test.mp3")
                .addFormDataPart("part3_url", "User/"+idByANDROID_ID+'/' + "No3"+"_"+idByANDROID_ID+start_time+"_test.mp3")
                .addFormDataPart("part4_url", "User/"+idByANDROID_ID+'/' + "No4"+"_"+idByANDROID_ID+start_time+"_test.mp3")
                .addFormDataPart("part5_url", "User/"+idByANDROID_ID+'/' + "No5"+"_"+idByANDROID_ID+start_time+"_test.mp3")
                .addFormDataPart("part6_url", "User/"+idByANDROID_ID+'/' + "No6"+"_"+idByANDROID_ID+start_time+"_test.mp3")

                .addFormDataPart("date_time", start_time)
                .build();

        Request req = new Request.Builder()
                .url(BASE_URL.toString())
                .post(formbody)
                .build();

        okHttpClient.newCall(req).enqueue(new Callback() {

            @Override
            public void onResponse(@com.google.firebase.database.annotations.NotNull Call call, @com.google.firebase.database.annotations.NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                System.out.println(responseBody);

                String [] array = responseBody.split(" ");
                for(int i=0; i<array.length; i++){
                    array_int.add(Float.parseFloat(array[i]));
                    System.out.println(array_int.get(i));
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast.makeText(TestResultActivity.this, "점수 산정 완료", Toast.LENGTH_SHORT).show();
                        success=true;
                    }
                }, 0);

            }

            @Override
            public void onFailure(@com.google.firebase.database.annotations.NotNull Call call, @NotNull IOException e) {
                System.out.println("fail");
            }
        });
    }





}
