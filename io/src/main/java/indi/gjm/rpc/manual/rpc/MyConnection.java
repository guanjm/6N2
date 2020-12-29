package indi.gjm.rpc.manual.rpc;

import io.netty.channel.socket.SocketChannel;

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

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

}
