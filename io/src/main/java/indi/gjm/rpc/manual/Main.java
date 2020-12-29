package indi.gjm.rpc.manual;

import indi.gjm.rpc.manual.protocol.MyProtocolImpl;
import indi.gjm.rpc.manual.proxy.MyProxy;

public class Main {

    public static void main(String[] args) {
        //获取代理
        MyInterface myInterface = MyProxy.getObject(MyProtocolImpl.NAME, MyInterface.class);
        //通过代理类调用方法
        String result = myInterface.print("hello world");
        System.out.println(result);
    }

}
