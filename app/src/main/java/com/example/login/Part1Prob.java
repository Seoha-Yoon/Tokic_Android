package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
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


public class Part1Prob extends AppCompatActivity {

    private static final Object BASE_URL = "http://3.139.81.205:5000/";
    public static final String test_or_verify = "test";
    public static final String part = "part_1";

    // countdown
    TextView textViewCountDown;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private long timeLeftInMillis;
    CountDownTimer countDownTimer;

    // DB
    private TestDBHelper mTestDBHelper;

    // upload audio
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private OkHttpClient okHttpClient;
    private String idByANDROID_ID;

    private String outputFile = null;
    private String outputUri = null;

    // POST
    String getTime;

    //Recording & Playing
    MediaPlayer player;
    MediaRecorder audioRecorder;
    Uri audiouri;
    ParcelFileDescriptor file;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1_prob);

        // 안드로이드폰 ID
        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // 현재 날짜
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("/yyyyMMdd_hhmmss");
        getTime = simpleDate.format(mDate);

        permissionCheck();

//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();   // 내부저장소에 저장되는 경로
//
//        outputUri = getTime;
//        outputUri += "_test_part1_";
//        outputUri += idByANDROID_ID;
//        outputUri += ".pcm";
//
//        outputFile += outputUri;

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


    private void recordAudio() {

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "No1."+idByANDROID_ID+getTime+"test.mp3");
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/TOKIC/");


        outputFile = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "No1."+idByANDROID_ID+getTime+"test.mp3";






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

            System.out.println("ㅇㅕ기야 여기 ");
            System.out.println(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            System.out.println(values);
            System.out.println(audiouri);
            System.out.println(file.getFileDescriptor());


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

            // firebase로 audio파일 저장하는 코드 -> 오류나면 이부분 주석처리할 것
            uploadAudio();

        }
    }

    private void playAudio() {
        try {
            closePlayer();

            FileInputStream fis = new FileInputStream(file.getFileDescriptor());
            player = new MediaPlayer();
            player.setDataSource(fis.getFD());
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
                finish();
                Toast.makeText(Part1Prob.this, "응답시간이 초과했습니다.", Toast.LENGTH_SHORT).show();
                stopAudio();
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


    private void uploadAudio(){
        // firebase
        mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference filepath = mStorage.child("Audio").child(outputUri);


        Uri uri = Uri.fromFile(new File(outputFile));

        System.out.println(outputFile);
        System.out.println(uri);
        System.out.println(filepath);

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();
                Toast.makeText(Part1Prob.this,"Uploading Finished", Toast.LENGTH_SHORT).show();

                // flask 통신
                okHttpClient = new OkHttpClient();

                // POST
                RequestBody formbody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("android_id",idByANDROID_ID)
                        .addFormDataPart("test_or_verify",test_or_verify)
                        .addFormDataPart("part",part)
                        .addFormDataPart("url", outputUri)
                        .addFormDataPart("date_time", getTime)
                        .build();

                Request req = new Request.Builder()
                        .url(BASE_URL + "post")
                        .post(formbody)
                        .build();

                okHttpClient.newCall(req).enqueue(new Callback() {

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        System.out.println(response.body().string());
                        System.out.println("여기야여기");
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("fail");
                        System.out.println("실패했나");

                    }
                });
            }
        });

    }



}










//
//
//
//    // voice recording
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private String outputFile = null;
//    private String outputUri = null;
//
//
//    private int mAudioSource = MediaRecorder.AudioSource.MIC;
//    private int mSampleRate = 8000;
//    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO;
//    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
//    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);
//
//    public AudioRecord mAudioRecord = null;
//
//    public Thread mRecordThread = null;
//    public boolean isRecording = false;
//
//    public AudioTrack mAudioTrack = null;
//    public Thread mPlayThread = null;
//    public boolean isPlaying = false;
//
//    private ImageButton record;
//    private Button stop, play, next;
//
//    // countdown
//    TextView textViewCountDown;
//    private static final long COUNTDOWN_IN_MILLIS = 30000;
//    private long timeLeftInMillis;
//    CountDownTimer countDownTimer;
//
//    // DB
//    private TestDBHelper mTestDBHelper;
//
//    // upload video
//    private StorageReference mStorage;
//    private ProgressDialog mProgress;
//    private OkHttpClient okHttpClient;
//    private String idByANDROID_ID;
//
//    // POST
//    String getTime;
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode){
//            case REQUEST_RECORD_AUDIO_PERMISSION:
//                if(grantResults.length>0) {
//                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(Part1Prob.this, "Audio 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
//                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                        Toast.makeText(Part1Prob.this, "Audio 권한을 사용자가 거부함.", Toast.LENGTH_LONG).show();
//                    }
//                }else{
//                    Toast.makeText(Part1Prob.this,"Audio 권한을 부여받지 못", Toast.LENGTH_LONG).show();
//                }
//                break;
//        }
//    }
//
//    // Storage Permissions
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//
//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_part1_prob);
//
//
//        // firebase
//        mStorage = FirebaseStorage.getInstance().getReference();
//        mProgress = new ProgressDialog(this);
//
//
//        // initialize DB
//        mTestDBHelper = new TestDBHelper(Part1Prob.this);
//
//        int permissionCheck = ContextCompat.checkSelfPermission(Part1Prob.this, Manifest.permission.RECORD_AUDIO);
//
//        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(Part1Prob.this,"Audio 권한 있음.", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(Part1Prob.this, "Audio 권한 없음.", Toast.LENGTH_LONG).show();
//            if (ActivityCompat.shouldShowRequestPermissionRationale(Part1Prob.this, Manifest.permission.RECORD_AUDIO)) {
//                Toast.makeText(Part1Prob.this, "Audio 권한 설명 필요함.", Toast.LENGTH_LONG).show();
//            } else {
//                ActivityCompat.requestPermissions(Part1Prob.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
//            }
//        }
//
//        verifyStoragePermissions(this);
//
//        // 버튼
//        record = (ImageButton)findViewById(R.id.record);
//        stop = (Button)findViewById(R.id.btn_submit);
//        play = (Button)findViewById(R.id.play);
//        next = (Button)findViewById(R.id.btn_next);
//
//
//
//        // 안드로이드폰 ID
//        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        // 현재 날짜
//        long now = System.currentTimeMillis();
//        Date mDate = new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("/yyyyMMdd_hhmmss");
//        getTime = simpleDate.format(mDate);
//
//        // outputFile = getExternalCacheDir().getAbsolutePath();
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();   // 내부저장소에 저장되는 경로
//
//        outputUri = getTime;
//        outputUri += "_test_part1_";
//        outputUri += idByANDROID_ID;
//        outputUri += ".pcm";
//
//        outputFile += outputUri;
//
//
//
//        //for audio recording
//        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
//        mAudioRecord.startRecording();
//
//        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
//
//        mRecordThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] readData = new byte[mBufferSize];
//                FileOutputStream fos = null;
//                try {
//                    fos = new FileOutputStream(outputFile);
//                } catch(FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                while(isRecording) {
//                    int ret = mAudioRecord.read(readData, 0, mBufferSize);
//                    Log.d(TAG, "read bytes is " + ret);
//
//                    try {
//                        fos.write(readData, 0, mBufferSize);
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//
//                mAudioRecord.stop();
//                mAudioRecord.release();
//                mAudioRecord = null;
//
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mPlayThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] writeData = new byte[mBufferSize];
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(outputFile);
//                }catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                DataInputStream dis = new DataInputStream(fis);
//                mAudioTrack.play();
//
//                while(isPlaying) {
//                    try {
//                        int ret = dis.read(writeData, 0, mBufferSize);
//                        if (ret <= 0) {
//                            (Part1Prob.this).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    isPlaying = false;
//                                    play.setText("Play");
//                                }
//                            });
//
//                            break;
//                        }
//                        mAudioTrack.write(writeData, 0, ret);
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                mAudioTrack.stop();
//                mAudioTrack.release();
//                mAudioTrack = null;
//
//                try {
//                    dis.close();
//                    fis.close();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//
//
//
////        record.setOnClickListener(new View.OnClickListener(){
////            @Override
////            public void onClick(View v) {
////                recordAudio();
////                Toast.makeText(Part1Prob.this,"Recording started...", Toast.LENGTH_SHORT).show();
////            }
////        });
////
////        play.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                playAudio();
////                Toast.makeText(Part1Prob.this,"재생 시작", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                countDownTimer.cancel();
//                Intent intent = new Intent(Part1Prob.this, Part2Prob.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//         //for Countdown
//        Toast.makeText(Part1Prob.this,"응답을 준비하세요.",Toast.LENGTH_SHORT).show();
//        textViewCountDown = findViewById(R.id.tv_countdown);
//        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
//        startCountDown();
//
//    }
//
//
//    public void uploadAudio(){
//
//        mProgress.setMessage("Uploading Audio...");
//        mProgress.show();
//
//        StorageReference filepath = mStorage.child("Audio").child(outputUri);
//
//        Uri uri = Uri.fromFile(new File(outputFile));
//
//        System.out.println(outputFile);
//        System.out.println(uri);
//        System.out.println(filepath);
//
//        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                mProgress.dismiss();
//                Toast.makeText(Part1Prob.this,"Uploading Finished", Toast.LENGTH_SHORT).show();
//
//                // flask 통신
//                okHttpClient = new OkHttpClient();
//
//                // POST
//                RequestBody formbody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("android_id",idByANDROID_ID)
//                        .addFormDataPart("test_or_verify",test_or_verify)
//                        .addFormDataPart("part",part)
//                        .addFormDataPart("url", outputUri)
//                        .addFormDataPart("date_time", getTime)
//                        .build();
//
//                Request req = new Request.Builder()
//                        .url(BASE_URL + "post")
//                        .post(formbody)
//                        .build();
//
//                okHttpClient.newCall(req).enqueue(new Callback() {
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        System.out.println(response.body().string());
//                        System.out.println("여기야여기");
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        System.out.println("fail");
//                        System.out.println("실패했나");
//
//                    }
//                });
//            }
//        });
//
//    }
//
//
//    private void startCountDown(){
//        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMillis = millisUntilFinished;
//                updateCountDownText();
//            }
//
//            @Override
//            public void onFinish() {
//                timeLeftInMillis = 0;
//                updateCountDownText();
//                finish();
//                Toast.makeText(Part1Prob.this, "응답시간이 초과했습니다.", Toast.LENGTH_SHORT).show();
//                // stopRecording();
//                Intent intent = new Intent(Part1Prob.this, Part2Prob.class);
//                startActivity(intent);
//            }
//        }.start();
//    }
//
//
//    private void updateCountDownText(){
//        int minutes = (int) (timeLeftInMillis / 1000) / 60;
//        int seconds = (int) (timeLeftInMillis / 1000) % 60;
//
//        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);
//
//        textViewCountDown.setText(timeFormatted);
//
//        // Start Audio Recording
//        if(seconds == 20) {
//            //Toast.makeText(Part1Prob.this, "응답을 시작하세요!", Toast.LENGTH_SHORT).show();
//            // recordAudio();
//        }
//    }
//
//    public void onRecord(View view) {
//        if(isRecording == true) {
//            isRecording = false;
//            Toast.makeText(Part1Prob.this, "녹음 멈춤!", Toast.LENGTH_SHORT).show();
//            uploadAudio();
//        }
//        else {
//            isRecording = true;
//            Toast.makeText(Part1Prob.this, "녹음 시작!", Toast.LENGTH_SHORT).show();
//
//            if(mAudioRecord == null) {
//                mAudioRecord =  new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
//                mAudioRecord.startRecording();
//            }
//            mRecordThread.start();
//        }
//
//    }
//
//    public void onPlay(View view) {
//        if(isPlaying == true) {
//            isPlaying = false;
//            play.setText("Play");
//        }
//        else {
//            isPlaying = true;
//            play.setText("Stop");
//
//            if(mAudioTrack == null) {
//                mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
//            }
//            mPlayThread.start();
//        }
//
//    }

