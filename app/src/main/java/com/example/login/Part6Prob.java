package com.example.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Part6Prob extends AppCompatActivity {
    // countdown
    TextView textViewCountDown;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private long timeLeftInMillis;
    CountDownTimer countDownTimer;

    // DB
    private TestDBHelper mTestDBHelper;

    // upload video
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private OkHttpClient okHttpClient;
    private String idByANDROID_ID;

    // POST
    String getTime;
    private static final Object BASE_URL = "http://3.139.81.205:5000/";
    public static final String test_or_verify = "test";

    //Recording & Playing
    MediaPlayer player;
    MediaRecorder audioRecorder;
    Uri audiouri;
    ParcelFileDescriptor file;

    private String outputFile = null;
    private String outputUri = null;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private String[] files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part6_prob);

        // 안드로이드폰 ID
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // initialize DB
        mTestDBHelper = new TestDBHelper(Part6Prob.this);

        // 현재 날짜
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("_yyyyMMdd_hhmmss");
        getTime = simpleDate.format(mDate);

        permissionCheck();

        ImageButton startRecord =findViewById(R.id.start_recording);
        ImageButton stopRecord = findViewById(R.id.stop_recording);
        ImageButton playRecord = findViewById(R.id.play_recording);
        Button next=findViewById(R.id.btn_next);

        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio();
                stopRecord.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), android.R.color.holo_red_dark));
            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
                playRecord.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), android.R.color.holo_red_dark));
            }
        });

        playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) playAudio();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Intent intent = new Intent(Part6Prob.this, TestResultActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button submit=findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitfile();
                scoreCalculation();
            }
        });


        // for Countdown
        textViewCountDown = findViewById(R.id.tv_countdown);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();
    }

    private void recordAudio() {

        // firebase에 저장되는 파일이름
        outputUri = "No6_"+idByANDROID_ID+Part1Prob.getTime+"_test.mp3";

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, outputUri);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/TOKIC/");

        // 안드로이드 local 경로 ?
        outputFile = "/sdcard/Music/TOKIC/" + outputUri;
        //outputFile = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + outputUri;
        audiouri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

        try {
            file = getContentResolver().openFileDescriptor(audiouri, "w");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (file != null) {
            audioRecorder = new MediaRecorder();
            audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            audioRecorder.setOutputFile(file.getFileDescriptor());
            audioRecorder.setAudioChannels(1);
            try {
                audioRecorder.prepare();
                audioRecorder.start();
                Toast.makeText(this, "녹음 시작됨.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void stopAudio() {
        if (audioRecorder != null) {
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;
            Toast.makeText(this, "녹음 중지됨.", Toast.LENGTH_SHORT).show();

        }
    }

    private void playAudio() {
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(file.getFileDescriptor());
            player.prepare();
            player.start();

            Toast.makeText(this, "재생 시작됨.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                Toast.makeText(Part6Prob.this, "Time Over", Toast.LENGTH_LONG).show();
                stopAudio();
                Intent intent = new Intent(Part6Prob.this, TestResultActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);

        textViewCountDown.setText(timeFormatted);

        // Start Audio Recording
        if(seconds == 20) {
          //  Toast.makeText(Part6Prob.this, "응답을 시작하세요!", Toast.LENGTH_SHORT).show();
          //  recordAudio();
        }
    }


    private void submitfile() {
            for(int i=1; i<7; i++){
                //여기 저장경로 이름만 수정해줘~~~
                outputFile = "/sdcard/Music/TOKIC/" + "No"+Integer.toString(i)+"_"+idByANDROID_ID+Part1Prob.getTime+"_test.mp3";
                Uri file = Uri.fromFile(new File(outputFile));
                StorageReference riversRef = storageRef.child("User/"+idByANDROID_ID+'/'+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(Part6Prob.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(Part6Prob.this, "업로드 성공", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void scoreCalculation(){
        // flask 통신
        okHttpClient = new OkHttpClient();

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

                .addFormDataPart("date_time", getTime)
                .build();

        Request req = new Request.Builder()
                .url(BASE_URL + "post")
                .post(formbody)
                .build();

        okHttpClient.newCall(req).enqueue(new Callback() {

            @Override
            public void onResponse(@com.google.firebase.database.annotations.NotNull Call call, @com.google.firebase.database.annotations.NotNull Response response) throws IOException {
                System.out.println(response.body().string());
                System.out.println("여기야여기");
            }

            @Override
            public void onFailure(@com.google.firebase.database.annotations.NotNull Call call, @NotNull IOException e) {
                System.out.println("fail");
                System.out.println("실패했나");

            }
        });



    }



}
