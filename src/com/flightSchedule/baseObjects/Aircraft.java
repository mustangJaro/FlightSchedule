/**
 * 
 */
package com.flightSchedule.baseObjects;

/**
 * @author andrew.j.jarosinski
 *
 */
public class Aircraft extends BaseObject {

	private String tailNumber="";
	public Aircraft(){
		super();
	}
	public Aircraft(String tailNumber){
		super();
		this.tailNumber = tailNumber;
	}
	public String getTailNumber() {
		return tailNumber;
	}
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
	public String getTailNumberShort() {
		if(tailNumber.length() > 3)
			return tailNumber.substring(tailNumber.length()-3);
		else
			return tailNumber;
	}
	
	@Override
	public String toString() {
		return tailNumber;
	}
}
