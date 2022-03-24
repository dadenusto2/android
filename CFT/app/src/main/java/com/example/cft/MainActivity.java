package com.example.cft;

import static java.lang.Double.parseDouble;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private ListView rateList;// список в активити
    private String url = "https://www.cbr-xml-daily.ru/daily_json.js";//ссылка на данные
    private ProgressDialog dialog;//окно конвертации
    private RateListAdapter adapter;//адаптер для валют
    private ArrayList<Rate> arrayList;//список валют
    private Menu menu;//меню

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateRates();
    }

    // Инициализация меню
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_manu, menu);
        menu = menu;
        return true;
    }

    //обработка меню
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.miUpdate:{
                updateRates();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    // получение данных от ЦБ
    public void updateRates(){
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {//Запрос либо извлекается по ссылке, либо из кэша
                    try {
                        // объект из ссылки
                        JSONObject js = new JSONObject(string);

                        // получение даты
                        String dateString = js.getString("Date");
                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date date = formatDate.parse(dateString);
                        ((TextView) findViewById(R.id.tvDate)).setText(date.toString());

                        // получение валют
                        String valute = js.getString("Valute");
                        JSONObject rates = new JSONObject(valute);
                        
                        //получение всех ключей в "Valute"
                        Iterator<String> keys = rates.keys();
                        arrayList = new ArrayList<Rate>();
                        // обработка валют
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String rateString = rates.getString(key);
                            JSONObject rateJson = new JSONObject(rateString);
                            arrayList.add(new Rate(rateJson.getString("Name"), parseDouble(rateJson.getString("Value")), Integer.parseInt(rateJson.getString("Nominal"))));
                        }
                        createStudentList(null);
                        Toast.makeText(getApplicationContext(), "Данные успешно обновлены!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException | ParseException e) {//Если ошибка в файле
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {// Если отсутсвует соеденение
                    Toast.makeText(getApplicationContext(), "Ошибка в загрузке данных!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(request);
        }

    // создание списка и обработка конвертации
    public void createStudentList(View view) {
        // связываем список в активити и адаптер для элементов
        rateList = (ListView) findViewById(R.id.lvRateList);
        adapter = new RateListAdapter(arrayList, this);
        rateList.setAdapter(adapter);

        AdapterView.OnItemClickListener clRate = new AdapterView.OnItemClickListener() {
            @Override
            // нажатие на валюту открывает диалоговое окно для ввода
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("Закрыть окно", null);

                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.rate_change, viewGroup, false);

                // названия валют
                ((TextView) dialogView.findViewById(R.id.tvRateName1)).setText("Российский рубль");
                ((TextView) dialogView.findViewById(R.id.tvRateName2)).setText(arrayList.get(position).getName());

                // оброботка конвертации
                EditText etRate1 = (EditText) dialogView.findViewById(R.id.etRate1);
                EditText etRate2 = (EditText) dialogView.findViewById(R.id.etRate2);

                // довавлем слушатель измение в editText для рублей
                etRate1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    //
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        try {
                            // если после 1-ого в строке "0" пишется любая другая цифра, удалаем из строки "0"
                            if(etRate1.getText().toString().charAt(0) == '0' && etRate1.length()>1 && etRate1.getText().toString().charAt(1) != '.'){
                                etRate1.setText(etRate1.getText().toString().substring(1));
                                etRate1.setSelection(etRate1.getText().length());
                            }
                            // если 2-й раз пишется ".", удалаем 2-ую
                            else if (etRate1.getText().toString().indexOf('.') != etRate1.getText().toString().lastIndexOf('.')) {
                                etRate1.setText(etRate1.getText().toString().substring(0, etRate1.getText().toString().lastIndexOf('.')));
                                etRate1.setSelection(etRate1.getText().length());
                            }
                            // если пишутся символы ",","+","-"," ", удалаем их
                            else if (etRate1.getText().toString().indexOf(' ') != -1 || etRate1.getText().toString().indexOf('-') != -1
                                    || etRate1.getText().toString().indexOf(',') != -1|| etRate1.getText().toString().indexOf('+') != -1) {
                                etRate1.setText(etRate1.getText().toString().substring(0, etRate1.getText().length()-1));
                                etRate1.setSelection(etRate1.getText().length());
                            }
                            // если первой написана ".", перед ней пишем "0"
                            else if (etRate1.getText().toString().charAt(0) == '.') {
                                etRate1.setText("0"+etRate1.getText().toString());
                                etRate1.setSelection(etRate1.getText().length());
                            }
                            //считаем курс
                            else {
                                double rate = Double.parseDouble(arrayList.get(position).getRate());
                                double valueInRubles = Double.parseDouble(String.valueOf(etRate1.getText()));
                                double nominale = Double.parseDouble(arrayList.get(position).getNominale());

                                double change = rate*valueInRubles/nominale;
                                etRate2.setText(Double.toString(change));
                            }
                        } catch (Exception e) {
                            etRate2.setText("");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                });
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                alertDialog.show();
            }
        };
        rateList.setOnItemClickListener(clRate);
    }
}