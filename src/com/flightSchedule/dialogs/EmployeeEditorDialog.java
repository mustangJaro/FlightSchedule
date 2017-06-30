/**
 * 
 */
package com.flightSchedule.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Period;
import com.flightSchedule.baseObjects.Employee.Type;
import com.flightSchedule.baseObjects.Rank;
import com.flightSchedule.core.SQLServerEmployees;
import com.flightSchedule.core.SQLServerEmployees.MOVE_DIRECTION;
import com.flightSchedule.core.SQLServerFlights;
import com.flightSchedule.provider.label.EmployeeLabelProvider;
import com.flightSchedule.views.FlightScheduleDetailView;
import com.flightSchedule.views.IListViewer;
import com.flightSchedule.views.FlightScheduleDetailView.REFRESH_TYPE;

/**
 * Dialog to add/edit/remove employees in the DB
 * 
 * @author andrew.j.jarosinski
 *
 */
public class EmployeeEditorDialog extends BaseEditorDialog<Employee, EmployeeList> {
	private static final String ID = "com.flightSchedule.dialogs.employeeEditor";
	
	private EmployeeList empList;
	private static final int TABLE_HEIGHT = 450;
	private String[] ranks;
	private String[] types;
	

	public EmployeeEditorDialog(Shell parentShell) {
		super(parentShell, "View/Edit Employees", TABLE_HEIGHT, true);
		empList = new EmployeeList();
		getRank();
		getTypes();
		setList(empList);
		setListeners(
			//add listener
			new SelectionListener() {
			
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button btn = (Button) e.getSource();
					AddEditDialog dialog = new AddEditDialog(btn.getShell(), DIALOG_TYPE.ADD);
					dialog.create();
					dialog.getShell().setSize(500, 175);
					dialog.open();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			}, 
			//edit listener
			new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button btn = (Button) e.getSource();
					AddEditDialog dialog = new AddEditDialog(btn.getShell(), DIALOG_TYPE.EDIT);
					dialog.create();
					dialog.getShell().setSize(500, 200);
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					dialog.setValues((Employee) selection.getFirstElement());
					dialog.open();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			}, 
			//remove listener
			new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Employee emp = (Employee) selection.getFirstElement();
					if(MessageDialog.openConfirm(getParentShell(), "Remove Employee", "Are you sure you want to remove an employee")){
						SQLServerEmployees server = new SQLServerEmployees();
						boolean result = server.removeEmployee(emp);
						IListViewer<Employee> content = (IListViewer<Employee>) viewer.getContentProvider();
						content.removeObject(emp);
						empList.refresh();
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
		});
		
		setMoveListeners(
			//move up button
			new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveEmployee(e, MOVE_DIRECTION.UP);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		}, 
			//move down button
			new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveEmployee(e, MOVE_DIRECTION.DOWN);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	private void moveEmployee(SelectionEvent e, MOVE_DIRECTION dir){
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		int index = viewer.getTable().getSelectionIndex();
		Employee emp = (Employee) selection.getFirstElement();
		SQLServerEmployees server = new SQLServerEmployees();
		server.moveEmployee(emp, dir);
		empList.refresh();
		setList(empList);
		viewer.refresh();
		if(dir.equals(MOVE_DIRECTION.UP))
			viewer.getTable().setSelection(index-1);
		else
			viewer.getTable().setSelection(index+1);
		runChangeSelection();
	}

	
	@Override
	protected void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		Table table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
	
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);		
		column.setText("Last Name");
		column.setWidth(150);
		
		column = new TableColumn(table, SWT.LEFT, 1);		
		column.setText("First Name");
		column.setWidth(100);
		
		column = new TableColumn(table, SWT.LEFT, 2);		
		column.setText("Rank");
		column.setWidth(50);
		
		column = new TableColumn(table, SWT.LEFT, 3);		
		column.setText("Type");
		column.setWidth(50);
		
		column = new TableColumn(table, SWT.CENTER, 4);		
		column.setText("FT");
		column.setWidth(25);
		
		viewer = new TableViewer(table);
		setProviders(new BaseContentProvider<Employee, EmployeeList>(viewer, empList), new EmployeeLabelProvider());
		
	}
	
	@Override
	public void create() {
		super.create();
		setMessage("View or change employees for flight scheduling");
	}

	public enum DIALOG_TYPE{
		ADD,
		EDIT
	};
	/**
	 * Dialog to add periods to DB
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	class AddEditDialog extends Dialog{
		private Text firstNameTxt;
		private Text lastNameTxt;
		private Combo rankCmbo;
		private Combo typeCmbo;
		private Button ftBtn;
		private DIALOG_TYPE diagType;
		private Employee givenEmployee;

		protected AddEditDialog(Shell parentShell, DIALOG_TYPE diagType) {
			super(parentShell);
			this.diagType = diagType;
			
		}
		
		@Override
		protected Control createContents(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);
			
			//create gridlayout with two columns
			GridLayout grid = new GridLayout(6, false);
			container.setLayout(grid);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			GridData gridTxtData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
			gridTxtData.minimumWidth = 100;
			GridData gridLgTxtData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
			Label descLbl = new Label(container, SWT.NONE);
			descLbl.setText("Last Name: ");
			lastNameTxt = new Text(container, SWT.BORDER);
			lastNameTxt.setLayoutData(gridLgTxtData);
			Label nameLbl = new Label(container, SWT.NONE);
			nameLbl.setText("First Name: ");
			firstNameTxt = new Text(container, SWT.BORDER);
			firstNameTxt.setLayoutData(gridLgTxtData);
			Label rankLbl = new Label(container, SWT.NONE);
			rankLbl.setText("Rank: ");
			rankLbl.setLayoutData(new GridData(SWT.LEAD, SWT.CENTER, false, true, 1, 1));
			rankCmbo = new Combo(container, SWT.DROP_DOWN);
			rankCmbo.setLayoutData(gridTxtData);
			rankCmbo.setItems(ranks);
			Label typeLbl = new Label(container, SWT.NONE);
			typeLbl.setText("Type: ");
			typeLbl.setLayoutData(new GridData(SWT.TRAIL, SWT.CENTER, false, true, 1, 1));
			typeCmbo = new Combo(container, SWT.DROP_DOWN);
			typeCmbo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			typeCmbo.setItems(types);
			
			ftBtn = new Button(container, SWT.CHECK);
			ftBtn.setText("Full-time");
			
			return (Composite) super.createContents(parent);
		}
		
		@Override
		protected void okPressed() {
			Type type = Employee.Type.valueOf(typeCmbo.getText().toUpperCase());
			Rank rank = Rank.valueOf(rankCmbo.getText().toUpperCase());
			SQLServerEmployees server = new SQLServerEmployees();
			Employee toAdd = new Employee(lastNameTxt.getText(), firstNameTxt.getText(), type, rank, ftBtn.getSelection());
			
			//TODO validate entered data, pretty much that there is some value there
			if(diagType.equals(DIALOG_TYPE.ADD)){
				boolean result = server.addEmployee(toAdd);
				if(result){
					IListViewer<Employee> content = (IListViewer<Employee>) viewer.getContentProvider();
					empList.refresh();
					content.addObject(toAdd);
					viewer.refresh();
					close();
				}else{
					MessageDialog.openError(getParentShell(), "Failure adding employee", "There was a problem adding the given employee");
				}
			}else{
				//edit dialog
				toAdd.setId(givenEmployee.getId());
				boolean result = server.updateEmployee(toAdd);
				if(result){
					IListViewer<Employee> content = (IListViewer<Employee>) viewer.getContentProvider();
					empList.refresh();
					content.removeObject(givenEmployee);
					content.addObject(toAdd);
					viewer.refresh();
					close();
				}else{
					MessageDialog.openError(getParentShell(), "Failure editing employee", "There was a problem editing the given employee");
				}
			}
		}
		
		protected void setValues(Employee emp){
			this.givenEmployee = emp;
			lastNameTxt.setText(emp.getLastName());
			firstNameTxt.setText(emp.getFirstName());
			if(emp.getRank() != null && !emp.getRank().equals(""))
				rankCmbo.setText(emp.getRank().toString());
			if(emp.getType() != null && !emp.getType().equals(""))
				typeCmbo.setText(emp.getType().toString());
			
			if(emp.isFullTime())
				ftBtn.setSelection(true);
		}

		
	}
	
	
	private void getRank(){
		List<String> ranks = new ArrayList<String>();
		for(Rank rank : Rank.values())
			ranks.add(rank.toString());
		
		this.ranks = ranks.toArray(new String[]{});
	}
	
	private void getTypes(){
		List<String> types = new ArrayList<String>();
		for(Type type : Employee.Type.values()){
			if(type.isDropDownEligible()){
				types.add(type.getDropDownLabel());
			}
		}
		
		this.types = types.toArray(new String[]{});
	}
	
	@Override
	protected void okPressed() {
		Shell psh = getParentShell();
		IWorkbenchWindow wind = (IWorkbenchWindow) psh.getData();
		IWorkbenchPage page = wind.getActivePage();
		FlightScheduleDetailView view = (FlightScheduleDetailView) page.findView(FlightScheduleDetailView.ID);
		view.refreshViewer(REFRESH_TYPE.EMPLOYEE);
		super.okPressed();
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
		return "Dialog to add/edit/remove employees";
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
		return "Employees";
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
