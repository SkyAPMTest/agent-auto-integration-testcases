FROM openjdk:8-jdk-alpine

ENV COLLECTOR_SERVER=127.0.0.1:12800

ADD startup.sh /usr/local/skywalking-customize-scenario/
ADD customize-scenario.jar /usr/local/skywalking-customize-scenario/
ADD customize_enhance.xml /usr/local/skywalking-customize-scenario/

ADD docker-entrypoint.sh /
RUN chmod +x /usr/local/skywalking-customize-scenario/customize_enhance.xml && chmod +x /usr/local/skywalking-customize-scenario/startup.sh && chmod +x /docker-entrypoint.sh

EXPOSE 8080

VOLUME /usr/local/skywalking-customize-scenario/agent
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["/usr/local/skywalking-customize-scenario/startup.sh"]