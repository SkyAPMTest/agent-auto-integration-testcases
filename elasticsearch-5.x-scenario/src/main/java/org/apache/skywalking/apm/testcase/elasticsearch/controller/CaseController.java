package org.apache.skywalking.apm.testcase.elasticsearch.controller;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author oatiz.
 */
@RestController
@RequestMapping("/case")
public class CaseController {

    @Value("${elasticsearch.host}")
    private String host;

    @SuppressWarnings("squid:S2259")
    @GetMapping("/elasticsearch")
    public String elasticsearchCase() {
        Client client = initTransportClient();
        String indexName = UUID.randomUUID().toString();
        try {
            // create
            index(client, indexName);
            // get
            get(client, indexName);
            // search
            search(client, indexName);
            // update
            update(client, indexName);
            // delete
            delete(client, indexName);
            // remove index
            client.admin().indices().prepareDelete(indexName).execute();
        } finally {
            if (null != client) {
                client.close();
            }
        }
        return "ok";
    }

    private void index(Client client, String indexName) {
        try {
            client.prepareIndex(indexName, "test", "1")
                .setSource(XContentFactory.jsonBuilder()
                    .startObject()
                    .field("name", "mysql innodb")
                    .field("price", "0")
                    .field("language", "chinese")
                    .endObject())
                .get();
        } catch (IOException e) {
            // nothing to do
        }
    }

    private void get(Client client, String indexName) {
        client.prepareGet().setIndex(indexName).setId("1").execute();
    }

    private void update(Client client, String indexName) {
        try {
            client.prepareUpdate(indexName, "test", "1")
                .setDoc(XContentFactory.jsonBuilder().startObject().field("price", "9.9").endObject())
                .execute();
        } catch (IOException e) {
            // nothing to do
        }
    }

    private void delete(Client client, String indexName) {
        client.prepareDelete(indexName, "test", "1").execute();
    }


    private void search(Client client, String indexName) {
        client.prepareSearch(indexName).setTypes("test").setSize(10).execute();
    }

    @SuppressWarnings("squid:S2095")
    private Client initTransportClient() {
        TransportClient client = null;
        try {
            Settings settings = Settings.builder()
                .put("cluster.name", "docker-cluster")
                .put("client.transport.sniff", false)
                .build();
            client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
        } catch (UnknownHostException e) {
            // nothing to do
        }

        return client;
    }

}
