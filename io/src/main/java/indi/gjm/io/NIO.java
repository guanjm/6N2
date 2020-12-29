package indi.gjm.io;

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
            //不设置false，accept[阻塞]
            serverSocketChannel.configureBlocking(false);

            //存储连接
            List<SocketChannel> container = new ArrayList<>();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            while (true) {
                //获取请求，当无请求时返回null
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel != null) {
                    System.out.println("accept_no_" + count++ + ": " +  socketChannel.getRemoteAddress());
                    //不设置false，read[阻塞]
                    socketChannel.configureBlocking(false);
                    container.add(socketChannel);
                }

                Iterator<SocketChannel> iterator = container.iterator();
                SocketChannel channel = null;
                try {
                    while (iterator.hasNext()) {
                        channel = iterator.next();
                        int length;
                        while ((length = channel.read(byteBuffer)) > 0) {
                            //读模式
                            byteBuffer.flip();
                            System.out.print("client_"+ channel.socket().getPort() +": ");
                            while (byteBuffer.hasRemaining()) {
                                System.out.print((char) byteBuffer.get());
                            }
                            System.out.println();
                            byteBuffer.clear();
                        }
                        if (length == -1) {
                            iterator.remove();
                            System.out.println("client_"+ channel.socket().getPort() +": close");
                        }
                    }
                } catch (IOException e) {
                    iterator.remove();
                    System.out.println("client_"+ channel.socket().getPort() +": close");
                } catch (Exception e) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
