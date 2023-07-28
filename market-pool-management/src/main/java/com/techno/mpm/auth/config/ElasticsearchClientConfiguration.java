package com.techno.mpm.auth.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages = "com.techno.mpm")
@EnableElasticsearchRepositories(basePackages = "com.techno.mpm.elastic.repository.candidate")
public class ElasticsearchClientConfiguration extends AbstractElasticsearchConfiguration {

	@SuppressWarnings("deprecation")
	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo("10.10.20.16:9200").build();
		return RestClients.create(clientConfiguration).rest();
	}

	@Bean
	public ElasticsearchRestTemplate elasticsearchRestTemplate() {
		return new ElasticsearchRestTemplate(elasticsearchClient());
	}
}