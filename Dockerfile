FROM clojure:tools-deps-alpine

ADD . /data
WORKDIR /data

RUN echo "@testing http://nl.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories &&\
    apk add --no-cache npm git &&\
    npm install -g shadow-cljs gulp-cli &&\
    npm install &&\
    cd /data/resources/src && gulp &&\
    cd /data && shadow-cljs compile boodle &&\
    clojure -e ''

CMD ["/usr/local/bin/clojure", "-M:run"]
