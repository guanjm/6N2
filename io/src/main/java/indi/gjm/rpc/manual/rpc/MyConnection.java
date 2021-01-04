package indi.gjm.rpc.manual.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 连接工厂
 * @author : guanjm
 * @date: 2020/12/29
 *
 */
public class MyConnection {

    public enum State {
        WORKING,
        ACTIVE;
    }

    private State state;

    private SocketChannel socketChannel;

    private ByteBuf byteBuf;

    private Set<ChannelHandler> handlers = new LinkedHashSet<>();

    public MyConnection(State state, SocketChannel socketChannel) {
        this.state = state;
        this.socketChannel = socketChannel;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public void setByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void addHandler(ChannelHandler channelHandler) {
        handlers.add(channelHandler);
        socketChannel.pipeline().addLast(channelHandler);
    }

    public void removeHandle() {
        Iterator<ChannelHandler> iterator = handlers.iterator();
        while (iterator.hasNext()) {
            ChannelHandler handler = iterator.next();
            socketChannel.pipeline().remove(handler);
            iterator.remove();
        }
    }

    public ChannelFuture write(ByteBuf data) throws InterruptedException {
        return socketChannel.writeAndFlush(data).sync();
    }

    public ByteBuf readBlocking() throws InterruptedException {
        synchronized (this){
            while (byteBuf == null) {
                wait();
            }
            return byteBuf;
        }
    }

}
