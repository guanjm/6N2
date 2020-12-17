package indi.gjm.netty.manual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        //1、创建socketChannel
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        //3、创建eventLoop
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        //4、channel注册到eventLoop
        eventLoopGroup.register(nioSocketChannel);
        //5、加入处理器
        nioSocketChannel.pipeline().addLast(new MyHandle());
        //2、连接端口
        ChannelFuture connect = nioSocketChannel.connect(new InetSocketAddress( 8001));

        //写数据前，同步，确认已连接
        connect.sync();
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(10);
        ByteBuf byteBuf1 = byteBuf.writeBytes("client connect succ！".getBytes());
        nioSocketChannel.writeAndFlush(byteBuf1);

    }

}
