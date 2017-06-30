package com.flightSchedule.core;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.db.Database;
import com.flightSchedule.baseObjects.Aircraft;
import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.baseObjects.Period;

public class SQLServerFlights extends SQLServerBase {
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	
	public boolean addPeriod(Period period){
		Database db = getDatabase();
		int result = 0;
		try{
			result = db.executeUpdate("insert into period(name, description) values ('" + period.getName() + "', '" + period.getDescription() + "')");
			
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		
		if (result > 0)
			return true;
		else
			return false;
	}
	
	public boolean removePeriod(Period period){
		Database db = getDatabase();
		int result = 0;
		
		try{
			result = db.executeUpdate("delete from period where name = '" + period.getName() + "'");
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		
		if(result > 0)
			return true;
		else
			return false;
	}
	
	public List<Period> getPeriodList(){

		List<Period> pds = new ArrayList<Period>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select name, description from period");
			
			while(rs.next()){
				pds.add(new Period(rs.getString("name"), rs.getString("description")));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		return pds;
		
	}

	public List<String> getPeriods(){

		List<String> pds = new ArrayList<String>();
		Database db = getDatabase();
		pds.add("");
		
		try{
			ResultSet rs = db.executeQuery("select name from period");
			
			while(rs.next()){
				pds.add(rs.getString("name"));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		return pds;
	}
	
	public boolean updateFlight(Flight flight){
		Database db = getDatabase();
		int result = 0;
		
		try{
			if(flight.getId() == null || flight.getId().equals(BigInteger.ZERO)){
				BigInteger id = db.executeUpdateWithId("insert into flight (date, period, hours, tailNumber, flightRemarks)"
						+ " values('" + sdf.format(flight.getDate().getTime()) + "', "
						+ "'" + flight.getPeriod() + "', "
						+ flight.getHours() + ", "
						+ "'" + flight.getAircraft().getTailNumber() + "', "
						+ "'" + flight.getRemarks() + "')");
				flight.setId(id);
				result = 1;
				
			}else{
				result = db.executeUpdate("update flight set period = '" + flight.getPeriod() + "'"
						+ ", hours = " + flight.getHours() + ""
						+ ", tailNumber = '" + flight.getAircraft().getTailNumber() + "' "
						+ ", flightRemarks = '" + flight.getRemarks() + "'"
						+ " where id = " + flight.getId());
			}
			
			if(result > 0){
				result = db.executeUpdate("delete from employeeFlight where flightId = " + flight.getId());
				
				if(flight.getPC().getId() != null){
					result = db.executeUpdate("insert into employeeFlight (flightId, employeeId, type) values"
							+ "(" + flight.getId() + ", " 
							+ flight.getPC().getId() + ", "
							+ "'" + Employee.Type.PC.toString() + "')");
				}
				if(flight.getPI().getId() != null){
					result = db.executeUpdate("insert into employeeFlight (flightId, employeeId, type) values"
							+ "(" + flight.getId() + ", " 
							+ flight.getPI().getId() + ", "
							+ "'" + Employee.Type.PI.toString() + "')");
				}
				if(flight.getCE1().getId() != null){
					result = db.executeUpdate("insert into employeeFlight (flightId, employeeId, type) values"
							+ "(" + flight.getId() + ", " 
							+ flight.getCE1().getId() + ", "
							+ "'" + Employee.Type.CE1.toString() + "')");
				}
				if(flight.getCE2().getId() != null){
					result = db.executeUpdate("insert into employeeFlight (flightId, employeeId, type) values"
							+ "(" + flight.getId() + ", " 
							+ flight.getCE2().getId() + ", "
							+ "'" + Employee.Type.CE2.toString() + "')");
				}
				
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		
		if(result > 0)
			return true;
		else
			return false;
		
	}
	
	public boolean removeFlight(Flight flight){
		Database db = getDatabase();
		int result = 0;
		
		try{
			result = db.executeUpdate("delete from flight where id = " + flight.getId());
			
			result = db.executeUpdate("delete from employeeFlight where flightId = " + flight.getId());
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		
		if(result > 0)
			return true;
		else
			return false;
		
	}
	
	public Flight getFlight(BigInteger id){
		Flight flight = new Flight();
		Database db = getDatabase();
		int rowNum = 1;
		try{
			ResultSet rs = db.executeQuery("select flight.id, flight.period, flight.hours, flight.tailNumber, flight.flightRemarks,"
											+ " empF.type as roleType, emp.Id as employeeId, emp.lastName, emp.firstName, emp.fullTime "
											+ " from employeeFlight empF"
											+ " join employee emp on emp.id = empF.employeeId"
											+ " join flight on flight.id = empF.flightId"
											+ " where empF.flightId = " + id);
			
			while(rs.next()){
				if(rowNum == 1){
					flight = fillFlight(rs);
					rowNum++;
				}
				Employee emp = new Employee(new BigInteger(rs.getString("employeeId")), rs.getString("lastName"), rs.getString("firstName"));
				emp.setFullTime(rs.getBoolean("fullTime"));
				String val = rs.getString("roleType");
				if(val != null){
					if(val.equals(Employee.Type.PC.toString()))
						flight.setPC(emp);
					else if(val.equals(Employee.Type.PI.toString()))
						flight.setPI(emp);
					else if(val.equals(Employee.Type.CE1.toString()))
						flight.setCE1(emp);
					else if(val.equals(Employee.Type.CE2.toString()))
						flight.setCE2(emp);
				}
			}
			
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		return flight;
	}
	
	public List<Flight> getFlights(Calendar date){

		Database db = getDatabase();
		
		List<Flight> flights = new ArrayList<Flight>();
		
		//TODO then add all the flights for full-time, followed by m-day soldiers
		try{
			//get all the full-time employees and base flight data
			String sql = "; with flight_cte (flightId, employeeId, roleType) "
								+ " as (select empF.flightId, empF.employeeId, empF.type from flight  "
								+ " join employeeFlight empF on empF.flightId = flight.id "
								+ " where flight.date = '" + sdf.format(date.getTime()) + "')"
							+ " select emp.Id as empId, emp.lastName, emp.firstName, emp.fulltime, "
							+ " cte.roleType, cte.flightId from employee emp "
							+ " left join flight_cte cte on cte.employeeId = emp.id "
							+ " left join flight on flight.id = cte.flightid "
							+ " where emp.fulltime = 1 or cte.roleType = '" + Employee.Type.PC.toString() + "'"
							+ " order by emp.fulltime desc, emp.sequence";
			ResultSet rs = db.executeQuery(sql);
			
			boolean prevFT = true;
			while(rs.next()){
				boolean ft = rs.getBoolean("fulltime");
				Employee emp = new Employee(new BigInteger(rs.getString("empId")), rs.getString("lastName"), rs.getString("firstName"));
				emp.setFullTime(ft);
				
				Flight fl = new Flight();
				fl.setPC(emp);
				
				String val = rs.getString("flightId");
				if(val != null){
					//get the rest of the crew
					if(rs.getString("roleType").equals(Employee.Type.PC.toString())){
						fl = getFlight(new BigInteger(val));						
					}else{
						fl.setPI(new Employee(null, "", "See Above"));
					}
				}
				fl.setDate(date);
				//create spacer row for flights
				if(prevFT && !ft){
					flights.add(new Flight(false));
				}
				prevFT = ft;
				flights.add(fl);
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		return flights;
		
	}
	
	private Flight fillFlight(ResultSet rs) throws SQLException{
		Flight flight = new Flight();
		flight.setId(new BigInteger(rs.getString("id")));
		flight.setPeriod(rs.getString("period"));
		flight.setHours(rs.getFloat("hours"));
		flight.setAircraft(new Aircraft(rs.getString("tailNumber")));
		flight.setRemarks(rs.getString("flightRemarks"));
		return flight;
		
	}
}
