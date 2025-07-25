package com.library.baseui.utile.file;

import android.content.Context;
import android.content.SharedPreferences;


import com.library.baseui.activity.BaseApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 私有数据：data目录 */
public class DataSave {
	private final static String NAME = "data";
	private static SharedPreferences shared;
	public static String REFRESH_TIME = "refresh_time";
	public static String TOKEN = "token";
	public static String LOGIN_NAME = "login_namme";
	public static String LOGIN_PWD = "login_pwd";
	//

	public final static String rn_entrance = "rn_entrance";//入口
	public final static String rn_version_name = "rn_version_name";//rn 版本
	public final static String rn_version_code = "rn_version_code";//rn 版本

	//厂商类型
	public static String MANUFACTURE_TYPE = "manufacturer_type";
	//是否支持角标
	public static String MANUFACTURE_BADGE = "manufacturer_badge";

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
