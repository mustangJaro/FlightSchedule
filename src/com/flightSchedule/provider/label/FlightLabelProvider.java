package com.flightSchedule.provider.label;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.flightSchedule.baseObjects.Flight;

/**
 * Takes the given object {@link Flight} and displays the information on the table
 * 
 * @author andrew.j.jarosinski
 *
 */
public class FlightLabelProvider extends LabelProvider implements ITableLabelProvider {
	private SimpleDateFormat formatter = new SimpleDateFormat("ddMMMyy");

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * Converts the object ({@link Flight} into an applicable value on the table for the given column
	 * 
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Flight flight = (Flight) element;
		switch(columnIndex){
			case 0:
				break;
			case 1:
				result = flight.getPeriod();
				break;
			case 2:
				if(flight.getHours() > 0f)
					result = String.valueOf(flight.getHours());
				break;
			case 3:
				result = flight.getAircraft().getTailNumber();
				break;
			case 4:
				result = flight.getPC().getReverseName();
				break;
			case 5:
//				result = "Sched";
				break;
			case 6:
				result = flight.getPI().getReverseName();
				break;
			case 7:
				result = flight.getCE1().getReverseName();
				break;
			case 8:
				result = flight.getCE2().getReverseName();
				break;
			case 9:
				if(flight.getCreatedBy() != null)
					result = flight.getCreatedBy().getInitials();
				break;
			case 10:
				if(flight.getCreatedDate() != null)
					result = formatter.format(flight.getCreatedDate());
				break;
			case 11:
				result = flight.getRemarks();
				break;
			default:
				break;
		}
		return result;
	}

	
}
