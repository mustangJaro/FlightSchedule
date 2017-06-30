package com.flightSchedule.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.flightSchedule.baseObjects.BaseObject;
import com.flightSchedule.baseObjects.Employee;
import com.flightSchedule.core.SQLServerFlights;
import com.flightSchedule.views.AbstractBaseList;

/**
 * A base class for editor dialogs.  After instantiating, the listeners and providers must be set to fully create
 * the dialog area.  {@link BaseEditorDialog#setList(AbstractBaseList) setList}, {@link BaseEditorDialog#setListeners(SelectionListener, SelectionListener, SelectionListener)
 * setListeners}, and {@link BaseEditorDialog#setProviders(BaseContentProvider, LabelProvider) setProviders} must be
 * called to initialize the proper objects prior to creating the dialog area ({@link BaseEditorDialog#createDialogArea(Composite) createDialogArea})
 * 
 * 
 * @author andrew.j.jarosinski
 *
 * @param <BASEOBJ>	The {@link BaseObject} type that's used in this editor.  Used for the type of {@link BaseContentProvider} and
 * {@link AbstractBaseList}
 */
public abstract class BaseEditorDialog<BASEOBJ extends BaseObject, BASELIST extends AbstractBaseList<BASEOBJ>> 
	extends TitleAreaDialog implements IWorkbenchAction {
	
	protected TableViewer viewer;
	private BASELIST baseList;
	private String title;
	private SelectionListener addSelectionListener;
	private SelectionListener editSelectionListener;
	private SelectionListener removeSelectionListener;
	private SelectionListener moveUpSelectionListener;
	private SelectionListener moveDownSelectionListener;
	private BaseContentProvider<BASEOBJ, BASELIST> contentProvider;
	private LabelProvider labelProvider;
	private int tableHeight;
	private boolean moveOptions;
	
	private Button moveUpBtn;
	private Button moveDownBtn;

	public BaseEditorDialog(Shell parentShell, String title, int tableHeight, boolean moveOptions) {
		super(parentShell);
		this.title = title;
		this.tableHeight = tableHeight;
		this.moveOptions = moveOptions;
	}
	
	/**
	 * Set the list used for manipulation in the viewing area
	 * 
	 * @param baseList
	 */
	protected void setList(BASELIST baseList){
		this.baseList = baseList;
	}
	
	/**
	 * Sets the listeners on the add/edit/remove buttons
	 * 
	 * @param addSelectionListener
	 * @param editSelectionListener
	 * @param removeSelectionListener
	 */
	protected void setListeners(SelectionListener addSelectionListener, SelectionListener editSelectionListener, SelectionListener removeSelectionListener){
		this.addSelectionListener = addSelectionListener;
		this.editSelectionListener = editSelectionListener;
		this.removeSelectionListener = removeSelectionListener;
	}
	
	/**
	 * Sets the move up/down listeners on the buttons
	 * 
	 * @param moveUpSelectionListener
	 * @param moveDownSelectionListener
	 */
	protected void setMoveListeners(SelectionListener moveUpSelectionListener, SelectionListener moveDownSelectionListener){
		this.moveUpSelectionListener = moveUpSelectionListener;
		this.moveDownSelectionListener = moveDownSelectionListener;
	}
	
	/**
	 * Setup the providers for content and labels
	 * 
	 * @param contentProvider
	 * @param labelProvider
	 */
	protected void setProviders(BaseContentProvider<BASEOBJ, BASELIST> contentProvider, LabelProvider labelProvider){
		this.contentProvider = contentProvider;
		this.labelProvider = labelProvider;
		
	}
	
	@Override
	public void create() {
		super.create();
		setTitle(title);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		
		//create gridlayout with two columns
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//create table of periods on first column		
		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5);
		tableData.minimumWidth = 250;
		tableData.minimumHeight = tableHeight;
		createTable(container);
		viewer.setLabelProvider(labelProvider);
		viewer.setContentProvider(contentProvider);
		viewer.setInput(baseList);
		viewer.getTable().setLayoutData(tableData);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				runChangeSelection();
			}
		});
		
		//create add/edit/remove buttons on second column
		GridData buttonData = new GridData(SWT.FILL, SWT.TOP, true, false);
		Button addBtn = new Button(container, SWT.NONE);
		addBtn.setText("Add");
		addBtn.setLayoutData(buttonData);
		addBtn.addSelectionListener(addSelectionListener);
		Button editBtn = new Button(container, SWT.NONE);
		editBtn.setText("Edit");
		editBtn.setLayoutData(buttonData);
		editBtn.addSelectionListener(editSelectionListener);
		Button removeBtn = new Button(container, SWT.NONE);
		removeBtn.setText("Remove");
		removeBtn.setLayoutData(buttonData);
		removeBtn.addSelectionListener(removeSelectionListener);
		
		if(moveOptions){
			//add move up/down buttons
			moveUpBtn = new Button(container, SWT.NONE);
			moveUpBtn.setText("Move Up");
			moveUpBtn.setLayoutData(buttonData);
			moveUpBtn.addSelectionListener(moveUpSelectionListener);
			moveDownBtn = new Button(container, SWT.NONE);
			moveDownBtn.setText("Move Down");
			moveDownBtn.setLayoutData(buttonData);
			moveDownBtn.addSelectionListener(moveDownSelectionListener);
		}
		
		
		return area;
	}
	
	@Override
	public void dispose() {
		viewer.getLabelProvider().dispose();
	}
	
	/**
	 * Enables/disables move up/down buttons when selection changes or as the method is called
	 */
	void runChangeSelection(){
		//only offer move buttons for fullTime employees, everyone else is alphabetical
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		Employee emp = (Employee) selection.getFirstElement();
		if(emp != null && emp.isFullTime()){
			int index = viewer.getTable().getSelectionIndex();
			if(index == 0){
				moveUpBtn.setEnabled(false);
				moveDownBtn.setEnabled(true);
			}else{
				TableItem tItem = viewer.getTable().getItem(index+1);
				Employee nextEmp = (Employee) tItem.getData();
				if(!nextEmp.isFullTime()){
					//the value selected is the end of the fullTime list
					moveUpBtn.setEnabled(true);
					moveDownBtn.setEnabled(false);							
				}else{
					moveUpBtn.setEnabled(true);
					moveDownBtn.setEnabled(true);
				}
			}
		}else{
			moveUpBtn.setEnabled(false);
			moveDownBtn.setEnabled(false);						
		}
	}
	

	/**
	 * Creates the table for formatting the tableViewer.  The format should match the {@link BaseEditorDialog#labelProvider}
	 * 
	 * @param parent
	 */
	protected abstract void createTable(Composite parent);
	
}
