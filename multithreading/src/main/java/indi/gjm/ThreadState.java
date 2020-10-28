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
        Object lock = new Object();
        Thread thread1 = null, thread2 = null, thread3 = null, thread4 = null;

        thread1 = new Thread(() -> {
            synchronized(lock) {
                //WAITING
                try {
                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread2 = new Thread(() -> {
            try {
                //TIMED_WAITING
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread3 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //BLOCKED
            synchronized(lock) {
            }
        });

        Thread finalThread1 = thread1;
        Thread finalThread2 = thread2;
        Thread finalThread3 = thread3;
        thread4 = new Thread(() -> {
            //RUNNABLE
            System.out.println(Thread.currentThread().getState());
            System.out.println(finalThread1.getState());
            System.out.println(finalThread2.getState());
            System.out.println(finalThread3.getState());
        });
        //NEW
        System.out.println(thread1.getState());
        TimeUnit.SECONDS.sleep(1);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

}