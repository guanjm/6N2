# 程序、进程、线程
> ## 程序
>   1. 指令、数据及其组织形式的描述。
> ## 进程
>   1. 计算机中的程序关于某数据集合上的一次运行活动。
>   2. 是系统进行资源分配和调度的基本单位。
>   3. 是操作系统结构的基础。
>   4. 堆、栈
> ## 线程
>   1. 是拥有资源和独立运行的最小单位。
>   2. 是处理器调度和分派的基本单位。
>   3. 也是程序执行的最小单位。
>   4. 程序计数器，寄存器，栈

# 并发和并行的区别
> ## concurrent  
>    单个处理器（CPU），多个线程操作，线程只能运行在同一cpu上，
>    线程互相抢占cpu资源（把cpu运行时间划分多个时间片分配给每个线程，占有cpu时间片的线程执行，其他线程才暂时挂起。）
>    实际上不可同时进行，宏观上同时运行（cpu运行速度快）。
> ## parallel 
>    多个处理器（CPU），多个线程操作，线程可能运行在不同的cpu上，
>    线程互相不抢占cpu资源，
>    实际上可以同时进行。
   

#线程的实现方式
> 1. 继承Thread类，重写run方法。
> 2. 实现Runnable接口，重写run方法。
> 3. 线程池的使用。  
> PS：1和2方式需通过start()方法来执行

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
    - wait() 线程等待，直到其他线程唤醒（需要持有锁(synchronized)）
        > 1. 当值为0时表示无限等待，无参方法其实就是调用了wait(0)
        > 2. 当线程不持有锁时，超时时间到了，转为阻塞状态。
        > 3. wait方法调用后立即释放锁
    - notify() 线程唤醒，唤醒一个处于等待的线程（需要持有锁(synchronized)）
        > 1. notify方法调用后不会立即释放锁，等到synchronized方法体执行完后才释放锁。
    - notifyAll() 线程唤醒，唤醒所有处于等待的线程（需要持有锁(synchronized)）
    
# synchronized
## 实现方式
- 普通方法 + synchronized = 普通方法 + synchronized(实例对象) {}
- 静态方法 + synchronized = 普通方法 + synchronized(类对象) {}
> 不属于方法定义，因此不能被继承。  
> 不能定义构造方法，不过可以用代码块实现  
> 互斥锁，可重入锁
> 不能使用String

## 实现原理
- 编译之后再同步的代码块前后加上monitorenter和monitorexit字节码指令。
- 依赖操作系统底层互斥锁实现，实现原子性操作和解决共享变量的内存可见性问题。






    

    



 

