# IO

## 计算机的组成
> 计算器、控制器、主存储器、输入设备（input）、输出设备（output）
> linux:一切皆文件

## linux
> - vfs: virtual file system（虚拟文件系统）
> - ll命令：
>   - -：普通文件（可执行文件，图片，文本）
>   - d：目录
>   - b：块设备（硬盘）能来回读写
>   - c：字符设备（网络io）有偏移量
>   - s：socket
>   - pipeline
>
> - ln    硬连接，两个文件指向同一块文件，两个文件inode相同  
>   ln -s 软链接，某个文件指向另外一个文件，两个文件inode不相同  
> - stat 查看文件信息状态  
> - vmstat：virtual memory statistics（虚拟内存统计）基于swag space（交换空间）  
> - lsof：lists openFiles（列出打开文件）  
> - df：disk free（磁盘使用）  
> - pcstat：pagecache statistics（页缓存统计）  
> - sysctl：system control（内核/系统配置控制）  
> - /proc 内核变量属性 （数字代表进程、其他文件代表其他内核属性）  
>   /proc/$$/fd ~~ lsof -op $$

## $$和$BASHPID
> 获取当前bash的pid  
> 优先级：$$ > | > $BASHPID  
> 前置知识：|管道前后分别是两个进程，bash是解释执行语句  
>   - echo $$ | cat 先解释$$,后解释|。所以$$优先获取当前进程id，再分别创建进程
>   - echo $BASHPID | cat 先解释|，后解释$BASHPID。所以先分别创建进程，后获取前一个进程的进程id。

## read 
> 对换行符敏感，读到换行符就中断。

## linux进程
> 进程间隔离  
> 子进程不能直接获取父进程变量，通过export输出为环境变量，子进程才能获取。

## 内存地址
> 逻辑地址：应用程序看到的内存地址，由段标识和段偏移量两个组成
> 物理地址：内存单元的真正地址

## pageCache 
> 4k=4096
> dirty：脏页，与硬盘不一致，需要刷写硬盘
>   - dirty内核配置：sysctl -a | grep dirty
>       - vm.dirty_ratio=30                     pagecache内存比例
>       - vm.dirty_background_ratio=10          pagecache内存比例（后台）
>       - vm.dirty_bytes=0                      pagecache字节阈值
>       - vm.dirty_background_bytes=0           pagecache字节阈值（后台）
>       - vm.dirty_writeback_centisecs=500      pagecache刷写间隔时长（百分之一秒）
>       - vm.dirty_expire_centisecs=3000        pagecache存活时长（百分之一秒）
>       - vm.dirtytime_expire_seconds=43200     pagecache文件时间戳更新（秒）
>       -   后台：到达阈值后，系统后台进程会开始将脏页刷写硬盘  
>       -   非后台：到达阈值后，新的io请求被阻挡，直到脏页刷写到硬盘

## 文件操作
> ### 读文件
>   1. read
>   2. 系统查找pagecache
>       - 若未存在，产生缺页异常，产生中断，创建页缓存，并返回
>       - 若存在，直接返回
>
> ### 写文件
>   1. write
>   2. 系统查找pagecache
>       - 若未存在，产生缺页异常，产生中断，创建页缓存，并写入页缓存
>       - 若存在，直接写入页缓存
>   3. pagecache刷写硬盘
>       - 用户手动fsync
>       - 系统pdflush进程定时刷写

## 命令
> read/write：系统调用（1、用户态切换内核态（需要用户内存和内核内存拷贝），读取硬盘，读取页缓存，页缓存拷贝到buffer）
> mmap：内存映射文件，直接写入pagecache

## ByteBuffer（java）
> - 实际是一个指定长度的字节数组容器，通过position，limit，capacity等数据来对数的读写。
> - flip()读, compact()写, clear()清除，position读/写起点，limit读/写上限，capacity容量
> - ByteBuffer.allocate(): on heap: jvm堆内
> - ByteBuffer.allocateDirect(): off heap：jvm堆外，java进程堆内
> - MappedByteBuffer 映射到内核内存，简而言之，直接操作pagecache

## IO类型
> 阻塞 （阻塞等待资源，accept，read/write）
> - accept: 获取建立的连接
> - read/write: 网卡buffer到内核内存，内核内存到用户内存
> 非阻塞 （直接返回，因此存在null）
> 同步 （处理资源）
> 异步 （linux暂无标准实现）

## 网络IO命令
> lsof -p [inode]
> netstat -natp
> tcpdump
> strace -off -o out [cmd]

## linux底层
> - 三次握手
>   1. socket = sfd
>   2. bind(sfd, port)
>   3. listen(sfd)  
> - 获取连接
>   4. accept(sfd) -> fd
> - 获取数据
>   5. recv(fd)
>
> - 多路复用
>   1. select(fds), poll(fds)
>   2. epoll_create() 创建红黑树
>   3. epoll_ctl 把需要监视文件放入红黑树，后续IO中断，回调处理buffer状态，复制到链表。
>   4. epoll_wait 程序调用，直接获取有状态fd的结果集。 

## blocking
> - 实现流程：
>   1. 建立监听（三次握手）
>   2. 循环（accept）阻塞获取socket
>   3. 为每个socket创建线程，并（read/write）阻塞读写socket数据，并做相应的业务处理
> - linux创建线程有最大限制
>   - ulimit -n 1024
>   - /proc/sys/fs/file-max

## nonblocking
> - 实现流程：
>   1. 建立监听（三次握手）
>   1. 循环（accept）非阻塞获取socket，若无新连接socket，直接返回null
>   2. 若有新连接socket，非阻塞读写socket数据，并做相应的业务处理（可通过消息队列异步处理）
> 1. 代码层遍历所有socket。
>   - 代码层获取socket，需要用户态转内核态。
> 2. select，poll，内核遍历所有socket，select FD_SETSIZE=1024 上限
>   - 每次调用，需传入需遍历的fds
>   - 每次调用，需遍历所有fd
>   - java通过jvm内置数据来模拟红黑树，
> 3. epoll，内核遍历所有socket
>   - 生成红黑数fd
>   - 代码层注册需监听fd到红黑数
>   - 当socket状态变化，记录到列表，（当数据到达网卡，产生IO中断，cpu调回调函数，同时触发记录）

## 阻塞和非阻塞
> 相同点:
>   1. 建立在三次握手基础下
>   2. 本质上通过遍历所有socket来接受数据
> 差异点：
>   1. 获取连接
>   2. 获取连接数据
>   3. 遍历方式
