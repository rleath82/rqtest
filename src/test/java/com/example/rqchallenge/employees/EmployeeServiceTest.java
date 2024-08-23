package com.example.rqchallenge.employees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	private final EmployeeConsumer employeeConsumer = Mockito.mock(EmployeeConsumer.class);
	private final EmployeeService employeeService = new EmployeeService(employeeConsumer);
	
	@Test
	public void getAllEmployeesTest() {
		Mockito.when(this.employeeConsumer.getAllEmployees()).thenReturn(getEmployeeResponse());
		List<Employee> employees = this.employeeService.getAllEmployees();
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(11, employees.size());
	}

	@Test
	public void getAllEmployees404Test() {
		Mockito.when(this.employeeConsumer.getAllEmployees())
			.thenThrow(WebClientResponseException.NotFound.create(404, "Not Found", HttpHeaders.EMPTY, null, null));
		List<Employee> employees = this.employeeService.getAllEmployees();
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(0, employees.size());
	}
	
	@Test
	public void getAllEmployees500Test() {
		Mockito.when(this.employeeConsumer.getAllEmployees())
			.thenThrow(WebClientResponseException.InternalServerError.create(500, "Internal Server Error", HttpHeaders.EMPTY, null, null));
		List<Employee> employees = this.employeeService.getAllEmployees();
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(0, employees.size());
	}
	
	@Test
	public void getEmployeesNameSmithSearch() {
		Mockito.when(this.employeeConsumer.getAllEmployees()).thenReturn(getEmployeeResponse());
		List<Employee> employees = this.employeeService.getEmployeesByNameSearch("Smith");
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(10, employees.size());
	}
	
	@Test
	public void getEmployeesNameLarrySearch() {
		Mockito.when(this.employeeConsumer.getAllEmployees()).thenReturn(getEmployeeResponse());
		List<Employee> employees = this.employeeService.getEmployeesByNameSearch("Larry");
		Assertions.assertNotNull(employees);
		Assertions.assertEquals(2, employees.size());
	}
	
	@Test
	public void getEmployeeByIdTest() {
		String response = "{\"status\":\"success\",\"data\":{\"id\":2,\"employee_name\":\"Garrett Winters\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"},\"message\":\"Successfully! Record has been fetched.\"}";
		Mockito.when(this.employeeConsumer.getEmployeeById(ArgumentMatchers.anyString())).thenReturn(response);
		Employee employee = this.employeeService.getEmployeeById("2");
		Assertions.assertNotNull(employee);
		Assertions.assertEquals("170750", employee.getSalary());
	}
	
	@Test
	public void getEmployeeById404Test() {
		Mockito.when(this.employeeConsumer.getEmployeeById(ArgumentMatchers.anyString()))
			.thenThrow(WebClientResponseException.NotFound.create(404, "Not Found", HttpHeaders.EMPTY, null, null));
		Employee employee = this.employeeService.getEmployeeById("2");
		Assertions.assertNull(employee);
	}
	
	@Test
	public void getEmployeeById500Test() {
		Mockito.when(this.employeeConsumer.getEmployeeById(ArgumentMatchers.anyString()))
			.thenThrow(WebClientResponseException.InternalServerError.create(500, "Internal Server Error", HttpHeaders.EMPTY, null, null));
		Employee employee = this.employeeService.getEmployeeById("2");
		Assertions.assertNull(employee);
	}
	
	@Test
	public void getHighestSalaryTest() {
		Mockito.when(this.employeeConsumer.getAllEmployees()).thenReturn(getEmployeeResponse());
		Integer salary = this.employeeService.getHighestSalaryOfEmployees();
		Assertions.assertEquals(901000, salary);
	}
	
	@Test
	public void getTop10HighestEarnersTest() {
		Mockito.when(this.employeeConsumer.getAllEmployees()).thenReturn(getEmployeeResponse());
		List<String> names = this.employeeService.getTopTenHighestEarningEmployeeNames();
		Assertions.assertEquals("Larry Sanders", names.get(0));
	}
	
	@Test
	public void createEmployeeTest() {
		Employee employee = new Employee();
		employee.setAge("24");
		employee.setId("1");
		employee.setName("Bob Smith");
		employee.setProfileImage("");
		employee.setSalary("234000");
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("1", employee);
		Mockito.when(this.employeeConsumer.createNewEmployee(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(employee);
		Employee createdEmployee = this.employeeService.createEmployee(employeeMap);
		Assertions.assertEquals("Bob Smith", createdEmployee.getName());
	}
	
	@Test
	public void createEmployeeFailTest() {
		Employee employee = new Employee();
		employee.setAge("24");
		employee.setId("1");
		employee.setName("Bob Smith");
		employee.setProfileImage("");
		employee.setSalary("234000");
		Map<String, Object> employeeMap = new HashMap<>();
		employeeMap.put("1", employee);
		Mockito.when(this.employeeConsumer.createNewEmployee(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
			.thenThrow(WebClientResponseException.InternalServerError.create(500, "InternalServerError", HttpHeaders.EMPTY, null, null));
		Employee createdEmployee = this.employeeService.createEmployee(employeeMap);
		Assertions.assertNull(createdEmployee);
	}
	
	@Test
	public void deleteEmployeeTest() {
		Mockito.when(this.employeeConsumer.deleteEmployee(ArgumentMatchers.anyString())).thenReturn("success");
		String status = this.employeeService.deleteEmployee("1");
		Assertions.assertEquals("success", status);
	}
	
	@Test
	public void deleteEmployeeFailTest() {
		Mockito.when(this.employeeConsumer.deleteEmployee(ArgumentMatchers.anyString()))
			.thenThrow(WebClientResponseException.InternalServerError.create(500, "Internal Server Error", HttpHeaders.EMPTY, null, null));
		String status = this.employeeService.deleteEmployee("1");
		Assertions.assertEquals("unsuccessful", status);
	}
	
	private EmployeeResponse getEmployeeResponse() {
		Employee employee = new Employee();
		employee.setAge("24");
		employee.setId("1");
		employee.setName("Bob Smith");
		employee.setProfileImage("");
		employee.setSalary("234000");
		
		Employee employee1 = new Employee();
		employee1.setAge("24");
		employee1.setId("2");
		employee1.setName("Jen Smith");
		employee1.setProfileImage("");
		employee1.setSalary("234001");
		
		Employee employee2 = new Employee();
		employee2.setAge("24");
		employee2.setId("3");
		employee2.setName("Jack Smith");
		employee2.setProfileImage("");
		employee2.setSalary("234002");
		
		Employee employee3 = new Employee();
		employee3.setAge("24");
		employee3.setId("4");
		employee3.setName("John Smith");
		employee3.setProfileImage("");
		employee3.setSalary("234500");
		
		Employee employee4 = new Employee();
		employee4.setAge("24");
		employee4.setId("5");
		employee4.setName("Ron Smith");
		employee4.setProfileImage("");
		employee4.setSalary("234874");
		
		Employee employee5 = new Employee();
		employee5.setAge("24");
		employee5.setId("6");
		employee5.setName("Rupert Smith");
		employee5.setProfileImage("");
		employee5.setSalary("234313");
		
		Employee employee6 = new Employee();
		employee6.setAge("24");
		employee6.setId("7");
		employee6.setName("Gubert Smith");
		employee6.setProfileImage("");
		employee6.setSalary("234852");
		
		Employee employee7 = new Employee();
		employee7.setAge("24");
		employee7.setId("8");
		employee7.setName("Jennifer Smith");
		employee7.setProfileImage("");
		employee7.setSalary("50000");
		
		Employee employee8 = new Employee();
		employee8.setAge("24");
		employee8.setId("9");
		employee8.setName("Larry Smith");
		employee8.setProfileImage("");
		employee8.setSalary("600000");
		
		Employee employee9 = new Employee();
		employee9.setAge("24");
		employee9.setId("10");
		employee9.setName("Zack Smith");
		employee9.setProfileImage("");
		employee9.setSalary("465020");
		
		Employee employee10 = new Employee();
		employee10.setAge("24");
		employee10.setId("11");
		employee10.setName("Larry Sanders");
		employee10.setProfileImage("");
		employee10.setSalary("901000");
		
		EmployeeResponse response = new EmployeeResponse();
		List<Employee> employees = new ArrayList<>();
		employees.add(employee);
		employees.add(employee1);
		employees.add(employee2);
		employees.add(employee3);
		employees.add(employee4);
		employees.add(employee5);
		employees.add(employee6);
		employees.add(employee7);
		employees.add(employee8);
		employees.add(employee9);
		employees.add(employee10);
		response.setEmployees(employees);
		response.setStatus("success");
		return response;
	}
}
