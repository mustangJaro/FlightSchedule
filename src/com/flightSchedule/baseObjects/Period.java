/**
 * 
 */
package com.flightSchedule.baseObjects;

/**
 * Stores period information for flights
 * 
 * @author andrew.j.jarosinski
 *
 */
public class Period extends BaseObject {
	private String name;
	private String description;

	public Period(){
		super();
	}
	public Period(String name, String desc){
		this.name = name;
		this.description = desc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
