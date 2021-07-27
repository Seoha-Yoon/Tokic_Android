package com.example.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class P1_mod_1_blank extends Fragment {

    TextView Q1,Q2,Q3,Q4,Q5,Q6;
    ArrayList blank_Q1 = new ArrayList();
    ArrayList blank_Q2 = new ArrayList();
    ArrayList blank_Q3 = new ArrayList();
    ArrayList blank_Q4 = new ArrayList();
    ArrayList blank_Q5 = new ArrayList();
    ArrayList blank_Q6 = new ArrayList();



    public static P1_mod_1_blank newInstance(){
        return new P1_mod_1_blank();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static String convertArrayListToStringAppend(ArrayList<String> strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String item : strings) { stringBuilder.append(item); }
        String strAppend = stringBuilder.toString();
        return strAppend; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.activity_p1_mod1_blank, container, false);

        int a;
        Q1 = fv.findViewById(R.id.blank1);
        Q2 = fv.findViewById(R.id.blank2);
        Q3 = fv.findViewById(R.id.blank3);
        Q4 = fv.findViewById(R.id.blank4);
        Q5 = fv.findViewById(R.id.blank5);
        Q6 = fv.findViewById(R.id.blank6);


        blank_Q1.add("제 취미는 ");
        blank_Q1.add("영화 보기예요.");

        a = (int)(Math.random()*blank_Q1.size());
        blank_Q1.add(a,"(    ) ");
        blank_Q1.remove(a+1);
        Q1.setText(convertArrayListToStringAppend(blank_Q1));


        blank_Q2.add("저는 ");
        blank_Q2.add("시간 있을 때 ");
        blank_Q2.add("영화관에 ");
        blank_Q2.add("가요. ");

        a = (int)(Math.random()*blank_Q2.size());
        blank_Q2.add(a,"(    ) ");
        blank_Q2.remove(a+1);
        Q2.setText(convertArrayListToStringAppend(blank_Q2));


        blank_Q3.add("집에서도 ");
        blank_Q3.add("영화를 ");
        blank_Q3.add("자주 ");
        blank_Q3.add("봐요. ");

        a = (int)(Math.random()*blank_Q3.size());
        blank_Q3.add(a,"(    ) ");
        blank_Q3.remove(a+1);
        Q3.setText(convertArrayListToStringAppend(blank_Q3));


        blank_Q4.add("저는 ");
        blank_Q4.add("재미있는 ");
        blank_Q4.add("영화를 ");
        blank_Q4.add("좋아해요. ");

        a = (int)(Math.random()*blank_Q4.size());
        blank_Q4.add(a,"(    ) ");
        blank_Q4.remove(a+1);
        Q4.setText(convertArrayListToStringAppend(blank_Q4));


        blank_Q5.add("슬픈 ");
        blank_Q5.add("영화도 ");
        blank_Q5.add("잘 봐요 ");

        a = (int)(Math.random()*blank_Q5.size());
        blank_Q5.add(a,"(    ) ");
        blank_Q5.remove(a+1);
        Q5.setText(convertArrayListToStringAppend(blank_Q5));

        blank_Q6.add("저는 ");
        blank_Q6.add("이번 주말에 ");
        blank_Q6.add("친구와 ");
        blank_Q6.add("함께 ");
        blank_Q6.add("영화관에 ");
        blank_Q6.add("갈거예요. ");

        a = (int)(Math.random()*blank_Q6.size());
        blank_Q6.add(a,"(    ) ");
        blank_Q6.remove(a+1);
        Q6.setText(convertArrayListToStringAppend(blank_Q6));


        return fv;
    }


}
