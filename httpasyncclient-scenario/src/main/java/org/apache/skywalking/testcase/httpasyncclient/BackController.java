package org.apache.skywalking.testcase.httpasyncclient;

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackController {
    private static Logger logger = Logger.getLogger(BackController.class);

    @RequestMapping("/back")
    public String back() throws ClassNotFoundException, SQLException {

        SQLUtils.query();
        logger.info("Hello back");
        return "Hello back";

    }

}
