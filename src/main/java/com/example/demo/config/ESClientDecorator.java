package com.example.demo.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


public class ESClientDecorator implements InitializingBean, DisposableBean {
    private RestHighLevelClient restHighLevelClient;

    private HttpHost[] httpHost;

    public ESClientDecorator(HttpHost[] httpHost) {
        this.httpHost = httpHost;
    }

    public RestHighLevelClient getRestHighLevelClient(String userName ,String passWord) {
        if (restHighLevelClient == null) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(userName, passWord));  //es账号密码
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            }));
        }
        return restHighLevelClient;
    }

    @Override
    public void destroy() throws Exception {
        restHighLevelClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHost));
    }

}
