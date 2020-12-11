package indi.gjm;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 单客户端
 * @author : guanjm
 * @date: 2020/12/8
 *
 */
public class SingleClient {

    public static void main(String[] args) {
        //创建socket
        try (Socket socket = new Socket()) {
            //指定端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            socket.connect(inetSocketAddress);
            System.out.println("connect by: " + socket.getLocalSocketAddress());
            OutputStream outputStream = socket.getOutputStream();
            byte[] bytes = new byte[1024];
            for (int length; (length = System.in.read(bytes)) != -1;) {
                outputStream.write(bytes, 0 , length);
                if ("exit".equals(new String(bytes, 0 , length).replace("\n", ""))) {
                    socket.close();
                    System.out.println("close");
                    return;
                }
                bytes = new byte[1024];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
