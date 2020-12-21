package indi.gjm.rpc.manual;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class Server {

    public static final int PORT = 8001;

    /**
     * 启动服务端
     * @author : guanjm
     * @date: 2020/12/21
     *
     */
    private static void startupServer() throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture bind = serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ResponseHandler())
                .bind(PORT);

        bind.sync();
        System.out.println("server startup success!!");

    }

    public static void main(String[] args) throws InterruptedException {
        startupServer();
    }
    
}

class ResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            Channel channel = ctx.channel();
            channel.writeAndFlush(msg);
        }
    }

}
