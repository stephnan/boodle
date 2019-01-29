FROM clojure:tools-deps-alpine

ADD . /data
WORKDIR /data

RUN echo "@testing http://nl.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories &&\
    apk add --no-cache yarn rlwrap@testing git &&\
    yarn global add shadow-cljs gulp-cli &&\
    yarn &&\
    cd /data/resources/src && yarn && gulp &&\
    cd /data && shadow-cljs compile boodle &&\
    clj -e ''

CMD ["/usr/local/bin/clj", "-A:run"]
