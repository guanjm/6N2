package indi.gjm;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;

/**
 * Thread常用方法
 * @Author : guanjm
 * @Date: 2020-10-29 01:00
 *
 */
public class T3_ThreadMethods {

    public static void main(String[] args) throws Exception {

        //线程让步（线程替换的记率加大）
//        threadYield();

        //线程休眠
//        threadSleep();

        //线程中断
        threadInterrupt();

    }

    private static void threadYield() {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                yield();
                System.out.println(Thread.currentThread().getName() + i);
            }
        }, "线程一").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                yield();
                System.out.println(Thread.currentThread().getName() + i);
            }
        }, "线程二").start();
    }

    private static void threadSleep() throws Exception {
        long now = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(2);
        System.out.println(System.currentTimeMillis()-now);
    }

    private static void threadInterrupt() throws InterruptedException {
        Thread thread = new Thread(() -> {
            long now = System.currentTimeMillis();
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("thread is interrupted");
            }
            System.out.println(System.currentTimeMillis()-now);
        });
        thread.start();
        TimeUnit.SECONDS.sleep(5);
        thread.interrupt();
    }





}
