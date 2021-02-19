package com.example.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
* ES配置类
* @author      gyl
* @date        2020/5/6 10:50
*/
@Component
@PropertySource("classpath:application.yaml")
public class ElasticsearchConfig {
    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    @Value("${elasticsearch.clusterNodes}")
    private String clusterNodes;

    @Value("${elasticsearch.userName}")
    private String userName;

    @Value("${elasticsearch.passWord}")
    private String passWord;
    /**
     * 初始化
     */
    @Bean(value = "highLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        return getEsClientDecorator().getRestHighLevelClient(userName,passWord);
    }

    @Bean
    @Scope("singleton")
    public ESClientDecorator getEsClientDecorator() {

        String cNodes = this.clusterNodes;
        String[] node = cNodes.split(",");
        int length = node.length;
        HttpHost[] httpHost = new HttpHost[length];
        for (int i = 0; i < length; i++) {
            String[] nodes = node[i].split(":");
            httpHost[i] = new HttpHost(nodes[0], Integer.valueOf(nodes[1]));
        }
        return new ESClientDecorator(httpHost);
    }
}
