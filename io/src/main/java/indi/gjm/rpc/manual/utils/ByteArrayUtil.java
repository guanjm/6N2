package indi.gjm.rpc.manual.utils;

import java.io.*;

public class ByteArrayUtil {

    private ByteArrayUtil() {}

    /**
     * java对象序列化
     * @author : guanjm
     * @date: 2020/12/22
     * @param object    java对象
     *
     */
    public static byte[] turnJavaObject(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * java对象反序列化
     * @author : guanjm
     * @date: 2020/12/22
     * @param bytes     java序列化对象
     *
     */
    public static Object javaObjectTurn(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * java基础封装类转byte[]
     * @author : guanjm
     * @date: 2020/12/22
     *
     */
    public static byte[] javaDataTurn(Object data) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            if (data instanceof Long) {
                dataOutputStream.writeLong((Long)data);
            } else if (data instanceof Integer) {
                dataOutputStream.writeInt((Integer)data);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * byte[]转java基础封装类
     * @author : guanjm
     * @date: 2020/12/29
     *
     */
    public static <T> T turnJavaData(byte[] bytes, Class<T> clazz) {
        Object result = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream)) {
            if (Long.class == clazz) {
                result = dataInputStream.readLong();
            } else if (Integer.class == clazz) {
                result = dataInputStream.readInt();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) result;
    }

}
