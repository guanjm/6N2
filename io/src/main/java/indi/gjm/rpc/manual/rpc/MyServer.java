package indi.gjm.rpc.manual.rpc;

import indi.gjm.rpc.manual.protocol.MyProtocolImpl;
import indi.gjm.rpc.manual.protocol.MyRequest;
import indi.gjm.rpc.manual.protocol.MyResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * @author : guanjm
 * @date: 2020/12/22
 *
 */
public class MyServer {

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MyHandlerForServer());
                    }
                })
                .bind(8001);
    }

}

class MyHandlerForServer extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            //请求内容
            MyRequest myRequest = (MyRequest) MyProtocolImpl.decode(byteBuf);
            MyResponse myResponse = new MyResponse();
            myResponse.setId(myRequest.getId());
            myResponse.setData(myRequest.getArgs()[0]);
            System.out.println(myResponse.getData());
            ByteBuf encode = MyProtocolImpl.encode(myResponse);
            channel.writeAndFlush(encode);
        }
    }

}