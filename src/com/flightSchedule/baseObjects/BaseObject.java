package com.flightSchedule.baseObjects;

import java.math.BigInteger;
import java.util.Date;

public class BaseObject {
	private Date createdDate;
	private Employee createdBy;
	private boolean editable = true;
	private BigInteger sequence;
	
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public BigInteger getSequence() {
		return sequence;
	}
	public void setSequence(BigInteger sequence) {
		this.sequence = sequence;
	}
	

}
