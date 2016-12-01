package com.example.android.sample.thread1;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ProgressBar bar1;
    ProgressBar bar2;
    TextView msgWorking;
    TextView msgReturned;
    boolean isRunning = false;
    final int MAX_SEC = 60;
    String strTest = "global value seen by all threads";
    int intTest = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar1 = (ProgressBar)findViewById(R.id.progress);
        bar2 = (ProgressBar)findViewById(R.id.progress2);
        bar1.setMax(MAX_SEC);
        bar1.setProgress(0);
        msgWorking = (TextView)findViewById(R.id.TextView01);
        msgReturned = (TextView)findViewById(R.id.TextView02);
        strTest+="-01";
        intTest = 1;
    }

    public void onStop(){
        super.onStop();
        isRunning = false;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String returnedValue = (String)msg.obj;
            msgReturned.setText("returend by background thread: ¥n"+returnedValue);
            bar1.incrementProgressBy(1);// let's try change your number
            if(bar1.getProgress() == MAX_SEC){
                msgReturned.setText("Done ¥n back thread has been stopped");
                isRunning = false;
            }
            if(bar1.getProgress()==bar1.getMax()){
                msgWorking.setText("Done");
                //delete progress bar
                bar1.setVisibility(View.INVISIBLE);
                bar2.setVisibility(View.INVISIBLE);
            }
            else{
                msgWorking.setText("Working..."+bar1.getProgress());
            }
        }
    };

    public void onStart(){
        super.onStart();
        bar1.setProgress(0);//set progressbar and run
        Thread background = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    for(int i = 0;i<MAX_SEC&& isRunning;i++){
                        //fake busy busy work here
                        Thread.sleep(1000);
                        Random rnd = new Random();
                        String data = "Thread Value :"+rnd.nextInt(101);
                        data += "¥n"+strTest+""+intTest;
                        intTest++;
                        Message msg = handler.obtainMessage(1,data);
                        //if thread is still alive send the message
                        if(isRunning){
                            handler.sendMessage(msg);
                        }
                    }
                }catch (Throwable t){
                    //just end the background thread
                }
            }
        });
        isRunning = true;
        background.start();
    }
}
