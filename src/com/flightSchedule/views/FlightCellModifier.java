package com.flightSchedule.views;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

import com.flightSchedule.baseObjects.Aircraft;
import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.core.ExceptionHandler;
import com.flightSchedule.views.FlightScheduleDetailView.REFRESH_TYPE;

/**
 * Used to modify {@link com.flightSchedule.baseObjects.Flight Flight} object data in the viewer
 * 
 * @author andrew.j.jarosinski
 *
 */
public class FlightCellModifier implements ICellModifier {
	private FlightScheduleDetailView fsView;
	
	public FlightCellModifier(FlightScheduleDetailView fsView) {
		super();
		this.fsView = fsView;
	}

	@Override
	public boolean canModify(Object element, String property) {
		if(((Flight) element).isEditable()){
			return fsView.canModify((Flight) element, property);
		}else
			return false;
	}
	public boolean canModify(Object element, int columnIndex) {
		if(((Flight) element).isEditable())
			return fsView.canModify(columnIndex);
		else
			return false;
	}
	public Object getValue(Object element, int columnIndex) {
		return getValue(element, fsView.getColumnNames().get(columnIndex));
	}

	@Override
	public Object getValue(Object element, String property) {
		int columnIndex = fsView.getColumnNames().indexOf(property);
		Object result = null;
		Flight flight = (Flight) element;
		String stringValue = "";
		
		switch(columnIndex){
			case 0:
				result = false;
				break;
			case 1:
				stringValue = flight.getPeriod();
				result = findValueInArray(stringValue, property);
				break;
			case 2:
				result = String.valueOf(flight.getHours());
				break;
			case 3:
				//for comboBoxCellEditor return selection id
				stringValue = flight.getAircraft().getTailNumber();
				result = getPositionInArray(stringValue, property);
				System.out.println("Tail # position: " + result);
				break;
			case 4:
				//for comboBoxCellEditor return selection id
				result = getPositionInArray(flight.getPC().getId(), property);
				System.out.println("PC position: " + result);
				break;
			case 5:
				result = "Sched";
				break;
			case 6:
				//for comboBoxCellEditor return selection id
				result = getPositionInArray(flight.getPI().getId(), property);
				System.out.println("PI position: " + result);
				break;
			case 7:
				//for comboBoxCellEditor return selection id
				result = getPositionInArray(flight.getCE1().getId(), property);
				System.out.println("CE position: " + result);
				break;
			case 8:
				//for comboBoxCellEditor return selection id
				result = getPositionInArray(flight.getCE2().getId(), property);
				System.out.println("CE position: " + result);
				break;
			case 9:
				//initials
			case 10:
				//date created
				break;
			case 11:
				result = flight.getRemarks();
				break;
			default:
				result = "";
			
		}
		
		
		return result;
	}
	
	private Integer findValueInArray(String value, String property){
		String[] pds = fsView.getChoices(property);
		int i = pds.length - 1;
		if (i > 0){
			while (!value.equals(pds[i]) && i > 0)
				--i;
		}
		return new Integer(i);
		
	}
	
	private String getValueFromArray(Object value, String property){
		if((Integer) value >= 0)
			return fsView.getChoices(property)[((Integer) value).intValue()].trim();
		else
			return "";
	}
	
	private BigInteger getIDFromArray(Object value, String property){
		if((Integer) value >= 0)
			return fsView.getIDs(property)[((Integer) value).intValue()];
		else
			return BigInteger.ZERO;
	}
	
	private int getPositionInArray(Object value, String property){
		try{
			BigInteger test = new BigInteger(value.toString());
			
			if(test.compareTo(BigInteger.ZERO) > 0){
				BigInteger[] ids = fsView.getIDs(property);
				for(int i = 0; i < ids.length; i++){
					if(ids[i].equals(test))
						return i;
				}
			}
		}catch(Exception e){
			ExceptionHandler.logException(e);
		}
		return 0;
		
	}

	public void modify(Object element, int columnIndex, Object value) {
		modify(element, fsView.getColumnNames().get(columnIndex), value);
	}

	@Override
	public void modify(Object element, String property, Object value) {
		int columnIndex = fsView.getColumnNames().indexOf(property);
		
		if(element != null){
			TableItem item;
			Flight flight = null;
			if(element instanceof TableItem){
				item = (TableItem) element;
				flight = (Flight) item.getData();
			}
			if(element instanceof Flight){
				flight = (Flight) element;
			}
			String valueString;
			float valueFloat;
			BigInteger valueBigInt;
			
			boolean updateDB = false;
			
			switch (columnIndex){
				case 0:
					break;
				case 1:
					valueString = getValueFromArray(value, property);
					if(flight.getPeriod() == null || !flight.getPeriod().equals(valueString)){
						flight.setPeriod(valueString);
						updateDB = true;
					}
					break;
				case 2:
					try{
						valueFloat = Float.valueOf(((String) value).trim());
						if(flight.getHours() != (valueFloat)){
							flight.setHours(valueFloat);
							updateDB = true;
						}
					}catch(NumberFormatException e){
						ExceptionHandler.logException(e);
						MessageDialog.openError(null, "Failure setting hours", "The flight hours must be a decimal number");
					}
					break;
				case 3:
					Aircraft ac = new Aircraft(getValueFromArray(value, property));
					if(flight.getAircraft() == null || !flight.getAircraft().equals(ac)){
						flight.setAircraft(ac);
						updateDB = true;
					}
					break;
				case 4:
					valueBigInt = getIDFromArray(value, property);
					if(flight.getPC().getId() == null || !flight.getPC().getId().equals(valueBigInt)){
						flight.setPC(new Employee(valueBigInt));
						updateDB = true;
					}
					break;
				case 5:
					valueString = "Sched";
					break;
				case 6:
					valueBigInt = getIDFromArray(value, property);
					if(flight.getPI().getId() == null || !flight.getPI().getId().equals(valueBigInt)){
						flight.setPI(new Employee(valueBigInt));
						updateDB = true;
					}
					break;
				case 7:
					valueBigInt = getIDFromArray(value, property);
					if(flight.getCE1().getId() == null || !flight.getCE1().getId().equals(valueBigInt)){
						flight.setCE1(new Employee(valueBigInt));
						updateDB = true;
					}
					break;
				case 8:
					valueBigInt = getIDFromArray(value, property);
					if(flight.getCE2().getId() == null || !flight.getCE2().getId().equals(valueBigInt)){
						flight.setCE2(new Employee(valueBigInt));
						updateDB = true;
					}
					break;
				case 9:
				case 10:
					//un-modifiable column
					break;
				case 11:
					valueString = ((String) value).trim();
					if(!flight.getRemarks().equals(valueString)){
						flight.setRemarks(valueString);
						updateDB = true;
					}
					break;
				default:
			}
			if(updateDB){
				fsView.getFlights().taskChanged(flight);
				fsView.refreshViewer(REFRESH_TYPE.EMPLOYEE);
			}
		}

	}

}
