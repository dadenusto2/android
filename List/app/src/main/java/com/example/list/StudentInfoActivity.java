package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class StudentInfoActivity extends AppCompatActivity {

    private SubjectListAdapter mSubjectListAdapter;
    private Student s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        Student s = getIntent().getParcelableExtra("student");
        ((TextView) findViewById(R.id.tvASI_FIO)).setText(s.getFIO());
        ((TextView) findViewById(R.id.tvASI_Facultet)).setText(s.getFacultet());
        ((TextView) findViewById(R.id.tvASI_Group)).setText(s.getGroup());

        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(), StudentInfoActivity.this);
        ((ListView) findViewById(R.id.lvASI_Subjects)).setAdapter(mSubjectListAdapter);
    }

    public void addSubject(View view) {
        s.addSubject(new Subject(
                ((EditText) findViewById(R.id.editASI_SubjectName)).getText().toString(),
                Integer.parseInt(((EditText) findViewById(R.id.editASI_Mark)).getText().toString())
        ));
        mSubjectListAdapter.notifyDataSetChanged();
    }

    public void onExit(View view){
        finish();
    }

    public void onSave(View view) {
        Intent intent = new Intent();
        intent.putExtra("student", s);
        setResult(RESULT_OK, intent);
        finish();
    }

}