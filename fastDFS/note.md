# FastDFS安装  [wiki](https://github.com/happyfish100/fastdfs/wiki)
> ## 使用的系统软件
> | 名称 | 说明 |
> | --- | --- |
> | centos | 7.x |
> | libfastcommon | FastDFS分离出的一些公用函数包 |
> | FastDFS | FastDFS本体 |
> | fastdfs-nginx-module | FastDFS和nginx的关联模块 |
> | nginx | nginx1.15.4 |
> 
> ## 编译环境
> - Centos ```yum install git gcc gcc-c++ make automake autoconf libtool pcre pcre-devel zlib zlib-devel openssl-devel wget vim -y```
> - Debian ```apt-get -y install git gcc g++ make automake autoconf libtool pcre2-utils libpcre2-dev zlib1g zlib1g-dev openssl libssh-dev wget vim```
>
> ## 磁盘目录
> | 说明 | 位置 |
> | --- | --- |
> | 所有安装包 | /usr/local/src |
> | 数据存储位置 | /home/dfs |
> ```
>   mkdir /home/dfs  #创建数据存储目录
>   cd /usr/loacl/src  #切换到安装目录准备下载安装包
> ```
> 
> ## 安装libfastcommon
> ```
>   git clone https://github.com/happyfish100/libfastcommon.git --depth 1
>   cd libfastcommon/
>   ./make.sh && ./make.sh install #编译安装
> ``` 
>
> ## 安装FastDFS
> ```
>   cd ../ #返回上一级目录
>   git clone https://github.com/happyfish100/fastdfs.git --depth 1
>   cd fastdfs/
>   ./make.sh && ./make.sh install #编译安装
>   #配置文件准备
>   cp /etc/fdfs/tracker.conf.sample /etc/fdfs/tracker.conf
>   cp /etc/fdfs/storage.conf.sample /etc/fdfs/storage.conf
>   cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf #客户端文件，测试用
>   cp /usr/local/src/fastdfs/conf/http.conf /etc/fdfs/ #供nginx访问使用
>   cp /usr/local/src/fastdfs/conf/mime.types /etc/fdfs/ #供nginx访问使用
> ```
> 
> ## 安装fastdfs-nginx-module
> ```
>   cd ../ #返回上一级目录
>   git clone https://github.com/happyfish100/fastdfs-nginx-module.git --depth 1
>   cp /usr/local/src/fastdfs-nginx-module/src/mod_fastdfs.conf /etc/fdfs
> ```
>
> ## 安装nginx
> ```
>   wget http://nginx.org/download/nginx-1.15.4.tar.gz #下载nginx压缩包
>   tar -zxvf nginx-1.15.4.tar.gz #解压
>   cd nginx-1.15.4/
>   #添加fastdfs-nginx-module模块
>   ./configure --add-module=/usr/local/src/fastdfs-nginx-module/src/ 
>   make && make install #编译安装
> ```


# 单机部署
> ## tracker配置
>
>