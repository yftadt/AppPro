package com.app.net.common;

import com.app.ui.activity.base.BaseApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/8.
 */

public class SSL {

    /*String url = Constraint.getUrl();
        if (url.startsWith("https")) {
        //https
        SslContextFactory sslContextFactory = new SslContextFactory();
        SSLSocketFactory sslSocketFactory = sslContextFactory.getSslSocket2().getSocketFactory();
        builder.socketFactory(sslSocketFactory);
        String hosts[] = {"https://api.gjwlyy.com/api/app"};
        // builder.hostnameVerifier(sslContextFactory.getHostnameVerifier(hosts));
        //Test.setSSLSocketFactory(builder);
        // Test2.getSSLSocketFactory(builder);
    }*/

    public OkHttpClient.Builder setSSLcer(OkHttpClient.Builder builder) {
        try {
            InputStream inputStream = BaseApplication.context.getAssets().open("check.cer");
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(inputStream));
            closeInputStream(inputStream);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
            //builder.socketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }

    public OkHttpClient.Builder setSSLbks2(OkHttpClient.Builder builder, int rawId) {
        InputStream keyStoreInput = null;
        try {
            //R.raw.keybase
            keyStoreInput = BaseApplication.context.getResources().openRawResource(rawId);
            //keyStoreType默认是BKS
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(keyStoreInput, null);
            //
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keyStore);
            //
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            KeyStore keyStore2 = KeyStore.getInstance("BKS");
            kmf.init(keyStore2, "123456".toCharArray());
            //
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //第一个参数是授权的密钥管理器，用来授权验证。
            //第二个是被授权的证书管理器，用来验证服务器端的证书。
            //第三个参数是一个随机数值，可以填写null
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
            builder.socketFactory(sslContext.getSocketFactory());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } finally {
            closeInputStream(keyStoreInput);
        }
        return builder;
    }

    public HostnameVerifier getHostnameVerifier(String[] hostUrls) {
        return new Hostname(hostUrls);
    }


    private void closeInputStream(InputStream keyStoreInput) {
        if (keyStoreInput == null) {
            return;
        }
        try {
            keyStoreInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            keyStoreInput = null;
        }
    }

    static class Hostname implements HostnameVerifier {
        private String[] hostUrls;

        public Hostname(String[] hostUrls) {
            this.hostUrls = hostUrls;
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            boolean ret = false;
            for (String host : hostUrls) {
                ret = host.equalsIgnoreCase(hostname);
                if (ret) {
                    break;
                }
            }
            return ret;
        }
    }
}
