package com.example.mylists;

//фио студентов и тд

/*
Новый проект
открывается список студентов
выбранный студент подсвечивает
в меню добавить удалить изменить студента
проверка ввода
по нажатию открываются предметы
меню аналогично студента
 */

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int mPosition;
    private Menu mMenu;
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        if(mStudents.size()==0){
            mMenu.findItem(R.id.stChange).setVisible(false);
            mMenu.findItem(R.id.stDelete).setVisible(false);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.miAbout:{
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
                infoDialog.setTitle("О программе");
                infoDialog.setMessage("Это программа группы 4ИТ/2!");
                infoDialog.setCancelable(false);
                infoDialog.setPositiveButton("Прочитано", null);
                infoDialog.show();
                return true;

            }
            case R.id.stAdd:{
                addStudent("", "", "");
                return true;
            }
            case R.id.stChange:{
                changeStudent(mPosition);
                return true;
            }
            case R.id.stDelete:{
                deleteStudent(mPosition);
                return true;
            }
            case R.id.miExit:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ll);

        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(
                ((Button) findViewById(R.id.bAddStudent)).getVisibility()
        );

        mStudents = new ArrayList<>();

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        int size = sPref.getInt("count", 0);
        if (size>0){
            Gson gson = (new GsonBuilder()).create();
            for (int i=0;i<size;++i){
                String s = sPref.getString("student"+i, "");
                if(!s.equals("")){
                    Student st = gson.fromJson(s, Student.class);
                    mStudents.add(st);
                }
            }
        }
        createStudentList(null);

        mIntentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            Student s = intent.getParcelableExtra("student");
                            mStudents.set(mPosition, s);
                            Toast.makeText(getApplicationContext(),
                                    "Студент: " + s.toString() + "\nУспешно сохранён", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    ArrayList<Student> mStudents;
    StudentListAdapter mStudentListAdapter;

    public void createStudentList(View view) {
        ListView listView = findViewById(R.id.lvList2);
        mStudentListAdapter=new StudentListAdapter(mStudents,this);
        listView.setAdapter(mStudentListAdapter);

        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bAddStudent)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bCreateStudentList)).setVisibility(View.GONE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mStudentListAdapter.colorFaculty(position, parent);
            }
        });

        AdapterView.OnItemClickListener clStudent = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
//
//                intent.putExtra("fio", mStudents.get(position).getFIO());
//                intent.putExtra("faculty", mStudents.get(position).getFaculty());
//                intent.putExtra("group", mStudents.get(position).getGroup());
//
//                Bundle bundle = new Bundle();
//                bundle.putString("fio", mStudents.get(position).getFIO());
//                bundle.putString("faculty", mStudents.get(position).getFaculty());
//                bundle.putString("group", mStudents.get(position).getGroup());
//
//                intent.putExtras(bundle);
                intent.putExtra("student", mStudents.get(position));
                mPosition=position;
                System.out.println(mPosition);
                for (int i = 0; i< listView.getCount();i++) {
                    if (i == position) {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.redShadow));
                    } else {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
                mIntentActivityResultLauncher.launch(intent);
            }
        };
        listView.setOnItemClickListener(clStudent);
    }

    public void changeStudent(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Добавить студента");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.student_input, null);
        inputDialog.setView(vv);
        Student s = mStudents.get(position);

        final EditText mFIO = vv.findViewById(R.id.editDialog_FIO);
        final EditText mFacultet = vv.findViewById(R.id.editDialog_Facultet);
        final EditText mGroup= vv.findViewById(R.id.editDialog_Group);
        mFIO.setText(s.getFIO(), TextView.BufferType.EDITABLE);
        mFacultet.setText(s.getFaculty(), TextView.BufferType.EDITABLE);
        mGroup.setText(s.getGroup(), TextView.BufferType.EDITABLE);

        inputDialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mStudents.set(position, new Student(
                        mFIO.getText().toString(),
                        mFacultet.getText().toString(),
                        mGroup.getText().toString()
                ));
                mStudentListAdapter.notifyDataSetChanged();
            }
    })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    public void addStudent(String fio, String facultet, String group) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Добавить студента");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.student_input, null);
        inputDialog.setView(vv);
        final EditText mFIO = vv.findViewById(R.id.editDialog_FIO);
        final EditText mFacultet = vv.findViewById(R.id.editDialog_Facultet);
        final EditText mGroup= vv.findViewById(R.id.editDialog_Group);
        mFIO.setText(fio, TextView.BufferType.EDITABLE);
        mFacultet.setText(facultet, TextView.BufferType.EDITABLE);
        mGroup.setText(group, TextView.BufferType.EDITABLE);
        inputDialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mFIO.getText().toString().isEmpty() || mFacultet.getText().toString().isEmpty()|| mGroup.getText().toString().isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Ошибка ввода");
                    alertDialog.setMessage("Введены не все данные!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    addStudent(mFIO.getText().toString(), mFacultet.getText().toString(), mGroup.getText().toString());
                                }
                            });
                    alertDialog.show();
                }
                else {
                    mStudents.add(new Student(
                            mFIO.getText().toString(),
                            mFacultet.getText().toString(),
                            mGroup.getText().toString()
                    ));
                    mMenu.findItem(R.id.stChange).setVisible(true);
                    mMenu.findItem(R.id.stDelete).setVisible(true);
                    mStudentListAdapter.notifyDataSetChanged();
                }
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    public void deleteStudent(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Удалить студента!");
        inputDialog.setCancelable(false);

        inputDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mStudents.remove(position);
                if(mStudents.size()==0){
                    mMenu.findItem(R.id.stChange).setVisible(false);
                    mMenu.findItem(R.id.stDelete).setVisible(false);
                }
                mStudentListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Нет", null);
        inputDialog.show();

    }

    protected void onDestroy(){
        if(mStudents!=null) {
            SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            ed.putInt("count", mStudents.size());
            for (int i=0;i<mStudents.size();++i){
                String s = gson.toJson(mStudents.get(i));
                ed.putString("student"+i, s);
            }
            ed.commit();
        }
        super.onDestroy();
    }
}