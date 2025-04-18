package com.example.ss_thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView count_view ;
    private int count = 0 ;
    private MyAsynchTask myAsynchTask;
    private boolean mStopLoop ;


    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_start = findViewById(R.id.button);
        Button btn_stop = findViewById(R.id.button2);
        count_view = findViewById(R.id.textView);

        Log.i(TAG,"Thread id:" + Thread.currentThread().getId());

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        handler = new Handler(getApplicationContext().getMainLooper());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            mStopLoop = true;
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mStopLoop) {
                        try {
                            Thread.sleep(1000);
                            count++;
                        } catch (InterruptedException e) {
                            Log.i(TAG,e.getMessage());
                        }
                        Log.i(TAG, "Thread id in While loop:" + Thread.currentThread().getId() + ", Coint : " + count);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                count_view.setText(" "+ count);
                            }
                        });
                    }
                }
            }).start();*/
            myAsynchTask = new MyAsynchTask();
            myAsynchTask.execute(count);


        }
        else if (v.getId() == R.id.button2) {
//            mStopLoop = false;
            // When the asynch task is stop you cannot start the same task 
            myAsynchTask.cancel(true);
        }

        }
        private class MyAsynchTask extends AsyncTask<Integer, Integer , Integer>{
            private int customCounter;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                customCounter = 0 ;
            }

            @Override
            protected Integer doInBackground(Integer... integers) {
                customCounter = integers[0];
                while (mStopLoop) {
                    try {
                        Thread.sleep(1000);
                        customCounter++;
                        publishProgress(customCounter);
                    } catch (InterruptedException e) {
                        Log.i(TAG,e.getMessage());
                    }

            }
                return customCounter;
        }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                count_view.setText("" + values[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                count_view.setText("" + integer);
                count = integer ;
            }
        }
}
