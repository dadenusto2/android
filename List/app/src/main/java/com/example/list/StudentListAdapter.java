package com.example.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentListAdapter extends BaseAdapter {
    ArrayList<Student> mStudents = new ArrayList<>();
    Context mContext;
    LayoutInflater mInflater;

    public StudentListAdapter(ArrayList<Student> students, Context context) {
        mStudents = students;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mStudents.size();
    }

    public Object getItem(int position) {
        return mStudents.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.student_element, parent, false);
        if (mStudents.isEmpty()) return view;

        ((TextView) view.findViewById(R.id.tvElementFIO)).setText(mStudents.get(position).getFIO());
        ((TextView) view.findViewById(R.id.tvElementFacultet)).setText(mStudents.get(position).getFacultet());
        ((TextView) view.findViewById(R.id.tvElementGroup)).setText(mStudents.get(position).getGroup());

        if (position % 2 == 1)
            ((LinearLayout) view.findViewById(R.id.llElement)).setBackgroundColor(
                    mContext.getResources().getColor(R.color.odd_element)
            );

        return view;
    }
}
