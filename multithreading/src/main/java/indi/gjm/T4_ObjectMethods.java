package indi.gjm;

import java.util.concurrent.TimeUnit;

/**
 * Object常用方法
 * @Author : guanjm
 * @Date: 2020-10-29 00:58
 *
 */
public class T4_ObjectMethods {

    public static void main(String[] args) throws Exception {
        //等待超时
        waitWithTime();

        //等待阻塞超时
        waitWithTimeWithBlocked();

        //等待唤醒
        waitAndNotify();
    }

    /**
     * 等待超时
     * @Author : guanjm
     * @Date: 2020/10/29 16:11
     *
     */
    private static void waitWithTime() {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    long now = System.currentTimeMillis();
                    lock.wait(2000);
                    System.out.println(Thread.currentThread().getName() + (System.currentTimeMillis() - now));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "等待超时线程").start();
    }

    /**
     * 等待阻塞超时
     *  返回结果可见：
     *      1、线程从等待状态（TIMED_WAITING）转为阻塞状态（BLOCKED）
     *         先是线程等待，超时后转为阻塞，抢占锁才能继续执行
     * @Author : guanjm
     * @Date: 2020/10/29 16:11
     *
     */
    private static void waitWithTimeWithBlocked() {
        Object lock = new Object();
        Thread thread = new Thread(() -> {
            try {
                synchronized (lock) {
                    long now = System.currentTimeMillis();
                    lock.wait(2000);
                    System.out.println(Thread.currentThread().getName() + (System.currentTimeMillis() - now));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "等待阻塞超时线程");
        thread.start();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(thread.getName() + thread.getState());
                synchronized (lock) {
                    TimeUnit.MILLISECONDS.sleep(5000);
                    System.out.println(thread.getName() + thread.getState());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 等待唤醒
     *  返回结果可见：
     *      1、notify不会立刻释放锁
     * @Author : guanjm
     * @Date: 2020/10/29 16:16
     *
     */
    private static void waitAndNotify() throws Exception {
        Object lock = new Object();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    synchronized (lock) {
                        lock.wait();
                        System.out.println(Thread.currentThread().getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "等待线程"+i).start();
        }
        TimeUnit.MILLISECONDS.sleep(2000);
        for (int i = 0; i < 10; i++) {
            TimeUnit.MILLISECONDS.sleep(1000);
            synchronized (lock) {
                System.out.println("唤醒一个线程start");
                lock.notify();
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("唤醒一个线程end");
            }
        }
    }

}
