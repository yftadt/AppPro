package com.library.baseui.utile.img;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageBase64 {
    //Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
    public static String convertBitmapToBase64Def(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    public static Bitmap convertBase64ToBitmap(String temp) {
        Bitmap img = null;
        try {
            String str=temp;
            //使用  Base64.DEFAULT 要去掉前缀，注意前缀是否去除
            str=str.replace("data:image/jpeg;base64,","");
            Log.d("base64转图得到数据", str);
            byte[] data = Base64.decode(str, Base64.DEFAULT);
            img = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.d("base64转图成功？",""+(img!=null));
        } catch (Exception e) {
            Log.d("base64转图失败", e.getMessage());
        }
        return img;
    }

}
