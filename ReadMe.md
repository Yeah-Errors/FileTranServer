# Yeah File Transmission Server
一个使用java socket的文件传输服务  

食用方式:计算机程序，非食用品，请在含有JDK环境下运行 

要求: JDK|JRE  > 8 

### 基础命令
```shell
java -jar yfth.jar -S|-R|-I|-H [option value]
#eage1(监听4951端口启用一个长期活的接收端):
java -jar yfth.jar -R -p 4951 -a true 
#eage2(连接至127.0.0.1的4951端口以进行文件发送)：
java -jar yfth.jar -S -h 127.0.0.1 -p 4951
```
##### -S 启用一个发送端:
###### 可选参数:  
`-h host` 连接至host;  
`-p port` 连接至指定端口;  
`-s pwd` 远程关闭接收端的接收服务,pwd在接收端`./conf`配置;  
`-f file` 发送指定文件至接收端，此选项可多次重复  
不使用参数代表以默认配置启动，首次启动后可以在`./conf/yfts.conf.xml`配置
`-c configFilePath` 加载指定路径的配置文件
##### -R 启用一个接收端:
###### 可选参数:  
`-p port` 监听指定端口  
`-a true|false` 是否保持接收端持续接收（false表示只接收一个文件就停止）  
`-f filePath` 指定接受后文件的存储位置（请确定所在位置有权限读写）  
`-c configFilePath` 加载指定路径的配置文件
不使用参数代表以默认配置启动，首次启动后可以在`./conf/yftr.conf.xml`配置  
####  -I 安装一个接收端服务（开机自启服务）: 内测中，即将支持
#### -H 输出帮助信息

### 发送端
**在首次启动发送端后会自动生成一个相关配置文件，位于同级目录的`conf/yfts.conf.xml`下**
```xml
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <comment>文件发送配置文件</comment>
    <entry key="port">4951</entry>
    <entry key="host">localhost</entry>
</properties>
```
其默认会连接`localhost`的`4951`端口
### 接收端
**在首次启动接收端端后会自动生成一个相关配置文件，位于同级目录的`conf/yftr.conf.xml`下**
```xml
<?xml version="1.0" encoding="utf-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<comment>接收端配置文件</comment>
<entry key="port">4951</entry>
<entry key="alive">false</entry>
<entry key="pwd">yeah</entry>
<entry key="path">xxx/r_res</entry>
</properties>
```
其默认会在`4951`端口监听并只接受一个文件，默认的远程关机密码为`yeah`,默认存储路径为`jar`包同级目录的`r_res`目录下  

也就是说在接收端启动后，你可以在另一台设备通过指令`java -jar yfth.jar -S -p ip`连接到接收端设备并传输文件  
你也可以通过`java -jar yfth.jar -S -s yeah`远程结束接收端的服务
