package com.example.rqchallenge.employees;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmployeeController {

	@Operation(summary = "Get All Employees", description = "Retrieves all employees from dummy API")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @GetMapping()
    ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

	@Operation(summary = "Get Employees by Name Search", description = "Retrieves employees from dummy API by name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @GetMapping("/search/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

	@Operation(summary = "Get Employee By ID", description = "Retrieves employee from dummy API by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

	@Operation(summary = "Get Highest Salary", description = "Retrieves the highest salary")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @GetMapping("/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

	@Operation(summary = "Get Top 10 Highest Earners", description = "Retrieves the top 10 highest earning employees")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @GetMapping("/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

	@Operation(summary = "Create Employee", description = "Creates a new employee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @PostMapping()
    ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);

	@Operation(summary = "Delete Employee", description = "Deletes employee by their ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "success"),
			@ApiResponse(responseCode = "404", description = "failure")
	})
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}
