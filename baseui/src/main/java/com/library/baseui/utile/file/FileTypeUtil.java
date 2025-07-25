package com.library.baseui.utile.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import sj.mblog.Logx;

/**
 * 判断文件类型
 */
public class FileTypeUtil {

    /**
     * 获取文件的mimeType
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        if (file == null || !file.exists()) {
            return "--";
        }
        try {
            String mimeType = readType(file);
            return String.format("image/%s", mimeType);
        } catch (IOException e) {
            Logx.d("---->获取文件类型失败：" + e);
        }
        return null;
    }

    /**
     * 读取文件类型
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static String readType(File file) throws IOException {
        FileInputStream fis = null;
        try {
            File f = file;
            if (!f.exists() || f.isDirectory() || f.length() < 8) {
                throw new IOException("the file [" + f.getAbsolutePath() + "] is not image !");
            }

            fis = new FileInputStream(f);
            byte[] bufHeaders = readInputStreamAt(fis, 0, 8);
             //
            Logx.d("获取文件类型-->byte:"+new String(bufHeaders));
            if (isJPEGHeader(bufHeaders)) {
                long skiplength = f.length() - 2 - 8; //第一次读取时已经读了8个byte,因此需要减掉
                byte[] bufFooters = readInputStreamAt(fis, skiplength, 2);
                if (isJPEGFooter(bufFooters)) {
                    return "jpeg";
                }
            }
            if (isPNG(bufHeaders)) {
                return "png";
            }
            if (isGIF(bufHeaders)) {

                return "gif";
            }
            if (isWEBP(bufHeaders)) {
                return "webp";
            }
            if (isBMP(bufHeaders)) {
                return "bmp";
            }
            if (isICON(bufHeaders)) {
                return "ico";
            }
            throw new IOException("the image's format is unkown!");

        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * 标示一致性比较
     *
     * @param buf     待检测标示
     * @param markBuf 标识符字节数组
     * @return 返回false标示标示不匹配
     */
    private static boolean compare(byte[] buf, byte[] markBuf) {
        for (int i = 0; i < markBuf.length; i++) {
            byte b = markBuf[i];
            byte a = buf[i];

            if (a != b) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param fis        输入流对象
     * @param skiplength 跳过位置长度
     * @param length     要读取的长度
     * @return 字节数组
     * @throws IOException
     */
    private static byte[] readInputStreamAt(FileInputStream fis, long skiplength, int length) throws IOException {
        byte[] buf = new byte[length];
        fis.skip(skiplength);  //
        int read = fis.read(buf, 0, length);
        return buf;
    }

    private static boolean isBMP(byte[] buf) {
        byte[] markBuf = "BM".getBytes();  //BMP图片文件的前两个字节
        return compare(buf, markBuf);
    }

    private static boolean isICON(byte[] buf) {
        byte[] markBuf = {0, 0, 1, 0, 1, 0, 32, 32};
        return compare(buf, markBuf);
    }

    private static boolean isWEBP(byte[] buf) {
        byte[] markBuf = "RIFF".getBytes(); //WebP图片识别符
        return compare(buf, markBuf);
    }

    private static boolean isGIF(byte[] buf) {

        byte[] markBuf = "GIF89a".getBytes(); //GIF识别符
        if (compare(buf, markBuf)) {
            return true;
        }
        markBuf = "GIF87a".getBytes(); //GIF识别符
        if (compare(buf, markBuf)) {
            return true;
        }
        return false;
    }


    private static boolean isPNG(byte[] buf) {

        byte[] markBuf = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}; //PNG识别符
        // new String(buf).indexOf("PNG")>0 //也可以使用这种方式
        return compare(buf, markBuf);
    }

    /**
     * JPEG开始符
     *
     * @param buf
     * @return
     */
    private static boolean isJPEGHeader(byte[] buf) {
        byte[] markBuf = {(byte) 0xff, (byte) 0xd8};

        return compare(buf, markBuf);
    }

    /**
     * JPEG结束符
     *
     * @param buf
     * @return
     */
    private static boolean isJPEGFooter(byte[] buf) {
        byte[] markBuf = {(byte) 0xff, (byte) 0xd9};
        return compare(buf, markBuf);
    }
}
