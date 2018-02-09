# 如何编写插件测试用例
## 用例工程的目录结构
用例工程是一个独立的Maven工程。该工程能够将用例打包成镜像。并且提供一个外部能够访问的Web服务

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

以下是用例中的配置文件的说明：

| 文件               | 用途                                                     |
|:-----|:---                                                      |
| docker-compose.xml | 用来定义用例的docker运行容器环境                         |
| expectedData.yaml  | 用来定义用例期望生成的Segment的数据                      |
| testcase.yml       | 用来定义用例的基本信息，例如测试框架，用例覆盖版本的信息 |

## 测试用例编写流程
1. 编写用例代码
2. 打包并测试用例镜像,确保在没有加载探针时的用例镜像能够正常运行
3. 编写期望数据文件
4. 编写用例配置文件
5. 测试用例

## 示例
下面以HttpClient插件为例子，HttpClient插件主要测试两点：1. 能否正确的创建HttpClient的Span 2. 能否正确的传递上下文，所以基于这两点，设计了如下设计用例:
HttpClient用例运行在Tomcat容器中，该用例包含两个Servlet: CaseServlet 和 ContextPropagateServlet，其中CaseServlet通过HttpClient调用ContextPropagateServlet.
运行流程如下：
```
+------------------------------------------+
|                                          |
|                Tomcat                    |
|                                          |
|                                          |
|        +------------------------+        |
|        |                        |        |
|        |                        |        |
|        |      CaseServlet       +-----------------------+
|        |                        |        |              |
|        |                        |        |              |
|        +------------------------+        |              |
|                                          |           HttpClient
|                                          |              |
|        +------------------------+        |              |
|        |                        |        |              |
|        |                        |        |              |
|        | ContextPropagateServlet<-----------------------+
|        |                        |        |
|        |                        |        |
|        |                        |        |
|        +------------------------+        |
|                                          |
|                                          |
|                                          |
|                                          |
+------------------------------------------+
```
### 编写用例代码
对pom.xml的一些建议，这样方便支持多版本测试，而不用修改任何代码
1. 建议将测试框架依赖的版本号设置成一个属性
2. 建议将镜像的版本号设置成一个属性，并且把镜像的版本号属性的值设置成框架版本号属性

HttpClient测试用例中的pom.xml将httpclient的版本号设置成`${test.framework.version}`,镜像版本号设置成`${docker.image.version}`属性。具体请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/pom.xml#L16-L17)

### 构建测试用例镜像
1. 添加Maven docker插件. 
在docker插件中有三个配置项需要注意：`imageName`, `imageTags`,`dockerDirectory`. 

| 字段            | 意义                                                                                              |
| ---             | ---                                                                                            |
| imageName       | 镜像名字。推荐格式: skywalking/xxx-scenario, 比如httpClient工程使用 skywalking/httpclient-scenario|
| dockerDirectory | 镜像构建目录。推荐目录: ${project.basedir}/docker                                               |
| imageTags       | 镜像版本。推荐插件的版本保持一致                                                                  |

2. 编写Dockerfile文件

3. 在config目录下添加docker-compose.xml
HttpClient测试用例的docker-compose.xml请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml)

4. 测试用例容器
- 进入`config`目录，并运行`docker-compose up`
- 访问容器暴露的web服务，确保用例能够正常运行

### 用例镜像加载探针
1. Dockerfile添加探针挂载目录

2. 在启动脚本中添加Agent参数
HttpClient运行在Tomcat中，javaagent参数应该添加在`${project.basedir}/docker/catalina.sh`. 具体参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/docker/catalina.sh#L107-110)

3. docker-compose.xml 添加 mock-collector 镜像. 具体请参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml#L19-31)

4. 修改docker-compose.xml中用例开放端口和镜像版本
- 将测试用例中的镜像版本替换成`{CASES_IMAGE_VERSION}`
- 将修改放的端口替换成`{SERVER_OUTPUT_PORT}`

5. 在docker-compose.xml中添加Agent的挂载. 具体参考[配置](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/docker-compose.yml#L13-14)

### 编写期望数据文件
HttpClient期望数据文件 --- [expectedData.yaml](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/config/expectedData.yaml)

1. 编写RegistryItems
以下是HttpClient的注册项的demo:
```yml
registryItems:
  applications:
  - {httpclient-case: nq 0}
  instances:
  - {httpclient-case: 1}
  operationNames:
  - httpclient-case: [/httpclient-case/case/httpclient,/httpclient-case/case/context-propagate]
```
以下对各个校验字段的描述:

|        | 描述                                                                                |
|:---            |:---                                                                                 |
| applications   | 注册的Aplication_code对应的application Id映射关系. `nq 0` 代表 applicationId不等于0 |
| instances      | application对应的实例数量                                                           |
| operationNames | 所有预期生成Span的OperationName列表.                                                |


2. 编写segmentItems
根据HttpClient用例的运行流程，可以推测出用例生成两个Segment. 第一个Segment是访问CaseServlet所产生的, 暂且叫它`SegmentA`。第二Segment是CaseServlet通过HttpClient调用ContextPropagateServlet所产生的, 暂且叫它`SegmentB`. SegmentB中包含一个SegmentRef，并且SegmentRef中的`parentTraceSegmentId`为SegmentA的segmentID。
```yml
  applicationCode: httpclient-case
  segmentSize: 2
```

以下对各个校验字段的描述:

| 字段 | 描述 |
|:--- |:---|
| applicationCode | 注册的Application_code.|
| segmentSize | 生成的Segment的数量. 目前支持的校验表达式有 `nq (not equal)` `eq (equals)` `gt (great than)` `ge (great equal)` |


由于skywalking对于Tomcat有埋点，所以SegmentA中会包含两个Span，第一个Span是Tomcat的埋点，第二个Span是HttpClient的埋点。

**注意**: 期望文件中Segment的Span是后进先出的顺序

SegmentA的部分生成的Span数据如下：
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
```

以下对各个校验字段的描述:

| 字段      | 描述                                                                                                                                                                                                                                  |
|:---           | ---                                                                                                                                                                                                                                    |
| operationName | Span的Operation Name                                                                                                                                                                                                                   |
| operationId   | OperationName对应的Id, 这个值目前为0                                                                                                                                                                                                   |
| parentSpanId  | Span的父级Span的Id.  **注意**: 第一个Span的parentSpanId为-1                                                                                                                                                                                                               |
| spanId        | Span的Id. **注意**: ID是从0开始.                                                                                                                                                                                                                     |
| startTime     | Span开始时间.                                                                                                                                                                                                                         |
| endTime       | Span的结束时间.                                                                                                                                                                                                                         |
| isError       | 是否出现异常. 如果Span抛出异常或者状态码大于400，这个值为 true, 否则为false                                                                                                                                                                          |
| componentName | 对应组件的名字。官方提供的[Component](https://github.com/apache/incubator-skywalking/blob/master/apm-protocol/apm-network/src/main/java/org/apache/skywalking/apm/network/trace/component/OfficialComponent.java)，则该值为null. |
| componentId   | 组件对应的ID. 。如果是官方提供的[Component](https://github.com/apache/incubator-skywalking/blob/master/apm-protocol/apm-network/src/main/java/org/apache/skywalking/apm/network/trace/component/OfficialComponent.java)，则该值为定义的组件ID   |
| tags          | Span设置的Tag. **注意**: tag的顺序即为在插件中设置的顺序                                                                                                                                                                                         |
| logs          | Span设置的log. **注意**: 顺序为设置Log的顺序                                                                                                                                                                                                      |
| SpanLayer     | 设置的SpanLayer. 目前可能的值为: DB, RPC_FRAMEWORK, HTTP, MQ, CACHE                                                                                                                                                                    |
| SpanType      | Span的类型. 目前的取值为 Exit, Entry, Local                                                                                                                                                                                            |
| peer          | 访问的远端IP. 目前只有Exit类型的Span, 这个值不为空                                                                                                                                                                                      |
| peerId        | 访问的远端IP的ID，这个值目前为0                                                                                                                                                                                                                         |

SegmentB的segmentRef如下：
```yml
{ 
    parentSpanId: 1, 
    parentTraceSegmentId: "${httpclient-case[0]}", 
    entryServiceName: "/httpclient-case/case/httpclient", 
    networkAddress: "127.0.0.1:8080",
    parentServiceName: "/httpclient-case/case/httpclient",
    entryApplicationInstanceId: nq 0 
}
```
以下对各个校验字段的描述:

| 字段                   | 描述                                                                                                                                                                                                    |
|:----                       |:----                                                                                                                                                                                                    |
| parentSpanId               | 调用端的SpanID. 例如HttpClient是由SegmentA的SpanID为1的调用的，所以该值为1                                                                                                                              |
| parentTraceSegmentId       | 调用端的SegmentID. 这个值的格式: ${APPLICATION_CODE[SEGMENT_INDEX]}, `SEGMENT_INDEX`是相对于期望文件的INDEX. 例如SegmentB由`httpclient-case`中的第0个Segment调用的，所以这个值为`${httpclient-case[0]}` |
| entryServiceName           | 调用链入口的Segment的服务名词. 例如HttpClient的entryServiceName为`/httpclient-case/case/httpclient`                                                                                                                                                                                          |
| networkAddress             | 被调用者的网络地址。例如CaseServlet通过127.0.0.1:8080调用到ContextPropagateServlet,所以这个值为127.0.0.1:8080                                                                                             |
| parentServiceName          | 调用端的SpanID等于0的OperationName                                                                                                                                                                      |
| entryApplicationInstanceId | 调用链入口的实例ID。                                                                                                                                                                                              |

### 编写用例配置文件
1. 添加testcase.yaml文件. HttpClient的[testcase.yaml](https://github.com/SkywalkingTest/skywalking-agent-testcases/blob/master/httpclient-4.3.x-scenario/testcase.yml)

以下对各个字段的描述:

| 字段             | 描述                                                                 |
|:---              |:---                                                                    |
| request_url      | 用例工程暴露的Web服务的地址, URL中的端口用`{SERVER_OUTPUT_PORT}`替代.  |
| test_framework   | 测试框架的名字. 例如HttpClient测试工程使用HttpClient作为test_framework |
| support_versions | 支持框架的版本列表。                                                   |

2. 添加Profile
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
# sh ${SKYWALKING_AGENT_TESTCASES_HOME}/deploy-test.sh \
-p ${project_name} \
-b ${agent_repository_branch} \
-r ${agent_repository_url}
```
脚本运行参数的描述:

| 运行参数 | 描述 |
|:--- |:---|
| -p project_dir_name | 指定测试用例进行测试. 默认为空，运行所有的测试用例工程 |
| -b agent_repository_branch | Skywalking Agent的工程的分支名. 默认为`master` |
| -r agent_repository_url | Skywalking Agent的工程的URL. 默认为`https://github.com/apache/incubator-skywalking.git`|
### 查看生成报告
测试用例运行完成之后，会自动生成报告，报告生成路径:
`${SKYWALKING_AGENT_TESTCASES_HOME}/workspace/report/{CURRENT_YEAR}/{CURRENT_MONTH}/apache/testreport-{CURRENT_DATE}.md`
