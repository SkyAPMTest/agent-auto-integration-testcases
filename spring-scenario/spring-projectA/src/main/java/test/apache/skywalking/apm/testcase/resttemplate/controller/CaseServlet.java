package test.apache.skywalking.apm.testcase.resttemplate.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.apache.skywalking.apm.testcase.resttemplate.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class CaseServlet extends HttpServlet {

    private Logger logger = LogManager.getLogger(CaseServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Create user
        HttpEntity<User> userEntity = new HttpEntity<User>(new User(1, "a"));
        new RestTemplate().postForEntity(Config.projectBURL() + "/create/", userEntity, Void.class);

        // Find User
        new RestTemplate().getForEntity(Config.projectBURL() + "/get/{id}", User.class, 1);

        //Modify user
        HttpEntity<User> updateUserEntity = new HttpEntity<User>(new User(1, "b"));
        new RestTemplate().put(Config.projectBURL() + "/update/{id}", updateUserEntity, userEntity.getBody().getId(), 1);

        //Delete user
        new RestTemplate().delete(Config.projectBURL() + "/delete/{id}", 1);

        //
        Request request = new Request.Builder().url(Config.projectCURL() + "/spring3/").build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            logger.info(response.toString());
        } catch (IOException e) {
        }


        Request inheritRequest = new Request.Builder().url(Config.projectDURL() + "/inherit/child/test").build();

        try {
            Response response = new OkHttpClient().newCall(inheritRequest).execute();
            logger.info(response.toString());
        } catch (IOException e) {
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write("success");
        printWriter.flush();
        printWriter.close();
    }
}
