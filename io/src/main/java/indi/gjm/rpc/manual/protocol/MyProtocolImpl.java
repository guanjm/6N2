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

    //当前协议标识
    private static final byte[] FLAG_BYTES = NAME.getBytes();

    //当前协议数据体最大长度
    private static final long MAX_LENGTH = 1L << (8 * 4);

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
     *  1、协议标识(flag_bytes)，占用4byte
     *      {@link indi.gjm.rpc.manual.protocol.MyProtocol.FLAG_BYTES}
     *  2、数据包长度(length_bytes)，占用4byte
     *      {@link indi.gjm.rpc.manual.protocol.MyProtocol.MAX_LENGTH}
     *  3、数据体(data_bytes)，通过javaObject序列化
     * @author : guanjm
     * @date: 2020/12/22
     * @param myRequest     数据体
     *
     */
    public static byte[] encode(MyRequest myRequest) {
        byte[] dataBytes = ByteArrayUtil.turnJavaObject(myRequest);
        long bodyLength = dataBytes.length;
        //数据体过大，抛弃异常
        if (bodyLength > MAX_LENGTH) {
            throw new RuntimeException("request data too large!!");
        }
        //数据体长度转byte
        byte[] lengthBytes = ByteArrayUtil.turnByteArray(bodyLength);
        ByteBuf byteBuf = Unpooled.copiedBuffer(FLAG_BYTES, lengthBytes, dataBytes);
        return byteBuf.array();
    }

}
