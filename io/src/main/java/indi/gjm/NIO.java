package indi.gjm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
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
            serverSocketChannel.configureBlocking(false);

            //存储连接
            List<SocketChannel> container = new ArrayList<>();
            //获取连接数据线程
            new Thread(() -> {
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                while (true) {
                    Iterator<SocketChannel> iterator = container.iterator();
                    SocketChannel channel = null;
                    try {
                        while (iterator.hasNext()) {
                            channel = iterator.next();
                            while (channel.read(byteBuffer) != 0) {
                                System.out.print("channel：" + channel);
                                //读模式
                                byteBuffer.flip();
                                while (byteBuffer.hasRemaining()) {
                                    System.out.print((char) byteBuffer.get());
                                }
                                byteBuffer.clear();
                                System.out.println(" ");
                            }
                        }
                    } catch (IOException e) {
                        iterator.remove();
                        System.out.println("close channel：" + channel);
                    } catch (Exception e) {

                    }
                }
            }).start();

            while (true) {
                //获取请求，当无请求时返回null
                SocketChannel channel = serverSocketChannel.accept();
                if (channel != null) {
                    System.out.println("accept success：" + channel);
                    channel.configureBlocking(false);
                    container.add(channel);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
