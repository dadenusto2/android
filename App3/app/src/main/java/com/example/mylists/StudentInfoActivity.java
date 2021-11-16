package com.example.mylists;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class StudentInfoActivity extends AppCompatActivity {
    private int mPosition;
    private Student s;
    private Menu mMenu;
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subject_menu, menu);
        mMenu = menu;
        if (s.getSubjects().size()==0){
            mMenu.findItem(R.id.changeSb).setVisible(false);
            mMenu.findItem(R.id.deleteSb).setVisible(false);

        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.addSb:{
                addSubject();
                return true;
            }
            case R.id.changeSb:{
                changeSubject(mPosition);
                return true;
            }
            case R.id.deleteSb:{
                deleteSubject(mPosition);
                return true;
            }
            case R.id.back:{
                onBackPassed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        s = getIntent().getParcelableExtra("student");
        ((TextView) findViewById(R.id.tvASI_FIO)).setText(s.getFIO());
        ((TextView) findViewById(R.id.tvASI_Faculty)).setText(s.getFaculty());
        ((TextView) findViewById(R.id.tvASI_Group)).setText(s.getGroup());

//        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(), StudentInfoActivity.this);
//        ((ListView) findViewById(R.id.lvASI_Subjects)).setAdapter(mSubjectListAdapter);

        mSubjects = new ArrayList<>();

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        int size = sPref.getInt("count", 0);
        String st = s.getFIO();
        if (size > 0) {
            Gson gson = (new GsonBuilder()).create();
            for (int i = 0; i < size; ++i) {
                String str = sPref.getString(st + "_subject" + i, "");
                if (!s.equals("")) {
                    Subject sb = gson.fromJson(str, Subject.class);
                    mSubjects.add(sb);
                }
            }
        }
        createSubjectList(null);

//        ((ListView) findViewById(R.id.lvASI_Subjects)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AlertDialog.Builder quitDialog = new AlertDialog.Builder(StudentInfoActivity.this);
//                quitDialog.setTitle("Удалить дисцеплину\"" + s.getSubjects().get(i).getName() + "\"?");
//
//                quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        s.getSubjects().remove(i);
//                        mSubjectListAdapter.notifyDataSetChanged();
//                    }
//                })
//                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        });
//                quitDialog.show();
//                return false;
//            }
//        });
//        ((ListView) findViewById(R.id.lvASI_Subjects)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showPopupMenu(view, i);
//            }
//        });

    }

    ArrayList<Subject> mSubjects;
    SubjectListAdapter mSubjectListAdapter;


    public void createSubjectList(View view) {
        ListView listView = findViewById(R.id.lvASI_Subjects);
        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(), this);
        listView.setAdapter(mSubjectListAdapter);

        AdapterView.OnItemClickListener clSubject = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPosition=position;
                System.out.println(mPosition);
                for (int i = 0; i< listView.getCount();i++) {
                    if (i == mPosition) {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.redShadow));
                    } else {
                        listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }
        };
        listView.setOnItemClickListener(clSubject);
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

    /*public void addSubject(View view) {
        s.addSubject(new Subject(
                ((EditText) findViewById(R.id.editDialog_subjectName)).getText().toString(),
                Integer.parseInt(((Spinner) findViewById(R.id.sDialog_mark)).getSelectedItem().toString())
        ));
        mSubjectListAdapter.notifyDataSetChanged();
    }*/

    public void addSubject() {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Добавление оценки");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_input, null);
        inputDialog.setView(vv);
        final EditText mName = vv.findViewById(R.id.editDialog_subjectName);
        final Spinner mMark = vv.findViewById(R.id.sDialog_mark);
        inputDialog.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                s.addSubject(new Subject(
                        mName.getText().toString(),
                        Integer.parseInt(mMark.getSelectedItem().toString())
                ));
//                mSubjects.add(new Subject(
//                                mName.getText().toString(),
//                                Integer.parseInt(mMark.getSelectedItem().toString())
//                        ));
                System.out.println(mName);
                mMenu.findItem(R.id.changeSb).setVisible(true);
                mMenu.findItem(R.id.deleteSb).setVisible(true);
                mSubjectListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    private int getIndex(Spinner spinner, Integer myInt){
        for (int i=0;i<spinner.getCount();i++){
            System.out.println(spinner.getItemAtPosition(i).toString() + " " + myInt);
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myInt.toString())){
                return i;
            }
        }
        return 0;
    }

    public void changeSubject(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Изменение оценки");
        inputDialog.setCancelable(false);
        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_input, null);
        inputDialog.setView(vv);
        final EditText mName = vv.findViewById(R.id.editDialog_subjectName);
        final Spinner mMark = vv.findViewById(R.id.sDialog_mark);
        mName.setText(s.getSubjects().get(position).getName());
        mMark.setSelection(getIndex(mMark, s.getSubjects().get(position).getMark()));

        inputDialog.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                s.getSubjects().set(mPosition, new Subject(
                        mName.getText().toString(),
                        Integer.parseInt(mMark.getSelectedItem().toString())
                ));
                mSubjectListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    public void deleteSubject(int position) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Удалить оценку?");
        inputDialog.setCancelable(false);

        inputDialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

    public void clSave(View view) {
        SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        ed.putInt("count", s.getSubjects().size());
        String st = s.getFIO();
        for (int i = 0; i < s.getSubjects().size(); ++i) {
            String sb = gson.toJson(s.getSubjects().get(i));
            ed.putString(st + "_subject" + i, sb);
        }
        ed.commit();
        Intent intent = new Intent();
        intent.putExtra("student", s);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void clExit(View view) {
        finish();
    }

    public void onBackPassed() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Сохранить изменения?");
        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clSave(null);
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
        /*if (s.getSubjects() != null) {
            SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            ed.putInt("count", s.getSubjects().size());
            String st = s.getFIO();
            for (int i = 0; i < s.getSubjects().size(); ++i) {
                String sb = gson.toJson(s.getSubjects().get(i));
                ed.putString(st + "_subject" + i, sb);
            }
            ed.commit();
        }*/
        super.onDestroy();

    }
}