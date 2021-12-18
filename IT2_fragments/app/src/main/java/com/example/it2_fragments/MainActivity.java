package com.example.it2_fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity OnCreate");
        setContentView(R.layout.activity_main);
        setContentView(R.layout.main_activity);
        frag1 = new Fragment1();
        frag2 = new Fragment2();
        chbStack = (CheckBox)findViewById(R.id.chbStack);
    }
    public void onStart(){
        super.onStart();
        Log.d(LOG_TAG, "MainActivity OnStart");
    }

    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "MainActivity OnResume");
    }

    public void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "MainActivity OnPause");
    }

    public void onStop(){
        super.onStop();
        Log.d(LOG_TAG, "MainActivity OnStop");
    }
    public void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "MainActivity OnDestroy");
    }

    Fragment1 frag1;
    Fragment2 frag2;
    CheckBox chbStack;

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClick(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fTrans = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btnAdd:
                fTrans.add(R.id.frgmCont, frag1);
                break;
            case R.id.btnRemove:
                fTrans.remove(frag1);
                break;
            case R.id.btnReplace:
                fTrans.replace(R.id.frgmCont, frag2);
                fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                break;
            case R.id.btnFind:
                frag1 = (Fragment1) fragmentManager.findFragmentById(R.id.fragmentContainerView);
                ((TextView) frag1.getView().findViewById(R.id.tvF1))
                        .setText("Acces to fragment 1 from Activity");
                frag2 = (Fragment2) fragmentManager.findFragmentById(R.id.fragmentContainerView2);
                ((TextView) frag2.getView().findViewById(R.id.tvF2))
                        .setText("Acces to fragment 2 from Activity");
                break;
        }
//        if(chbStack.isChecked()) fTrans.addToBackStack(null);
        fTrans.commit();
    }
}