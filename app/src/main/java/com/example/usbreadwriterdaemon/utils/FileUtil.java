package com.example.usbreadwriterdaemon.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.EncodingUtils;


/**
 * Created by chaos on 2016/5/9.
 */
public class FileUtil {
    private static final String TAG = "FileUtil ";
    // 文件分隔符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR + "config" + FILE_SEPARATOR;
    // private static final String FILE_PATH = Environment.getExternalStorageDirectory() + FILE_SEPARATOR ;
    public static final String FILE_NAME = FILE_PATH + "gateway_config.txt";

    // 根缓存目录
    private static String cacheRootPath = "";

    public static void write(String content) {
        if (content != null && !content.equals("")) {
            File fileDirectory = new File(FILE_PATH);
            if (!fileDirectory.exists()) {
                fileDirectory.mkdir();
            }
            FileOutputStream fileOutputStream = null;
            try {
                byte[] buffer = content.getBytes();
                fileOutputStream = new FileOutputStream(new File(FILE_NAME));
                fileOutputStream.write(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static String read(String filePath) {
        String content = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int length = fileInputStream.available();
            byte[] buffer = new byte[length];
            fileInputStream.read(buffer);
            content = EncodingUtils.getString(buffer, "UTF-8");
            System.out.println(TAG + "read content = " + content);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static String Read(String filePath) {
        StringBuffer stringBuffer = new StringBuffer();
        String path = filePath;

        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        /*if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
            ToastUtil.showToast("The File doesn't not exist.");
        } else {*/
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        //newList.add(line+"\n");
                        stringBuffer.append(line + "\n");

                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                ToastUtil.showToast("The File doesn't not exist.");
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                ToastUtil.showToast(e.getMessage());
                Log.d("TestFile", e.getMessage());
            }
        //}

        return stringBuffer.toString()+"  ypk";
    }


    public static String read() {
        String content = "";
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
                int length = fileInputStream.available();
                byte[] buffer = new byte[length];
                fileInputStream.read(buffer);
                content = EncodingUtils.getString(buffer, "UTF-8");
                System.out.println(TAG + "read content = " + content);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(TAG + "read file inExists");
        }
        return content;
    }


    /**
     * sd卡是否可用
     *
     * @return
     */
    public boolean isSdCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public String createRootPath(Context mContext) {
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = mContext.getApplicationContext().getExternalCacheDir()
                    .getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = mContext.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    private static String createDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }


    /**
     * 删除文件或者文件夹
     *
     * @param file
     */
    public static void deleteFileOrDirectory(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                // 删除空文件夹
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }
                // 递归删除文件夹下的子文件
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFileOrDirectory(childFiles[i]);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将内容写入文件
     *
     * @param filePath eg:/mnt/sdcard/demo.txt
     * @param content  内容
     */
    public static void writeFileSdcard(String filePath, String content,
                                       boolean isAppend) {

        try {
            FileOutputStream fout = new FileOutputStream(filePath, isAppend);
            byte[] bytes = content.getBytes();

            fout.write(bytes);

            fout.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    /**
     * 判断当前线程是否为主线程
     *
     * @return true:是主线程
     */
    public static boolean isInMainThread() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i(TAG, " myLooper=" + myLooper + ";mainLooper=" + mainLooper);
        return myLooper == mainLooper;
    }


}
