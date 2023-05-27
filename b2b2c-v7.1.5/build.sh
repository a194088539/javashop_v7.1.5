#!/bin/bash
#创建所需目录
base_dir=/opt/v70/api
rm -rf $base_dir
mkdir -p $base_dir/config
mkdir -p $base_dir/base
mkdir -p $base_dir/buyer
mkdir -p $base_dir/consumer
mkdir -p $base_dir/manager
mkdir -p $base_dir/seller

#build the jar
mvn clean install -DskipTests

##将jar copy至相应目录
cp ./config-server/target/config-server-7.0.0.jar $base_dir/config/
cp ./base-api/target/base-api-7.0.0.jar $base_dir/base/
cp ./buyer-api/target/buyer-api-7.0.0.jar $base_dir/buyer/
cp ./consumer/target/consumer-7.0.0.jar $base_dir/consumer/
cp ./manager-api/target/manager-api-7.0.0.jar $base_dir/manager/
cp ./seller-api/target/seller-api-7.0.0.jar $base_dir/seller/

#配置文件模板
template=`cat ./template.yml`

#用模板为每个应用生成配置项
printf  "service_name=base-api\nservice_port=7000\ncat << EOF\n$template\nEOF" | bash > $base_dir/base/bootstrap.yml
printf  "service_name=buyer-api\nservice_port=7002\ncat << EOF\n$template\nEOF" | bash > $base_dir/buyer/bootstrap.yml
printf  "service_name=seller-api\nservice_port=7003\ncat << EOF\n$template\nEOF" | bash > $base_dir/seller/bootstrap.yml
printf  "service_name=manager-api\nservice_port=7004\ncat << EOF\n$template\nEOF" | bash > $base_dir/manager/bootstrap.yml
printf  "service_name=consumer\nservice_port=6001\ncat << EOF\n$template\nEOF" | bash > $base_dir/consumer/bootstrap.yml
cp ./config-server/src/main/resources/application.properties $base_dir/config