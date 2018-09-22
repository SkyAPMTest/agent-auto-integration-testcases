package test.apache.skywalking.apm.testcase.jedis;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RedisCommandExecutor commandExecutor = new RedisCommandExecutor();
        commandExecutor.set("a", "a");
        commandExecutor.get("a");
        commandExecutor.del("a");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write("success");
        printWriter.flush();
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
