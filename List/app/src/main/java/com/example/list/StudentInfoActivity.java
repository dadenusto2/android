package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class StudentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_element);
//        ((TextView) findViewById(R.id.tvElementFIO)).setText(getIntent().getStringExtra("fio"));
//        ((TextView) findViewById(R.id.tvElementFacultet)).setText(getIntent().getStringExtra("facultet"));
//        ((TextView) findViewById(R.id.tvElementGroup)).setText(getIntent().getStringExtra("group"));

//        Bundle bundle = getIntent().getExtras();
//        ((TextView) findViewById(R.id.tvElementFIO)).setText(getIntent().getStringExtra("fio"));
//        ((TextView) findViewById(R.id.tvElementFacultet)).setText(getIntent().getStringExtra("facultet"));
//        ((TextView) findViewById(R.id.tvElementGroup)).setText(getIntent().getStringExtra("group"));
        Student s = getIntent().getParcelableExtra("student");
        ((TextView) findViewById(R.id.tvElementFIO)).setText(s.getFIO());
        ((TextView) findViewById(R.id.tvElementFacultet)).setText(s.getFacultet());
        ((TextView) findViewById(R.id.tvElementGroup)).setText(s.getGroup());

    }
}