==========================================================
**** application.properties 파일에 해당 내용 추가 ****
==========================================================

# properties 파일은 key 와 value 의 쌍으로 이루어진 데이터 형식을 갖는다.
# Spring Boot 는 전통적으로 사용해왔던 Spring MVC 가 설정이 복잡하고, 학습 진입장벽이 높기 때문에,
# 개발자들로 하여금 개발에만 집중할 수 있도록 복잡한 설정을 내부적으로 알아서 처리한다.
# 따라서 개발자는 앞으로 server.xml, context.xml, web.xml 등을 따로 설정할 필요 없다.

spring.application.name=noticeapp

# 포트 번호 바꾸기 (내장 Tomcat 이기 때문에 이 설정만 하면 됨)
server.port=9999

# 로그 설정
logging.level.root=info
logging.level.com.ch.noticeapp=debug

#데이터베이스 연동 정보
spring.datasource.url=jdbc:mysql://localhost:3306/kiosk
spring.datasource.username=boot
spring.datasource.password=1234
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Mybatis 의 설정 파일 및 매퍼 파일의 위치 알려주기
# mybatis.config-location=classpath:/mybatis/mybatis-config.xml
# mybatis 앞에 /(루트) 표시는 resources 를 의미.
mybatis.type-aliases-package=com.ch.noticeapp.notice.dto.mybatis
mybatis.configuration.map-underscore-to-camel-case=true
mybatis.mapper-locations=classpath:/mybatis/**/*Mapper.xml


==========================================================
**** application.yaml 파일에 해당 내용 추가 ****
==========================================================

# yaml 을 이용하면 properties 파일 보다 중복이 발생하지 않고 코드량이 적어진다. 들여쓰기로 중복을 방지.

spring:
  application:
    name: noticeapp

  datasource:
    url: jdbc:mysql://localhost:3306/kiosk?useSSL=false&characterEncoding=UTF-8
    username: boot
    password: "1234"
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQLDialect

    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
    open-in-view: false

logging:
  level:
    root: info
    com.ch.noticeapp: debug

    org.hibernate.SQL: debug
    org.hibernate.jdbc.bind: trace

server:
  port: 9999



==========================================================
**** nginx 서버 추가(nginx 사이트 가서 다운로드) ****
nginx.conf 파일에 해당 내용 덮어쓰기
==========================================================

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       7777;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   C:/front_workspace/notice_client;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}

