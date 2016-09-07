package com.app.utiles.other;

import android.content.Context;
import android.content.SharedPreferences;


import com.app.ui.activity.base.BaseApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 私有数据：data目录 */
public class DataSave {
	private final static String NAME = "data";
	private static SharedPreferences shared;

	/** 保存数据 */
	public static void stringSave(String name, Object value) {
		if (shared == null) {
			shared = BaseApplication.context.getSharedPreferences(NAME, 0);
		}
		shared.edit().putString(name, String.valueOf(value)).commit();
		shared = null;
	}

	/** 读取数据 */
	public static String stringGet(String name) {
		if (shared == null) {
			shared = BaseApplication.context.getSharedPreferences(NAME, 0);
		}
		return shared.getString(name, "");
	}


	/**
	 * 将obj 存储到data目录
	 *
	 * @param obj
	 * @param fileName
	 */
	public static void objectSave(Object obj, String fileName) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fis = BaseApplication.context.openFileOutput(
					fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 取Object
	 *
	 * @param fileName
	 * @return
	 */
	public static Object objectGet(String fileName) {
		Object obj = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
					BaseApplication.context.openFileInput(fileName));
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
