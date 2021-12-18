package com.example.it2_fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    final String LOG_TAG = "myLogs";

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 OnCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Fragment1 OnCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_1, container, false);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 OnAttach");
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 OnActivityCreated");
    }

    public void onStart(){
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 OnStart");
    }

    public void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "Fragment1 OnResume");
    }

    public void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "Fragment1 OnPause");
    }

    public void onStop(){
        super.onStop();
        Log.d(LOG_TAG, "Fragment1 OnStop");
    }
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(LOG_TAG, "Fragment1 OnDestroyView");
    }
    public void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, "Fragment1 OnDestroy");
    }
    public void onDetach(){
        super.onDetach();
        Log.d(LOG_TAG, "Fragment1 OnDetach");
    }
}