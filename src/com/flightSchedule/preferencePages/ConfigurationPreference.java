package com.flightSchedule.preferencePages;

import java.io.IOException;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.flightSchedule.core.ExceptionHandler;

import flightschedule.View;

/**
 * Manages the configuration preference section.  This includes <code>SERVER_NAME</code> and <code>DB_NAME</code>
 * parameters for connection to the database server.
 * 
 * @author andrew.j.jarosinski
 *
 */
public class ConfigurationPreference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public static final String ID = "com.flightSchedule.config.general";
	private static final String SERVER_NAME = "ServerName";
	private static final String DB_NAME = "DBName";

	private StringFieldEditor serverNameEditor;
	private StringFieldEditor dbNameEditor;

	private PreferenceStore prefStore;
	
	public ConfigurationPreference(){
		this(new PreferenceStore(View.PREF_STORE));
	}
	
	public ConfigurationPreference(PreferenceStore prefStore) {
		super("Server Configuration", FieldEditorPreferencePage.GRID);
		this.prefStore = prefStore;
		setPreferenceStore(prefStore);
		
		this.prefStore.setDefault(SERVER_NAME, "ngwia4-8rgaa-03");
		this.prefStore.setDefault(DB_NAME, "WINGULLSA");
		
		try {
			this.prefStore.load();
		} catch (IOException e) {
			ExceptionHandler.logException(e);
		}
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		serverNameEditor.setStringValue(prefStore.getString(SERVER_NAME));
		dbNameEditor.setStringValue(prefStore.getString(DB_NAME));
	}

	@Override
	protected void createFieldEditors() {
		serverNameEditor = new StringFieldEditor(SERVER_NAME, "Server Name:", getFieldEditorParent());
		dbNameEditor = new StringFieldEditor(DB_NAME, "SQL Database Name:", getFieldEditorParent());

		
		addField(serverNameEditor);
		addField(dbNameEditor);
	}
	/**
	 * Convenience method for getting the stored server name. 
	 * @return
	 */
	public String getServerName(){
		return prefStore.getString(SERVER_NAME);
	}
	/**
	 * Convenience method for getting the stored DB name. 
	 * @return
	 */
	public String getDBName(){
		return prefStore.getString(DB_NAME);
	}

	@Override
	public void init(IWorkbench workbench) {
		
	}
	
	private boolean saveData(){
		try {
			prefStore.save();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	@Override
	public boolean performOk() {
		if( super.performOk())
			return saveData();
		else
			return false;
	}

}
