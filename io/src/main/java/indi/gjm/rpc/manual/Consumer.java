package indi.gjm.rpc.manual;

/**
 *
 * @author : guanjm
 * @date: 2020/12/21
 *
 */
public class Consumer {

    public static void main(String[] args) {
        MyInterface bean = MyProxy.getBean(MyInterface.class);
        String result = bean.print("hello world");
        System.out.println(result);
    }

}
