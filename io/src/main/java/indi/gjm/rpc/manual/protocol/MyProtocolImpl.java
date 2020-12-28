package indi.gjm.rpc.manual.protocol;

import indi.gjm.rpc.manual.rpc.MyRpc;
import indi.gjm.rpc.manual.utils.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Proxy;

/**
 * 协议
 *
 * @author : guanjm
 * @date: 2020/12/22
 *
 */
public class MyProtocolImpl {

    //当前协议名称
    public static final String NAME = "gjm1";

    //当前协议标识记录字节数
    private static final int FLAG_BYTE_SIZE = 4;

    //当前协议数据包长度记录字节数
    private static final int DATA_LENGTH_BYTE_SIZE = 4;

    public static <T> T getObject(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> {
                    String className = method.getDeclaringClass().getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    //封装请求数据体
                    MyRequest myRequest = new MyRequest(className, methodName, parameterTypes, args);
                    //编码
                    byte[] encodeData = encode(myRequest);
                    return MyRpc.invoke(encodeData);
                });
    }

    /**
     * 协议编码
     * 当前协议组成：
     *  1、协议标识(flag)，占用4byte
     *      {@link indi.gjm.rpc.manual.protocol.MyProtocolImpl.FLAG_BYTE_SIZE}
     *  2、数据包长度(data_length)，占用4byte
     *      {@link indi.gjm.rpc.manual.protocol.MyProtocolImpl.DATA_LENGTH_BYTE_SIZE}
     *  3、数据体(data)，通过javaObject序列化
     * @author : guanjm
     * @date: 2020/12/22
     * @param myRequest     数据体
     *
     */
    public static byte[] encode(MyRequest myRequest) {
        //编码数据体
        byte[] dataByteArray = ByteArrayUtil.turnJavaObject(myRequest);
        ByteBuf flagBytes = Unpooled.copiedBuffer(NAME.getBytes(),
                0, FLAG_BYTE_SIZE);
        ByteBuf dataLengthBytes = Unpooled.copiedBuffer(ByteArrayUtil.turnByteArray(dataByteArray.length),
                0, DATA_LENGTH_BYTE_SIZE);
        ByteBuf dataBytes = Unpooled.copiedBuffer(dataByteArray);
        ByteBuf byteBuf = Unpooled.copiedBuffer(flagBytes, dataLengthBytes, dataBytes);
        return byteBuf.array();
    }

}
