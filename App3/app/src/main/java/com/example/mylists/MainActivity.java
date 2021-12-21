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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "App3";
    private int mPosition;
    private Menu mMenu;
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;
    private dbHelperStudent dbHelperStudent;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private Integer userId=0;
    private Integer lastID=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        if(mStudents.size() == 0 || mPosition==-1){
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
        mPosition = -1;
        mStudents = new ArrayList<>();
        dbHelperStudent = new dbHelperStudent(getApplicationContext());
        db = dbHelperStudent.getReadableDatabase();
        userCursor =  db.rawQuery("select * from "+ dbHelperStudent.TABLE, null);
//        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
//        int size = sPref.getInt("count", 0);
//        if (size>0){
//            Gson gson = (new GsonBuilder()).create();
//            for (int i=0;i<size;++i){
//                String s = sPref.getString("student"+i, "");
//                if(!s.equals("")){
//                    Student st = gson.fromJson(s, Student.class);
//                    mStudents.add(st);
//                }
//            }
//        }
        while(userCursor.moveToNext()) {
            userId=userCursor.getInt(0);
            Student st = new Student(userCursor.getInt(0), userCursor.getString(1), userCursor.getString(2), userCursor.getString(3));
            mStudents.add(st);
        }
        lastID=userId;
        createStudentList(null);

        mIntentActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            Student s = intent.getParcelableExtra("student");
                            System.out.println(s);
                            System.out.println(s.getSubjects().size());
                            if (mPosition == -1) {
                                mStudents.add(s);
                                mPosition = mStudents.size()-1;
                            }
                            else {
                                mStudents.set(mPosition, s);
                            }
                            mMenu.findItem(R.id.stChange).setVisible(false);
                            mMenu.findItem(R.id.stDelete).setVisible(false);
                            mStudentListAdapter.notifyDataSetChanged();
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

        AdapterView.OnItemLongClickListener clLStudent = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j< listView.getCount();j++) {
                    listView.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.white));
                }
                mPosition = -1;
                mMenu.findItem(R.id.stChange).setVisible(false);
                mMenu.findItem(R.id.stDelete).setVisible(false);
                return true;
            }
        };
        AdapterView.OnItemClickListener clStudent = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < listView.getCount(); j++) {
                    if (j == i)
                        listView.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.odd_element));
                    else
                        listView.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.white));
                }
                mPosition = i;
                mMenu.findItem(R.id.stChange).setVisible(true);
                mMenu.findItem(R.id.stDelete).setVisible(true);
            }
        };
        listView.setOnItemLongClickListener(clLStudent);
        listView.setOnItemClickListener(clStudent);
    }

    public void changeStudent(int position) {
        Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
        Log.d(TAG, mStudents.get(mPosition).toString());
        intent.putExtra("student", mStudents.get(mPosition));
        mMenu.findItem(R.id.stChange).setVisible(true);
        mMenu.findItem(R.id.stDelete).setVisible(true);
        mIntentActivityResultLauncher.launch(intent);
    }

    public void addStudent(String fio, String facultet, String group) {
        Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
        mPosition = -1;
        intent.putExtra("student", new Student(lastID+1, "", "", ""));
        mMenu.findItem(R.id.stChange).setVisible(true);
        mMenu.findItem(R.id.stDelete).setVisible(true);
        mIntentActivityResultLauncher.launch(intent);
        lastID++;
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
                mPosition = -1;
                mMenu.findItem(R.id.stChange).setVisible(false);
                mMenu.findItem(R.id.stDelete).setVisible(false);
                mStudentListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Нет", null);
        inputDialog.show();

    }

    protected void onDestroy(){
        if(mStudents!=null) {
//            SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
//            GsonBuilder builder = new GsonBuilder();
//            Gson gson = builder.create();
//            ed.putInt("count", mStudents.size());

            Log.d(TAG, Integer.toString(userId));
            Log.d(TAG, Integer.toString(mStudents.size()));
            for (int i=0;i<mStudents.size();++i){
                Student sdb = mStudents.get(i);
//                String s = gson.toJson(mStudents.get(i));
//                ed.putString("student"+i, s);
                ContentValues cv = new ContentValues();
                cv.put(dbHelperStudent.COLUMN_ID, sdb.getID());
                cv.put(dbHelperStudent.COLUMN_fio, sdb.getFIO());
                cv.put(dbHelperStudent.COLUMN_faculty, sdb.getFaculty());
                cv.put(dbHelperStudent.COLUMN_group, sdb.getGroup());
                Log.d(TAG, Integer.toString(sdb.getID()));
                try {
                    if (sdb.getID() <= userId) {
                        Log.d(TAG, "update");
                        Log.d(TAG, cv.toString());
                        db.update(dbHelperStudent.TABLE, cv, dbHelperStudent.COLUMN_ID + "=" + sdb.getID(), null);
                    } else {
                        Log.d(TAG, "insert");
                        db.insert(dbHelperStudent.TABLE, null, cv);
                    }

                }
                catch (Exception e){
                    Log.d(TAG, "delete");
                    db.delete(dbHelperStudent.TABLE, "id = ?", new String[]{String.valueOf(userId)});
                }
            }
//            ed.commit();
        }
        super.onDestroy();
    }
}