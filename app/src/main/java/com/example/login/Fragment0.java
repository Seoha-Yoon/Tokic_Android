package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class Fragment0 extends Fragment {
    public Fragment0(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fv =inflater.inflate(R.layout.fragment0,container,false);
        Button btn_mk_test = fv.findViewById(R.id.btn_mk_test);

        btn_mk_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MockTestActivity.class);
                startActivity(intent);
            }
        });

        return fv;
    }
}
