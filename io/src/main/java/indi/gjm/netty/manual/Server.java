package indi.gjm.netty.manual;

import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        //1、创建serverSocketChannel
        NioServerSocketChannel nioServerSocketChannel = new NioServerSocketChannel();
        /*3、创建eventLoop 等同 Selector
          不注册报错：java.lang.IllegalStateException: channel not registered to an event loop
         */
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        //4、channel注册到selector
        eventLoopGroup.register(nioServerSocketChannel);
//        //2、绑定端口
//        ChannelFuture bind = nioServerSocketChannel.bind(new InetSocketAddress(8001));
//
//        //绑定完成后
//        ChannelFuture sync = bind.sync();
//        nioServerSocketChannel.write("server started~~~");
    }

}
