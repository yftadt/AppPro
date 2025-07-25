package com.library.baseui.utile.other;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.library.baseui.utile.time.DateUtile;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

public class APKSignature {

    // 如需要小写则把ABCDEF改成小写
    private final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * "检测应用程序是否是用CN=Android Debug,O=Android,C=US"的debug信息来签名的
     * 判断签名是debug签名还是release签名
     */
    private final X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");
    private Signature[] signatures;

    public APKSignature(Signature[] signatures) {
        this.signatures = signatures;
    }

    public APKSignature(Context context, String applicationId) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(applicationId,
                    PackageManager.GET_SIGNATURES);
            this.signatures = info.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * 进行转换
     */
    private String toHexString(byte[] bData) {
        StringBuilder sb = new StringBuilder(bData.length * 2);
        for (int i = 0; i < bData.length; i++) {
            sb.append(HEX_DIGITS[(bData[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[bData[i] & 0x0f]);
        }
        return sb.toString();
    }

    //比较MD5
    public boolean isMd5(String md5) {
        String temp = getMD5();
        if (TextUtils.isEmpty(temp)) {
            return true;
        }
        temp = temp.toUpperCase();
        md5 = md5.toUpperCase();
        return temp.equals(md5);
    }

    /**
     * 返回MD5
     */
    public String getMD5() {
        return getValue("MD5");
    }

    /**
     * SHA1
     */
    public String getSHA1() {
        return getValue("SHA-1");

    }

    /**
     * SHA256
     */
    public String getSHA256() {
        return getValue("SHA-256");
    }

    private String getValue(String type) {
        String value = "";
        if (signatures == null) {
            return value;
        }
        MessageDigest digest = null;
        switch (type) {
            case "SHA-256":
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case "SHA-1":
                try {
                    digest = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
            case "MD5":
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
        if (digest == null) {
            return value;
        }
        for (Signature s : signatures) {
            digest.update(s.toByteArray());
        }
        value = toHexString(digest.digest());
        return value;
    }

    /**
     * 判断签名是debug签名还是release签名
     *
     * @return true = 开发(debug.keystore)，false = 上线发布（非.android默认debug.keystore）
     */
    public boolean isDebuggable() {
        // 判断是否默认key(默认是)
        boolean debuggable = true;
        try {
            for (int i = 0, c = signatures.length; i < c; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (Exception e) {
        }
        return debuggable;
    }

    /**
     * 获取App 证书对象
     */
    public X509Certificate getX509Certificate() {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream stream = new ByteArrayInputStream(signatures[0].toByteArray());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
            return cert;
        } catch (Exception e) {
        }
        return null;
    }

    public static void test(Context context, String packName) {
        PackageManager manager = context.getPackageManager();
        try {

            PackageInfo info = manager.getPackageInfo(packName, PackageManager.GET_SIGNATURES);
            test(info);
        } catch (PackageManager.NameNotFoundException e) {
            BaseUILog.e("获取签名信息失败");
            e.printStackTrace();
        }
    }

    private static void test(PackageInfo info) {
        APKSignature apkInfo = new APKSignature(info.signatures);
        String md5 = "MD5：" + apkInfo.getMD5(); // 设置MD5
        String sha1 = "SHA1：" + apkInfo.getSHA1(); // 设置SHA1
        String sha256 = "SHA256：" + apkInfo.getSHA256(); // 设置SHA256
        // 获取证书对象
        X509Certificate cert = apkInfo.getX509Certificate();
        //
        Date time = cert.getNotBefore();
        Date time2 = cert.getNotAfter();
        String validity = "有效期：" +
                DateUtile.getDateFormat(time, DateUtile.DATA_FORMAT_YMD) +
                " 至 " +
                DateUtile.getDateFormat(time2, DateUtile.DATA_FORMAT_YMD);
        //
        // 证书是否过期 true = 过期,false = 未过期
        boolean isEffective = false;
        try {
            cert.checkValidity();
        } catch (CertificateExpiredException e) {
            //CertificateExpiredException - 如果证书已过期。
            e.printStackTrace();
            isEffective = true;
        } catch (CertificateNotYetValidException e) {
            // CertificateNotYetValidException - 如果证书不再有效。
            e.printStackTrace();
            isEffective = true;
        }
        String effective = "是否过期：" + isEffective;
        // 证书发布方
        String principal = "证书发布方：" + cert.getIssuerX500Principal().toString();
        // 证书版本号
        String principalVersion = "证书版本号：" + cert.getVersion();
        // 证书算法名称
        String principalSigAlgName = "证书算法名称：" + cert.getSigAlgName();
        // 证书算法OID
        String principalOID = "证书算法OID：" + cert.getSigAlgOID();
        // 证书机器码
        String principalNumer = "证书机器码：" + cert.getSerialNumber().toString();
        //公匙
        String pubKey = "公匙：" + cert.getPublicKey().toString();   //公钥
        String subjectDN = "subjectDN：" + cert.getSubjectDN();

        // 证书 DER编码
        String principalDER = "DER：";
        try {
            byte[] tbs = cert.getTBSCertificate();
            principalDER = "DER：" + apkInfo.toHexString(tbs);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        String value = pubKey + "\n" + md5 + "\n" + sha1 + "\n" + sha256 + "\n" + validity + "\n" + effective + "\n" +
                principal + "\n" + principalVersion + "\n" + principalSigAlgName + "\n" +
                principalOID + "\n" + principalNumer + "\n" + principalDER + "\n" + subjectDN;
        BaseUILog.e(value);
    }
}
