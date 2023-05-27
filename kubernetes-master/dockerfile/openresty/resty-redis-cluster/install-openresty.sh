#!/usr/bin/env bash

#安装基本类库
yum install readline-devel pcre-devel openssl-devel gcc git -y

#安装openresty
cd /opt/
wget https://openresty.org/download/openresty-1.13.6.1.tar.gz
tar -zxvf openresty-1.13.6.1.tar.gz
cd openresty-1.13.6.1
./configure
make
make install
echo "OpenResty安装完成"

#安装redis-lua连接池
cd /opt/resty-redis-cluster/lib
gcc redis_slot.c -fPIC -shared -o redis_slot.so
mv redis_slot.so /usr/local/openresty/lualib/
mv rediscluster.lua /usr/local/openresty/lualib/resty/
mkdir /usr/local/openresty/nginx/lua
mv cluster_connector.lua /usr/local/openresty/nginx/lua/
mv single_connector.lua /usr/local/openresty/nginx/lua/
echo "redis lua 连接池安装完成"
