package indi.gjm.netty.standard;

import indi.gjm.netty.manual.MyHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author : guanjm
 * @Date: 2020/12/18
 */
public class Server {

    public static void main(String[] args) throws Exception {
        ChannelFuture bind = new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new MyHandle());
                    }
                })
                .bind(8001);

        bind.sync();
        System.out.println("server bind success");

    }

}
