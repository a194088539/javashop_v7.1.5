#!/bin/bash
#创建所需目录
base_dir=.

#配置文件模板
if [[ "$1" == "ssl" ]];then
	nginx_conf=`cat ./nginx-ssl.conf`
	#--传输协议--#
	PROTOCOL='https'
else
	nginx_conf=`cat ./nginx.conf`
	PROTOCOL='http'
fi

domain_js=`cat ./domain.js`
api_js=`cat ./api.js`


#---------网关域名端口号配置----------#
BUYER_PC_DOMAIN='pc.shopsuccess.com'
BUYER_PC_PORT='443'

BUYER_WAP_DOMAIN='m.shopsuccess.com'
BUYER_WAP_PORT='443'

SELLER_DOMAIN='seller.shopsuccess.com'
SELLER_PORT='443'

ADMIN_DOMAIN='admin.shopsuccess.com'
ADMIN_PORT='443'



#---------API域名端口号配置----------#
BASE_API_DOMAIN='api-base.shopsuccess.com'
BASE_API_PORT='443';


BUYER_API_DOMAIN='api-buyer.shopsuccess.com'
BUYER_API_PORT='443'

SELLER_API_DOMAIN='api-seller.shopsuccess.com'
SELLER_API_PORT='443'

ADMIN_API_DOMAIN='api-admin.shopsuccess.com'
ADMIN_API_PORT='443'


#---------服务地址配置----------#
BASE_API_SERVICE='192.168.2.103:7000'
BUYER_API_SERVICE='192.168.2.103:7002'
SELLER_API_SERVICE='192.168.2.103:7003'
ADMIN_API_SERVICE='192.168.2.103:7004'

BUYER_PC_SERVICE='192.168.2.103:3000'
BUYER_WAP_SERVICE='192.168.2.103:3001'

STATIC_PAGE_SERVICE='192.168.2.103:8081'

#---------ssl证书位置,非ssl不用动----------#
CRT_POSISION='/opt/ssl/nginx.crt'
KEY_POSITION='/opt/ssl/nginx.key'

#---------以下是生成的脚本，不要动----------#
buyer_pc_str="BUYER_PC_DOMAIN=${BUYER_PC_DOMAIN}\nBUYER_PC_PORT=${BUYER_PC_PORT}\n"
buyer_wap_str="BUYER_WAP_DOMAIN=${BUYER_WAP_DOMAIN}\nBUYER_WAP_PORT=${BUYER_WAP_PORT}\n"
seller_str="SELLER_DOMAIN=${SELLER_DOMAIN}\nSELLER_PORT=${SELLER_PORT}\n"
admin_str="ADMIN_DOMAIN=${ADMIN_DOMAIN}\nADMIN_PORT=${ADMIN_PORT}\n"

node_pc_str="BUYER_PC_SERVICE=${BUYER_PC_SERVICE}\n"
node_wap_str="BUYER_WAP_SERVICE=${BUYER_WAP_SERVICE}\n"

base_api_str="BASE_API_DOMAIN=${BASE_API_DOMAIN}\nBASE_API_SERVICE=${BASE_API_SERVICE}\nBASE_API_PORT=${BASE_API_PORT}\n"
buyer_api_str="BUYER_API_DOMAIN=${BUYER_API_DOMAIN}\nBUYER_API_SERVICE=${BUYER_API_SERVICE}\nBUYER_API_PORT=${BUYER_API_PORT}\n"
seller_api_str="SELLER_API_DOMAIN=${SELLER_API_DOMAIN}\nSELLER_API_SERVICE=${SELLER_API_SERVICE}\nSELLER_API_PORT=${SELLER_API_PORT}\n"
admin_api_str="ADMIN_API_DOMAIN=${ADMIN_API_DOMAIN}\nADMIN_API_SERVICE=${ADMIN_API_SERVICE}\nADMIN_API_PORT=${ADMIN_API_PORT}\n"
static_page_api_str="STATIC_PAGE_SERVICE=${STATIC_PAGE_SERVICE}\n"

ssl_position_str="CRT_POSISION=${CRT_POSISION}\nKEY_POSITION=${KEY_POSITION}\n"

protocol="PROTOCOL=${PROTOCOL}\n"

cd /opt/server/ui
git reset --hard master
git pull

#echo  $base_str"cat << EOF\n$template\nEOF" 
printf  "${ssl_position_str}${protocol}${buyer_pc_str}${buyer_wap_str}${seller_str}${admin_str}${node_pc_str}${node_wap_str}${base_api_str}${buyer_api_str}${seller_api_str}${admin_api_str}${static_page_api_str}cat << EOF\n$nginx_conf\nEOF" | bash > /usr/local/nginx/conf/nginx.conf
printf  "${protocol}${buyer_pc_str}${buyer_wap_str}${seller_str}${admin_str}${node_pc_str}${node_wap_str}${base_api_str}${buyer_api_str}${seller_api_str}${admin_api_str}${static_page_api_str}cat << EOF\n$domain_js\nEOF" | bash > /opt/server/ui/ui-domain/domain.js
printf  "${protocol}${buyer_pc_str}${buyer_wap_str}${seller_str}${admin_str}${node_pc_str}${node_wap_str}${base_api_str}${buyer_api_str}${seller_api_str}${admin_api_str}${static_page_api_str}cat << EOF\n$api_js\nEOF" | bash > /opt/server/ui/ui-domain/api.js
sh /opt/server/ui/deploy.sh copy