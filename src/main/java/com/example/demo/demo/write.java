package com.example.demo.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.demo.bean.UserTags;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 写数据到excel
 */
//@Component
public class write implements ApplicationRunner {


    @Qualifier("highLevelClient")
    @Autowired
    RestHighLevelClient client;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ArrayList<UserTags> list = new ArrayList<>();

//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.termsQuery("tagType.keyword","18"));

        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("tagType.keyword", "19");
        SearchRequest searchRequest = new SearchRequest("tags");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(termsQueryBuilder);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);



        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();

        for(SearchHit hit: hits){
            list.add(JSON.parseObject(hit.getSourceAsString(), UserTags.class));
        }
        // 写入Excel
        simpleWrite(list);
    }

    private void simpleWrite(ArrayList<UserTags> list) {
        String fileName = "用户所在地标签3"+".xlsx";
        EasyExcel.write(fileName,UserTags.class).sheet(1,"所在地区").doWrite(list);
    }


}
