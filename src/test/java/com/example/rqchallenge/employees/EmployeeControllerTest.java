package com.example.rqchallenge.employees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
	private final EmployeeService employeeService = Mockito.mock(EmployeeService.class);
	private final EmployeeController employeeController = new EmployeeController(employeeService);
	
	@Test
	public void getAllEmployeesTest() throws IOException {
		Mockito.when(this.employeeService.getAllEmployees()).thenReturn(employees());
		ResponseEntity<List<Employee>> response = this.employeeController.getAllEmployees();
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getAllEmployeesNullTest() throws IOException {
		Mockito.when(this.employeeService.getAllEmployees()).thenReturn(null);
		ResponseEntity<List<Employee>> response = this.employeeController.getAllEmployees();
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void getEmployeesByNameSearchTest() {
		Mockito.when(this.employeeService.getEmployeesByNameSearch(ArgumentMatchers.anyString())).thenReturn(employees());
		ResponseEntity<List<Employee>> response = this.employeeController.getEmployeesByNameSearch("Bob");
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getEmployeesByNameSearchNullTest() {
		Mockito.when(this.employeeService.getEmployeesByNameSearch(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());
		ResponseEntity<List<Employee>> response = this.employeeController.getEmployeesByNameSearch("Bob");
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void getEmployeeByIdTest() {
		Mockito.when(this.employeeService.getEmployeeById(ArgumentMatchers.anyString())).thenReturn(employees().get(0));
		ResponseEntity<Employee> response = this.employeeController.getEmployeeById("1");
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getEmployeeByIdNullTest() {
		Mockito.when(this.employeeService.getEmployeeById(ArgumentMatchers.anyString())).thenReturn(null);
		ResponseEntity<Employee> response = this.employeeController.getEmployeeById("1");
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void getHighestSalaryOfEmployeesTest() {
		Mockito.when(this.employeeService.getHighestSalaryOfEmployees()).thenReturn(80000);
		ResponseEntity<Integer> response = this.employeeController.getHighestSalaryOfEmployees();
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getHighestSalaryOfEmployeesNullTest() {
		Mockito.when(this.employeeService.getHighestSalaryOfEmployees()).thenReturn(null);
		ResponseEntity<Integer> response = this.employeeController.getHighestSalaryOfEmployees();
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void getTopTenHighestEarningEmployeeNamesTest() {
		List<String> names = new ArrayList<>();
		names.add("Bob");
		Mockito.when(this.employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(names);
		ResponseEntity<List<String>> response = this.employeeController.getTopTenHighestEarningEmployeeNames();
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void getTopTenHighestEarningEmployeeNamesNullTest() {		
		Mockito.when(this.employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(null);
		ResponseEntity<List<String>> response = this.employeeController.getTopTenHighestEarningEmployeeNames();
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void createEmployeeTest() {
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("1", employees().get(0));
		Mockito.when(this.employeeService.createEmployee(ArgumentMatchers.anyMap())).thenReturn(employees().get(0));
		ResponseEntity<Employee> response = this.employeeController.createEmployee(employeeMap);
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void createEmployeeFailTest() {
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("1", employees().get(0));
		Mockito.when(this.employeeService.createEmployee(ArgumentMatchers.anyMap())).thenReturn(null);
		ResponseEntity<Employee> response = this.employeeController.createEmployee(employeeMap);
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	@Test
	public void deleteEmployeeByIdTest() {
		Mockito.when(this.employeeService.deleteEmployee(ArgumentMatchers.anyString())).thenReturn("success");
		ResponseEntity<String> response = this.employeeController.deleteEmployeeById("1");
		Assertions.assertNotNull(response.getBody());
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void deleteEmployeeByIdFailTest() {
		Mockito.when(this.employeeService.deleteEmployee(ArgumentMatchers.anyString())).thenReturn("failure");
		ResponseEntity<String> response = this.employeeController.deleteEmployeeById("1");
		Assertions.assertNull(response.getBody());
		Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
	
	private List<Employee> employees() {
		Employee employee = new Employee();
		employee.setAge("24");
		employee.setId("1");
		employee.setName("Bob Smith");
		employee.setProfileImage("");
		employee.setSalary("234000");
		return Arrays.asList(employee);
	}
}
