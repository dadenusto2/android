package com.example.mylists;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

public class StudentInfoActivity extends AppCompatActivity {

    private SubjectListAdapter mSubjectListAdapter;
    private Student s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

//        ((TextView) findViewById(R.id.tvElementFIO)).setText(getIntent().getStringExtra("fio"));
//        ((TextView) findViewById(R.id.tvElementFaculty)).setText(getIntent().getStringExtra("faculty"));
//        ((TextView) findViewById(R.id.tvElementGroup)).setText(getIntent().getStringExtra("group"));

//        Bundle bundle = getIntent().getExtras();
//        ((TextView) findViewById(R.id.tvElementFIO)).setText(bundle.getString("fio"));
//        ((TextView) findViewById(R.id.tvElementFaculty)).setText(bundle.getString("faculty"));
//        ((TextView) findViewById(R.id.tvElementGroup)).setText(bundle.getString("group"));

        s = getIntent().getParcelableExtra("student");
        ((TextView) findViewById(R.id.tvASI_FIO)).setText(s.getFIO());
        ((TextView) findViewById(R.id.tvASI_Faculty)).setText(s.getFaculty());
        ((TextView) findViewById(R.id.tvASI_Group)).setText(s.getGroup());

        mSubjectListAdapter = new SubjectListAdapter(s.getSubjects(),StudentInfoActivity.this);
        ((ListView) findViewById(R.id.lvASI_Subjects)).setAdapter(mSubjectListAdapter);


        ((ListView) findViewById(R.id.lvASI_Subjects)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder quitDialog = new AlertDialog.Builder(StudentInfoActivity.this);
                quitDialog.setTitle("Удалить дисцеплину\"" + s.getSubjects().get(i).getName()+"\"?");

                quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s.getSubjects().remove(i);
                        mSubjectListAdapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                quitDialog.show();
                return false;
            }
        });
        ((ListView) findViewById(R.id.lvASI_Subjects)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPopupMenu(view, i);
            }
        });
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

    public void addSubject(View view) {
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(StudentInfoActivity.this);
        inputDialog.setTitle("Инофрмация о дисциплине");
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
                mSubjectListAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("Отмена", null);
        inputDialog.show();
    }

    public void clSave(View view) {
        Intent intent = new Intent();
        intent.putExtra("student",s);
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


}