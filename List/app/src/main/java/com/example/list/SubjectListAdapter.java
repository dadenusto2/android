package com.example.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SubjectListAdapter extends BaseAdapter {
    ArrayList<Subject> mSubject;
    Context mContext;
    LayoutInflater mInflater;

    public SubjectListAdapter(ArrayList<Subject> subject, Context context) {
        mSubject = subject;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSubject.size();
    }

    @Override
    public Object getItem(int i) {
        return mSubject.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.subject_element, parent, false);
        if (mSubject.isEmpty()) return view;

        ((TextView) view.findViewById(R.id.tvSubjectName)).setText(mSubject.get(i).getName());
        ((TextView) view.findViewById(R.id.tvSubjectMark)).setText(mSubject.get(i).getMark().toString());

        return view;
    }
}
