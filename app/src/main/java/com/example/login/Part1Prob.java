package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class Part1Prob extends AppCompatActivity {

    private String LOG_TAG = "Record_log";
    private static final Object BASE_URL = "http://18.118.47.176:5000/";

    @Override
    public void onBackPressed() {
    }

    // voice recording
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String outputFile = null;

    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 44100;
    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);

    public AudioRecord mAudioRecord = null;

    public Thread mRecordThread = null;
    public boolean isRecording = false;

    public AudioTrack mAudioTrack = null;
    public Thread mPlayThread = null;
    public boolean isPlaying = false;

    private ImageButton record;
    private Button stop, play, next;

    // countdown
    TextView textViewCountDown;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private long timeLeftInMillis;
    CountDownTimer countDownTimer;

    // DB
    private TestDBHelper mTestDBHelper;

    // upload video
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private TextView tvMainText;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        System.out.println("====================="+user+"======================");
//        if (user != null) {
//            // do your stuff
//        } else {
//            signInAnonymously();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length>0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Part1Prob.this, "Audio 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(Part1Prob.this, "Audio 권한을 사용자가 거부함.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Part1Prob.this,"Audio 권한을 부여받지 못", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1_prob);

        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);


        // initialize DB
        mTestDBHelper = new TestDBHelper(Part1Prob.this);

        int permissionCheck = ContextCompat.checkSelfPermission(Part1Prob.this, Manifest.permission.RECORD_AUDIO);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(Part1Prob.this,"Audio 권한 있음.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(Part1Prob.this, "Audio 권한 없음.", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(Part1Prob.this, Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(Part1Prob.this, "Audio 권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(Part1Prob.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }

        verifyStoragePermissions(this);

        record = (ImageButton)findViewById(R.id.record);
        stop = (Button)findViewById(R.id.btn_submit);
        play = (Button)findViewById(R.id.play);
        next = (Button)findViewById(R.id.btn_next);

        // 안드로이드폰 ID
        String idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // 현재 날짜
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("/yyyyMMdd_hhmmss");
        String getTime = simpleDate.format(mDate);

        // outputFile = getExternalCacheDir().getAbsolutePath();
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();   // 내부저장소에 저장되는 경로
        outputFile += getTime;
        outputFile += "_test_part1_";
        outputFile += idByANDROID_ID;
        outputFile += ".3gp";

        //for audio recording
        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
        mAudioRecord.startRecording();

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);

        mRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] readData = new byte[mBufferSize];
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }

                while(isRecording) {
                    int ret = mAudioRecord.read(readData, 0, mBufferSize);
                    Log.d(TAG, "read bytes is " + ret);

                    try {
                        fos.write(readData, 0, mBufferSize);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mPlayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] writeData = new byte[mBufferSize];
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(outputFile);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                DataInputStream dis = new DataInputStream(fis);
                mAudioTrack.play();

                while(isPlaying) {
                    try {
                        int ret = dis.read(writeData, 0, mBufferSize);
                        if (ret <= 0) {
                            (Part1Prob.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isPlaying = false;
                                    play.setText("Play");
                                }
                            });

                            break;
                        }
                        mAudioTrack.write(writeData, 0, ret);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                mAudioTrack.stop();
                mAudioTrack.release();
                mAudioTrack = null;

                try {
                    dis.close();
                    fis.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




        // flask 통신
        OkHttpClient okHttpClient = new OkHttpClient();

        // POST TEST
        RequestBody formbody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("android_id",idByANDROID_ID)
                .addFormDataPart("url", outputFile)
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


        System.out.println("==================="+outputFile+"=====================");

//        record.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                recordAudio();
//                Toast.makeText(Part1Prob.this,"Recording started...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playAudio();
//                Toast.makeText(Part1Prob.this,"재생 시작", Toast.LENGTH_SHORT).show();
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                Intent intent = new Intent(Part1Prob.this, Part2Prob.class);
                startActivity(intent);
                finish();
            }
        });

         //for Countdown
        Toast.makeText(Part1Prob.this,"응답을 준비하세요.",Toast.LENGTH_SHORT).show();
        textViewCountDown = findViewById(R.id.tv_countdown);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
                System.out.println("Firebase SignIn Success");
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public void uploadAudio(){

        mProgress.setMessage("Uploading Audio...");
        mProgress.show();

        StorageReference filepath = mStorage.child("Audio").child("new_audio.3gp");

        Uri uri = Uri.fromFile(new File(outputFile));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                tvMainText.setText("Uploading Finished");

                Toast.makeText(Part1Prob.this,"Uploading Finished", Toast.LENGTH_SHORT).show();
            }
        });

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
                finish();
                Toast.makeText(Part1Prob.this, "응답시간이 초과했습니다.", Toast.LENGTH_SHORT).show();
                // stopRecording();
                Intent intent = new Intent(Part1Prob.this, Part2Prob.class);
                startActivity(intent);
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
            Toast.makeText(Part1Prob.this, "응답을 시작하세요!", Toast.LENGTH_SHORT).show();
            // recordAudio();
        }
    }

    public void onRecord(View view) {
        if(isRecording == true) {
            isRecording = false;
            Toast.makeText(Part1Prob.this, "녹음 시작!", Toast.LENGTH_SHORT).show();
        }
        else {
            isRecording = true;
            Toast.makeText(Part1Prob.this, "녹음멈춤!", Toast.LENGTH_SHORT).show();

            if(mAudioRecord == null) {
                mAudioRecord =  new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
                mAudioRecord.startRecording();
            }
            mRecordThread.start();
        }

    }

    public void onPlay(View view) {
        if(isPlaying == true) {
            isPlaying = false;
            play.setText("Play");
        }
        else {
            isPlaying = true;
            play.setText("Stop");

            if(mAudioTrack == null) {
                mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
            }
            mPlayThread.start();
        }

    }


}
