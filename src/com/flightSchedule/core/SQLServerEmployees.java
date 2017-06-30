package com.flightSchedule.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.db.Database;
import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.baseObjects.Period;
import com.flightSchedule.baseObjects.Rank;
import com.flightSchedule.baseObjects.Role;


public class SQLServerEmployees extends SQLServerBase {
	private static final String MDAY_SEQUENCE = "200";

	public enum EMPLOYEE_TYPE{
		PC,
		PI,
		CE
	};
	
	public enum MOVE_DIRECTION{
		UP,
		DOWN
	};
	
	public boolean addEmployee(Employee emp){
		Database db = getDatabase();
		int result = 0;
		int fullTime = 0;
		BigInteger seq = new BigInteger(MDAY_SEQUENCE);
		try{
			if(emp.isFullTime()){
				fullTime = 1;
				emp.setSequence(seq);
			}else{
				//find max sequence and add one prior to insert
				ResultSet rs = db.executeQuery("select max(sequence) from employee where sequence < " + new BigInteger(MDAY_SEQUENCE));
				emp.setSequence(new BigInteger(rs.getString("sequence")).add(BigInteger.ONE));
			}
			
			
			result = db.executeUpdate("insert into employee(lastName, firstName, rank, type, fullTime, sequence, password, role) values ('" 
						+ emp.getLastName() + "', '" + emp.getFirstName() + "', '" + emp.getRank() + "', '" + emp.getType()
						+ "', " + fullTime + ", " + emp.getSequence() + ", '" + hashPassword(emp.getLastName(), emp.getSalt()) + "'"
								+ ", " + emp.getRole().getId() + ")");
			
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
	
	public Employee getEmployee(BigInteger id){
		Employee emp = new Employee();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime, sequence, role from employee "
					+ " where id = " + id);
			
			while(rs.next()){

				emp = fillEmployee(rs);
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return emp;		
	}
	
	public List<Employee> getAllEmployees(){
		List<Employee> employees = new ArrayList<Employee>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime, sequence, role from employee "
					+ " order by fullTime desc, sequence, lastName, firstName");
			
			while(rs.next()){

				employees.add(fillEmployee(rs));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return employees;		
	}
	
	public LinkedHashMap<BigInteger, String> getAllEmployeesByName(){
		LinkedHashMap<BigInteger, String> emps = new LinkedHashMap<BigInteger, String>();
		emps.put(BigInteger.ZERO, "");
		

		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime, sequence from employee order by lastName, firstName");
			
			while(rs.next()){

				Employee emp = fillEmployee(rs);
				emps.put(emp.getId(), emp.getReverseName());
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		return emps;
	}
	
	public List<Employee> getFullTimeEmployees(){
		List<Employee> employees = new ArrayList<Employee>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime, sequence from employee where fullTime = 1 order by lastName, firstName");
			
			while(rs.next()){
				
				employees.add(fillEmployee(rs));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return employees;
	}
	
	/**
	 * Convenience method to reduce loops, storing the full-time employees as a PC
	 * @param date	Set date of flight as this date
	 * @return
	 */
	public List<Flight> getFullTimeEmployeesAsFlight(Calendar date){
		List<Flight> employees = new ArrayList<Flight>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime, sequence from employee where fullTime = 1");
			
			while(rs.next()){
				
				employees.add(new Flight(fillEmployee(rs), date));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return employees;
	}
	
	public String[] getFullTimeEmployeesByNameInArray(){
		List<String> employees = new ArrayList<String>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName, rank, type, fullTime from employee where fullTime = 1");
			
			while(rs.next()){
				employees.add(rs.getString("lastName"));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return employees.toArray(new String[]{});
	}
	
	/**
	 * Get's employees by the given type
	 * 
	 * @param type
	 * @return	Returns a map with a key of the employee ID and a string value of the last name, comma first name
	 */
	public Map<BigInteger, String> getEmployeesByType(EMPLOYEE_TYPE type){
		Map<BigInteger, String> employees = new LinkedHashMap<BigInteger, String>();
		Database db = getDatabase();
		employees.put(BigInteger.ZERO, "");
		
		String condition = "";
		switch(type){
			case PI:
				condition = " where (type = '" + Employee.Type.PC.toString() + "' "
						+ "or type = '" + Employee.Type.PI.toString() + "')";
				break;
			case CE:
				condition = " where (type = '" + Employee.Type.PC.toString() + "' "
						+ "or type = '" + Employee.Type.PI.toString() + "' "
						+ "or type = 'CE' "
						+ "or type = '" + Employee.Type.CE1.toString() + "' "
						+ "or type = '" + Employee.Type.CE2.toString() + "')";
				break;
		}
		
		
		try{
			ResultSet rs = db.executeQuery("select id, lastName, firstName from employee " + condition + " order by lastName, firstName");
			
			while(rs.next()){
				employees.put(new BigInteger(rs.getString("id")), 
						(rs.getString("lastName") + ", " + rs.getString("firstName")));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return employees;
		
	}
	
	public String[] getWorkSchedules(){
		List<String> scheds = new ArrayList<String>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("");
			
			while(rs.next()){
//				scheds.add(rs.getString("schedule"));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		
		return scheds.toArray(new String[]{});
	}
	
	public boolean updateEmployee(Employee emp){
		Database db = getDatabase();
		int result = 0;
		
		int ft = 0;
		if(emp.isFullTime())
			ft = 1;
		
		try{
			result = db.executeUpdate("update employee set lastName = '" + emp.getLastName() + "'"
					+ ", firstName = '" + emp.getFirstName() + "'"
					+ ", rank = '" + emp.getRank().toString() + "' "
					+ ", type = '" + emp.getType().toString() + "'"
					+ ", fullTime = " + ft + ""
					+ ", sequence = " + emp.getSequence() + ""
					+ ", role = " + emp.getRole().getId() + ""
					+ " where id = " + emp.getId());
			
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
	

	
	public boolean removeEmployee(Employee emp){
		Database db = getDatabase();
		int result = 0;
		
		try{
			result = db.executeUpdate("delete from employee where id = " + emp.getId());
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
	
	public boolean moveEmployee(Employee emp, MOVE_DIRECTION dir){
		Database db = getDatabase();
		int result = 0;
		
		BigInteger resultantSeq;
		String whereCondition = "";
		if(dir.equals(MOVE_DIRECTION.UP)){
			resultantSeq = emp.getSequence().add(new BigInteger("-1"));
			whereCondition = "(select max(sequence) from employee where sequence < " + emp.getSequence() + ")";
		}else{
			resultantSeq = emp.getSequence().add(new BigInteger("1"));
			whereCondition = "(select min(sequence) from employee where sequence > " + emp.getSequence() + ")";
		}
			
		
		try{
			//find record for dest direction
			ResultSet rs = db.executeQuery("select id from employee where sequence = " + whereCondition);
			rs.next();
			BigInteger foundId = new BigInteger(rs.getString("id"));
			
			//update given/found emp with new sequence
			result = db.executeUpdate("update employee set sequence = " + resultantSeq + " where id = " + emp.getId());
			result = db.executeUpdate("update employee set sequence = " + emp.getSequence() + " where id = " + foundId);
			
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
	
	/**
	 * Convenience method that calls {@link SQLServerEmployees#updatePassword(Employee, String)  updatePassword}
	 * 
	 * @param emp
	 * @return
	 */
	public boolean resetPassword(Employee emp){
		return updatePassword(emp, emp.getLastName());
	}
	
	public boolean updatePassword(Employee emp, String password){
		Database db = getDatabase();
		int result = 0;
		
		try{
			result = db.executeUpdate("update employee set password = '" + hashPassword(password, emp.getSalt()) + "'"
					+ " where id = " + emp.getId());
			
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
	
	public boolean isPasswordMatch(BigInteger id, String enteredValue){
		Database db = getDatabase();
		boolean result = false;
		
		try{
			ResultSet rs = db.executeQuery("select lastName, firstName, password from employee where id = " + id);
			rs.next();
			String password = rs.getString("password");
			Employee emp = new Employee(id, rs.getString("lastName"), rs.getString("firstName"));
			if(password.equals(hashPassword(enteredValue, emp.getSalt())))
				result = true;
			
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally{
			db.close();
		}
		
		return result;
	}
	
	private String hashPassword(String enteredValue, String salt){
	    try {
	        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
	        String passWithSalt = enteredValue + salt;
	        byte[] passBytes = passWithSalt.getBytes();
	        byte[] passHash = sha256.digest(passBytes);             
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< passHash.length ;i++) {
	            sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));         
	        }
	        String generatedPassword = sb.toString();
	        return generatedPassword;
	    } catch (NoSuchAlgorithmException e) {
			ExceptionHandler.logException(e);
		}       
	    return null;

	}
	
	private Employee fillEmployee(ResultSet rs) throws SQLException{
		Employee emp = new Employee();
		emp.setId(new BigInteger(rs.getString("id")));
		emp.setLastName(rs.getString("lastName"));
		emp.setFirstName(rs.getString("firstName"));
		String rank = rs.getString("rank");
		if(rank != null && !rank.equals(""))
			emp.setRank(Rank.valueOf(rank));
		String type = rs.getString("type");
		if(type != null && !type.equals("")){
			if(type.equals("CE"))
				emp.setType(Employee.Type.CE1);
			else
				emp.setType(Employee.Type.valueOf(type));
		}
		emp.setFullTime(rs.getBoolean("fullTime"));
		emp.setSequence(new BigInteger(rs.getString("sequence")));
		try{
			int id = rs.getInt("role");
			for(Role role : Role.values()){
				if(role.getId() == id){
					emp.setRole(role);
					break;
				}
			}
		}catch(SQLException e){
			//some queries don't require this
		}
		return emp;
		
	}
}
