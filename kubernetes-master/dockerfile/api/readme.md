# registry.cn-beijing.aliyuncs.com/javashop7/api:1.0镜像说明



## 基本用法

> docker run -e Jar=/opt/v70/base-api/app.jar -e JAR_ARG=--spring.config.location=/opt/v70/api/base-api/config/bootstrap.yml  \
> --expose=7000 -p 7000:7000 \
> -v /Users/kingapex/docker/v70/:/opt/v70 \
> --rm --name=baseapi registry.cn-beijing.aliyuncs.com/javashop7/api:1.0



## 参数说明

一、环境变量

| 参数 | 说明                 | 必须 | 默认值 |
| :--- | :------------------- | :--- | ------ |
| Jar  | 要运行的Jar包        | 是   | 无     |
| JAR_ARG  | 要加载的外部配置文件所在位置等 | 是   | 无  |
| JAVA_OPTS  | 初始内存 线程内存等  | 否   |  -Xmx256m -Xss256k |

二、端口

要开放端口请和每个jar要开放的端口一一对应