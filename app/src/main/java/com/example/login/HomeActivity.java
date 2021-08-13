package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private static final Object BASE_URL = "http://18.118.47.176:5000/";
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

        // 안드로이드폰 고유 ID
        String idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(HomeActivity.this, idByANDROID_ID, Toast.LENGTH_SHORT).show();


        // flask 통신
        OkHttpClient okHttpClient = new OkHttpClient();

        // POST TEST
        RequestBody formbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key","value")
                .addFormDataPart("key2", "value")
                .build();

        Request req = new Request.Builder()
                .url(BASE_URL + "post")
                .post(formbody)
                .build();

        okHttpClient.newCall(req).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println(response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("fail");

            }

        });

        // GET TEST
        Request request = new Request.Builder().url("http://18.118.47.176:5000/one").build();


        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                TextView textView = findViewById(R.id.textview);
                System.out.println(response.body().string());
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                TextView textView = findViewById(R.id.textview);
                System.out.println("fail");

            }

        });
    }



}