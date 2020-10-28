package indi.gjm;

import java.util.concurrent.TimeUnit;

/**
 * 线程状态
 * @Author : ex_guanjm
 * @Date: 2020/10/28 17:36
 *
 */
public class ThreadState {

    public static void main(String[] args) throws Exception {

        //NEW
        System.out.println(new Thread().getState());

        //RUNNABLE
        System.out.println(Thread.currentThread().getState());

        Object lock = new Object();

        //TIMED_WAITING
        Thread thread1 = new Thread(() -> {
            try {
                synchronized (lock) {
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //WAITING
        Thread thread2 = new Thread(() -> {
            try {
                thread1.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //BLOCKED
        Thread thread3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (lock) {
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(thread1.getState());
        System.out.println(thread2.getState());
        System.out.println(thread3.getState());
        TimeUnit.SECONDS.sleep(2);
        System.out.println(thread1.getState());
    }

}