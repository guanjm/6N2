#并发和并行的区别
- ##concurrent
    单个处理器（CPU），多个线程操作，线程只能运行在同一cpu上，
    线程互相抢占cpu资源（把cpu运行时间划分多个时间片分配给每个线程，占有cpu时间片的线程执行，其他线程才暂时挂起。）
    实际上不可同时进行，宏观上同时运行（cpu运行速度快）。
- ##parallel 
    多个处理器（CPU），多个线程操作，线程可能运行在不同的cpu上，
    线程互相不抢占cpu资源，
    实际上可以同时进行。

#线程的实现方式
1. 继承Thread类，重写run方法。
2. 实现Runnable接口，重写run方法。
3. 线程池的使用。  
PS：1和2方式需通过start()方法来执行

#Thread的状态
- ##NEW(0)
    线程还未执行start()方法
- ##RUNNABLE(4)
    线程已执行start()方法，处于执行中或待执行，根据系统cpu来控制
- ##BLOCKED(1024)
    线程阻塞等待锁
    - wait for monitor lock
- ##WAITING(16)
    线程等待
    - Object.wait()
    - Thread.join()
    - LockSupport.park()
- ##TIMED_WAITING(32)
    线程等待，并提供超时
    - Thread.sleep(times)
    - Object.wait(times)
    - Thread.join(times)
    - LockSupport.parkNanos(times)
    - LockSupport.until(times)
-  ##TERMINATED(2)
    线程执行完成状态
    
# 线程的常用方法
- ##Thread
    - yield() 线程让步，临时让出cpu执行时间片，线程状态不变
    - sleep(times) 线程休眠，状态进入等待状态（timed_waiting），休眠完毕后进入就绪状态（Runnable）
    - interrupt() 线程中断，本地方法设置中断标记
    - setPriority(priority) 线程优先级（1-10）,值越大优先级越大
    - join() 线程等待，等待目标线程完成才继续执行（死亡），底层其实调用了wait()
- ##Object
    - wait() 线程等待，直到其他线程唤醒（需要持有锁，调用后会释放锁），当参数为0时无限等待
    - notify() 线程唤醒，唤醒一个处于等待的线程（需要持有锁，方法体调用后会释放锁）
    - notifyAll() 线程唤醒，唤醒所有处于等待的线程（需要持有锁，方法体调用后会释放锁）
    PS: HotSpot底层其实维护了一个队列来存储等待线程，会按顺序逐一唤醒线程

    

    



 

