package indi.gjm.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * netty-字节数组
 * @author : guanjm
 * @date: 2020/12/17
 *
 */
public class ByteBufDemo {

    public static void main(String[] args) {
        //初始容量
        int initialCapacity = 10;
        //最大容量
        int maxCapacity = 40;
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(initialCapacity, maxCapacity);
        for (int i = 0; i < 10; i++) {
            print(byteBuf);
            byteBuf.writeBytes("1234".getBytes());
        }
    }

    private static void print(ByteBuf byteBuf) {
        System.out.println("----------------------------------------");
        //是否可读
        System.out.println("isReadable: "+byteBuf.isReadable());
        //可读起始下标
        System.out.println("readerIndex: "+byteBuf.readerIndex());
        //可读长度
        System.out.println("readableBytes: "+byteBuf.readableBytes());
        //是否可写
        System.out.println("isWritable: "+byteBuf.isWritable());
        //可写起始下标
        System.out.println("writerIndex: "+byteBuf.writerIndex());
        //可写长度
        System.out.println("writableBytes: "+byteBuf.writableBytes());
        //字节数组目前容量
        System.out.println("capacity: "+byteBuf.capacity());
        //字节数组最大容量
        System.out.println("maxCapacity: "+byteBuf.maxCapacity());

    }


}
