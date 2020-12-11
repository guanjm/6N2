package indi.gjm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 多客户端
 * @author : guanjm
 * @date: 2020/12/8
 *
 */
public class MultiClient {

    public static void main(String[] args) {
        int count = 6_0000;
        //指定端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
        for (int i = 0; i < count; i++) {
            try (Socket socket = new Socket()) {
                socket.connect(inetSocketAddress);
                System.out.println("connect by：" + socket.getLocalSocketAddress());
            } catch (IOException e) {
                return;
            }
        }
    }

}
