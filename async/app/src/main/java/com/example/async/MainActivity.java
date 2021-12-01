package com.example.async;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo=(TextView)findViewById(R.id.tvInfo);
        ((Button) findViewById(R.id.btn1)).setOnClickListener(v->{
            MyTask mt = new MyTask();
            mt.execute();
        });
        ((Button) findViewById(R.id.btn2)).setOnClickListener(v->{
            MyTask2 mt = new MyTask2();
            mt.execute("file_path_1", "file_path_2", "file_path_3", "file_path_4");
        });
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
        }
    }

    class MyTask2 extends AsyncTask<String, Integer, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                int cnt = 0;
                for(String url: urls){
                    downloadFile(url);
                    publishProgress(++cnt);
                }
                TimeUnit.SECONDS.sleep(2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvInfo.setText("Downloaded " + values[0] + " files");
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
        }
        private void downloadFile(String url) throws InterruptedException{
            TimeUnit.SECONDS.sleep(2);
        }
    }
}