package com.example.usbreadwriterdaemon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yuanpk on 2018/8/1  9:38
 * <p>
 * Description:TODO
 */
public class NetUtils {
    /**
     * 判断网络是否连接,并且是否有效
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected() && info.isAvailable()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {


                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 在子线程里开启该方法，可检测当前网络是否能打开网页
     * true是可以上网，false是不能上网
     */
    public static void isOnline(final Handler mHandler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
               /* URL url;
                try {
                    url = new URL("http://www.wode369.com/jf/front/home");//http://www.wode369.com/jf/front/home   https://www.baidu.com/
                    InputStream stream = url.openStream();
                    mHandler.sendEmptyMessage(0);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                }*/
                URL url = null;
                try {
                    url = new URL("https://www.baidu.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        System.out.println("NetUtils.run 200");
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                }


            }
        }).start();


    }


    /**
     * @return
     * @author cat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     */
    public static void ping(final Handler mHandler) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    String ip = "https://www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
                    Process p = Runtime.getRuntime().exec("/system/bin/ping -c 3 -w 10 " + ip);//  -c ping网址3次  -w 10  以秒为单位指定超时间隔，是指超时时间为10秒

                    // 读取ping的内容，可不加。
                    InputStream input = p.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    StringBuffer stringBuffer = new StringBuffer();
                    String content = "";
                    while ((content = in.readLine()) != null) {
                        stringBuffer.append(content);
                    }
                    Log.i("TTT", "result content : " + stringBuffer.toString());


                    // PING的状态
                    int status = p.waitFor();
                    if (status == 0) {
                        result = "successful~";
                        mHandler.sendEmptyMessage(0);
                    } else {
                        result = "failed~ cannot reach the IP address";
                        mHandler.sendEmptyMessage(1);
                    }

                } catch (IOException e) {
                    result = "failed~ IOException";
                    mHandler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    result = "failed~ InterruptedException";
                    mHandler.sendEmptyMessage(1);
                } finally {
                    Log.i("TTT", "result = " + result);
                }


            }
        }).start();


    }


}
