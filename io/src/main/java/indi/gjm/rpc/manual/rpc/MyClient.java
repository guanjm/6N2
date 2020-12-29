package indi.gjm.rpc.manual.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class MyClient {

    private static MyConnectionPool connectionPool;

    private MyClient() {}

    public static synchronized MyConnectionPool getConnectionPool() {
        if (connectionPool == null) {
            connectionPool = new MyConnectionPool("127.0.0.1", 8001, 10);
        }
        return connectionPool;
    }

    /**
     * 远程调用
     * @author : guanjm
     * @date: 2020/12/22
     * @param protocol      协议
     * @param data          数据包
     *
     */
    public static ByteBuf invoke(String protocol, ByteBuf data) {
        MyConnection connection = null;
        try {
            //获取连接
            connection = getConnectionPool().getConnection(protocol);
            SocketChannel socketChannel = connection.getSocketChannel();
            ChannelPipeline pipeline = socketChannel.pipeline();
            MyHandlerForClient handler = new MyHandlerForClient();
            //添加处理器
            pipeline.addLast(handler);
            //发送
            socketChannel.writeAndFlush(data).sync();
            Object response = handler.getResponse();
            //移除处理器
            pipeline.remove(handler);
            return (ByteBuf) response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //释放连接
            getConnectionPool().releaseConnection(connection);
        }
    }

}

class MyHandlerForClient extends ChannelInboundHandlerAdapter {

    final byte[] lock = new byte[0];
    private ByteBuf byteBuf;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        synchronized (lock) {
            if (msg instanceof ByteBuf) {
                byteBuf = (ByteBuf) msg;
                lock.notifyAll();
            }
        }
    }

    Object getResponse() throws InterruptedException {
        synchronized (lock) {
            while (byteBuf == null) {
                lock.wait();
            }
            return byteBuf;
        }
    }
}