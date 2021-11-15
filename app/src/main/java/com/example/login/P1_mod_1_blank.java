package com.example.login;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;

public class P1_mod_1_blank extends Fragment {

    // for making problems
    TextView Q1, Q2, Q3, Q4, Q5, Q6, Q1_rec, Q2_rec, Q3_rec, Q4_rec, Q5_rec, Q6_rec;
    ArrayList<String> blank_Q1 = new ArrayList();
    ArrayList<String> blank_Q2 = new ArrayList();
    ArrayList<String> blank_Q3 = new ArrayList();
    ArrayList<String> blank_Q4 = new ArrayList();
    ArrayList<String> blank_Q5 = new ArrayList();
    ArrayList<String> blank_Q6 = new ArrayList();


    // for checking answer
    HashSet<String> answerSet = new HashSet<>();
    final int PERMISSION = 1;
    SpeechRecognizer mRecognizer;
    ImageButton stt1, stt2, stt3, stt4, stt5, stt6;
    Intent intent;
    String ans;
    int questionNum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static String convertArrayListToStringAppend(ArrayList<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : strings) {
            stringBuilder.append(item);
        }
        String strAppend = stringBuilder.toString();
        return strAppend;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.activity_p1_mod1_blank, container, false);
        ans = "";
        questionNum = 0;

        int a;
        Q1 = fv.findViewById(R.id.blank1);
        Q2 = fv.findViewById(R.id.blank2);
        Q3 = fv.findViewById(R.id.blank3);
        Q4 = fv.findViewById(R.id.blank4);
        Q5 = fv.findViewById(R.id.blank5);
        Q6 = fv.findViewById(R.id.blank6);

        Q1_rec = fv.findViewById(R.id.blank1_ans);
        Q2_rec = fv.findViewById(R.id.blank2_ans);
        Q3_rec = fv.findViewById(R.id.blank3_ans);
        Q4_rec = fv.findViewById(R.id.blank4_ans);
        Q5_rec = fv.findViewById(R.id.blank5_ans);
        Q6_rec = fv.findViewById(R.id.blank6_ans);

        blank_Q1.add("제 취미는 ");
        blank_Q1.add("영화 보기예요.");

        a = (int) (Math.random() * blank_Q1.size());
        blank_Q1.add(a, "_____ ");
        answerSet.add(blank_Q1.remove(a + 1));
        Q1.setText(convertArrayListToStringAppend(blank_Q1));


        blank_Q2.add("저는 ");
        blank_Q2.add("시간 있을 때 ");
        blank_Q2.add("영화관에 ");
        blank_Q2.add("가요. ");

        a = (int) (Math.random() * blank_Q2.size());
        blank_Q2.add(a, "_____ ");
        answerSet.add(blank_Q2.remove(a + 1));
        Q2.setText(convertArrayListToStringAppend(blank_Q2));


        blank_Q3.add("집에서도 ");
        blank_Q3.add("영화를 ");
        blank_Q3.add("자주 ");
        blank_Q3.add("봐요. ");

        a = (int) (Math.random() * blank_Q3.size());
        blank_Q3.add(a, "_____ ");
        answerSet.add(blank_Q3.remove(a + 1));
        Q3.setText(convertArrayListToStringAppend(blank_Q3));


        blank_Q4.add("저는 ");
        blank_Q4.add("재미있는 ");
        blank_Q4.add("영화를 ");
        blank_Q4.add("좋아해요. ");

        a = (int) (Math.random() * blank_Q4.size());
        blank_Q4.add(a, "_____ ");
        answerSet.add(blank_Q4.remove(a + 1));
        Q4.setText(convertArrayListToStringAppend(blank_Q4));


        blank_Q5.add("슬픈 ");
        blank_Q5.add("영화도 ");
        blank_Q5.add("잘 봐요 ");

        a = (int) (Math.random() * blank_Q5.size());
        blank_Q5.add(a, "_____ ");
        answerSet.add(blank_Q5.remove(a + 1));
        Q5.setText(convertArrayListToStringAppend(blank_Q5));

        blank_Q6.add("저는 ");
        blank_Q6.add("이번 주말에 ");
        blank_Q6.add("친구와 ");
        blank_Q6.add("함께 ");
        blank_Q6.add("영화관에 ");
        blank_Q6.add("갈거예요. ");

        a = (int) (Math.random() * blank_Q6.size());
        blank_Q6.add(a, "_____ ");
        answerSet.add(blank_Q6.remove(a + 1));
        Q6.setText(convertArrayListToStringAppend(blank_Q6));

        // 퍼미션 체크
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        stt1 = fv.findViewById(R.id.stt1);
        stt2 = fv.findViewById(R.id.stt2);
        stt3 = fv.findViewById(R.id.stt3);
        stt4 = fv.findViewById(R.id.stt4);
        stt5 = fv.findViewById(R.id.stt5);
        stt6 = fv.findViewById(R.id.stt6);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        stt1.setOnClickListener(v -> {
            Q1_rec.setText("");
            ans = "제 취미는 영화 보기예요";
            questionNum = 1;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        stt2.setOnClickListener(v -> {
            Q2_rec.setText("");
            ans = "저는 시간 있을 때 영화관에 가요";
            questionNum = 2;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        stt3.setOnClickListener(v -> {
            Q3_rec.setText("");
            ans = "집에서도 영화를 자주 봐요";
            questionNum = 3;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        stt4.setOnClickListener(v -> {
            Q4_rec.setText("");
            ans = "저는 재미있는 영화를 좋아해요.";
            questionNum = 4;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        stt5.setOnClickListener(v -> {
            Q5_rec.setText("");
            ans = "슬픈 영화도 잘 봐요.";
            questionNum = 5;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        stt6.setOnClickListener(v -> {
            Q6_rec.setText("");
            ans = "저는 이번 주말에 친구와 함께 영화관에 갈거에요.";
            questionNum = 6;
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        return fv;
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Toast.makeText(getActivity(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "Recognizer가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }

            Toast.makeText(getActivity(), "에러가 발생하였습니다.: " + message, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            //Log.d("MyApp", Integer.toString(matches.get(0).length()));

            SpannableStringBuilder builder = new SpannableStringBuilder();
            for(int i=0, j=0; i<matches.get(0).length() && j<ans.length(); i++, j++) {

                // check blank or not
                if(ans.charAt(j)==' '){
                    SpannableString str = new SpannableString(" ");
                    str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str.length(), 0);
                    builder.append(str);
                    i--;
                    continue;
                }

                if(matches.get(0).charAt(i)==' ') i++;
                // check out of bound or not
                if(j>=ans.length() || i>=matches.get(0).length()) break;

                SpannableString str = new SpannableString(Character.toString(matches.get(0).charAt(i)));

                if(ans.charAt(j) == matches.get(0).charAt(i)){
                    str.setSpan(new ForegroundColorSpan(Color.BLUE), 0, str.length(), 0);
                }
                else {
                    str.setSpan(new ForegroundColorSpan(Color.RED), 0, str.length(), 0);
                }
                builder.append(str);


            }

            switch (questionNum){
                case 1:
                    Q1_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
                case 2:
                    Q2_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
                case 3:
                    Q3_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
                case 4:
                    Q4_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
                case 5:
                    Q5_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
                case 6:
                    Q6_rec.setText(builder, TextView.BufferType.SPANNABLE);
                    break;
            }

        }

        @Override
        public void onPartialResults(Bundle results) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }


    };
}
