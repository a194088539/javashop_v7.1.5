FROM docker.elastic.co/elasticsearch/elasticsearch:6.6.2

MAINTAINER kingapex@javashop.cn

COPY root/ /
USER root
RUN chmod 775 /run.sh

CMD ["/run.sh"]