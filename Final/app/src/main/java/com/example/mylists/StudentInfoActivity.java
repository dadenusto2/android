package com.example.mylists;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// окно с информацией о студентах и их предметах
public class StudentInfoActivity extends AppCompatActivity {
    private int mPosition;
    private Student s;
    private Menu mMenu;
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;
    private dbHelperSubject dbHelperSubject;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private static final String TAG = "App3";

    ArrayList<Subject> mSubjects;
    ArrayList<Integer> delList= new ArrayList<>();
    SubjectListAdapter mSubjectListAdapter;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_menu, menu);
        mMenu = menu;
        if (s.getSubjects().size()==0){
            mMenu.findItem(R.id.changeSb).setVisible(false);
            mMenu.findItem(R.id.deleteSb).setVisible(false);
        }
        return true;
    }

    // меню выбора опций
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.addSb:{// добавить предмет
                addSubject(true);
                return true;
            }
            case R.id.changeSb:{// изменить предмет
                changeSubject(mPosition, true);
                return true;
            }
            case R.id.deleteSb:{// удалить предмет
                deleteSubject(mPosition);
                return true;
            }
            case R.id.back:{// возврат к предыдущему окну
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // полчуть данные по прдемета из бд
    class downloadFromDB extends AsyncTask<Student,Student,Student> {
        Student st;
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Wait to download subjects from db");
            super.onPreExecute();
        }
        @Override
        protected Student doInBackground(Student... students) {
            st = students[0];
            Log.d(TAG, "Start download subjects from db");
            dbHelperSubject = new dbHelperSubject(getApplicationContext());
            db = dbHelperSubject.getReadableDatabase();
            dbHelperSubject.onCreate(db);
            userCursor = db.rawQuery("select * from "+ dbHelperSubject.TABLE, null);
//            Gson gson = (new GsonBuilder()).create();
            while (userCursor.moveToNext()) {
                if (userCursor.getInt(1)==s.getID()) {
                    Subject sb = new Subject(userCursor.getInt(0), userCursor.getInt(1), userCursor.getString(2), userCursor.getInt(3));
                    Log.d(TAG, sb.toString());
                    st.addSubject(sb);
                }
            }
            return st;
        }
        @Override
        public void onPostExecute(Student results) {
            super.onPostExecute(results);
            s = st;
            createSubjectList(null);
            Log.d(TAG, "End download subjects from db");
        }
    }

    // сохранить данные о предметах в БД
    class saveToDB extends AsyncTask<Student, Student, Void> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Wait to save subjects to db");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Student... students) {
            Student st = students[0];
            for (int i = 0; i < st.getSubjects().size(); ++i) {
                Subject sb = st.getSubjects().get(i);
                Log.d(TAG, sb.getName());
//                db.execSQL("insert or replace into subjects (id_student, Name, mark) values ('" + sb.getIDStudent()+"', '" + sb.getName()+"', '"+ sb.getMark()+ "'); ");
                if(sb.getID()==null){
                    db.execSQL("insert into subjects (id_student, Name, mark) values ('" + sb.getIDStudent()+"', '" + sb.getName()+"', '"+ sb.getMark()+ "'); ");
                }
                else{
                    db.execSQL("replace into subjects (id, id_student, Name, mark) values ('" +sb.getID()+"', '"  + sb.getIDStudent()+"', '" + sb.getName()+"', '"+ sb.getMark()+ "'); ");
                }
            }
            for (int i=0; i<delList.size();i++){
                db.delete(dbHelperSubject.TABLE, "id = ?", new String[]{String.valueOf(delList.get(i))});
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "End save subjects to db");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        s = getIntent().getParcelableExtra("student");// полчуев из intent данные о студенте
        downloadFromDB dfb = new downloadFromDB();
        dfb.execute(s);
        // добавляем данные в поля
        ((EditText) findViewById(R.id.etASI_FIO)).setText(s.getFIO());
        ((EditText) findViewById(R.id.etASI_Faculty)).setText(s.getFaculty());
        ((EditText) findViewById(R.id.etASI_Group)).setText(s.getGroup());

        mSubjects = new ArrayList<>();
        delList= new ArrayList<>();
        mPosition = -1;
    }

    public void createSubjectList(View view) {
        ListView listView = findViewById(R.id.lvASI_Subjects);
        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(), this);
        listView.setAdapter(mSubjectListAdapter);

        AdapterView.OnItemClickListener clSubject = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPosition=position;
                for (int i = 0; i< listView.getCount(); i++) {
                    if (i == mPosition) {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.odd_element));
                    } else {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                    mMenu.findItem(R.id.changeSb).setVisible(true);
                    mMenu.findItem(R.id.deleteSb).setVisible(true);
                }
            }
        };
        AdapterView.OnItemLongClickListener clLSubject = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j< listView.getCount();j++) {
                    listView.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.white));
                }
                mPosition = -1;
                mMenu.findItem(R.id.changeSb).setVisible(false);
                mMenu.findItem(R.id.deleteSb).setVisible(false);
                return true;
            }
        };
        listView.setOnItemClickListener(clSubject);
        listView.setOnItemLongClickListener(clLSubject);
    }

    public void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Integer mark;
                switch (item.getItemId()) {
                    case R.id.popup5: {mark = 5; break;}
                    case R.id.popup4: {mark = 4; break;}
                    case R.id.popup3: {mark = 3; break;}
                    case R.id.popup2: {mark = 2; break;}
                    default:
                        return false;
                }
                if ((s!=null)&&(position<s.getSubjects().size())){
                    s.getSubjects().get(position).setMark(mark);
                    mSubjectListAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        popupMenu.show();
    }
    // добавление предмета
    public void addSubject(boolean b) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Добавление оценки");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_input, null);
        inputDialog.setView(vv);
        final EditText mName = vv.findViewById(R.id.editDialog_subjectName);
        final Spinner mMark = vv.findViewById(R.id.sDialog_mark);
        if (!b)
            mName.setError("Не введено название");
        inputDialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!mName.getText().toString().isEmpty()) {
                    s.addSubject(new Subject(
                            s.getID(),
                            mName.getText().toString(),
                            Integer.parseInt(mMark.getSelectedItem().toString())
                    ));
                    mMenu.findItem(R.id.changeSb).setVisible(false);
                    mMenu.findItem(R.id.deleteSb).setVisible(false);
                    mSubjectListAdapter.notifyDataSetChanged();
                }
                else {
                    addSubject(false);
                }
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    private int getIndex(Spinner spinner, Integer myInt){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myInt.toString())){
                return i;
            }
        }
        return 0;
    }

    // изменение предмета
    public void changeSubject(int position, boolean b) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Изменение оценки");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_input, null);
        inputDialog.setView(vv);
        final EditText mName = vv.findViewById(R.id.editDialog_subjectName);
        final Spinner mMark = vv.findViewById(R.id.sDialog_mark);
        Integer mID = s.getSubjects().get(position).getID();
        mName.setText(s.getSubjects().get(position).getName());
        mMark.setSelection(getIndex(mMark, s.getSubjects().get(position).getMark()));
        if (!b)
            mName.setError("Не введено название");
        inputDialog.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!mName.getText().toString().isEmpty()) {// если название предмета введено, то сохранем его
                    s.getSubjects().set(mPosition, new Subject(
                            mID,
                            s.getID(),
                            mName.getText().toString(),
                            Integer.parseInt(mMark.getSelectedItem().toString())
                    ));
                    mMenu.findItem(R.id.changeSb).setVisible(false);
                    mMenu.findItem(R.id.deleteSb).setVisible(false);
                    mSubjectListAdapter.notifyDataSetChanged();
                }
                else {// если название предмета не введено, то снова его запускаем
                    AlertDialog alertDialog = new AlertDialog.Builder(StudentInfoActivity.this).create();
                    alertDialog.setTitle("Ошибка ввода!");
                    alertDialog.setMessage("Название оценки не введено!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    changeSubject(position,false);
                }
            }

        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    // удаление предмета
    public void deleteSubject(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Удалить оценку?");
        inputDialog.setCancelable(false);

        inputDialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {// добавлем id оценки в список
                    delList.add(s.getSubjects().get(position).getID());
                }catch (Exception e){

                }
                s.getSubjects().remove(position);
                if(s.getSubjects().size()==0){
                    mMenu.findItem(R.id.changeSb).setVisible(false);
                    mMenu.findItem(R.id.deleteSb).setVisible(false);
                }
                mSubjectListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    // сохранить изменения
    public void clSave() {
        // если введены не все данные по студенту, то их надо ввести
        if (((EditText) findViewById(R.id.etASI_FIO)).getText().toString().isEmpty()||
            ((EditText) findViewById(R.id.etASI_Faculty)).getText().toString().isEmpty()||
            ((EditText) findViewById(R.id.etASI_Group)).getText().toString().isEmpty()){
            EditText mFIO = findViewById(R.id.etASI_FIO);
            EditText mFaculty = findViewById(R.id.etASI_Faculty);
            EditText mGroup = findViewById(R.id.etASI_Group);
            if (((EditText) findViewById(R.id.etASI_FIO)).getText().toString().isEmpty())
                mFIO.setError("Не введено ФИО!");
            if (((EditText) findViewById(R.id.etASI_Faculty)).getText().toString().isEmpty())
                mFaculty.setError("Не введён факультет!");
            if (((EditText) findViewById(R.id.etASI_Group)).getText().toString().isEmpty())
                mGroup.setError("Не введена группа!");
        }
        else{//если вс ввелено сохраняем
            saveToDB std = new saveToDB();
            std.execute(s);
            Student newS = new Student(
                    s.getID(),
                    ((EditText) findViewById(R.id.etASI_FIO)).getText().toString(),
                    ((EditText) findViewById(R.id.etASI_Faculty)).getText().toString(),
                    ((EditText) findViewById(R.id.etASI_Group)).getText().toString()
            );
            Intent intent = new Intent();
            intent.putExtra("student", newS);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void clExit(View view) {
        finish();
    }

    public void onBackPressed() {// при нажатии назад появляется диалог сохраняем или нет
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Сохранить изменения?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clSave();
            }
        })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clExit(null);
                    }
                });
        quitDialog.show();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}