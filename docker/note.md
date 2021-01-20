# 虚拟化和容器化区别
>   - 虚拟化：每个虚拟环境都有操作系统，有资源隔离
>   - 容器化：每个容器环境只有运行工具，无资源隔离

# 安装教程
>   1. yum install -y yum-utils device-mapper-persistent-data lvm2  
>       **device-mapper-persistent-data lvm2 数据存储驱动，用于docker的数据存储**
>   2. yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo  
>       **添加yum源**
>   3. yum makecache fast  
>       **检测yum源速度，centos8 去掉fast**
>   4. yum -y install docker-ce  
>       **安装社区版docker，centos8 加上--nobest**
>   5. service docker start  
>       **启动docker服务**
>   6. 阿里云加速服务，加快镜像下载速度

# 使用教程
>  - docker pull hello-world  
>       **下载镜像**
>  - docker run hello-workd  
>       **创建容器并运行**

# 基本概念
>   - docker是提供应用打包，部署与运行应用的容器化平台
>       ```
>           应用程序
>           docker engine（docker引擎）
>           可用资源（物理机/虚拟机）
>       ```
>   - docker engine,C/S架构，http协议
>       ```
>           docker CLI(client)
>           REST API
>           docker daemon(server)
>       ```
