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
     * Long转byte[]
     * @author : guanjm
     * @date: 2020/12/22
     *
     */
    public static byte[] turnByteArray(long l) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {
            dataOutputStream.writeLong(l);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
