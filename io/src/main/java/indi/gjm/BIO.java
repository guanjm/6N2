package indi.gjm;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 阻塞IO（Blocking IO）
 * @author : guanjm
 * @date: 2020/12/8
 *
 */
public class BIO {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            int count = 1;
            //指定socket
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            //指定请求队列最大长度
            int backlog = 10;
            serverSocket.bind(inetSocketAddress, backlog);

            while (count <= 100) {
                //阻塞获取请求
                Socket socket = serverSocket.accept();
                final int clientNo = count++;
                System.out.println("accept success：" + socket);
                new Thread(() -> {
                    try (InputStream inputStream = socket.getInputStream()) {
                        byte[] bytes = new byte[1024];
                        while (inputStream.read(bytes) != -1) {
                            System.out.println("connect-" + clientNo +": " + new String(bytes));
                        }
                    } catch (Exception e) {
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
