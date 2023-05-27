## 概述
管理端属于静态页面，只需要基于nginx镜像，将dist目录放入镜像中即可。\
所以，我们在build镜像之前，需要将项目build好。

## 项目打包

- 拷贝公共代码

```bash
# 进入ui目录
cd ui/

# 拷贝公共资源
sh ./deploy.sh copy
```

- 安装依赖

```bash
# 进到平台管理目录
cd ui/manager-admin/

# 安装依赖
sudo npm install

# 项目打包
npm run build:prod
```

## 镜像制作
- 将`set-envs.sh`、`Dockerfile`、`nginx.conf`文件放到平台管理根目录下（默认包含有这些文件）\
使用docker命令制作镜像即可。

- 制作镜像
```bash
# 进入平台管理根目录
cd ui/manager-admin/

# build镜像
docker build -t manager-admin:1.0 .
```
