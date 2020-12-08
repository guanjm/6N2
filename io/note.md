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
> read/write：系统调用（1、用户态切换内核态，读取硬盘，读取页缓存，页缓存拷贝到buffer）
> mmap：内存映射文件，直接写入pagecache