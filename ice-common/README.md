# 对象系列化 
1.protobuf
lib/ 目录下为 protobuf 依赖相关包
/srv/main/proto/ 目录下为消息协议定义

2.pom.xml 中配置了 自动编译proto文件生成 java 的maven 插件
执行 protobuf:compile 编译即可

3.protobuf 参考
1) 基础教程：http://www.tianshouzhi.com/api/tutorials/netty/342
2）协议规范：https://www.cnblogs.com/tohxyblog/p/8974763.html
3）IDEA 集成protobuf 插件：https://www.cnblogs.com/TechSnail/p/7793813.html