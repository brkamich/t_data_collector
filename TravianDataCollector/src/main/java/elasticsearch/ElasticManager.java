/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elasticsearch;

import data.GameObject;
import data.Player;
import java.io.IOException;
import model.Model;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
/**
 *
 * @author Michal
 */
public class ElasticManager {
    private int bulkSize;
    private RestHighLevelClient client;
    public ElasticManager(){
        client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        bulkSize = 100;
    }
    
    public void sendDataToElastic(Model model) throws IOException{
        BulkRequest bRequest = new BulkRequest();
        BulkResponse bResponse;
        int count = 0;
        for(GameObject o : model.getObjects()){
            bRequest.add(o.getIndexRequest(model.getStrTimestamp()));
            count++;
            if(count>100){
                count = 0;
                bResponse = client.bulk(bRequest);
                bRequest = new BulkRequest();
            }
        }
        if(count>0){
            bResponse = client.bulk(bRequest);
        }
    }
}
