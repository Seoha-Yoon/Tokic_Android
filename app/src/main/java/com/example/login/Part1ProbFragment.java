package com.example.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;


public class Part1ProbFragment extends Fragment {

    // voice recording
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder recorder;
    private String outputFile;

    // playing voice
    private MediaPlayer player;
    private int position = 0;

    private ImageButton record;
    private Button stop, play;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length>0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Audio 권한을 사용자가 승인함.", Toast.LENGTH_LONG).show();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getActivity(), "Audio 권한을 사용자가 거부함.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(),"Audio 권한을 부여받지 못", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_part1_prob, container, false);

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);

        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(),"Audio 권한 있음.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(),"Audio 권한 없음.", Toast.LENGTH_LONG).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(getActivity(),"Audio 권한 설명 필요함.", Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }

        record = (ImageButton)rootView.findViewById(R.id.record);
        stop = (Button)rootView.findViewById(R.id.btn_submit);
        play = (Button)rootView.findViewById(R.id.play);

        outputFile = getActivity().getExternalCacheDir().getAbsolutePath();
        outputFile += "/audiorecordtest.3gp";

        record.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                recordAudio();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
                Toast.makeText(getActivity(),"재생 시작", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void recordAudio(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(outputFile);

        try{
            recorder.prepare();

            Toast.makeText(getActivity(),"녹음 시작", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            e.printStackTrace();
        }
        recorder.start();
    }

    public void stopRecording(){
        if(recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;

            Toast.makeText(getActivity(),"녹음 중지", Toast.LENGTH_SHORT).show();
        }
    }

    public void playAudio(){
        try {
            closePlayer();

            player = new MediaPlayer();
            player.setDataSource(outputFile);
            player.prepare();
            player.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closePlayer(){
        if(player !=null){
            player.release();
            player = null;
        }
    }

}