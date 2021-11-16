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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private int mPosition;
    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

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

    public void createList1(View view){
        String[] catNames = new String[]{
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                "Китти", "Масяня", "Симба"
        };
        Arrays.sort(catNames);

        ListView listView = findViewById(R.id.lvList1);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, catNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Выбрано: "+((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    ArrayList<Student> mStudents;
    StudentListAdapter mStudentListAdapter;

    public void createStudentList(View view) {
        //mStudents=new ArrayList<>();
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

//                intent.putExtra("fio", mStudents.get(position).getFIO());
//                intent.putExtra("faculty", mStudents.get(position).getFaculty());
//                intent.putExtra("group", mStudents.get(position).getGroup());

//                Bundle bundle = new Bundle();
//                bundle.putString("fio", mStudents.get(position).getFIO());
//                bundle.putString("faculty", mStudents.get(position).getFaculty());
//                bundle.putString("group", mStudents.get(position).getGroup());
//
//                intent.putExtras(bundle);
                intent.putExtra("student", mStudents.get(position));
                mPosition=position;
                //startActivity(intent);
                mIntentActivityResultLauncher.launch(intent);
            }
        };
        listView.setOnItemClickListener(clStudent);
    }

    public void addStudent(View view) {

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.editFIO)).getText().toString())){
            ((EditText) findViewById(R.id.editFIO)).setError("Не Указанно ФИО");
            return;
        }

        mStudents.add(new Student(
                ((EditText) findViewById(R.id.editFIO)).getText().toString(),
                ((EditText) findViewById(R.id.editFaculty)).getText().toString(),
                ((EditText) findViewById(R.id.editGroup)).getText().toString()
        ));
        ((EditText) findViewById(R.id.editFIO)).setText("");
        //((EditText) findViewById(R.id.editFaculty)).setText("");
        //((EditText) findViewById(R.id.editGroup)).setText("");
        mStudentListAdapter.notifyDataSetChanged();
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