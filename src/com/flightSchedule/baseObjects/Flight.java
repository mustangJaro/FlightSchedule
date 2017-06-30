/**
 * 
 */
package com.flightSchedule.baseObjects;

import java.math.BigInteger;
import java.util.Calendar;

/**
// * Base Object for retrieving/storing flight information to/from the DB
 * 
 * @author andrew.j.jarosinski
 *
 */
public class Flight extends BaseObject {
	private BigInteger id;
	private Calendar date;
	private String period = "";
	private float hours;
	private Aircraft aircraft = new Aircraft();
	private Employee pc = new Employee();
	private Employee pi = new Employee();
	//TODO change to list of CEs
	private Employee ce1 = new Employee();
	private Employee ce2 = new Employee();
	private String remarks = "";
	
	public Flight(){
		super();
		setCreatedBy(new Employee());
	}
	public Flight(boolean editable){
		super();
		setEditable(editable);
	}
	public Flight(BigInteger id){
		this();
		this.id = id;
	}
	public Flight(Employee employee, Calendar date){
		this();
		this.pc = employee;
		this.date = date;
	}
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public float getHours() {
		return hours;
	}
	public void setHours(float hours) {
		this.hours = hours;
	}
	public Aircraft getAircraft() {
		return aircraft;
	}
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}
	
	public Employee getPC() {
		return pc;
	}
	public void setPC(Employee pc) {
		this.pc = pc;
	}
	public Employee getPI() {
		return pi;
	}
	public void setPI(Employee pi) {
		this.pi = pi;
	}
	public Employee getCE1() {
		return ce1;
	}
	public void setCE1(Employee ce) {
		this.ce1 = ce;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "Flight Period " + period + " for " + hours + " hours on A/C " + aircraft;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	public Employee getCE2() {
		return ce2;
	}
	public void setCE2(Employee ce2) {
		this.ce2 = ce2;
	}
	
}
