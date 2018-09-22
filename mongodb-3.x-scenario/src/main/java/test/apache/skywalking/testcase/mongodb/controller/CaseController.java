package test.apache.skywalking.testcase.mongodb.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.mongodb.client.model.Filters.eq;

@Controller
@RequestMapping("/case")
@PropertySource("classpath:application.properties")
public class CaseController {

    private Logger logger = LogManager.getLogger(CaseController.class);
    @Value(value = "${mongodb.host}")
    private String mongoDBHost;

    @RequestMapping("/mongodb")
    @ResponseBody
    public String mongoDBCase() {
        logger.info("mongodb host: {} ", mongoDBHost);
        MongoClient mongoClient = new MongoClient(mongoDBHost, 27017);
        MongoDatabase db = mongoClient.getDatabase("test-database");
        db.createCollection("testCollection");
        try {
            MongoCollection<Document> collection = db.getCollection("testCollection");
            Document document = Document.parse("{id: 1, name: \"test\"}");
            collection.insertOne(document);

            FindIterable<Document> findIterable = collection.find(eq("name", "test"));
            Document findDocument = findIterable.first();
            logger.info("find id[{}] document, and the name is {}", findDocument.get("id"), findDocument.get("name"));

            collection.updateOne(eq("name", "test"), BsonDocument.parse("{ $set : { \"name\": \"testA\"} }"));

            findIterable = collection.find(eq("name", "testA"));
            findDocument = findIterable.first();
            logger.info("find id[{}] document, and the name is {}", findDocument.get("id"), findDocument.get("name"));

            collection.deleteOne(eq("id", "1"));
        } finally {
            mongoClient.dropDatabase("test-database");
        }

        return "success";
    }
}
