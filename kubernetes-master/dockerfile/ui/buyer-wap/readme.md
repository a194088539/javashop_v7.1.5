## 概述
买家端属于服务端渲染，需要在服务器上启动nodejs服务。\
镜像制作需要基于nodejs，所以镜像会比较大。

## 镜像制作
- 将`.dockerignore`、`Dockerfile`文件放到买家WAP端根目录下（默认包含这些文件）\
使用docker命令制作镜像即可。

- 制作镜像
```bash
# 进入买家PC端根目录
cd ui/buyer/wap/themes/default/

# build镜像
docker build -t buyer-pc:1.0 .
```
