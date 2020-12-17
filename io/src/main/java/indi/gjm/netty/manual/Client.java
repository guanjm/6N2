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
        //5、创建管道
        ChannelPipeline pipeline = nioSocketChannel.pipeline();
        //6、添加处理器
        pipeline.addLast(new ChannelHandler() {
            @Override
            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                System.out.println("handlerAdded");
            }

            @Override
            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                System.out.println("handlerRemoved");
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                System.out.println("exceptionCaught");
            }
        });
        //2、连接端口
        ChannelFuture connect = nioSocketChannel.connect(new InetSocketAddress("10.73.40.26", 8001));


        //java.lang.UnsupportedOperationException: unsupported message type: String (expected: ByteBuf, FileRegion)
//        ChannelFuture writeAndFlush = nioSocketChannel.writeAndFlush("123");
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(10);
        ByteBuf byteBuf1 = byteBuf.writeBytes("123".getBytes());

        ChannelFuture writeAndFlush = nioSocketChannel.writeAndFlush(byteBuf1);

        ChannelFuture writeAndFlushSync = writeAndFlush.sync();
        System.out.println("client write succ");

    }

}
