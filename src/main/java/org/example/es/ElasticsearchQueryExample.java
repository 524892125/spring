package org.example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ElasticsearchQueryExample {

    private static final String INDEX_NAME = "test_index"; // Replace with your index name
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        // 创建客户端
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);





//        insertRandomData(client, 5);

        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME); // 替换为你的索引名
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 使用QueryBuilders构建查询条件
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); // 这里是匹配所有文档

        // 根据字段查询
        QueryBuilder queryBuilder = new MatchQueryBuilder("field1", "value23");
        searchSourceBuilder.query(queryBuilder); // 这里是匹配所有文档

        // 设置查询构建器
        searchRequest.source(searchSourceBuilder);

        try {
            // 执行查询并获取响应
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            // 处理响应
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void insertRandomData(RestHighLevelClient client, int numberOfDocuments) {
        for (int i = 0; i < numberOfDocuments; i++) {
            Map<String, Object> jsonMap = generateRandomDocument();
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME)
                    .source(jsonMap, XContentType.JSON);
            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                System.out.println("Inserted document: " + indexResponse.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, Object> generateRandomDocument() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("field1", "value" + RANDOM.nextInt(100));
        jsonMap.put("field2", RANDOM.nextInt(1000));
        // Add more fields as needed
        return jsonMap;
    }
}
