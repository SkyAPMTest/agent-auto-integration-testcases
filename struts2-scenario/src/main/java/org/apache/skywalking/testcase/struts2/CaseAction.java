package org.apache.skywalking.testcase.struts2;

public class CaseAction {

    public String execute() throws InterruptedException {
        Thread.sleep(2 * 1000);
        return "SUCCESS";
    }
}
