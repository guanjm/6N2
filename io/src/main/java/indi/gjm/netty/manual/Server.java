package indi.gjm.netty.manual;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        //1、创建 serverSocketChannel
        NioServerSocketChannel nioServerSocketChannel = new NioServerSocketChannel();
        //3、创建 eventLoop 等同 Selector
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        //5、加入处理器
//        nioServerSocketChannel.pipeline().addLast(new MyHandle(eventLoopGroup));
        nioServerSocketChannel.pipeline().addLast(new ChannelInitializer<NioServerSocketChannel>() {
            @Override
            protected void initChannel(NioServerSocketChannel serverSocketChannel) throws Exception {
                serverSocketChannel.pipeline().addLast(new MyHandle());
            }
        });
        //4、channel 注册到 eventLoop
        eventLoopGroup.register(nioServerSocketChannel);
        //2、绑定端口
        ChannelFuture bind = nioServerSocketChannel.bind(new InetSocketAddress( 8001));

        bind.sync();
        System.out.println("server bind success");

    }

}
