package com.flightSchedule.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.core.SQLServerFlights;

public class FlightList extends AbstractBaseList<Flight> {
	private static final int MAX_FLIGHTS = 25;
	private List<Flight> flights = new ArrayList<Flight>();
	private List<String> periods = new ArrayList<String>();

	public FlightList(Calendar flightsForDate) {
		initData(flightsForDate);
	}
	
	private void initData(Calendar flightsForDate){
		SQLServerFlights sqlFlights = new SQLServerFlights();
		periods = sqlFlights.getPeriods();
		flights = sqlFlights.getFlights(flightsForDate);

		fillFlights(flightsForDate);
		
	}
	
	public List<Flight> getFlights(){
		return flights;
	}
	
	public List<String> getPeriods(){
		return periods;
	}
	public String[] getPeriodsArray(){
		return periods.toArray(new String[]{});
	}
	
	public void refreshPeriods(){
		SQLServerFlights sqlFlights = new SQLServerFlights();
		periods = sqlFlights.getPeriods();		
	}
	
	public void refreshFlights(Calendar flightsForDate){
		SQLServerFlights sqlFlights = new SQLServerFlights();
		flights = sqlFlights.getFlights(flightsForDate);
		fillFlights(flightsForDate);
	}
	
	private void fillFlights(Calendar flightsForDate){
		while(flights.size() < MAX_FLIGHTS){
			flights.add(new Flight(new Employee(), flightsForDate));
		}
	}
	
	@Override
	public void taskChanged(Flight flight) {
		super.taskChanged(flight);
		SQLServerFlights flights = new SQLServerFlights();
		flights.updateFlight(flight);
	}
}
