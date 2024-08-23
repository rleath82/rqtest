package com.example.rqchallenge.employees;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeResponse {
	private String status;

	@JsonProperty("data")
	private List<Employee> employees;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}
