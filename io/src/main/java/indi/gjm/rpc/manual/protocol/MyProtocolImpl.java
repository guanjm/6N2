package indi.gjm.rpc.manual.protocol;

import indi.gjm.rpc.manual.rpc.MyClient;
import indi.gjm.rpc.manual.utils.ByteArrayUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * 协议
 *
 * 当前协议组成：
 *  1、协议标识(flag)，占用4byte
 *      {@link indi.gjm.rpc.manual.protocol.MyProtocolImpl.FLAG_BYTE_SIZE}
 *  2、数据包长度(data_length)，占用4byte
 *      {@link indi.gjm.rpc.manual.protocol.MyProtocolImpl.DATA_LENGTH_BYTE_SIZE}
 *  3、数据体(data)，通过javaObject序列化
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
                    ByteBuf encodeData = encode(myRequest);
                    //远程调用
                    ByteBuf response = MyClient.invokeBlocking(NAME, encodeData);
//                    ByteBuf response = MyClient.invokeNonBlocking(NAME, encodeData);
                    //解码
                    MyResponse decodeData = (MyResponse)decode(response);
                    return decodeData.getData();
                });
    }

    /**
     * 协议编码
     * @author : guanjm
     * @date: 2020/12/22
     * @param data     数据体
     *
     */
    public static ByteBuf encode(Object data) {
        byte[] dataByteArray = ByteArrayUtil.turnJavaObject(data);
        //数据体
        ByteBuf dataBytes = Unpooled.copiedBuffer(dataByteArray);
        //协议标识
        ByteBuf flagBytes = Unpooled.copiedBuffer(NAME.getBytes(), 0, FLAG_BYTE_SIZE);
        //数据体长度
        byte[] dataLengthByteArray = ByteArrayUtil.javaDataTurn(dataByteArray.length);
        ByteBuf dataLengthBytes = Unpooled.copiedBuffer(dataLengthByteArray,
                dataLengthByteArray.length - DATA_LENGTH_BYTE_SIZE, dataLengthByteArray.length);
        return Unpooled.copiedBuffer(flagBytes, dataLengthBytes, dataBytes);
    }

    /**
     * 协议解码
     * @author : guanjm
     * @date: 2020/12/29
     * @param byteBuf   字节数组
     *
     */
    public static Object decode(ByteBuf byteBuf) {
        try {
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            //协议标识
            byteBuf.readBytes(tmp, FLAG_BYTE_SIZE);
            byte[] flagByteArray = tmp.toByteArray();
            if (!NAME.equals(new String(flagByteArray))) {
                throw new RuntimeException("return data protocol match fail");
            }
            tmp.reset();
            //数据体长度
            byteBuf.readBytes(tmp, DATA_LENGTH_BYTE_SIZE);
            byte[] dataLengthByteArray = tmp.toByteArray();
            long dataLength = ByteArrayUtil.turnJavaData(dataLengthByteArray, Integer.class);
            tmp.reset();
            //数据体
            byteBuf.readBytes(tmp, (int)dataLength);
            byte[] dataByteArray = tmp.toByteArray();
            return ByteArrayUtil.javaObjectTurn(dataByteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
