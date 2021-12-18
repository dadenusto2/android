package com.example.async;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        ((Button) findViewById(R.id.btn3)).setOnClickListener(v->{
            mt3 = new MyTask3();
            mt3.execute();
        });
        ((Button) findViewById(R.id.btn4)).setOnClickListener(v->{
            showResult2();
        });
        ((Button) findViewById(R.id.btn5)).setOnClickListener(v->{
            mt4 = new MyTask4();
            mt4.execute();
        });
        ((Button) findViewById(R.id.btn6)).setOnClickListener(v->{
//            cancelTask();
            if(mt4==null) mt4 = new MyTask4();
            tvInfo.setText(mt4.getStatus().toString());
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

    @SuppressLint("StaticFieldLeak")
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

    MyTask3 mt3;

    private void showResult() {
        if (mt3 == null) return;
        int result = -1;
        try{
            Log.d("myLogs", "Try to get result");
            result = mt3.get();
            Log.d("myLogs", "get returns " + result);
            Toast.makeText(this,"get returns " + result, Toast.LENGTH_LONG).show();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showResult2() {
        if (mt3 == null) return;
        int result = -1;
        try{
            Log.d("myLogs", "Try to get result");
            result = mt3.get(1, TimeUnit.SECONDS);
            Log.d("myLogs", "get returns " + result);
            Toast.makeText(this,"get returns " + result, Toast.LENGTH_LONG).show();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        }catch (TimeoutException e) {
            Log.d("myLogs", "get timeout, result " + result);
            e.printStackTrace();
        }
    }

    class MyTask3 extends AsyncTask<Void, Void, Integer> {
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
            Log.d("myLogs", "Begin");
        }
        protected Integer doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100500;
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            tvInfo.setText("End. Result = "+ result);
            Log.d("myLogs", "End. Result = "+ result);
        }
    }

    private void cancelTask(){
        if(mt4 == null) return;
        Log.d("myLogs", "cancel result "+ mt4.cancel(false));
    }

    MyTask4 mt4;

    class MyTask4 extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
            Log.d("myLogs", "Begin");
        }
        protected Void doInBackground(Void... params) {
            try {
                for(int i = 0; i<5;i++){
                    TimeUnit.SECONDS.sleep(1);
                    if (isCancelled()) return null;
                    Log.d("myLogs", "isCanceled "+ isCancelled());
                }
            }
            catch (InterruptedException e) {
                Log.d("myLogs", "Interrupted");
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvInfo.setText("End");
            Log.d("myLogs", "End");
        }

        protected void onCancelled(){
            super.onCancelled();
            tvInfo.setText("Cancel");
            Log.d("myLogs", "Cancel");
        }
    }
}