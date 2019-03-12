# 如何编写插件测试用例
## 用例工程的目录结构
用例工程是一个独立的Maven工程。该工程能将工程打包镜像,并要求提供一个外部能够访问的Web服务用例测试调用链追踪。

以下是用例工程的目录图:
```
[plugin_testcase]
  |__ [config]
  | |__ docker-compse.yml
  | |__ expectedData.yaml
  |__ [src]
  | |__ [main]
  | |    ...
  | |__ [resources]
  | |    ...
  |__ pom.xml
  |__ testcase.yml

[] = directory
```

### 文件用途说明

以下是用例工程中配置文件的说明：

| 文件               | 用途                                                     |
|:-----|:---                                                      |
| docker-compose.xml | 定义用例的docker运行容器环境                           |
| expectedData.yaml  | 定义用例期望生成的Segment的数据                        |
| testcase.yml       | 定义用例的基本信息，如: 被测试框架名称、版本号        |

## 测试用例编写流程
1. 编写用例代码
2. 打包并测试用例镜像,确保在没有加载探针时的用例镜像能够正常运行
3. 编写期望数据文件
4. 编写用例配置文件
5. 测试用例

## 示例
下面以HttpClient插件为例子，HttpClient插件主要测试两点，能否正确的创建HttpClient的Span以及能否正确的传递上下文，基于这两点，设计如下用例：

```
+-------------+         +------------------+            +-------------------------+
|   Browser   |         |  Case Servlet    |            | ContextPropagateServlet |
|             |         |                  |            |                         |
+-----|-------+         +---------|--------+            +------------|------------+
      |                           |                                  |
      |                           |                                  |
      |       WebHttp            +-+                                 |
      +------------------------> |-|         HttpClient             +-+
      |                          |--------------------------------> |-|
      |                          |-|                                |-|
      |                          |-|                                |-|
      |                          |-| <--------------------------------|
      |                          |-|                                +-+
      | <--------------------------|                                 |
      |                          +-+                                 |
      |                           |                                  |
      |                           |                                  |
      |                           |                                  |
      |                           |                                  |
      +                           +                                  +
```

### 编写用例代码

pom.xml最佳实践:
1. 测试框架的版本号设置为属性变量
2. 镜像的版本号设置成属性变量，并且使用框架版本号作为镜像的版本号

具体请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/pom.xml#L16-L17)

### 构建测试用例镜像
1. 添加Maven docker插件. 

在配置插件的过程中，以下三个字段需注意:

| 字段            | 意义                                                                                              |
| ---             | ---                                                                                            |
| imageName       | 镜像名字。推荐格式: skywalking/xxx-scenario, 比如httpClient工程使用 skywalking/httpclient-scenario|
| dockerDirectory | 镜像构建目录。推荐目录: ${project.basedir}/docker                                               |
| imageTags       | 镜像版本。推荐插件的版本保持一致                                                                  |

2. 编写Dockerfile文件

3. 编写docker-compose.xml文件
- 在config目录创建docker-compose.xml
- 编写docker-compose.xml

HttpClient测试用例的docker-compose.xml请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml)

4. 运行maven的docker插件

执行`mvn package docker:build`命令打包用例镜像

5. 运行并测试用例容器
- 进入`config`目录，并运行`docker-compose up`
- 访问容器暴露的web服务，确保用例能够正常运行

### 添加探针配置信息

1. Dockerfile添加Agent挂载目录

在Dockerfile文件中添加`VOLUME`配置项,具体参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/docker/Dockerfile#L11)

2. 启动脚本添加Agent启动参数

HttpClient运行在Tomcat中，javaagent参数应该添加在`${project.basedir}/docker/catalina.sh`. 具体参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/docker/catalina.sh#L107-110)

3. 添加 mock-collector 镜像¹
- docker-compose文件中添加mock-collector镜像
- 测试用例镜像link上mock-collector镜像，保证容器间的网络可见

具体请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml#L19-31)

4. 修改docker-compose.xml中用例开放端口和镜像版本
- 将测试用例中的镜像版本替换成`{CASES_IMAGE_VERSION}`
- 将修改放的端口替换成`{SERVER_OUTPUT_PORT}`

5. docker-compose文件添加Agent挂载配置

docker-compose文件中添加Agent挂载目录配置。具体参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml#L13-14)

6. 测试用例镜像, 具体参考文档中的测试章节

### 期望数据文件
HttpClient期望数据文件 --- [expectedData.yaml](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/expectedData.yaml)

#### 期望数据
期望数据文件用来描述用例生成的Segment数据，文件主要包含两部分内容：注册项 和 Segment数据. Segment数据中包含对Span的校验和Segment个数的校验。
在介绍文件组成部分之前，先介绍下期望数据中的校验描述符:

**数字型字段校验描述符**

| 描述符  | 描述               |
| :---     | :---               |
| `nq`     | 不等于             |
| `eq`     | 等于，默认可以不写 |
| `ge`     | 大于等于           |
| `gt`     | 大于               |

**字符串型字段描述符**

| 描述符     | 描述                   |
| :---       | :---                   |
| `not null` | 不为null               |
| `null`     | 空字符或者null         |
| `eq`        | 精确匹配. 默认可以不写 |

以上就是所有的期望数据中的校验描述符。下面介绍文件的主要组成部分:

**注册项数据校验格式**
```yml
registryItems:
  applications:
  - APPLICATION_CODE: APPLICATION_ID(int)
  ...
  instances:
  - APPLICATION_CODE: INSTANCE_COUNT(int)
  ...
  operationNames:
  - APPLICATION_CODE: [ SPAN_OPERATION(string), ... ]
  ...
```

以下对各个校验字段的描述:

| 字段           | 描述                                                                |
| :---           | :---                                                                |
| applications   | 注册的Aplication_code和application Id映射关系.目前只需校验不为0即可 |
| instances      | Application生成的实例数                                             |
| operationNames | 所有预期生成Span的OperationName列表                                 |


**Segments数据校验格式**
```yml
segments:
-
  applicationCode: APPLICATION_CODE(string)
  segmentSize: SEGMENT_SIZE(int)
  segments:
  - segmentId: SEGMENT_ID(string)
    spans:
        ....
```

以下对各个校验字段的描述:

| 字段            | 描述                                               |
| :---            | :---                                               |
| applicationCode | 待校验Segment的ApplicationCode.                    |
| segmentSize     | 待校验Segment的ApplicationCode生成的Segment的数量. |
| segmentId       | segment的trace ID.                                 |
| spans           | segment生成的Span列表                              |

**Span数据校验格式**

**注意**: 期望文件中Segment的Span是按照Span的结束顺序进行排列

```yml
    operationName: OPERATION_NAME(string)
    operationId: SPAN_ID(int)
    parentSpanId: PARENT_SPAN_ID(int)
    spanId: SPAN_ID(int)
    startTime: START_TIME(int)
    endTime: END_TIME(int)
    isError: IS_ERROR(string: true, false)
    spanLayer: SPAN_LAYER(string: DB, RPC_FRAMEWORK, HTTP, MQ, CACHE)
    spanType: SPAN_TYPE(string: Exit, Entry, Local )
    componentName: COMPONENT_NAME(string)
    componentId: COMPONENT_ID(int)
    tags:
    - {key: TAG_KEY(string), value: TAG_VALUE(string)}
    ...
    logs: 
    - {key: LOG_KEY(string), value: LOG_VALUE(string)}
    ...
    peer: PEER(string)
    peerId: PEER_ID(int)
    refs:
    - {
       parentSpanId: PARENT_SPAN_ID(int), 
       parentTraceSegmentId: PARENT_TRACE_SEGMENT_ID(string), 
       entryServiceName: ENTRY_SERVICE_NAME(string), 
       networkAddress: NETWORK_ADDRESS(string),
       parentServiceName: PARENT_SERVICE_NAME(string),
       entryApplicationInstanceId: ENTRY_APPLICATION_INSTANCE_ID(int) 
     }
   ...
```

以下对各个校验字段的描述:

| 字段      | 描述                                                                                                                                                                                                                                  |
|:---           | ---                                                                                                                                                                                                                                    |
| operationName | Span的Operation Name                                                                                                                                                                                                                   |
| operationId   | OperationName对应的Id, 这个值目前为0                                                                                                                                                                                                   |
| parentSpanId  | Span的父级Span的Id.  **注意**: 第一个Span的parentSpanId为-1                                                                                                                                                                                                               |
| spanId        | Span的Id. **注意**: ID是从0开始.                                                                                                                                                                                                                     |
| startTime     | Span开始时间. 目前不支持精确匹配，只需判断不为0即可                                                                                                                                                                                                     |
| endTime       | Span的结束时间.目前不支持精确匹配，只需判断不为0即可                                                                                                                                                                                                                |
| isError       | 是否出现异常. 如果Span抛出异常或者状态码大于400，该值为true, 否则为false                                                                                                                                                                            |
| componentName | 对应组件的名字。官方提供的[Component](https://github.com/apache/incubator-skywalking/blob/master/apm-protocol/apm-network/src/main/java/org/apache/skywalking/apm/network/trace/component/OfficialComponent.java)，则该值为null. |
| componentId   | 组件对应的ID. 。如果是官方提供的[Component](https://github.com/apache/incubator-skywalking/blob/master/apm-protocol/apm-network/src/main/java/org/apache/skywalking/apm/network/trace/component/OfficialComponent.java)，则该值为定义的组件ID   |
| tags          | Span设置的Tag. **注意**: tag的顺序即为在插件中设置的顺序                                                                                                                                                                                         |
| logs          | Span设置的log. **注意**: 顺序为设置Log的顺序                                                                                                                                                                                                      |
| SpanLayer     | 设置的SpanLayer. 目前可能的值为: DB, RPC_FRAMEWORK, HTTP, MQ, CACHE                                                                                                                                                                    |
| SpanType      | Span的类型. 目前的取值为 Exit, Entry, Local                                                                                                                                                                                            |
| peer          | 访问的远端IP. Exit类型的Span, 该值非空                                                                                                                                                                                                                       |
| peerId        | 访问的远端IP的ID，该值目前为0                                                                                                                                                                                                                          |

以下对SegmentRef各个校验字段的描述:

| 字段                   | 描述                                                                                                                                                                                                    |
|:----                       |:----                                                                                                                                                                                                    |
| parentSpanId               | 调用端的SpanID. 例如HttpClient是由SegmentA的SpanID为1的调用的，所以该值为1                                                                                                                              |
| parentTraceSegmentId       | 调用端的SegmentID. 格式: ${APPLICATION_CODE[SEGMENT_INDEX]}, `SEGMENT_INDEX`是相对于期望文件的INDEX. 例如SegmentB由`httpclient-case`中的第0个Segment调用的，所以这个值为`${httpclient-case[0]}`     |
| entryServiceName           | 调用链入口的Segment的服务名词. 例如HttpClient的entryServiceName为`/httpclient-case/case/httpclient`                                                                                                                                                                                          |
| networkAddress             | 被调用者的网络地址。例如CaseServlet通过127.0.0.1:8080调用到ContextPropagateServlet,所以这个值为127.0.0.1:8080                                                                                             |
| parentServiceName          | 调用端的SpanID等于0的OperationName                                                                                                                                                                      |
| entryApplicationInstanceId | 调用链入口的实例ID。                                                                                                                                                                

#### 编写期望数据流程
1. 编写RegistryItems

HttpClient测试用例中运行在Tomcat容器中，所以httpclient的实例数为1, 并且applicationId不为0。HttpClient Span的OperationName和ContextPropagateServlet生成的Span的OperationName一致，所以operationNames中只有两个operationName.
```yml
registryItems:
  applications:
  - {httpclient-case: nq 0}
  instances:
  - {httpclient-case: 1}
  operationNames:
  - httpclient-case: [/httpclient-case/case/httpclient,/httpclient-case/case/context-propagate]
```

2. 编写segmentItems

根据HttpClient用例的运行流程，推断httpclient-case产生两个Segment. 第一个Segment是访问CaseServlet所产生的, 暂且叫它`SegmentA`。第二Segment是ContextPropagateServlet所产生的, 暂且叫它`SegmentB`.

```yml
segments:
  - applicationCode: httpclient-case
    segmentSize: 2
```

Skywalking支持Tomcat埋点，所以SegmentA中会包含两个Span，第一个Span是Tomcat的埋点，第二个Span是HttpClient的埋点.

SegmentA的生成的Span数据如下：
```yml
    - segmentId: not null
      spans:
        -
          operationName: /httpclient-case/case/context-propagate
          operationId: eq 0
          parentSpanId: 0
          spanId: 1
          startTime: nq 0
          endTime: nq 0
          isError: false
          spanLayer: Http
          spanType: Exit
          componentName: null
          componentId: eq 2
          tags:
            - {key: url, value: 'http://127.0.0.1:8080/httpclient-case/case/context-propagate'}
            - {key: http.method, value: GET}
          logs: []
          peer: null
          peerId: eq 0
        -
          operationName: /httpclient-case/case/httpclient
          operationId: eq 0
          parentSpanId: -1
          spanId: 0
          startTime: nq 0
          endTime: nq 0
          spanLayer: Http
          isError: false
          spanType: Entry
          componentName: null
          componentId: 1
          tags:
            - {key: url, value: 'http://localhost:{SERVER_OUTPUT_PORT}/httpclient-case/case/httpclient'}
            - {key: http.method, value: GET}
          logs: []
          peer: null
          peerId: eq 0
```

SegmentB由于Skywalking对于Tomcat进行埋点会产生一个Span，并且SegmentA传递ContextTrace给SegmentB，对于SegmentB需要校验SegmentRef数据.

SegmentB的Span校验数据格式如下：
```yml
- segmentId: not null
  spans:
  -
   operationName: /httpclient-case/case/context-propagate
   operationId: eq 0
   parentSpanId: -1
   spanId: 0
   tags: 
   - {key: url, value: 'http://127.0.0.1:8080/httpclient-case/case/context-propagate'}
   - {key: http.method, value: GET}
   logs: []
   startTime: nq 0
   endTime: nq 0
   spanLayer: Http
   isError: false
   spanType: Entry
   componentName: null
   componentId: 1
   peer: null
   peerId: eq 0
   refs: 
   - {parentSpanId: 1, parentTraceSegmentId: "${httpclient-case[0]}", entryServiceName: "/httpclient-case/case/httpclient", networkAddress: "127.0.0.1:8080",parentServiceName: "/httpclient-case/case/httpclient",entryApplicationInstanceId: nq 0 }
```

### 编写用例配置文件
1. 添加testcase.yaml文件

testcase.yaml文件格式如下:
```yml
testcase:
  request_url: TESTCASE_REQUEST_URL 
  test_framework: TEST_FRAMEWORK_NAME 
  support_versions:
    - VERSION
```

以下对各个字段的描述:

| 字段             | 描述                                                                 |
|:---              |:---                                                                    |
| request_url      | 用例工程暴露的Web服务的地址, URL中的端口用`{SERVER_OUTPUT_PORT}`替代.  |
| test_framework   | 测试框架的名字. 例如HttpClient测试工程使用HttpClient作为test_framework |
| running_mode     | 跑测试框架的模式. TOGETHER(默认可不写), SINGLE, WITH_OPTIONAL 三种模式 例如HttpClient测试工程使用TOGETHER作为running_mode的模式 |
| support_versions | 支持框架的版本列表                                                    |

以下为HttpClient的testcase.yaml文件:
```yml
testcase:
  request_url: http://localhost:{SERVER_OUTPUT_PORT}/httpclient-case/case/httpclient 
  support_versions:
    - 4.3
    ...
    - 4.5.3
```

2. 添加Profile配置

在pom.xml中添加`profiles`配置节点。其中每一个`profile`就代表中`testcase.yaml`中support_versions列表中的一个, `profile`中的id命名格式为: `${project.dir_name}-${support_version}`.
HttpClient支持4.3到4.5.3 14个版本, 所以在pom.xml中添加14个`profile`，如下:
```xml
<profiles>
    <profile>
        <id>httpclient-4.3.x-scenario-4.5.3</id>
        <properties>
            <test.framework.version>4.5.3</test.framework.version>
        </properties>
    </profile>
    ....
    <profile>
        <id>httpclient-4.3.x-scenario-4.3</id>
        <properties>
            <test.framework.version>4.3</test.framework.version>
        </properties>
    </profile>
</profiles>
```


## 运行测试用例

### 安装环境
运行测试用例目前需要在本地安装以下环境:
* docker
* docker-compose
* maven
* git

### 运行测试用例

```shell
# export project_name=httpclient-4.3.x-scenario
# export agent_repository_branch=master
# export agent_repository_url=https://github.com/apache/incubator-skywalking.git
# export parallel_running_case_number=2
# sh ${SKYWALKING_AGENT_TESTCASES_HOME}/deploy-test.sh \
-p ${project_name} \
-b ${agent_repository_branch} \
-r ${agent_repository_url} \
--parallel_running_case_number ${parallel_running_case_number}
```
脚本运行参数的描述:

| 运行参数                   | 描述                                                                            |
| :---                       | :---                                                                            |
| -p project_dir_name        | 指定测试用例进行测试. 默认运行所有的测试用例工程                             |
| -b agent_repository_branch | Skywalking工程的分支名. 默认为`master`                                          |
| -r agent_repository_url    | Skywalking工程的URL. 默认为`https://github.com/apache/incubator-skywalking.git` |
| --parallel_running_case_number parallel_running_case_number    | 并行运行测试用例的数量. 默认为8，请根据自己的机器配置设置合理数量 |

### 查看生成报告
测试用例运行完成之后，会生成报告. 报告生成路径:
`${SKYWALKING_AGENT_TESTCASES_HOME}/workspace/report/{CURRENT_YEAR}/{CURRENT_MONTH}/{COMMITTER}/testreport-{TEST_DATE}.md`

以下对路径中的字段的描述:

| 字段          | 描述                                         |
| :---          | :---                                         |
| CURRENT_YEAR  | 测试的年份                                   |
| CURRENT_MONTH | 测试的月份                                   |
| COMMITTER     | Skywalking工程的地址中的用户名. 默认为apache |
| TEST_DATE     | 测试的时间                                   |

### 查看校验日志
校验的完整日志路径:
`${SKYWALKING_AGENT_TESTCASES_HOME}/workspace/logs/validate-{CURRENT_DATE}.log`


¹ `mock-collector`用来模拟Collector来接受探针上传的数据，源码地址:https://github.com/SkywalkingTest/skywalking-mock-collector.git.
