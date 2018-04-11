FROM openjdk:8-jdk

ENV COLLECTOR_SERVER=127.0.0.1:12800 \
    ROCKETMQ_VERSION=3.2.6 \
    JDK_CROSS_THREAD_SCENARIO_HOME=/usr/local/jdk-cross-thread-scenario \
    ROCKETMQ_NAMESRV=localhost:9876


WORKDIR  ${JDK_CROSS_THREAD_SCENARIO_HOME}

RUN mkdir -p  logs agent
COPY jdk-cross-thread-scenario.jar ${JDK_CROSS_THREAD_SCENARIO_HOME}
#
# Volumes
#
VOLUME /usr/local/jdk-cross-thread-scenario/logs
VOLUME /usr/local/jdk-cross-thread-scenario/agent
#
# ports
#
EXPOSE 8080
#
# entry point
#
#ENTRYPOINT /docker-entrypoint.sh
#
#
#
COPY startup.sh ${JDK_CROSS_THREAD_SCENARIO_HOME}
#
#
#
CMD sh ${JDK_CROSS_THREAD_SCENARIO_HOME}/startup.sh