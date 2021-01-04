package indi.gjm.rpc.manual.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池
 * @author : guanjm
 * @date: 2020/12/29
 *
 */
public class MyConnectionPool {

    AtomicInteger wait = new AtomicInteger(0);
    AtomicInteger notify = new AtomicInteger(0);

    //连接地址
    private final String host;

    //连接端口
    private final int port;

    //最大连接数
    private final int maxSize;

    final byte[] lock = new byte[0];

    private final Map<String, NioEventLoopGroup> initFlag = new HashMap<>();

    private final Map<String, List<MyConnection>> connectionPool = new HashMap<>();

    public MyConnectionPool(String host, int port, int maxSize) {
        this.host = host;
        this.port = port;
        this.maxSize = maxSize;
    }

    /**
     * 创建连接
     * @author : guanjm
     * @date: 2020/12/29
     * @param protocol      协议
     *
     */
    private MyConnection createConnection(String protocol) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = initFlag.computeIfAbsent(protocol, key -> new NioEventLoopGroup());
        ChannelFuture connect = new Bootstrap()
                .group(eventLoopGroup)
                .handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                    }
                })
                .channel(NioSocketChannel.class)
                .connect(new InetSocketAddress(host, port)).sync();
        SocketChannel channel = (SocketChannel) connect.channel();
//        System.out.println("create connect address: "+channel.localAddress());
        return new MyConnection(MyConnection.State.ACTIVE, channel);
    }

    /**
     * 获取连接
     * @author : guanjm
     * @date: 2020/12/29
     * @param protocol      协议
     *
     */
    public MyConnection getConnection(String protocol) throws InterruptedException {
        while (true) {
            synchronized (lock) {
                //获取连接池
                List<MyConnection> myConnections = connectionPool.computeIfAbsent(protocol, key -> new ArrayList<>(maxSize));
                //从连接池获取有效的连接
                for (MyConnection myConnection : myConnections) {
                    if (MyConnection.State.ACTIVE.equals(myConnection.getState())) {
                        myConnection.setState(MyConnection.State.WORKING);
                        return myConnection;
                    }
                }
                //当连接池数量未达到最大值，创建连接
                if (myConnections.size() < maxSize) {
                    MyConnection myConnection = createConnection(protocol);
                    myConnection.setState(MyConnection.State.WORKING);
                    myConnections.add(myConnection);
                    return myConnection;
                    //当连接池数量达到最大值，等待
                } else {
                    lock.wait();
//                    System.out.println("wait: " + wait.incrementAndGet());
                }
            }
        }
    }

    /**
     * 释放连接
     * @author : guanjm
     * @date: 2020/12/29
     *
     */
    public void releaseConnection(MyConnection myConnection) {
        synchronized (lock) {
            if (myConnection != null) {
                myConnection.removeHandle();
                myConnection.setState(MyConnection.State.ACTIVE);
                myConnection.setByteBuf(null);
                lock.notifyAll();
//                System.out.println("notify: " + notify.incrementAndGet());
            }
        }
    }

}