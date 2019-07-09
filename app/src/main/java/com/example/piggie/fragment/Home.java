package com.example.piggie.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.piggie.R;

public class Home extends Fragment {
    private View vHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vHome = inflater.inflate(R.layout.frag_home, container, false);

        Button home_mongo = vHome.findViewById(R.id.home_mongo);
        home_mongo.setOnClickListener(new FragMongo());

        Button home_sequel = vHome.findViewById(R.id.home_sequel);
        home_sequel.setOnClickListener(new FragSequel());

        Button home_report = vHome.findViewById(R.id.home_report);
        home_report.setOnClickListener(new FragReport());
        return vHome;
    }

    private class FragMongo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new Mongo()).commit();
        }
    }

    private class FragSequel implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new Sequel()).commit();
        }
    }

    private class FragReport implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new Report()).commit();
        }
    }
}
