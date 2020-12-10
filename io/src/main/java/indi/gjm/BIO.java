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
            //记录连接数
            int count = 1;
            //指定端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            //指定请求队列最大长度
            int backlog = 1;
            serverSocket.bind(inetSocketAddress, backlog);

            while (true) {
                //[阻塞]获取请求
                Socket socket = serverSocket.accept();
                final int ClientNo = count++;
                System.out.println("accept success：" + socket);
                //为每个请求创建新的线程，获取传输数据
                new Thread(() -> {
                    try (InputStream inputStream = socket.getInputStream()) {
                        byte[] bytes = new byte[1024];
                        //[阻塞]获取传输数据
                        while (inputStream.read(bytes) != -1) {
                            System.out.println("client-"+ClientNo + "-"+ socket.getPort() +": " + new String(bytes));
                        }
                        System.out.println("client-"+ClientNo + "-"+ socket.getPort() +": close");
                    } catch (Exception e) {
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
