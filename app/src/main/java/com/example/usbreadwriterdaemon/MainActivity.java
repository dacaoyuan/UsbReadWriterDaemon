package com.example.usbreadwriterdaemon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.usbreadwriterdaemon.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.button1)
    Button mButton1;
    @BindView(R.id.button2)
    Button mButton2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: msg.what=" + msg.what);

            if (msg.what == 0) {
                Toast.makeText(MainActivity.this, "网络有效！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(MainActivity.this, "无效网络", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                // NetUtils.ping(mHandler);
                startActivity(new Intent(MainActivity.this, MethodOneActivity.class));


                break;
            case R.id.button2:
                //NetUtils.isOnline(mHandler);

                startActivity(new Intent(MainActivity.this, MethodTwoActivity.class));

                break;
        }
    }
}
