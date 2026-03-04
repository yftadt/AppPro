package com.library.baseui.utile.file;

import android.graphics.Bitmap;
import android.util.Log;

import com.library.baseui.utile.img.ImageFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
    //==================第二种===============
    /**
     * 判断图片文件的格式（高效，基于文件头）
     *
     * @param file 目标图片文件
     * @return 图片格式枚举
     */
    public static  ImageFormat redImgFileType(File file) {
        // 校验文件合法性
        if (file == null || !file.exists() || !file.isFile() || file.length() < 2) {
            return  ImageFormat.UNKNOWN;
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
             ImageFormat imageFormat = getImageFormat(inputStream);
            Log.d("文件类型File", "type：" + imageFormat.name());
            return imageFormat;
        } catch (Exception e) {
            Log.d("文件类型File", "读取文件头失败：" + e.getMessage());
            return  ImageFormat.UNKNOWN;
        } finally {
            // 关闭输入流，避免内存泄漏
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public enum ImageFormat {
        JPEG, PNG, GIF, WEBP, BMP, HEIC, UNKNOWN
    }

    /**
     * 判断输入流中的图片格式（核心方法）
     *
     * @param inputStream 图片输入流（注意：流读取后会移动指针，若需复用流需重置）
     * @return 图片格式枚举
     */
    public static ImageFormat getImageFormat(InputStream inputStream) {
        byte[] headerBytes = new byte[16]; // 读取前16字节覆盖所有常用格式的文件头
        int readCount;
        try {
            readCount = inputStream.read(headerBytes);
        } catch (IOException e) {
            Log.e("文件类型File", "读取流失败：" + e.getMessage());
            return ImageFormat.UNKNOWN;
        }

        if (readCount < 2) {
            return ImageFormat.UNKNOWN;
        }

        // JPEG/JPG：文件头为 0xFF 0xD8
        if (headerBytes[0] == (byte) 0xFF && headerBytes[1] == (byte) 0xD8) {
            return ImageFormat.JPEG;
        }

        // PNG：文件头为 0x89 0x50 0x4E 0x47 0x0D 0x0A 0x1A 0x0A
        if (headerBytes[0] == (byte) 0x89 && headerBytes[1] == (byte) 0x50 && headerBytes[2] == (byte) 0x4E && headerBytes[3] == (byte) 0x47) {
            return ImageFormat.PNG;
        }

        // GIF：文件头为 GIF87a 或 GIF89a（前3字节 0x47 0x49 0x46）
        if (headerBytes[0] == (byte) 0x47 && headerBytes[1] == (byte) 0x49 && headerBytes[2] == (byte) 0x46) {
            return ImageFormat.GIF;
        }

        // WEBP：前4字节 0x52 0x49 0x46 0x46，第9-12字节 0x57 0x45 0x42 0x50
        if (readCount >= 12 && headerBytes[0] == (byte) 0x52 && headerBytes[1] == (byte) 0x49 && headerBytes[2] == (byte) 0x46 && headerBytes[3] == (byte) 0x46 && headerBytes[8] == (byte) 0x57 && headerBytes[9] == (byte) 0x45 && headerBytes[10] == (byte) 0x42 && headerBytes[11] == (byte) 0x50) {
            return ImageFormat.WEBP;
        }

        // BMP：文件头为 0x42 0x4D
        if (headerBytes[0] == (byte) 0x42 && headerBytes[1] == (byte) 0x4D) {
            return ImageFormat.BMP;
        }

        // HEIC/HEIF：第5-11字节匹配 0x66 0x74 0x79 0x70 0x68 0x65
        if (readCount >= 12 && headerBytes[4] == (byte) 0x66 && headerBytes[5] == (byte) 0x74 && headerBytes[6] == (byte) 0x79 && headerBytes[7] == (byte) 0x70 && headerBytes[8] == (byte) 0x68 && headerBytes[9] == (byte) 0x65) {
            return ImageFormat.HEIC;
        }

        // 未匹配到已知格式
        return ImageFormat.UNKNOWN;
    }
    //====================================第三种
    public static void redImgBitType(Bitmap bitmap) {
        // 步骤1: 将Bitmap转换为字节流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 使用JPEG格式压缩图片，质量为100%，只关心数据转换，不进行图像压缩展示
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] bitmapData = outputStream.toByteArray();
        // 步骤2: 读取前100字节
        byte[] first100Bytes = new byte[100];
        System.arraycopy(bitmapData, 0, first100Bytes, 0, Math.min(bitmapData.length, 100));
        redImgType(first100Bytes);

    }

    private static void redImgType(byte[] data) {
        String headerString = new String(data, Charset.forName("utf-8"));
        Logx.d("图片类型4", headerString);
    }
}
