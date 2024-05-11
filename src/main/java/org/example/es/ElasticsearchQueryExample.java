package org.example.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.System.exit;

public class ElasticsearchQueryExample {

    private static final String INDEX_NAME = "student"; // Replace with your index name
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

            RestHighLevelClient client = createRestClientBuilder();
        aggsAvg(client);

//        insertRandomData(client, 100);
        exit(1);

        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME); // 替换为你的索引名
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 使用QueryBuilders构建查询条件
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery()); // 这里是匹配所有文档

        // 根据字段查询
//        QueryBuilder queryBuilder = new MatchQueryBuilder("field1", "value23");
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("field1", "value23");
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

    public static RestHighLevelClient createRestClientBuilder() {
        // 创建客户端
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("192.168.9.128", 9200, "http"));
        return new RestHighLevelClient(builder);
    }

    public static void aggsAvg (RestHighLevelClient client) throws IOException {
        // 创建SearchRequest
        SearchRequest searchRequest = new SearchRequest("student");

        // 构建聚合
        AvgAggregationBuilder avgAgeAgg = AggregationBuilders.avg("avg_age")
                .field("age");

        AvgAggregationBuilder avgAge188Agg = AggregationBuilders.avg("avg_age_188")
                .script(new Script("doc['age'].value + 188"));

        // 构建查询
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(0); // 不返回任何文档，只返回聚合结果
        searchSourceBuilder.aggregation(avgAgeAgg);
        searchSourceBuilder.aggregation(avgAge188Agg);

        searchRequest.source(searchSourceBuilder);

        // 执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        // 输出结果
        System.out.println(response);
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

        jsonMap.put("name", getRandomChineseString(RANDOM.nextInt(3)));
        jsonMap.put("age", RANDOM.nextInt(100));
        jsonMap.put("gender", RANDOM.nextBoolean() ? "male" : "female");
        jsonMap.put("phone", RANDOM.nextLong());
        jsonMap.put("email", getRandomChineseString(RANDOM.nextInt(3)) + "@example.com");
        return jsonMap;
    }

    /***
     * 生成固定长度随机中文，kuojung
     * @param n 中文个数
     * @return 中文串
     */
    public static String getRandomChineseString(int n){
        String zh_cn = "";
        String str ="";

        // Unicode中汉字所占区域\u4e00-\u9fa5,将4e00和9fa5转为10进制
        int start = Integer.parseInt("4e00", 16);
        int end = Integer.parseInt("9fa5", 16);

        for(int ic=0;ic<n;ic++){
            // 随机值
            int code = (new Random()).nextInt(end - start + 1) + start;
            // 转字符
            str = new String(new char[] { (char) code });
            zh_cn = zh_cn + str;
        }
        return zh_cn;
    }
}
