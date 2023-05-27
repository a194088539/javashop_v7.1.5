FROM registry.cn-beijing.aliyuncs.com/javashop-k8s-images/nginx:alpine
MAINTAINER javamall.com.cn
RUN apk update && apk add tzdata \
    && cp -r -f /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
