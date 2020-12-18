package indi.gjm.netty.manual;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Author : guanjm
 * @Date: 2020/12/17
 */
public class MyHandle extends ChannelInboundHandlerAdapter {

    private NioEventLoopGroup eventLoopGroup;

    public MyHandle() {
    }

    public MyHandle(NioEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof SocketChannel) {
            SocketChannel socketChannel = (SocketChannel) msg;
            //注册新channel记得加入handle
            socketChannel.pipeline().addLast(new MyHandle(eventLoopGroup));
            eventLoopGroup.register(socketChannel);
        } else if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            CharSequence charSequence = byteBuf.getCharSequence(byteBuf.readerIndex(), byteBuf.readableBytes(), CharsetUtil.UTF_8);
            System.out.println(charSequence);
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

}
