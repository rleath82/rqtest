package com.example.rqchallenge.employees;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Employee Controller")
@RestController
public class EmployeeController implements IEmployeeController {
	private final EmployeeService employeeService;
	
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
		List<Employee> employees = this.employeeService.getAllEmployees();		
		if(employees == null || employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}
	
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {		
		List<Employee> employees = this.employeeService.getEmployeesByNameSearch(searchString);
		if(employees == null || employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}
	
	public ResponseEntity<Employee> getEmployeeById(String id) {
		Employee employee = this.employeeService.getEmployeeById(id);
		if(employee == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(employee, HttpStatus.OK);
	} 
	
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		Integer salary = this.employeeService.getHighestSalaryOfEmployees();
		if(salary == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(salary, HttpStatus.OK);
	}
	
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		List<String> topTenEarners = this.employeeService.getTopTenHighestEarningEmployeeNames();
		if(topTenEarners == null || topTenEarners.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(topTenEarners, HttpStatus.OK);
	}
	
	public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
		Employee employee = this.employeeService.createEmployee(employeeInput);
		if(employee == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}
	
	public ResponseEntity<String> deleteEmployeeById(String id) {
		String status = this.employeeService.deleteEmployee(id);
		if(status.equals("failure")) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
}
