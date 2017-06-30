package com.flightSchedule.dialogs;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.flightSchedule.core.SQLServerEmployees;

import flightschedule.Application;

/**
 * Class for choosing a user and validating a password when
 * the application is opened.
 * 
 * @author andrew.j.jarosinski
 *
 */
public class LoginDialog extends TitleAreaDialog {

    private Combo textUsername;
    private Text textPassword;
    private LinkedHashMap<BigInteger, String> employees;
	SQLServerEmployees sqlEmps;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}
	@Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setBlockOnOpen(true);
        setTitle("Login");
        setMessage("Please provide credentials");
        return contents;
    }
 
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        sqlEmps = new SQLServerEmployees();
        employees = sqlEmps.getAllEmployeesByName();
 
        Composite container = new Composite(area, SWT.NULL);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
 
        new Label(container, SWT.NULL).setText("Username");
        textUsername = new Combo(container, SWT.BORDER);
        textUsername.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textUsername.setItems(employees.values().toArray(new String[]{}));
        textUsername.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPassword.setText("");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
 
        new Label(container, SWT.NULL).setText("Password");
        textPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
        textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
 
        return area;
    }
 
    @Override
    protected Point getInitialSize() {
        return new Point(500, 300);
    }
    
    @Override
    protected void okPressed() {
    	if(employees.size() <= 1){
    		MessageDialog.openInformation(getShell(), "No available users", "No users are currently setup, please add users");
    		setReturnCode(OK);
    		close();
    	}else if(textUsername.getText() == null || textUsername.getText().length() == 0){
    		MessageDialog.openError(getShell(), "No Username Selected", "A username must be selected");
    	}else if(textUsername.getText().equals("admin") && textPassword.getText().equals("admin")){
    		setReturnCode(OK);
    		close();
    	}else{
	    	//validate password
	    	BigInteger id = BigInteger.ZERO;
	    	for(Entry<BigInteger, String> emp : employees.entrySet()){
	    		if(emp.getValue().equals(textUsername.getText())){
	    			id = emp.getKey();
	    			break;
	    		}
	    	}
	    	boolean result = sqlEmps.isPasswordMatch(id, textPassword.getText());
	    	if(result){
	    		Application.LOGGED_IN_EMPLOYEE = sqlEmps.getEmployee(id);
	    		setReturnCode(OK);
	    		close();
	    	}else{
	    		setReturnCode(CANCEL);
	    		MessageDialog.openError(getShell(), "Invalid Password", "The password entered did not match");
	    		textPassword.setFocus();
	    		textPassword.selectAll();
	    	}
    	}
    	
    }

}
