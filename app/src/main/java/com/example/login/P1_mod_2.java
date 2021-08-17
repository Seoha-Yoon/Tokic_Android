package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P1_mod_2 extends AppCompatActivity {

    Button sen1,sen2,sen3,btn_submit,btn_eraser;
    String txt1,txt2,txt3;
    EditText note;

    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p1_mod2);

        btn_back=findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(P1_mod_2.this, P1_main.class);
                startActivity(intent);
            }
        });


        ArrayList<String> l = new ArrayList<>();
        ArrayList<String> answer = new ArrayList<>();
        ArrayList<String> key = new ArrayList<>();

        note = (EditText)findViewById(R.id.note);

        sen1 = (Button)findViewById(R.id.sentence1);
        sen2 = (Button)findViewById(R.id.sentence2);
        sen3 = (Button)findViewById(R.id.sentence3);

        btn_submit = (Button)findViewById(R.id.btn_sbm);
        btn_eraser = (Button)findViewById(R.id.btn_eraser);

        txt1 = "저의 취미는 영화보기에요.";
        txt2 = "저는 시간 있을 때 영화관에 가요.";
        txt3 = "집에서도 영화를 자주 봐요.";

        key.add(txt1);
        key.add(txt2);
        key.add(txt3);

        l.add(txt1);
        l.add(txt2);
        l.add(txt3);

        Collections.shuffle(l);

        sen1.setText(l.get(0));
        sen2.setText(l.get(1));
        sen3.setText(l.get(2));

            sen1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answer.size()<key.size()){
                        note.setText(note.getText() + "A");
                        answer.add(l.get(0));
                    }
                }
            });

            sen2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answer.size()<key.size()){
                    note.setText(note.getText() + "B");
                    answer.add(l.get(1));
                    }
                }
            });

            sen3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(answer.size()<key.size()){
                        note.setText(note.getText() + "C");
                        answer.add(l.get(2));
                    }

                }
            });

        btn_submit.setVisibility(View.VISIBLE);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(answer.size()==key.size()) {
                    if (answer.equals(key)){
                        Intent intent = new Intent(P1_mod_2.this, P1_mod_2_correct.class);
                        startActivity(intent);
                    }

                    //오류 수정해야함
                    else if(!(answer.equals(key))){
                        Intent intent = new Intent(P1_mod_2.this, P1_mod_2_fail.class);
                        startActivity(intent);
                    }

                    else
                        Toast.makeText(getApplicationContext(),"NULL 오류",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"모든 문장을 순서대로 클릭하세요",Toast.LENGTH_SHORT);
                  //  toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });

        btn_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.clear();
                note.setText(" ");
            }
        });




    }
}