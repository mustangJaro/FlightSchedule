package com.flightSchedule.actions;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.activities.ActivitiesPreferencePage;

import com.flightSchedule.preferencePages.*;

import flightschedule.View;

public class ConfigurationAction implements IWorkbenchAction {
	private static final String CONFIG_ACTION_ID = "Configuration.Action.ID";
	private String desc;
	private boolean enabled = true;
	private IWorkbenchWindow window;
	private PreferenceManager prefManager;
	private PreferenceStore prefStore;
	
	public ConfigurationAction(String text, IWorkbenchWindow window) {
		prefStore = new PreferenceStore(View.PREF_STORE);
		desc = text;
		this.window = window;
	}

	@Override
	public void runWithEvent(Event event) {
		prefManager = new PreferenceManager();
		prefManager.addToRoot(new PreferenceNode(ConfigurationPreference.ID, new ConfigurationPreference(prefStore)));
		PreferenceDialog pref = new PreferenceDialog(window.getShell(), prefManager);
		pref.open();
	}

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
		return desc;
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
		return CONFIG_ACTION_ID;
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
		return desc;
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
		return enabled;
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
		desc = text;		
	}

	@Override
	public void setDisabledImageDescriptor(ImageDescriptor newImage) {
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		desc = text;
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
