package indi.gjm;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            //记录连接数
            int count = 1;
            //指定端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            //指定请求队列最大长度
            int backlog = 50;
            serverSocketChannel.bind(inetSocketAddress, backlog);

            //存储连接
//            List<SocketChannel> container = new ArrayList<>();
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//            new Thread(() -> {
//                while (true) {
//                    for (SocketChannel channel : container) {
//                        try {
//                            while (channel.read(byteBuffer) != -1) {
//                                //读模式
//                                byteBuffer.flip();
//                                byte[] bytes = new byte[1024];
//                                byteBuffer.get(bytes);
//                                System.out.println(new String(bytes));
//                                byteBuffer.clear();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();

            while (true) {
                //获取请求，当无请求时返回null
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    System.out.println("accept success：" + channel);
//                    container.add(channel);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
