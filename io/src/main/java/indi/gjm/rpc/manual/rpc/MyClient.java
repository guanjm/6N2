package indi.gjm.rpc.manual.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

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
     * 远程调用（阻塞）
     * @author : guanjm
     * @date: 2020/12/22
     * @param protocol      协议
     * @param data          数据包
     *
     */
    public static ByteBuf invokeBlocking(String protocol, ByteBuf data) {
        MyConnection connection = null;
        try {
            //获取连接
            connection = getConnectionPool().getConnection(protocol);
            //添加处理器
            connection.addHandler(new MyHandlerForClientBlocking(connection));
            //发送
            connection.write(data);
            //读取
//            return connection.read();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //释放连接
            getConnectionPool().releaseConnection(connection);
        }
    }


}

class MyHandlerForClientBlocking extends ChannelInboundHandlerAdapter {

    private final MyConnection connection;

    public MyHandlerForClientBlocking(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        synchronized (connection) {
            if (msg instanceof ByteBuf) {
                connection.setByteBuf((ByteBuf) msg);
                connection.notifyAll();
            }
        }
    }

}