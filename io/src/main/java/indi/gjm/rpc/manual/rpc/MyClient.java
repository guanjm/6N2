package indi.gjm.rpc.manual.rpc;

import indi.gjm.rpc.manual.protocol.MyProtocolImpl;
import indi.gjm.rpc.manual.protocol.MyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

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
            return connection.readBlocking();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //释放连接
            getConnectionPool().releaseConnection(connection);
        }
    }

    /**
     * 远程调用（非阻塞）
     * @author : guanjm
     * @date: 2021/1/4
     * @param protocol      协议
     * @param data          数据包
     *
     */
    public static ByteBuf invokeNonBlocking(String protocol, ByteBuf data) {
        MyConnection connection = null;
        try {
            //获取连接
            connection = getConnectionPool().getConnection(protocol);
            //事件
            Event event = new Event();
            //添加处理器
            connection.addHandler(new MyHandlerForClientNonBlocking(event));
            //发送
            connection.write(data);
            //释放连接
            getConnectionPool().releaseConnection(connection);
            //读取
            return event.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

class MyHandlerForClientNonBlocking extends ChannelInboundHandlerAdapter {

    private static AtomicInteger count = new AtomicInteger(0);

    private final Event event;

    public MyHandlerForClientNonBlocking(Event event) {
        this.event = event;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        synchronized (event) {
            if (msg instanceof ByteBuf) {
                event.setByteBuf((ByteBuf) msg);
                event.notifyAll();
                Object decode = MyProtocolImpl.decode((ByteBuf) msg);
                System.out.println("read1:"+count.incrementAndGet() +": "+ ((MyResponse)decode).getId());
            }
        }
    }

}

class Event{

    private static AtomicInteger count = new AtomicInteger(0);

    private ByteBuf byteBuf;

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public ByteBuf read() throws InterruptedException {
        synchronized (this){
            while (byteBuf == null) {
                wait();
            }
            Object decode = MyProtocolImpl.decode(byteBuf);
            System.out.println("read2:"+count.incrementAndGet() +": "+ ((MyResponse)decode).getId());
            return byteBuf;
        }
    }

}