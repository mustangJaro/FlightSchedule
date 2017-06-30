package com.flightSchedule.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.flightSchedule.baseObjects.Period;
import com.flightSchedule.core.SQLServerFlights;
import com.flightSchedule.views.AbstractBaseList;

/**
 * @author andrew.j.jarosinski
 *
 */
public class PeriodList extends AbstractBaseList<Period> {
	private List<Period> periods = new ArrayList<Period>();

	public PeriodList() {
		super();
		initData();
	}
	
	private void initData(){
		SQLServerFlights sqlFlights = new SQLServerFlights();
		periods = sqlFlights.getPeriodList();
		
	}
	
	public List<Period> getPeriods(){
		return periods;
	}
	public Period[] getPeriodsArray(){
		return periods.toArray(new Period[]{});
	}
	
	public void refresh(){
		initData();
	}

}
