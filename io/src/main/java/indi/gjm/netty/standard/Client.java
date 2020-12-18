package indi.gjm.netty.standard;

import indi.gjm.netty.manual.MyHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author : guanjm
 * @Date: 2020/12/18
 */
public class Client {

    public static void main(String[] args) throws Exception {
        ChannelFuture connect = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new MyHandle());
                    }
                })
                .connect(new InetSocketAddress(8001));

        Channel channel = connect.sync().channel();
        System.out.println("client connect success");

        while (true) {
            byte[] bytes = new byte[128];
            while (System.in.read(bytes) > 0) {
                ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(128);
                byteBuf.writeBytes(bytes);
                //会调用byteBuf.release()，后续无法再使用
                //需要调用flush才会真正刷写
                channel.writeAndFlush(byteBuf).sync();
                bytes = new byte[128];
            }
        }

    }
    
}
