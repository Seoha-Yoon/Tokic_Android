package com.example.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.ListFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class P1_module extends Fragment {

    public static P1_module newInstance(){
        return new P1_module();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv = inflater.inflate(R.layout.p1_module, container, false);

        ImageButton btn_P1_mod1=fv.findViewById(R.id.btn_play1);
        ImageButton btn_P1_mod2=fv.findViewById(R.id.btn_play2);
        ImageButton btn_P1_mod3=fv.findViewById(R.id.btn_play3);

        btn_P1_mod1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_P1_mod1.setBackgroundResource(R.drawable.play_button2);

                Intent intent = new Intent(getActivity(), P1_mod_1.class);
                startActivity(intent);

            }
        });

        btn_P1_mod2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_P1_mod2.setBackgroundResource(R.drawable.play_button2);
                Intent intent = new Intent(getActivity(), P1_mod_2.class);
                startActivity(intent);

            }
        });

        btn_P1_mod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_P1_mod3.setBackgroundResource(R.drawable.play_button2);
                Intent intent = new Intent(getActivity(), P1_mod_3.class);
                startActivity(intent);

            }
        });

        return fv;
    }

}
