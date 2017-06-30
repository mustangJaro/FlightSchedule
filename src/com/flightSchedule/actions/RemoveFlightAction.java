package com.flightSchedule.actions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.core.SQLServerFlights;

public class RemoveFlightAction implements IWorkbenchAction {
	private static final String REMOVE_FLIGHT_ACTION_ID = "Flight.Remove.Action.ID";
	private Flight flight;
	private List<IPropertyChangeListener> propListeners = new ArrayList<IPropertyChangeListener>();

	public RemoveFlightAction(Flight flight){
		super();
		this.flight = flight;
	}

	@Override
	public void runWithEvent(Event event) {
		if(flight.getId() == null || flight.getId().equals(BigInteger.ZERO)){
			MessageDialog.openInformation(null, "No Flight Exists", "No flight exists for the selected row");
		}else{
			if(MessageDialog.openConfirm(null, "Remove Flight", "Are you sure you want to remove this flight?")){
				SQLServerFlights flights = new SQLServerFlights();
				if(flights.removeFlight(flight)){
					notifyListeners();
				}else{
					MessageDialog.openError(null, "Failure", "Something happened removing the flight, try again");
				}
			}
		}
	}
	private void notifyListeners(){
		for(IPropertyChangeListener listener : propListeners){
			listener.propertyChange(new PropertyChangeEvent(flight, "remove", true, false));
		}
	}
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		propListeners.add(listener);
	}

	@Override
	public int getAccelerator() {
		return 0;
	}

	@Override
	public String getActionDefinitionId() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return null;
	}

	@Override
	public HelpListener getHelpListener() {
		return null;
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		return null;
	}

	@Override
	public String getId() {
		return REMOVE_FLIGHT_ACTION_ID;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return null;
	}

	@Override
	public int getStyle() {
		return 0;
	}

	@Override
	public String getText() {
		return "Remove Flight";
	}

	@Override
	public String getToolTipText() {
		return null;
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return false;
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
	}

	@Override
	public void run() {
	}

	@Override
	public void setActionDefinitionId(String id) {

	}

	@Override
	public void setChecked(boolean checked) {

	}

	@Override
	public void setDescription(String text) {

	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {

	}

	@Override
	public void setEnabled(boolean enabled) {

	}

	@Override
	public void setHelpListener(HelpListener listener) {

	}

	@Override
	public void setHoverImageDescriptor(ImageDescriptor newImage) {

	}

	@Override
	public void setId(String id) {

	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage) {

	}

	@Override
	public void setMenuCreator(IMenuCreator creator) {

	}

	@Override
	public void setText(String text) {

	}

	@Override
	public void setToolTipText(String text) {

	}

	@Override
	public void setAccelerator(int keycode) {

	}

	@Override
	public void dispose() {
		propListeners = new ArrayList<IPropertyChangeListener>();
	}

}
