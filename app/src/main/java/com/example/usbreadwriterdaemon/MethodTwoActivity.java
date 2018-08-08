package com.example.usbreadwriterdaemon;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usbreadwriterdaemon.receiver.UsbStatusChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class MethodTwoActivity extends AppCompatActivity {

    private String strFilePath;
    private TextView textView;
    private TextView textView1;

    private JZVideoPlayerStandard jzVideoPlayerStandard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_two);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        textView = findViewById(R.id.tv);
        textView1 = findViewById(R.id.tv1);

        EventBus.getDefault().register(this);

        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.jzvideoPlayerStandard);
        /*jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4",
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "饺子闭眼睛");*/

       /* cpAssertVideoToLocalPath();
        jzVideoPlayerStandard.setUp(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子不信");*/


    }

    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4");
            myInput = this.getAssets().open("local_video.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(UsbStatusChangeEvent event) {
        strFilePath = event.filePath;

        //textView.setText("u盘路径：" + strFilePath);

        File file = new File(strFilePath + "/wode369tv", "local.mkv");
        //Toast.makeText(this, "getPath=" + file.getPath(), Toast.LENGTH_SHORT).show();
        textView.setText("file.getPath()=" + file.getPath());


        jzVideoPlayerStandard.setUp(file.getPath(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子不信");


    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }


    }

}
