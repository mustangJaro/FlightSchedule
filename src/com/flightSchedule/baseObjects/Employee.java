package com.flightSchedule.baseObjects;

import java.math.BigInteger;

public class Employee extends BaseObject {
	
	public enum Type{
		PC("PC", "PC", true),
		PI("PI", "PI", true),
		CE1("CE1", "CE", true),
		CE2("CE2", "CE", false),
		OR("OR", "OR", true);
		
		private final String text;
		private final String dropDownLabel;
		private final boolean dropDownEligible;
		
		Type(String text, String dropDownLabel, boolean dropDownEligible){
			this.text = text;
			this.dropDownLabel = dropDownLabel;
			this.dropDownEligible = dropDownEligible;
		}
		
		@Override
		public String toString() {
			return text;
		}

		public String getDropDownLabel() {
			return dropDownLabel;
		}

		public boolean isDropDownEligible() {
			return dropDownEligible;
		}
		
	}

	private BigInteger id;
	private String lastName = "";
	private String firstName;
	private Type type;
	private Rank rank;
	private boolean fullTime;
	private Role role;
	public Employee(){
		super();
	}
	public Employee(BigInteger id){
		super();
		this.id = id;
	}
	public Employee(String lastName, String firstName, Type type, Rank rank, boolean fullTime, Role role){
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.type = type;
		this.rank = rank;
		this.fullTime = fullTime;
		this.role = role;
	}
	public Employee(BigInteger id, String lastName, String firstName){
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	public boolean isFullTime() {
		return fullTime;
	}
	public void setFullTime(boolean fullTime) {
		this.fullTime = fullTime;
	}
	public String getReverseName(){
		if(lastName == null || lastName.equals("")){
			if(firstName == null || firstName.equals(""))
				return "";
			else
				return firstName;
		}else
			return lastName + ", " + firstName;
	}
	public String getInitials(){
		if(firstName == null || lastName == null || firstName.length() == 0 || lastName.length() == 0)
			return "";
		else
			return firstName.substring(0, 1) + lastName.substring(0, 1);
	}
	public String getSalt(){
		return getInitials();
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
