
FROM fluent/fluentd:onbuild

USER root
#ENV http_proxy 'http://87.254.212.121:8080/'
#ENV https_proxy 'http://87.254.212.121:8080/'

RUN apk add --update --virtual .build-deps \
        sudo build-base ruby-dev \

# cutomize following instruction as you wish
#	&& sudo -u fluent gem install --http-proxy http://87.254.212.121:8080 \ 
	&& sudo -u fluent gem install \
           fluent-plugin-elasticsearch \
           fluent-plugin-record-reformer \

#	&& sudo -u fluent gem sources --clear-all --http-proxy http://87.254.212.121:8080 \
	&& sudo -u fluent gem sources --clear-all \
	&& apk del .build-deps \
	&& rm -rf /var/cache/apk/* \
           /home/fluent/.gem/ruby/2.3.0/cache/*.gem

USER root
#ENV http_proxy=
#ENV https_proxy=
EXPOSE 24284
