package com.example.usbreadwriterdaemon.utils;

/**
 * Created by yuanpk on 2018/8/2  14:35
 * <p>
 * Description:TODO
 */

import android.os.Environment;

import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * 文件操作工具类
 *
 * @author ldm
 * @description：
 * @date 2016-4-28 下午3:17:10
 */
public final class FileUtil2 {
    public static final String DEFAULT_BIN_DIR = "usb";

    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getExternalStorageDirectory()
                .getAbsoluteFile()
                + File.separator
                + folderName
                + File.separator);
        file.mkdirs();
        return file;
    }

    /**
     * 关闭流
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void redFileStream(OutputStream os, InputStream is) throws IOException {
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        os.close();
        is.close();
    }

    /**
     * @description 把本地文件写入到U盘中
     * @author ldm
     * @time 2017/8/22 10:22
     */
    public static void saveSDFile2OTG(final File f, final UsbFile usbFile) {
        UsbFile uFile = null;
        FileInputStream fis = null;
        try {//开始写入
            fis = new FileInputStream(f);//读取选择的文件的
            if (usbFile.isDirectory()) {//如果选择是个文件夹
                UsbFile[] usbFiles = usbFile.listFiles();
                if (usbFiles != null && usbFiles.length > 0) {
                    for (UsbFile file : usbFiles) {
                        if (file.getName().equals(f.getName())) {
                            file.delete();
                        }
                    }
                }
                uFile = usbFile.createFile(f.getName());
                UsbFileOutputStream uos = new UsbFileOutputStream(uFile);
                try {
                    redFileStream(uos, fis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
