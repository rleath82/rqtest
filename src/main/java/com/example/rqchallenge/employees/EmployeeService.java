package com.example.rqchallenge.employees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService {
	private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
	
	private final EmployeeConsumer employeeConsumer;
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public EmployeeService(EmployeeConsumer employeeConsumer) {
		this.employeeConsumer = employeeConsumer;
	}
	
	public List<Employee> getAllEmployees() {
		EmployeeResponse response = null;
		try {
			response = this.employeeConsumer.getAllEmployees();
		} catch (WebClientRequestException webClientRequestException) {
			log.error("Caught WebClientRequestException requesting all employees", webClientRequestException);
		} catch (WebClientResponseException webClientResponseException) {
			if(webClientResponseException.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("Caught not found requesting all employees");
			} else {
				log.error("Caught other exception requesting all employees", webClientResponseException);
			}
		}
		if(response == null || response.getEmployees().isEmpty()) {
			log.error("getAllEmployees() returned null");
			return new ArrayList<>();
		}
		log.debug("getAllEmployees returned {} employees", response.getEmployees().size());
		return response.getEmployees();
	}
	
	public List<Employee> getEmployeesByNameSearch(String nameString) {
		List<Employee> employees = this.getAllEmployees();
		List<String> employeeNames = employees.stream().map(Employee::getName).collect(Collectors.toList());
		List<Employee> filteredEmployees = new ArrayList<>();
		for(String name : employeeNames) {
			List<String> nameSplit = Arrays.asList(name.toLowerCase(Locale.US).split(" "));
			for(String word : nameString.split(" ")) {
				if(nameSplit.contains(word.toLowerCase(Locale.US))) {
					employees.stream().filter(e -> e.getName().equals(name)).findFirst().ifPresent(filteredEmployees::add);
				}
			}
		}
		return filteredEmployees;
	}
	
	public Employee getEmployeeById(String id) {
		String response = null;
		try {
			response = this.employeeConsumer.getEmployeeById(id);
		} catch (WebClientResponseException webClientResponseException) {
			if(webClientResponseException.getStatusCode() == HttpStatus.NOT_FOUND) {
				log.error("Caught not found requesting employee with id: {}", id);
			} else {
				log.error("Caught other exception requesting employee with id: {}", id, webClientResponseException);
			}
		}
		
		if(response == null) {
			return null;
		}
		Employee employee = new Employee();		
		try {
			JsonNode rootNode = objectMapper.readTree(response);			
			employee.setAge(rootNode.path("data").path("employee_age").asText());
			employee.setId(rootNode.path("data").path("id").asText());
			employee.setName(rootNode.path("data").path("employee_name").asText());
			employee.setSalary(rootNode.path("data").path("employee_salary").asText());
			employee.setProfileImage(rootNode.path("data").path("profile_image").asText());		
		} catch (JsonMappingException ex) {
			log.error("Caught JsonMappingException", ex);
		} catch (JsonProcessingException ex) {
			log.error("Caught JsonProcessingException", ex);
		}
		
		
		return employee;
	}
	
	public Integer getHighestSalaryOfEmployees() {
		List<Employee> employees = this.getAllEmployees();
		if(employees.isEmpty()) {
			return null;
		}
		return Integer.valueOf(Collections.max(employees, Comparator.comparing(e -> Integer.valueOf(e.getSalary()))).getSalary());		
	}
	
	public List<String> getTopTenHighestEarningEmployeeNames() {
		List<Employee> employees = this.getAllEmployees();
		if(employees.isEmpty()) {
			return null;
		}
		List<Integer> salaries = employees.stream().map(e -> Integer.parseInt(e.getSalary())).collect(Collectors.toList());
		Collections.sort(salaries, Collections.reverseOrder());
		List<Employee> sortedEmployees = new ArrayList<>();
		for(Integer s : salaries) {
			employees.stream().filter(e -> e.getSalary().equals(String.valueOf(s))).findFirst().ifPresent(sortedEmployees::add);;
		}
		return sortedEmployees.stream().limit(10).map(Employee::getName).collect(Collectors.toList());
	}
	
	public Employee createEmployee(Map<String, Object> employeeInput) {		
		for(Map.Entry<String, Object> entry : employeeInput.entrySet()) {
			Employee employee = objectMapper.convertValue(entry.getValue(), Employee.class);
			employee.setId(entry.getKey());	
			try {
				return this.employeeConsumer.createNewEmployee(employee.getName(), employee.getSalary(), employee.getAge());
			} catch (WebClientResponseException webClientResponseException) {
				log.error("Caught WebClientResponseException creating employee", webClientResponseException);
			} catch (Exception ex) {
				log.error("Caught other exception creating employee", ex);
			}
			
		}
		return null;
	}
	
	public String deleteEmployee(String id) {
		try {
			return this.employeeConsumer.deleteEmployee(id);
		} catch (WebClientResponseException webClientResponseException) {
			log.error("Caught WebClientResponseException deleting employee with id: {}", id, webClientResponseException);
		} catch (Exception ex) {
			log.error("Caught other exception deleting employee with id: {}", id, ex);
		}
		return "unsuccessful";
	}
}
