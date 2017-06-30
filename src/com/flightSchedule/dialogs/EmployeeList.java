package com.flightSchedule.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Period;
import com.flightSchedule.core.SQLServerEmployees;
import com.flightSchedule.core.SQLServerFlights;
import com.flightSchedule.views.AbstractBaseList;

/**
 * @author andrew.j.jarosinski
 *
 */
public class EmployeeList extends AbstractBaseList<Employee> {
	private List<Employee> employees = new ArrayList<Employee>();

	public EmployeeList() {
		super();
		initData();
	}
	
	private void initData(){
		SQLServerEmployees sqlEmployees = new SQLServerEmployees();
		employees = sqlEmployees.getAllEmployees();
		
	}
	
	public List<Employee> getPeriods(){
		return employees;
	}
	public Employee[] getPeriodsArray(){
		return employees.toArray(new Employee[]{});
	}
	
	public void refresh(){
		initData();
	}

}
