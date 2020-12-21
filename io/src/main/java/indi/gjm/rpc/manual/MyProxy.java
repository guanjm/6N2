package indi.gjm.rpc.manual;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;

/**
 * 
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class MyProxy {

    private MyProxy() {}

    public static <T> T getBean(Class<T> clazz) {
        //生成代理类
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            //获取调用信息
            String interfaceName = method.getDeclaringClass().getName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            MyRequest request = new MyRequest(interfaceName, methodName, parameterTypes, args);
            //序列化
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            //远程调用
            return MyRpc.invoke(bytes);
        });
    }

}