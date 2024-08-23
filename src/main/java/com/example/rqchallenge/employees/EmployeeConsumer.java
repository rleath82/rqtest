package com.example.rqchallenge.employees;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class EmployeeConsumer {
	private static final Logger log = LoggerFactory.getLogger(EmployeeConsumer.class);
	
	private final WebClient dummyWebClient;	
	private final String getEmployeesUri = "/api/v1/employees";
	private final String getEmployeeByIdUri = "/api/v1/employee/{id}";
	private final String createEmployeeUri = "/api/v1/create";
	private final String deleteEmployeeUri = "/api/v1/delete/{id}";
	
	public EmployeeConsumer(@Qualifier("dummyWebClient") WebClient dummyWebClient) {
		this.dummyWebClient = dummyWebClient;
	}
	
	public EmployeeResponse getAllEmployees() {
		log.trace("Requesting all employees");		
		return this.dummyWebClient.get()
				.uri(this.getEmployeesUri)
				.retrieve()
				.bodyToMono(EmployeeResponse.class)
				.retryWhen(Retry.max(1).filter(this::is5xxServerError))
				.switchIfEmpty(Mono.just(new EmployeeResponse()))
				.block();
	}
	
	public String getEmployeeById(String id) {
		log.trace("Requesting employee by id: {}", id);
		return this.dummyWebClient.get()
				.uri(this.getEmployeeByIdUri, id)
				.retrieve()
				.bodyToMono(String.class)
				.retryWhen(Retry.max(1).filter(this::is5xxServerError))
				.switchIfEmpty(Mono.just(new String()))
				.block();
	}
	
	public Employee createNewEmployee(String name, String salary, String age) {
		log.trace("Creating new employee with name: {}, salary: {}, and age: {}", name, salary, age);
		return this.dummyWebClient.post()
				.uri(uriBuilder -> uriBuilder
						.path(this.createEmployeeUri)
						.queryParam("name", "{name}")
						.queryParam("salary", "{salary}")
						.queryParam("age", "{age}")
						.build(name, salary, age))
				.retrieve()
				.bodyToMono(Employee.class)
				.retryWhen(Retry.max(1).filter(this::is5xxServerError))
				.switchIfEmpty(Mono.just(new Employee()))
				.block();
	}
	
	public String deleteEmployee(String id) {
		log.trace("Deleting employee with id: {}", id);
		return this.dummyWebClient.delete()
				.uri(this.deleteEmployeeUri, id)
				.retrieve()
				.bodyToMono(String.class)
				.retryWhen(Retry.max(1).filter(this::is5xxServerError))
				.switchIfEmpty(Mono.just(new String()))
				.block();
	}
	
	private boolean is5xxServerError(Throwable throwable) {
		return throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode().is4xxClientError();
	}
}
