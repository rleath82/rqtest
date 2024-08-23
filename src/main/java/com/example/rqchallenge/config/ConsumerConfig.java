package com.example.rqchallenge.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class ConsumerConfig {
	private final String dummyUrl;
	
	public ConsumerConfig(@Value("${service.dummy.url}") String dummyUrl) {
		this.dummyUrl = dummyUrl;
	}
	
	@Bean("dummyWebClient")
	WebClient dummyWebClient() {
		ClientHttpConnector clientConnector = new ReactorClientHttpConnector(HttpClient
				.create(ConnectionProvider.builder("dummyWebClient")
						.maxConnections(ConnectionProvider.DEFAULT_POOL_MAX_CONNECTIONS)
						.pendingAcquireMaxCount(1000)
						.maxIdleTime(Duration.ofSeconds(10))
						.build())
				.wiretap(false)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.responseTimeout(Duration.ofSeconds(10)));
		
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(dummyUrl);
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
		
		return WebClient.builder()
				.clientConnector(clientConnector)
				.baseUrl(dummyUrl)
				.uriBuilderFactory(factory)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
