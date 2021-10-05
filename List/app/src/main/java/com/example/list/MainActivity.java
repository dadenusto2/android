package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ll);
        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(
                ((Button) findViewById(R.id.bAddStudent)).getVisibility()
        );
    }

    public void createList1(View view){
        String[] catNames = new String[] {
                    "Рыжик", "Барсик", "Мурзик", "Мурка",
                    "Томасик", "Кристина", "Пушок", "Дымка","Кузя",
                    "Китти", "Масяня", "Симба"
        };

        ListView listview = findViewById(R.id.lvList1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, catNames);

        listview.setAdapter(adapter);

        /*
        ((ListView) findViewById(R.id.lvList1)).setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, catNames)
        );*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View itemClicked, int posiion, long id) {
                Toast.makeText(getApplicationContext(),
                        "Нажато '"+((TextView) itemClicked).getText()+"'",
                Toast.LENGTH_SHORT).show();
            }
        });
    }

    ArrayList<Student> mStudents;
    StudentListAdapter mStudentListAdapter;

    public void createStudentList(View view){

        mStudents = new ArrayList<>();
        ListView listView = findViewById(R.id.lvList2);
        mStudentListAdapter = new StudentListAdapter(mStudents, this);
        listView.setAdapter(mStudentListAdapter);

        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bAddStudent)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bCreateStudentList)).setVisibility(View.GONE);
    }

    public void addStudent(View view) {
        mStudents.add(new Student(
                ((EditText) findViewById(R.id.editFIO)).getText().toString(),
                ((EditText) findViewById(R.id.editFacultet)).getText().toString(),
                ((EditText) findViewById(R.id.editGroup)).getText().toString()
        ));
        ((EditText) findViewById(R.id.editFIO)).setText("");
        ((EditText) findViewById(R.id.editFacultet)).setText("");
        ((EditText) findViewById(R.id.editGroup)).setText("");
        mStudentListAdapter.notifyDataSetChanged();
    }

    public void showAllStudentsByFaculty(View view) {
        mStudentListAdapter.setChooseFaculty(((TextView) view.findViewById(R.id.tvElementFacultet)).getText().toString().trim());
        mStudentListAdapter.notifyDataSetChanged();
    }
}