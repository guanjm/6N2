package indi.gjm.rpc.manual;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 *
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class ChannelPool {

    public static final int PORT = 8001;

    private static NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private ChannelPool() {}

    public static Channel getChannel(ChannelHandler channelHandler) throws InterruptedException {
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        nioSocketChannel.pipeline().addLast(channelHandler);
        eventLoopGroup.register(nioSocketChannel);
        ChannelFuture connect = nioSocketChannel.connect(new InetSocketAddress(PORT));
        connect.sync();
        return connect.channel();
    }

}