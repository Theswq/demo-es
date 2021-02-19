package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.example.demo.bean.UserTags;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
// dataListener不能被spring管理.每次excel都要new,然后里面用到spring,可以用构造方法传进去
public class UserTagsListener extends AnalysisEventListener<UserTags> {


      RestHighLevelClient client;

    public UserTagsListener(RestHighLevelClient client){
        this.client = client;
    }

    /**
     * 每次处理500条,可以调大些,然后清理list,方便内存回收
     */
    private static final int BATHCH_COUNT = 500;

    /**
     * 存放读取出来的数据
     */
    List<UserTags> list = new ArrayList<>(BATHCH_COUNT);

    /**
     * 每解析一条数据,都会来调用
     * @param userTags
     * @param analysisContext
     */
    @Override
    public void invoke(UserTags userTags, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(userTags));
        list.add(userTags);

        if(list.size() >= BATHCH_COUNT){
            saveData(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(list.size()>0){
            saveData(list);
        }
        log.info("所有数据解析完成!");
    }

    private void saveData(List<UserTags> list){
        log.info("{}条数据，开始存储数据库！", list.size());
        // 批量插入es
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(t -> {
            IndexRequest indexRequest = new IndexRequest("tags","tag",t.getTagId());
            indexRequest.source(JSON.toJSONString(t), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulkResponse = null;
        try {
            bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if( null != bulkResponse && bulkResponse.hasFailures()){
            for(BulkItemResponse bulkItemResponse : bulkResponse){
                if(bulkItemResponse.isFailed()){
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                }
            }
        }

        log.info("存储数据库成功！");
    }
}
