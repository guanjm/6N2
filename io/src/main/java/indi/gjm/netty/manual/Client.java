package indi.gjm.netty.manual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Client {

    public static void main(String[] args) throws InterruptedException, IOException {
        //1、创建socketChannel
        NioSocketChannel nioSocketChannel = new NioSocketChannel();
        //3、创建eventLoop
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        //5、加入处理器
        nioSocketChannel.pipeline().addLast(new MyHandle(eventLoopGroup));
        //4、channel注册到eventLoop
        eventLoopGroup.register(nioSocketChannel);
        //2、连接端口
        ChannelFuture connect = nioSocketChannel.connect(new InetSocketAddress("10.73.40.26", 8001));

        connect.sync();
        System.out.println("client connect success");

        while (true) {
            byte[] bytes = new byte[128];
            while (System.in.read(bytes) > 0) {
                ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(128);
                byteBuf.writeBytes(bytes);
                //会调用byteBuf.release()，后续无法再使用
                //需要调用flush才会真正刷写
                nioSocketChannel.writeAndFlush(byteBuf).sync();
                bytes = new byte[128];
            }
        }

    }

}
;