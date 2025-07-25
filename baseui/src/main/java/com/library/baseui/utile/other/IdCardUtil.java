package com.library.baseui.utile.other;

import android.text.TextUtils;

import com.library.baseui.utile.time.CalendarUtile;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类
 *
 * @author guom
 * @version 2.0, 2019-05-15
 */

public class IdCardUtil {

    /**
     * 身份证号码共18位，由17位本体码和1位校验码组成
     * 前6位是地址码，表示登记户口时所在地的行政区划代码，依照《中华人民共和国行政区划代码》国家标准（GB/T2260）的规定执行；
     * 7到14位是出生年月日，采用YYYYMMDD格式；
     * 15到17位是顺序码，表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编订的顺序号，顺序码的奇数分配给男性，偶数分配给女性，即第17位奇数表示男性，偶数表示女性；
     * 第18位是校验码，采用ISO 7064:1983, MOD 11-2校验字符系统
     * <p>
     * 一代身份证与二代身份证的区别在于：
     * <p>
     * 一代身份证是15位，二代身份证是18位；
     * 一代身份证出生年月日采用YYMMDD格式，二代身份证出生年月日采用YYYYMMDD格式；
     * 一代身份证无校验码，二代身份证有校验码。
     */

    // 18位二代身份证号码的正则表达式
    private static final String REGEX_ID_NO_18 = "^"
            + "\\d{6}" // 6位地区码
            + "(18|19|([23]\\d))\\d{2}" // 年YYYY
            + "((0[1-9])|(10|11|12))" // 月MM
            + "(([0-2][1-9])|10|20|30|31)" // 日DD
            + "\\d{3}" // 3位顺序码
            + "[0-9Xx]" // 校验码
            + "$";
    /**
     * 1-2位省、自治区、直辖市代码；
     * <p>
     * 3-4位地级市、盟、自治州代码；
     * <p>
     * 5-6位县、县级市、区代码；
     * <p>
     * 7-12位出生年月日,比如670401代表1967年4月1日，与18位的第一个区别；
     * <p>
     * 13-15位为顺序号，其中15位男为单数，女为双数；
     */
    // 15位一代身份证号码的正则表达式
    private static final String REGEX_ID_NO_15 = "^"
            + "\\d{6}" // 6位地区码
            + "\\d{2}" // 年YYYY
            + "((0[1-9])|(10|11|12))" // 月MM
            + "(([0-2][1-9])|10|20|30|31)" // 日DD
            + "\\d{3}"// 3位顺序码
            + "$";

    // 验证身份证是否合法
    public static boolean validateCard(String idCard) {
        boolean isCard = isNo18(idCard);
        if (!isCard) {
            isCard = isNo15(idCard);
        }
        return isCard;
    }

    //检验18位身份证号码
    public static boolean isNo18(String idNo18) {
        boolean isNo18 = false;
        if (idNo18 == null || idNo18.length() != 18) {
            return isNo18;
        }
        boolean isVerify = idNo18.matches(REGEX_ID_NO_18);
        if (!isVerify) {
            return isNo18;
        }
        String verifyNumber = onVerifyNumber(idNo18);
        String tempVerify = idNo18.substring(17, 18);
        if (!TextUtils.isEmpty(verifyNumber) && verifyNumber.equals(tempVerify)) {
            isNo18 = true;
        }
        return isNo18;
    }

    /**
     * 计算校验码
     * <p>
     * 适用于18位的二代身份证号码
     * 用前17位号码 算出第18位
     * </p>
     *
     * @param idNo18 身份证
     * @return 校验码
     */
    private static String onVerifyNumber(String idNo18) {
        // 加权因子
        int[] W = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] masterNumberArray = idNo18.toCharArray();
        int sum = 0;
        for (int i = 0; i < W.length; i++) {
            sum += Integer.parseInt(String.valueOf(masterNumberArray[i])) * W[i];
        }
        if (sum == 0) {
            return "";
        }
        //校验码数组
        String[] verifyNumbers = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        // 得到校验码
        int index = sum % 11;
        if (index >= verifyNumbers.length) {
            return "";
        }
        String checkNumber = verifyNumbers[index];
        // 返回校验码
        return checkNumber;
    }

    //检验15位身份证号码
    public static boolean isNo15(String idNo15) {
        boolean isNo15 = false;
        // 校验身份证号码的长度
        if (idNo15 == null || idNo15.length() != 15) {
            return isNo15;
        }
        boolean isVerify = idNo15.matches(REGEX_ID_NO_15);
        if (!isVerify) {
            return isNo15;
        }
        //检验省份
        String provinceId = idNo15.substring(0, 2);
        String provinceName = getProvinceName(provinceId);
        if (TextUtils.isEmpty(provinceName)) {
            return isNo15;
        }
        //出生年月日
        String birthCode = idNo15.substring(6, 12);
        int y = NumberUtile.getStringToInt(19 + birthCode.substring(0, 2));
        if (y <= 0) {
            return isNo15;
        }
        int month = NumberUtile.getStringToInt(birthCode.substring(2, 4));
        if (month <= 0 || month > 12) {
            return isNo15;
        }
        int day = NumberUtile.getStringToInt(birthCode.substring(4, 6));
        if (day <= 0) {
            return isNo15;
        }
        //某月份的最后一天
        int dayBig = CalendarUtile.getMonthEndDay(y, month - 1);
        isNo15 = day <= dayBig;
        return isNo15;
    }

    /**
     * 15位一代身份证号码升级18位二代身份证号码
     * <p>
     * 为15位的一代身份证号码增加年份的前2位和最后1位校验码
     * </p>
     *
     * @param idNo15 15位的一代身份证号码
     * @return 18位的二代身份证号码
     */
    public static String updateIDNo15to18(String idNo15) {
        // 校验身份证号码的长度
        if (idNo15 == null || idNo15.length() != 15) {
            return "";
        }
        boolean isVerify = idNo15.matches(REGEX_ID_NO_15);
        if (!isVerify) {
            return "";
        }
        // 得到本体码，因一代身份证皆为19XX年生人，年份中增加19，组成4位
        String masterNumber = idNo15.substring(0, 6) + "19" + idNo15.substring(6);
        // 计算校验码
        String checkNumber = onVerifyNumber(masterNumber);
        // 返回本体码+校验码=完整的身份证号码
        return masterNumber + checkNumber;
    }

    //获取性别
    public static String getSex(String number) {
        String sex = "";
        if (TextUtils.isEmpty(number)) {
            return sex;
        }
        int length = number.length();
        int sexCode = -1;
        switch (length) {
            case 15:
                boolean isNo15 = isNo15(number);
                if (!isNo15) {
                    break;
                }
                String sexValue = number.substring(14, 15);
                sexCode = NumberUtile.getStringToInt(sexValue);
                break;
            case 18:
                boolean isNo18 = isNo18(number);
                if (!isNo18) {
                    break;
                }
                sexValue = number.substring(16, 17);
                sexCode = NumberUtile.getStringToInt(sexValue);
                break;
        }
        if (sexCode == -1) {
            return sex;
        }
        if (sexCode % 2 != 0) {
            sex = "男";
        } else {
            sex = "女";
        }
        return sex;
    }

    //获取年龄
    public static String getAge(String number) {
        String age = "";
        if (TextUtils.isEmpty(number)) {
            return age;
        }
        int birthYer = 0;
        int length = number.length();
        switch (length) {
            case 15:
                boolean isNo15 = isNo15(number);
                if (!isNo15) {
                    break;
                }
                String birthCode = number.substring(6, 8);
                birthYer = NumberUtile.getStringToInt(19 + birthCode);

                break;
            case 18:
                boolean isNo18 = isNo18(number);
                if (!isNo18) {
                    break;
                }
                birthYer = NumberUtile.getStringToInt(number.substring(6, 10));

                break;
        }
        if (birthYer <= 0) {
            return age;
        }
        //
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int y = nowYear - birthYer;
        if (y <= 0) {
            return age;
        }
        //
        age = String.valueOf(y);
        return age;
    }

    //获取年龄
    public static String getAgeAccurate(String number) {
        String age = "";
        if (TextUtils.isEmpty(number)) {
            return age;
        }
        int birthYer = 0;
        int month = 0;
        int day = 0;
        int length = number.length();
        switch (length) {
            case 15:
                boolean isNo15 = isNo15(number);
                if (!isNo15) {
                    break;
                }
                String birthCode = number.substring(6, 8);
                birthYer = NumberUtile.getStringToInt(19 + birthCode);
                month = NumberUtile.getStringToInt(number.substring(8, 10));
                day = NumberUtile.getStringToInt(number.substring(10, 12));
                break;
            case 18:
                boolean isNo18 = isNo18(number);
                if (!isNo18) {
                    break;
                }
                birthYer = NumberUtile.getStringToInt(number.substring(6, 10));
                month = NumberUtile.getStringToInt(number.substring(10, 12));
                day = NumberUtile.getStringToInt(number.substring(12, 14));
                break;
        }
        BaseUILog.e("年月日：" + birthYer + "-" + month + "-" + day);
        if (birthYer <= 0) {
            return age;
        }
        //
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int y = nowYear - birthYer;
        if (y <= 0) {
            return age;
        }
        if (nowMonth < month) {
            y -= 1;
        }
        if (nowMonth == month && nowDay < day) {
            y -= 1;
        }
        //
        age = String.valueOf(y);
        return age;
    }

    //获取生日
    public static String getBirth(String number) {
        String birth = "";
        if (TextUtils.isEmpty(number)) {
            return birth;
        }
        int length = number.length();
        switch (length) {
            case 15:
                boolean isNo15 = isNo15(number);
                if (!isNo15) {
                    break;
                }
                birth = 19 + number.substring(6, 12);
                break;
            case 18:
                boolean isNo18 = isNo18(number);
                if (!isNo18) {
                    break;
                }
                birth = number.substring(6, 14);
                break;
        }
        return birth;
    }

    //获取省份
    public static String getCardProvinceName(String number) {
        String provinceName = "";
        if (TextUtils.isEmpty(number)) {
            return provinceName;
        }
        int length = number.length();
        String provinceId = "";
        switch (length) {
            case 15:
                boolean isNo15 = isNo15(number);
                if (!isNo15) {
                    break;
                }
                provinceId = number.substring(0, 2);
                break;
            case 18:
                boolean isNo18 = isNo18(number);
                if (!isNo18) {
                    break;
                }
                provinceId = number.substring(0, 2);
                break;
        }
        provinceName = getProvinceName(provinceId);
        return provinceName;
    }

    private static Map<String, String> provinceCodes = new HashMap();

    //获取省份
    private static String getProvinceName(String provinceId) {
        if (TextUtils.isEmpty(provinceId)) {
            return "";
        }
        if (provinceCodes.size() == 0) {
            provinceCodes.put("11", "北京");
            provinceCodes.put("12", "天津");
            provinceCodes.put("13", "河北");
            provinceCodes.put("14", "山西");
            provinceCodes.put("15", "内蒙古");
            provinceCodes.put("21", "辽宁");
            provinceCodes.put("22", "吉林");
            provinceCodes.put("23", "黑龙江");
            provinceCodes.put("31", "上海");
            provinceCodes.put("32", "江苏");
            provinceCodes.put("33", "浙江");
            provinceCodes.put("34", "安徽");
            provinceCodes.put("35", "福建");
            provinceCodes.put("36", "江西");
            provinceCodes.put("37", "山东");
            provinceCodes.put("41", "河南");
            provinceCodes.put("42", "湖北");
            provinceCodes.put("43", "湖南");
            provinceCodes.put("44", "广东");
            provinceCodes.put("45", "广西");
            provinceCodes.put("46", "海南");
            provinceCodes.put("50", "重庆");
            provinceCodes.put("51", "四川");
            provinceCodes.put("52", "贵州");
            provinceCodes.put("53", "云南");
            provinceCodes.put("54", "西藏");
            provinceCodes.put("61", "陕西");
            provinceCodes.put("62", "甘肃");
            provinceCodes.put("63", "青海");
            provinceCodes.put("64", "宁夏");
            provinceCodes.put("65", "新疆");
        }
        String provinceName = provinceCodes.get(provinceId);
        return provinceName;
    }
}
