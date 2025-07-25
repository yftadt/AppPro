package test.app.net.socket;



import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import test.app.utiles.other.DLog;

public class TcpTimeoutExample {
    public   void test() {
        try {
            //InetAddress serverAddress = InetAddress.getByName("example.com");
            //InetAddress serverAddress = InetAddress.getLoopbackAddress();
            InetAddress serverAddress =  InetAddress.getLocalHost();
            String hotsAddr = serverAddress.getHostAddress();
            byte[] addr = serverAddress.getAddress();
            String hostName = serverAddress.getHostName();
            String canonicalHostName = serverAddress.getCanonicalHostName();
            DLog.e("===>"+"hotsAddr:"+hotsAddr+" hostName:"+hostName+" canonicalHostName:"+canonicalHostName);
            int port = 80;

            // 创建一个Socket实例
            Socket socket = new Socket();

            // 设置连接超时时间（单位：毫秒）
            int timeout = 3000; // 3秒
            socket.connect(new InetSocketAddress(serverAddress, port), timeout);

            // 连接成功后的操作...

        } catch (Exception e) {
            // 异常处理...
            DLog.e("===>"+e.getMessage());
        }
    }
}
