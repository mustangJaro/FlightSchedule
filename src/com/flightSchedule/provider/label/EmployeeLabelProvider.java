package com.flightSchedule.provider.label;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.views.FlightScheduleDetailView;

import flightschedule.Application;


/**
 * Base implementation for displaying labels for Employees
 * 
 * @author andrew.j.jarosinski
 *
 */
public class EmployeeLabelProvider extends LabelProvider implements ITableLabelProvider {
	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "checked";
	public static final String UNCHECKED_IMAGE  = "unchecked";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();
	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */ 
	static {
		String iconPath = "images/"; 
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
				EmployeeLabelProvider.class, 
				iconPath + CHECKED_IMAGE + ".png"
				)
			);
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
				EmployeeLabelProvider.class, 
				iconPath + UNCHECKED_IMAGE + ".png"
				)
			);	
	}
	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	private Image getImage(boolean isSelected) {
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return  imageRegistry.get(key);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Image img = null;
		switch(columnIndex){
			case 4:
				img = getImage(((Employee) element).isFullTime());
			break;
		}
		return img;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Employee emp = (Employee) element;
		switch(columnIndex){
			case 0:
				result = emp.getLastName();
				break;
			case 1:
				result = emp.getFirstName();
				break;
			case 2:
				if(emp.getRank() != null && !emp.getRank().equals(""))
					result = emp.getRank().toString();
				break;
			case 3:
				if(emp.getType() != null && !emp.getType().equals(""))
					result = emp.getType().toString();
				break;
			case 4:
				break;
		}
		return result;
	}
	

}
