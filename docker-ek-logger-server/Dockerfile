FROM alpine:3.5
ENV LANG C.UTF-8
ENV http_proxy 'http://87.254.212.121:8080/'
ENV https_proxy 'http://87.254.212.121:8080/'

RUN ["adduser","-D","es"]
RUN { \
		echo '#!/bin/sh'; \
		echo 'set -e'; \
		echo; \
		echo 'dirname "$(dirname "$(readlink -f "$(which javac || which java)")")"'; \
	} > /usr/local/bin/docker-java-home \
	&& chmod +x /usr/local/bin/docker-java-home
ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk/jre
ENV PATH $PATH:/usr/lib/jvm/java-1.8-openjdk/jre/bin:/usr/lib/jvm/java-1.8-openjdk/bin
RUN set -x \
	&& apk add --no-cache \
		openjdk8-jre \
	&& [ "$JAVA_HOME" = "$(docker-java-home)" ]

ADD ./elk /
RUN apk add --no-cache nodejs
RUN ["chown","-R","es:es","/elasticsearch-5.2.1"]
RUN ["chown","-R","es:es","/kibana-5.2.1"]
RUN ["chown","-R","es:es","/startup.sh"]
#USER root
#RUN echo "vm.max_map_count=262144" >> /etc/sysctl.conf
#RUN ["sysctl","-w","vm.max_map_count=262144"]

USER es
#EXPOSE 9200 9300
#CMD sh /elasticsearch-5.2.1/bin/elasticsearch && sh /kibana-5.2.1/bin/kibana
#CMD sh /kibana-5.2.1/bin/kibana
CMD sh /startup.sh
