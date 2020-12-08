package indi.gjm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * NEW IO
 * @author : guanjm
 * @date: 2020/12/8
 *
 */
public class NIO {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            //指定端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            //指定请求队列最大长度
            int backlog = 50;
            serverSocket.bind(inetSocketAddress, backlog);

            //存储连接
            List<SocketChannel> container = new ArrayList<>();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            new Thread(() -> {
                while (true) {
                    for (SocketChannel channel : container) {
                        try {
                            while (channel.read(byteBuffer) != -1) {
                                //读模式
                                byteBuffer.flip();
                                byte[] bytes = new byte[1024];
                                byteBuffer.get(bytes);
                                System.out.println(new String(bytes));
                                byteBuffer.clear();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            ServerSocketChannel serverSocketChannel = serverSocket.getChannel();
            while (true) {
                //获取请求，当无请求时返回null
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    container.add(channel);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
