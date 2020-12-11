package indi.gjm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 多路复用IO
 * @author : guanjm
 * @date: 2020/12/11
 *
 */
public class MultiplexingIO {

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            //记录连接数
            int count = 1;
            //指定端口
            InetSocketAddress inetSocketAddress = new InetSocketAddress(8001);
            //指定请求队列最大长度
            int backlog = 50;
            serverSocketChannel.bind(inetSocketAddress, backlog);
            //不设置false，报IllegalBlockingModeException
            serverSocketChannel.configureBlocking(false);

            //多路复用器
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //[阻塞]获取变更的channel

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            while (true) {
                int select = selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel channel = socketChannel.accept();
                        System.out.println("accept_no_" + count++ + ": " + channel.getRemoteAddress());
                        //不设置false，报IllegalBlockingModeException
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        while (channel.read(byteBuffer) > 0) {
                            //读模式
                            byteBuffer.flip();
                            System.out.print("client_"+ channel.socket().getPort() +": ");
                            while (byteBuffer.hasRemaining()) {
                                System.out.print((char)byteBuffer.get());
                            }
                            System.out.println();
                            byteBuffer.clear();
                        }
                    }
                    iterator.remove();
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
