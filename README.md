# Notice
This repo doesn't accept any pull request anymore. It is **archived and read-only**. All plugin tests are moving into [Apache main repo](https://github.com/apache/skywalking).

# Agent Test Cases
This repository includes all javaagent test cases. Each case based on docker and docker-compose tech. 
The [collector simulator](https://github.com/SkywalkingTest/skywalking-mock-collector) receives the segments，
and the [test tool](https://github.com/SkywalkingTest/agent-integration-testtool) checks the result and report [the report repository](https://github.com/SkywalkingTest/agent-integration-test-report).

# Test Result
The **master branch** result is [root folder](https://github.com/SkywalkingTest/agent-integration-test-report). 
The other branches and tags test results are in the folder of branch or tag name.

# Document
* [English](docs/how-to-write-a-plugin-testcase.md)
* [中文](docs/how-to-write-a-plugin-testcase-cn.md)


# License
Apache 2.0
