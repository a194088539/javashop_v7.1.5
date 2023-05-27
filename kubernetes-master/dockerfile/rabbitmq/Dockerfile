FROM pivotalrabbitmq/rabbitmq-autocluster

MAINTAINER kingapex@javashop.cn

RUN apk update && apk add ca-certificates && \
    apk add tzdata && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
    

ADD plugins/*.ez /usr/lib/rabbitmq/plugins
RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange