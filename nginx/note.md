# nginx.conf
> 1. user [userName] [groupName]  **指定运行用户或者用户组**  
> 2. worker_processes [count]  **worker进程数，通常与CPU的数量相同**  
> 3. error_log [filePath(.log)]  **全局错误日志路径**  
> 4. pid [filePath(.pid)]  **记录当前启动nginx的进程id**  
> 5. events {}
>   - worker_connections [count]  **单个worker进程最大并发连接数**  
> 6. http {}
>   - include [filePath]  **导入其他配置文件**
>   - default_type [contentType]  **内容类型**
>   - log_format main [日志格式]  **设定日志**
>   - access_log [filePath]  **访问日志**
>   - rewrite_log [on/off]  **重写日志**
>   - sendfile [on/off]  **是否调用sendfile函数（zero copy方式）来输出文件**
>       - on 用来进行下载等应用磁盘io负载应用
>       - off 平衡磁盘和网络IO处理速度，降低系统的uptime
>   - keepalive_timeout [second]  **连接超时时间**
>   - tcp_nodelay [on/off]
>   - gzip [on/off] **gzip压缩**
>   - gzip_types [content-type] **gzip压缩类型**
>   - gzip_vary [on/off]  ****
>   - upstream [serverName] {}  **设定实际服务器列表**
>       - server [ip:port] [weight]=[number]  **weigth权重**
>   - server {}  **http服务器**
>       - listen [port]  **监听端口**
>       - server_name [hostname]  **http服务器访问名**
>       - index [filePath(.html)]  **首页访问路径**
>       - root [fileDir]  **webapp目录**
>       - charset [charsetType]  **字符集类型**
>       - proxy_connet_timeout [second]  **代理连接超时时间**
>       - proxy_send_timeout [second]  **代理发送超时时间**
>       - proxy_read_timeout [second]  **连接成功后，代理接受超时时间**
>       - proxy_buffer_size [size]  **保护用户头信息的缓存区大小**
>       - proxy_buffers [number] [size]
>       - proxy_busy_buffers_size [size]  **高负荷下缓冲大小（proxy_buffers\*2）**
>       - proxy_temp_file_write_size [size]  **缓存文件夹大小，当大于此值，从upstream服务器传**
>       - client_max_body_size [size]  **客户端请求最大值**
>       - client_body_buffer_size [size]  **缓冲区客户端请求最大值**
>       - proxy_set_header [key] [value]  **代理头部设置**
>           - Host $host;
>           - X-Forwarder-For $remote_addr
>       - location [regexp] {}
>           - proxy_pass [url]  **反向代理-反向代理路径，可配置upstream**
>           - root [fileDir]  **静态文件-静态文件目录**
>           - expires [times]  **静态文件-静态文件过期时间**
>           - stub_status [on/off]  ****
>           - access_log  [on/off]  ****
>           - auth_basic []
>           - auth_basic_user_file []
>           - deny []  **禁止访问**
>       - error_page [code] [filePath]  **异常页面**
>