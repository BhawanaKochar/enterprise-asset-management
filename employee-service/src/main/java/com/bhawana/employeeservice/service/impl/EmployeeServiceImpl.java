package com.bhawana.employeeservice.service.impl;

import com.bhawana.commonlibrary.exception.ResourceNotFoundException;
import com.bhawana.employeeservice.entity.Employee;
import com.bhawana.employeeservice.repository.EmployeeRepository;
import com.bhawana.employeeservice.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        repository.findByEmployeeCode(employee.getEmployeeCode())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Employee code already exists.");
                });

        repository.findByEmail(employee.getEmail())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Email already exists.");
                });

        return repository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id : " + id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existing = getEmployeeById(id);

        existing.setEmployeeCode(employee.getEmployeeCode());
        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(employee.getDepartment());
        existing.setDesignation(employee.getDesignation());
        existing.setPhone(employee.getPhone());
        existing.setJoiningDate(employee.getJoiningDate());
        existing.setStatus(employee.getStatus());

        return repository.save(existing);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = getEmployeeById(id);

        repository.delete(employee);
    }
}