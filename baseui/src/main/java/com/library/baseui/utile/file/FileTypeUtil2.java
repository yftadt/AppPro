package com.library.baseui.utile.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sj.mblog.Logx;

public class FileTypeUtil2 {

    // 文件头与文件类型映射表
    private static final Map<String, String> FILE_HEADERS = new HashMap<String, String>() {{
        put("FFD8FF", "jpg");
        put("89504E47", "png");
        put("47494638", "gif");
        put("49492A00", "tif");
        put("424D", "bmp");
        put("41433130", "dwg");
        put("38425053", "psd");
        put("7B5C727466", "rtf");
        put("3C3F786D6C", "xml");
        put("68746D6C3E", "html");
        put("44656C69766572792D646174653A", "eml");
        put("D0CF11E0", "doc");
        put("5374616E64617264204A", "mdb");
        put("25504446", "pdf");
        put("504B0304", "zip");
        put("52617221", "rar");
        put("57415645", "wav");
        put("41564920", "avi");
        put("2E7261FD", "ram");
        put("2E524D46", "rm");
        put("000001BA", "mpg");
        put("000001B3", "mpg");
        put("6D6F6F76", "mov");
        put("3026B2758E66CF11", "asf");
        put("4D546864", "mid");
    }};

    /**
     * 新增方法：通过文件头判断文件类型
     * @param file 文件
     * @return 文件类型(扩展名)，如"jpg"，未知类型返回空字符串
     */
    public static String getFileTypeByHeader(File file) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(file);
            byte[] b = new byte[8]; // 读取前8个字节
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
            Logx.d("文件类型--》value="+value);
            // 遍历文件头映射表
            for (Map.Entry<String, String> entry : FILE_HEADERS.entrySet()) {
                String header = entry.getKey();
                if (value.startsWith(header)) {
                    return entry.getValue();
                }
            }
        } catch (Exception e) {
            Logx.d("获取类型失败--》"+e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 将字节数组转换为十六进制字符串
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString().toUpperCase();
    }

}
