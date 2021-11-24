package com.example.it2_handle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView tvInfo;
    Button btnStart;
    final String LOG_TAG = "myLogs";

    Handler h, h1;

    final int STATUS_NONE = 0; //нет коннекта
    final int STATUS_CONNECTING = 1; // подключается
    final int STATUS_CONNECTED = 2; // коннектед

    TextView tvStatus;
    Button btnConnect;
    ProgressBar pbConnect;

    final int STATUS_DOWNLOAD_START = 3; // загрузка началась
    final int STATUS_DOWNLOAD_FILE = 4; // файл загружен
    final int STATUS_DOWNLOAD_END = 5; // загрузка закончена
    final int STATUS_DOWNLOAD_NONE = 6; // нет файлов для загрузки
    Handler h2;
    ProgressBar pbDownload;
    Button  btnConnectOther;

    Handler hh1;
    Handler.Callback hc = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.d(LOG_TAG, "what = "+ msg.what);
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hh1=new Handler(hc);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        btnStart = (Button) findViewById(R.id.btnStart);

        h = new Handler(Looper.getMainLooper()){
            public void handleMessage(android.os.Message msg){
                tvInfo.setText("Закачано файлов: " + msg.what);
                if (msg.what == 10) btnStart.setEnabled(true);
            }
        };

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnConnectOther = (Button) findViewById(R.id.btnConnectOther);
        pbConnect = (ProgressBar) findViewById(R.id.pbConnect);

        h1 = new Handler(Looper.getMainLooper()){
            public void handleMessage(android.os.Message msg){
                switch (msg.what){
                    case STATUS_NONE:
                        btnConnectOther.setEnabled(true);
                        tvStatus.setText("Not connected");
                        pbDownload.setVisibility(View.GONE);
                        break;
                    case STATUS_CONNECTING:
                        btnConnectOther.setEnabled(false);
                        //pbConnect.setVisibility(View.VISIBLE);
                        tvStatus.setText("Connecting");
                        break;
                    case STATUS_CONNECTED:
                        btnConnectOther.setVisibility(View.GONE);
                        tvStatus.setText("Connected");
                        break;
                }
            }
        };
        h1.sendEmptyMessage(STATUS_NONE);

        pbDownload = (ProgressBar) findViewById(R.id.pbDownload);
        h2 = new Handler(Looper.getMainLooper()){
            public void handleMessage(android.os.Message msg){
                switch (msg.what){
                    case STATUS_NONE:
                        btnConnectOther.setEnabled(true);
                        tvStatus.setText("Not connected");
                        pbDownload.setVisibility(View.GONE);
                        break;
                    case STATUS_CONNECTING:
                        btnConnectOther.setEnabled(false);
                        //pbConnect.setVisibility(View.VISIBLE);
                        tvStatus.setText("Connecting");
                        break;
                    case STATUS_CONNECTED:
                        //btnConnectOther.setVisibility(View.GONE);
                        tvStatus.setText("Connected");
                        break;
                    case STATUS_DOWNLOAD_START:
                        tvStatus.setText("Start download " + msg.arg1 + " files");
                        pbDownload.setMax(msg.arg1);
                        pbDownload.setProgress(0);
                        pbDownload.setVisibility(View.VISIBLE);
                        break;
                    case STATUS_DOWNLOAD_FILE:
                        tvStatus.setText("Downloading. Left " +msg.arg2 + " files");
                        pbDownload.setProgress(msg.arg1);
                        saveFile((byte[]) msg.obj);
                        break;
                    case STATUS_DOWNLOAD_END:
                        tvStatus.setText("Download complete");
                        break;
                    case STATUS_DOWNLOAD_NONE:
                        tvStatus.setText("No files to download");
                        break;
                }
            }
        };
    }

    byte[] downloadOtherFile() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return new byte[1024];
    }

    void saveFile(byte[] file){

    }



    public void onclick(View view) {

        switch (view.getId()) {
            case R.id.btnConnectOther:
                Thread t2 = new Thread(new Runnable() {
                    Message msg;
                    byte[] file;
                    Random rand = new Random();
                    @Override
                    public void run() {
                        try{
                            h2.sendEmptyMessage(STATUS_CONNECTING);

                            TimeUnit.SECONDS.sleep(1);
                            h2.sendEmptyMessage(STATUS_CONNECTED);

                            TimeUnit.SECONDS.sleep(2);
                            int filesCount = rand.nextInt(15);
                            if (filesCount == 0) {
                                h2.sendEmptyMessage(STATUS_DOWNLOAD_NONE);
                                TimeUnit.MILLISECONDS.sleep(1500);
                                h2.sendEmptyMessage(STATUS_NONE);
                            }
                            msg = h2.obtainMessage(STATUS_DOWNLOAD_START, filesCount, 0);
                            h2.sendMessage(msg);
                            for (int i =1; i<=filesCount;i++){
                                file = downloadOtherFile();
                                msg= h2.obtainMessage(STATUS_DOWNLOAD_FILE, i, filesCount-i, file);
                                h2.sendMessage(msg);
                            }
                            h2.sendEmptyMessage(STATUS_DOWNLOAD_END);
                            TimeUnit.MILLISECONDS.sleep(1500);
                            h2.sendEmptyMessage(STATUS_NONE);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
                t2.start();
                break;
            case R.id.btnStart:

                btnStart.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i <=10; i++){
                            downloadFile();
//                            tvInfo.setText("Закачано файлов: " + i);
                            h.sendEmptyMessage(i);
                            Log.d(LOG_TAG, "Закачано файлов " + i);
                        }
                    }
                });
                t.start();
                break;

            case R.id.btnTest:
                Log.d(LOG_TAG, "test");
                break;
            case R.id.btnConnect:
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            h1.sendEmptyMessage(STATUS_CONNECTING);
                            TimeUnit.SECONDS.sleep(2);
                            h1.sendEmptyMessage(STATUS_CONNECTED);
                            TimeUnit.SECONDS.sleep(3);
                            h1.sendEmptyMessage(STATUS_NONE);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
                t1.start();
                break;
            default:
                break;
        }
    }

    public void onClick1(View view){
        hh1.sendEmptyMessageAtTime(999, 29000);
        hh1.sendEmptyMessageAtTime(1, 1000);
        hh1.sendEmptyMessageAtTime(2, 2000);
        hh1.sendEmptyMessageAtTime(3, 3000);
        hh1.sendEmptyMessageAtTime(2, 4000);
        hh1.sendEmptyMessageAtTime(5, 5000);
        hh1.sendEmptyMessageAtTime(2, 6000);
        hh1.sendEmptyMessageAtTime(7, 7000);
        hh1.sendEmptyMessageAtTime(2, 8000);
        hh1.sendEmptyMessageAtTime(9, 9000);
    }

    public void onClick2(View view){
        hh1.removeMessages(2);
    }

    void downloadFile(){
        try{
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}