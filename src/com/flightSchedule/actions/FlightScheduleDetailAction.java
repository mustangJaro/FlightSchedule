package com.flightSchedule.actions;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.handlers.HandlerUtil;

import com.flightSchedule.core.ExceptionHandler;

public class FlightScheduleDetailAction implements IWorkbenchAction {
	private static final String FS_DETAIL_ID = "com.flightSchedule.views.FlightScheduleDetailView";

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {

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
		return "Opens the Flight Schedule Detail by day";
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
		return FS_DETAIL_ID;
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
		return "Flight Schedule Detail";
	}

	@Override
	public String getToolTipText() {
		return "Opens the Flight Schedule Detail by day";
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
	public void runWithEvent(Event event) {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(FS_DETAIL_ID);
		} catch (PartInitException e) {
			ExceptionHandler.logException("failure loading view", e);
		}

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

	}

}
