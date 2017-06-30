package com.flightSchedule.provider.label;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.baseObjects.Period;

/**
 * Base implementation for displaying labels for Periods
 * 
 * @author andrew.j.jarosinski
 *
 */
public class PeriodLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Period period = (Period) element;
		switch(columnIndex){
			case 0:
				result = period.getName();
				break;
			case 1:
				result = period.getDescription();
				break;
		}
		return result;
	}

}
