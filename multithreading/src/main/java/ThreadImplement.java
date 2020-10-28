import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.yield;

/**
 * 线程的实现方式
 * @Author : ex_guanjm
 * @Date: 2020/10/28 10:36
 *
 */
public class ThreadImplement {

    public static void main(String[] args) {
        //方式一：
        Thread1 thread1 = new Thread1("线程一");
        //方式二：
        Thread thread2 = new Thread(new Thread2(),"线程二");
        //方式三：
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        thread1.start();
        thread2.start();
        executorService.submit(new Thread2());
        executorService.shutdown();
    }

}

class Thread1 extends Thread {

    public Thread1(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            yield();
        }
    }
}

class Thread2 implements Runnable {

    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            yield();
        }
    }

}
