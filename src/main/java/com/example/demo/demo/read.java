package com.example.demo.demo;

import com.alibaba.excel.EasyExcel;
import com.example.demo.bean.UserTags;
import com.example.demo.listener.UserTagsListener;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class read implements ApplicationRunner {

    @Qualifier("highLevelClient")
    @Autowired
    RestHighLevelClient client;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 读取excel的数据插入到es中.
        String fileName = "用户所在地标签2.xlsx";
        EasyExcel.read(fileName, UserTags.class,new UserTagsListener(client)).sheet("所在地市").doRead();
    }
}
