package com.flightSchedule.dialogs;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.flightSchedule.core.SQLServerEmployees;
import com.flightSchedule.views.FlightScheduleDetailView;
import com.flightSchedule.views.FlightScheduleDetailView.REFRESH_TYPE;

import flightschedule.Application;

public class ChangePasswordDialog extends TitleAreaDialog implements IWorkbenchAction {
	private static final String ID = "com.flightSchedule.dialogs.changePasswordDialog";

    private Text textPassword;
    private Text textConfirmPassword;
	public ChangePasswordDialog(Shell parentShell) {
		super(parentShell);
	}
	@Override
    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setBlockOnOpen(true);
        setTitle("Change Password");
        setMessage("Please provide a new password");
        return contents;
    }
 
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
 
        Composite container = new Composite(area, SWT.NULL);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        new Label(container, SWT.NULL).setText("Password");
        textPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
        textPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
 
        new Label(container, SWT.NULL).setText("Confirm Password");
        textConfirmPassword = new Text(container, SWT.PASSWORD | SWT.BORDER);
        textConfirmPassword.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
 
        return area;
    }
 
    @Override
    protected Point getInitialSize() {
        return new Point(500, 300);
    }

	
	@Override
	protected void okPressed() {
		if(textPassword.getText().equals(textConfirmPassword.getText())){
			//matched entries, update DB
			SQLServerEmployees emps = new SQLServerEmployees();
			boolean result = emps.updatePassword(Application.LOGGED_IN_EMPLOYEE, textPassword.getText());
			if(result){
				MessageDialog.openInformation(getShell(), "Password Changed", "The password was successfully changed");
				setReturnCode(OK);
				close();
			}else
				MessageDialog.openError(getShell(), "Password Failure", "Updating the password failed");
		}else{
			MessageDialog.openError(getShell(), "Password Error", "Passwords do not match, please try again");
		}
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
		return "Enter password twice below";
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
		return ID;
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
		return "Change Password";
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
	public void runWithEvent(Event event) {
		open();

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
