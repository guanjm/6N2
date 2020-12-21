package indi.gjm.rpc.manual;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class MyRpc {

    /**
     * 远程调用
     * @author : guanjm
     * @date: 2020/12/21
     * @param bytes     请求信息
     */
    public static byte[] invoke(byte[] bytes) throws Exception {
        byte[] result = null;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Channel channel = ChannelPool.getChannel(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        if (msg instanceof ByteBuf) {
                        }
                    }
                });
                countDownLatch.countDown();
            }
        });
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        channel.writeAndFlush(byteBuf);
        //等待回写
        countDownLatch.await();
        return result;
    }
    
}

