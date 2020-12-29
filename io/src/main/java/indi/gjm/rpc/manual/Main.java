package indi.gjm.rpc.manual;

import indi.gjm.rpc.manual.protocol.MyProtocolImpl;
import indi.gjm.rpc.manual.proxy.MyProxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger();
        //获取代理
        MyInterface myInterface = MyProxy.getObject(MyProtocolImpl.NAME, MyInterface.class);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            //通过代理类调用方法
//            executorService.submit(() -> System.out.println(atomicInteger.incrementAndGet() + ": " + myInterface.print("hello world")));
            executorService.submit(() -> myInterface.print("hello world"));
        }
    }

}
