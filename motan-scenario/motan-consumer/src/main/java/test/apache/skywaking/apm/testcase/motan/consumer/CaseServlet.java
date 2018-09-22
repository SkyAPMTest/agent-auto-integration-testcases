package test.apache.skywalking.apm.testcase.motan.consumer;

import com.weibo.api.motan.common.MotanConstants;
import com.weibo.api.motan.util.MotanSwitcherUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import test.apache.skywalking.apm.testcase.motan.interfaces.BusinessService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MotanSwitcherUtil.setSwitcherValue(MotanConstants.REGISTRY_HEARTBEAT_SWITCHER, true);

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:motan_client.xml");
        BusinessService greetService = applicationContext.getBean(BusinessService.class);
        String result = greetService.hello("test");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(result);
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
