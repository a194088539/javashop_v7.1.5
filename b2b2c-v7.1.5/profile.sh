#!/bin/bash
#创建所需目录
base_dir=.

#配置文件模板
template=`cat ./template.yml`

#用模板为每个应用生成配置项
printf  "service_name=base-api\nservice_port=7000\ncat << EOF\n$template\nEOF" | bash > $base_dir/base-api/src/main/resources/bootstrap.yml
printf  "service_name=buyer-api\nservice_port=7002\ncat << EOF\n$template\nEOF" | bash > $base_dir/buyer-api/src/main/resources/bootstrap.yml
printf  "service_name=seller-api\nservice_port=7003\ncat << EOF\n$template\nEOF" | bash > $base_dir/seller-api/src/main/resources/bootstrap.yml
printf  "service_name=manager-api\nservice_port=7004\ncat << EOF\n$template\nEOF" | bash > $base_dir/manager-api/src/main/resources/bootstrap.yml
printf  "service_name=consumer\nservice_port=6001\ncat << EOF\n$template\nEOF" | bash > $base_dir/consumer/src/main/resources/bootstrap.yml
