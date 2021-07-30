package com.techgeeknext.repository.employee;

import com.techgeeknext.entities.employee.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

}
