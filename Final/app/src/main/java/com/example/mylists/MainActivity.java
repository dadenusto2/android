package com.example.mylists;

//фио студентов и тд

/*
открывается список студентов
выбранный студент подсвечивает
в меню добавить удалить изменить студента
проверка ввода
по нажатию открываются предметы
меню аналогично студента
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Final";// тэг для логов
    private int mPosition;// выбранный студент
    private Menu mMenu;// меню
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;// активность с инфо о студенте
    private dbHelperStudent dbHelperStudent;// бд студентов
    private SQLiteDatabase db;
    private Integer userId=0;// последний Id
    private Integer lastID=0;// последний Id студента (с учетом добавленных)

    // создание меню выбора опций
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        try {
            // если нет данных, не показываем кнопки изменения и добавления студента
            if (mStudents.size() == 0 || mPosition == -1) {
                mMenu.findItem(R.id.stChange).setVisible(false);
                mMenu.findItem(R.id.stDelete).setVisible(false);
            }
        }catch(Exception e){

        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    // задание дейсвия на кнопки выбора опций
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.miAbout:{// о программе
                AlertDialog.Builder infoDialog = new AlertDialog.Builder(MainActivity.this);
                infoDialog.setTitle("О программе");
                infoDialog.setMessage("Это программа группы 4ИТ/2!");
                infoDialog.setCancelable(false);
                infoDialog.setPositiveButton("Прочитано", null);
                infoDialog.show();
                return true;
            }
            case R.id.stAdd:{// добавить нового студента
                addStudent();
                return true;
            }
            case R.id.stChange:{// добавить сущесвующего студента
                changeStudent(mPosition);
                return true;
            }
            case R.id.stDelete:{// добавить текущего студента
                deleteStudent(mPosition);
                return true;
            }
            case R.id.miExit:{// выход
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // получить студентов из БД через AsyncTask
    class dowloadFromDB extends AsyncTask<Void, ArrayList<Student>, ArrayList<Student>>{
        // список для студентов
        ArrayList<Student> resultStudents = new ArrayList<>();
        Cursor userCursor;// для загрузки
        @Override
        protected void onPreExecute() {// ждем вызова
            Log.d(TAG, "Wait to download students from db");
            super.onPreExecute();
        }
        @Override
        protected ArrayList<Student> doInBackground(Void... voids) {// получаем данные из бд
            Log.d(TAG, "Start download students from db");
            dbHelperStudent = new dbHelperStudent(getApplicationContext());
            db = dbHelperStudent.getReadableDatabase();
            userCursor =  db.rawQuery("select * from "+ dbHelperStudent.TABLE, null);
            while(userCursor.moveToNext()) {
                userId=userCursor.getInt(0);
                Log.d(TAG, "Downloaded "+userId+" student!");
                Student st = new Student(userCursor.getInt(0), userCursor.getString(1), userCursor.getString(2), userCursor.getString(3));
                resultStudents.add(st);
            }
            lastID=userId;
            return resultStudents;
        }
        @Override
        // передаем внутренний список студентов во внешний список
        public void onPostExecute(ArrayList<Student> results) {
            super.onPostExecute(results);
            Log.d(TAG, "Size "+ String.valueOf(results.size()));
            mStudents = results;
            createStudentList();
            Log.d(TAG, "End download students from db");
        }
    }

    // сохранение изменений в бд
    class saveToDB extends AsyncTask<ArrayList<Student>, ArrayList<Student>, Void> {
        ArrayList<Student> resultStudents;
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Wait to save students to db");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(ArrayList<Student>... arrayLists) {
            resultStudents = arrayLists[0];
            Log.d(TAG, "Start save students to db");
            if(resultStudents!=null) {
                Log.d(TAG, Integer.toString(userId));
                Log.d(TAG, Integer.toString(resultStudents.size()));
                for (int i=0;i<resultStudents.size();++i){
                    Student sdb = resultStudents.get(i);
                    ContentValues cv = new ContentValues();
                    cv.put(dbHelperStudent.COLUMN_ID, sdb.getID());
                    cv.put(dbHelperStudent.COLUMN_fio, sdb.getFIO());
                    cv.put(dbHelperStudent.COLUMN_faculty, sdb.getFaculty());
                    cv.put(dbHelperStudent.COLUMN_group, sdb.getGroup());
                    Log.d(TAG, Integer.toString(sdb.getID()));
                    // если текущий id был раньше, то обновляем
                    if (sdb.getID() <= userId) {
                        Log.d(TAG, "update");
                        Log.d(TAG, cv.toString());
                        db.update(dbHelperStudent.TABLE, cv, dbHelperStudent.COLUMN_ID + "=" + sdb.getID(), null);
                        // если текущего Id не было (те новый), то добавляем
                    } else if (sdb.getID() > userId){
                        Log.d(TAG, "insert");
                        db.insert(dbHelperStudent.TABLE, null, cv);
                    }
                }
                for (int i=0; i<delStdID.size();i++){
                    Log.d(TAG, "delete");
                    dbHelperSubject dbHelperSubject = new dbHelperSubject(getApplicationContext());
                    SQLiteDatabase dbC = dbHelperSubject.getReadableDatabase();
                    dbC.delete(com.example.mylists.dbHelperSubject.TABLE, "id_student = ?", new String[]{String.valueOf(delStdID.get(i))});
                    db.delete(com.example.mylists.dbHelperStudent.TABLE, "id = ?", new String[]{String.valueOf(delStdID.get(i))});
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "End save students to db");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dowloadFromDB dfd = new dowloadFromDB();
        dfd.execute();
        setContentView(R.layout.main_ll);
        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(
                ((Button) findViewById(R.id.bAddStudent)).getVisibility()
        );
        mPosition = -1;
        //mStudents = new ArrayList<>();
        delStdID = new ArrayList<>();
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
    // список студентов
    ArrayList<Student> mStudents;
    // список id студетов, которых надо будет elfkbnm
    ArrayList<Integer> delStdID;
    // адаптер списка
    StudentListAdapter mStudentListAdapter;

    // создание списка студетов
    public void createStudentList() {
        ListView listView = findViewById(R.id.lvList2);
        mStudentListAdapter=new StudentListAdapter(mStudents,this);
        listView.setAdapter(mStudentListAdapter);

        ((LinearLayout) findViewById(R.id.llInput)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bAddStudent)).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.bCreateStudentList)).setVisibility(View.GONE);

        AdapterView.OnItemLongClickListener clLStudent = new AdapterView.OnItemLongClickListener() {
            @Override
            // долгое нажатие для снятия выбора и скрытия пунктов меню "изменить и удалить"
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
            // при нажатии на студента он подсвечивается и появляются пункты меню "изменить и удалить"
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
    // изменение студента
    public void changeStudent(int position) {
        // в intent передается текущий студент
        Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
        Log.d(TAG, mStudents.get(mPosition).toString());
        intent.putExtra("student", mStudents.get(mPosition));
        mMenu.findItem(R.id.stChange).setVisible(true);
        mMenu.findItem(R.id.stDelete).setVisible(true);
        // вызываем окно с информацией о студнете и предметах
        mIntentActivityResultLauncher.launch(intent);
    }
    // сохдание нового студента
    public void addStudent() {
        Intent intent = new Intent(MainActivity.this, StudentInfoActivity.class);
        mPosition = -1;
        intent.putExtra("student", new Student(lastID+1, "", "", ""));
        mMenu.findItem(R.id.stChange).setVisible(true);
        mMenu.findItem(R.id.stDelete).setVisible(true);
        mIntentActivityResultLauncher.launch(intent);
        lastID++;
    }
    // удаляем студента через AlertDialog
    public void deleteStudent(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("Удалить студента!");
        inputDialog.setCancelable(false);

        inputDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = mStudents.get(position).getID();
                delStdID.add(id);
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

    // при выходе сохраняем данные в бд
    protected void onDestroy(){
        saveToDB std = new saveToDB();
        std.execute(mStudents);
        super.onDestroy();
    }
}