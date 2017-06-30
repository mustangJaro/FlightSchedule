/**
 * 
 */
package com.flightSchedule.dialogs;


import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.baseObjects.Period;
import com.flightSchedule.core.SQLServerEmployees;
import com.flightSchedule.core.SQLServerFlights;
import com.flightSchedule.dialogs.EmployeeEditorDialog.AddEditDialog;
import com.flightSchedule.dialogs.EmployeeEditorDialog.DIALOG_TYPE;
import com.flightSchedule.provider.label.EmployeeLabelProvider;
import com.flightSchedule.provider.label.PeriodLabelProvider;
import com.flightSchedule.views.FlightScheduleDetailView;
import com.flightSchedule.views.FlightScheduleDetailView.REFRESH_TYPE;
import com.flightSchedule.views.IListViewer;


/**
 * Creates a dialog for viewing/editing flight periods
 * 
 * @author andrew.j.jarosinski
 *
 */
public class PeriodEditorDialog extends BaseEditorDialog<Period, PeriodList> {
	private static final String ID = "com.flightSchedule.dialogs.periodEditor";
	
	private PeriodList periodList;
	private static final int TABLE_HEIGHT = 200;

	public PeriodEditorDialog(Shell parentShell) {
		super(parentShell, "View/Edit Periods", TABLE_HEIGHT, false);
		periodList = new PeriodList();
		
		setList(periodList);
		setListeners(
			//add listener
			new SelectionListener() {
			
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button btn = (Button) e.getSource();
					AddDialog dialog = new AddDialog(btn.getShell());
					dialog.create();
					dialog.getShell().setSize(500, 200);
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
					EditDialog dialog = new EditDialog(btn.getShell());
					dialog.create();
					dialog.getShell().setSize(500, 200);
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					dialog.setValues((Period) selection.getFirstElement());
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
					Period period = (Period) selection.getFirstElement();
					if(MessageDialog.openConfirm(getParentShell(), "Remove Period", "Are you sure you want to remove a period")){
						SQLServerFlights server = new SQLServerFlights();
						boolean result = server.removePeriod(period);
						IListViewer<Period> content = (IListViewer<Period>) viewer.getContentProvider();
						content.removeObject(period);
						periodList.refresh();
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
		});
	}
	
	@Override
	public void create() {
		super.create();
		setMessage("View or change periods for flight scheduling");
	}
	
//	@Override
//	protected Control createDialogArea(Composite parent) {
//		Composite area = (Composite) super.createDialogArea(parent);
//		Composite container = new Composite(area, SWT.NONE);
//		
//		//create gridlayout with two columns
//		GridLayout layout = new GridLayout(2, false);
//		container.setLayout(layout);
//		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		
//		//create table of periods on first column		
//		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
//		tableData.minimumWidth = 250;
//		createTable(container);
//		periodTable.setLabelProvider(new PeriodLabelProvider());
//		periodTable.setContentProvider(new PeriodContentProvider());
//		periodTable.setInput(periodList);
//		periodTable.getTable().setLayoutData(tableData);
//		
//		//create add/edit/remove buttons on second column
//		GridData buttonData = new GridData(SWT.FILL, SWT.TOP, true, true);
//		Button addBtn = new Button(container, SWT.NONE);
//		addBtn.setText("Add");
//		addBtn.setLayoutData(buttonData);
//		addBtn.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Button btn = (Button) e.getSource();
//				AddDialog dialog = new AddDialog(btn.getShell());
//				dialog.create();
//				dialog.getShell().setSize(500, 200);
//				dialog.open();
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//			}
//		});
//		Button editBtn = new Button(container, SWT.NONE);
//		editBtn.setText("Edit");
//		editBtn.setLayoutData(buttonData);
//		editBtn.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Button btn = (Button) e.getSource();
//				EditDialog dialog = new EditDialog(btn.getShell());
//				dialog.create();
//				dialog.getShell().setSize(500, 200);
//				IStructuredSelection selection = (IStructuredSelection) periodTable.getSelection();
//				dialog.setValues((Period) selection.getFirstElement());
//				dialog.open();
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//			}
//		});
//		Button removeBtn = new Button(container, SWT.NONE);
//		removeBtn.setText("Remove");
//		removeBtn.setLayoutData(buttonData);
//		removeBtn.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				IStructuredSelection selection = (IStructuredSelection) periodTable.getSelection();
//				Period period = (Period) selection.getFirstElement();
//				if(MessageDialog.openConfirm(getParentShell(), "Remove Period", "Are you sure you want to remove a period")){
//					SQLServerFlights server = new SQLServerFlights();
//					boolean result = server.removePeriod(period);
//					IListViewer<Period> content = (IListViewer<Period>) periodTable.getContentProvider();
//					periodList.refresh();
//					content.removeObject(period);
//					periodTable.refresh();
//				}
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				
//			}
//		});
//		
//		
//		return area;
//	}
	

	@Override
	protected void createTable(Composite parent){
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		Table table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
	
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);		
		column.setText("Name");
		column.setWidth(70);
		
		column = new TableColumn(table, SWT.LEFT, 1);		
		column.setText("Description");
		column.setWidth(250);

		viewer = new TableViewer(table);
		setProviders(new BaseContentProvider<Period, PeriodList>(viewer, periodList), new PeriodLabelProvider());
	}
	
	/**
	 * Dialog to add periods to DB
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	class AddDialog extends Dialog{
		private Text nameTxt;
		private Text descTxt;

		protected AddDialog(Shell parentShell) {
			super(parentShell);
		}
		
		@Override
		protected Control createContents(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);
			
			//create gridlayout with two columns
			GridLayout grid = new GridLayout(2, false);
			container.setLayout(grid);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			GridData gridTxtData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Label nameLbl = new Label(container, SWT.NONE);
			nameLbl.setText("Name: ");
			nameTxt = new Text(container, SWT.BORDER);
			nameTxt.setLayoutData(gridTxtData);
			Label descLbl = new Label(container, SWT.NONE);
			descLbl.setText("Description: ");
			descTxt = new Text(container, SWT.BORDER);
			descTxt.setLayoutData(gridTxtData);
			
			return (Composite) super.createContents(parent);
		}
		
		@Override
		protected void okPressed() {
			//TODO validate entered data, pretty much that there is some value there
			SQLServerFlights server = new SQLServerFlights();
			Period toAdd = new Period(nameTxt.getText(), descTxt.getText());
			boolean result = server.addPeriod(toAdd);
			if(result){
				IListViewer<Period> content = (IListViewer<Period>) viewer.getContentProvider();
				periodList.refresh();
				content.addObject(toAdd);
				viewer.refresh();
				close();
			}else{
				MessageDialog.openError(getParentShell(), "Failure adding period", "There was a problem adding the given period");
			}
		}

		
	}
	
	/**
	 * Dialog to edit periods in DB
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	class EditDialog extends Dialog{
		private Text nameTxt;
		private Text descTxt;
		private Period givenPeriod;

		protected EditDialog(Shell parentShell) {
			super(parentShell);
		}
		
		@Override
		protected Control createContents(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);
			
			//create gridlayout with two columns
			GridLayout grid = new GridLayout(2, false);
			container.setLayout(grid);
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			GridData gridTxtData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Label nameLbl = new Label(container, SWT.NONE);
			nameLbl.setText("Name: ");
			nameTxt = new Text(container, SWT.BORDER);
			nameTxt.setLayoutData(gridTxtData);
			Label descLbl = new Label(container, SWT.NONE);
			descLbl.setText("Description: ");
			descTxt = new Text(container, SWT.BORDER);
			descTxt.setLayoutData(gridTxtData);
			
			return (Composite) super.createContents(parent);
		}
		
		public void setValues(Period period){
			this.givenPeriod = period;
			nameTxt.setText(period.getName());
			descTxt.setText(period.getDescription());
		}
		
		@Override
		protected void okPressed() {
			//TODO validate entered data, pretty much that there is some value there
			SQLServerFlights server = new SQLServerFlights();
			Period toAdd = new Period(nameTxt.getText(), descTxt.getText());
			boolean result = server.removePeriod(givenPeriod);
			result = server.addPeriod(toAdd);
			if(result){
				IListViewer<Period> content = (IListViewer<Period>) viewer.getContentProvider();
				periodList.refresh();
				content.removeObject(givenPeriod);
				content.addObject(toAdd);
				viewer.refresh();
				close();
			}else{
				MessageDialog.openError(getParentShell(), "Failure adding period", "There was a problem adding the given period");
			}
		}

		
	}
	
	/**
	 * Basic wrapper for periods.  Updates viewer when changes are made
	 * and manages listeners on flights.
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	class PeriodContentProvider implements IStructuredContentProvider, IListViewer<Period>{
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if(newInput != null)
				((PeriodList) newInput).addChangeListener(this);
			if(oldInput != null)
				((PeriodList) oldInput).removeChangeListener(this);
		}
		@Override
		public Object[] getElements(Object inputElement) {
			return periodList.getPeriodsArray();
		}
		@Override
		public void addObject(Period period) {
			viewer.add(period);			
		}
		@Override
		public void removeObject(Period period) {
			viewer.remove(period);
		}
		@Override
		public void updateObject(Period period) {
			viewer.update(period, null);
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
		return "View/Edit Flight Periods";
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
		return "Periods";
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
	protected void okPressed() {
		Shell psh = getParentShell();
		IWorkbenchWindow wind = (IWorkbenchWindow) psh.getData();
		IWorkbenchPage page = wind.getActivePage();
		FlightScheduleDetailView view = (FlightScheduleDetailView) page.findView(FlightScheduleDetailView.ID);
		view.refreshViewer(REFRESH_TYPE.PERIOD);
		super.okPressed();
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
		close();
	}

}
