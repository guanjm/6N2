package indi.gjm.rpc.manual.proxy;

import java.lang.reflect.Method;

public class MyProxy {

    /**
     * 获取代理对象
     * @author : guanjm
     * @date: 2020/12/22
     * @param protocolName  协议名称
     * @param clazz         接口名称
     *
     */
    public static <T> T getObject(String protocolName, Class<T> clazz) {
        try {
            if ("gjm1".equals(protocolName)) {
                Class<?> protocol = Class.forName("indi.gjm.rpc.manual.protocol.MyProtocolImpl");
                Method method = protocol.getMethod("getObject", Class.class);
                return (T) method.invoke(protocol, clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("please import correspond class");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
