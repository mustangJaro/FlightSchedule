package com.flightSchedule.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import com.flightSchedule.actions.RemoveFlightAction;
import com.flightSchedule.baseObjects.Aircraft;
import com.flightSchedule.baseObjects.Flight;
import com.flightSchedule.core.SQLServerAircraft;
import com.flightSchedule.core.SQLServerEmployees;
import com.flightSchedule.provider.label.FlightLabelProvider;

public class FlightScheduleDetailView extends ViewPart implements IPropertyChangeListener, Printable {
	public static final String ID = "com.flightSchedule.views.FlightScheduleDetailView";
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
	private static SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
	private static SimpleDateFormat dayNumOfWeek = new SimpleDateFormat("EEEE");
	
	private static Integer TEXT_MARGIN = 3;

	private FlightScheduleDetailTableViewer viewer;     
	private Table table;   
	private DateTime dateTime;
	private Label dayLabel;
	private Label scheduleText;
	private Label noteText;
	private Calendar currentLoadedDate;
	private Integer tableWidth;
	
	private FlightList flights;
	private Map<BigInteger, String> availAircraft = new HashMap<BigInteger, String>();
	private Map<BigInteger, String> fullTimeNames = new HashMap<BigInteger, String>();
	private String[] schedules = new String[]{};
	private Map<BigInteger, String> pis = new HashMap<BigInteger, String>();
	private Map<BigInteger, String> ces = new HashMap<BigInteger, String>();
	

	// Set the table column property names
	private static final String COMPLETED_COLUMN 		= "!";
	private static final String PERIOD_COLUMN 	= "PD";
	private static final String HOURS_COLUMN 			= "HR";
	private static final String TAILNUMBER_COLUMN 		= "Tail #";
	private static final String PC_COLUMN 		= "PC";
	private static final String WORKSCHEDULE_COLUMN 		= "SCHED";
	private static final String PI_COLUMN 		= "PI";
	private static final String CE_COLUMN 		= "CE";
	private static final String ENTEREDBY_COLUMN 		= "Initial";
	private static final String CREATED_DATE_COLUMN 		= "Created";
	private static final String REMARKS_COLUMN 		= "Remarks";

	// Set column names
	/* When adding a column name, make sure these places get updated
	*		createTable()
	*		createTableViewer()
	*		getChoices() - if it's a combobox
	*		FlightCellModifier
	*		FlightLabelProvider
	*/
	/**
	 * Definition for columns to include name, horizontal alignment and width of column
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	enum columnSpec{
		COMPLETED_COLUMN(FlightScheduleDetailView.COMPLETED_COLUMN, SWT.CENTER, 20, 0), 
		PERIOD_COLUMN(FlightScheduleDetailView.PERIOD_COLUMN, SWT.LEFT, 45, 1),
		HOURS_COLUMN(FlightScheduleDetailView.HOURS_COLUMN, SWT.LEFT, 45, 2),
		TAILNUMBER_COLUMN(FlightScheduleDetailView.TAILNUMBER_COLUMN, SWT.CENTER, 100, 3),
		PC_COLUMN(FlightScheduleDetailView.PC_COLUMN, SWT.CENTER, 150, 4),
		WORKSCHEDULE_COLUMN(FlightScheduleDetailView.WORKSCHEDULE_COLUMN, SWT.CENTER, 60, 5),
		PI_COLUMN(FlightScheduleDetailView.PI_COLUMN, SWT.CENTER, 150, 6),
		CE1_COLUMN(FlightScheduleDetailView.CE_COLUMN, SWT.CENTER, 150, 7),
		CE2_COLUMN(FlightScheduleDetailView.CE_COLUMN, SWT.CENTER, 150, 8),
		ENTEREDBY_COLUMN(FlightScheduleDetailView.ENTEREDBY_COLUMN, SWT.CENTER, 50, 9),
		CREATED_DATE_COLUMN(FlightScheduleDetailView.CREATED_DATE_COLUMN, SWT.CENTER, 100, 10),
		REMARKS_COLUMN(FlightScheduleDetailView.REMARKS_COLUMN, SWT.CENTER, 200, 11)
		;
		
		private final String name;
		private final Integer alignment;
		private final Integer width;
		private final Integer colNumber;
		
		columnSpec(final String name, final Integer alignment, final Integer width, final Integer colNumber){
			this.name = name;
			this.alignment = alignment;
			this.width = width;
			this.colNumber = colNumber;
		}
		
		String getName(){
			return name;
		}
		Integer getWidth(){
			return width;
		}

		Integer getAlignment() {
			return alignment;
		}

		public Integer getColNumber() {
			return colNumber;
		}
		
	}
	private List<String> columnNames = new ArrayList<String>();
	
	public enum REFRESH_TYPE{
		PERIOD,
		EMPLOYEE,
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		currentLoadedDate = Calendar.getInstance();
		loadData();
		
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		parent.setLayout(grid);
		
		GridLayout headerGrid = new GridLayout();
		headerGrid.numColumns = 2;
		Composite upperSection = new Composite(parent, SWT.FILL);
		upperSection.setLayout(headerGrid);
		upperSection.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		// create next/previous/choose date buttons
		GridData dateGridData = new GridData(GridData.CENTER, GridData.CENTER, true, false, 2, 1);
		dateGridData.minimumWidth = 125;
		dateGridData.minimumHeight = 50;
		Font headerFont = JFaceResources.getFont(JFaceResources.HEADER_FONT);
		
		dateTime = new DateTime(upperSection, SWT.DROP_DOWN);
		dateTime.setDate(currentLoadedDate.get(Calendar.YEAR), currentLoadedDate.get(Calendar.MONTH), currentLoadedDate.get(Calendar.DAY_OF_MONTH));
		dateTime.setFont(headerFont);
		dateTime.setLayoutData(dateGridData);
		dateTime.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				DateTime date = (DateTime) e.getSource();
				date.getMonth();
				currentLoadedDate.set(date.getYear(), date.getMonth(), date.getDay());
				setDayHeaderInfo();
				loadData();
				viewer.refresh();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		TCHAR lpszFormat = new TCHAR (0, "dd-MMM-yy", true);
		OS.SendMessage (dateTime.handle, OS.DTM_SETFORMAT, 0, lpszFormat);
		
		dayLabel = new Label(upperSection, SWT.CENTER);
		dayLabel.setLayoutData(dateGridData);

		
		
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1);
		Label scheduleLabel = new Label(upperSection, SWT.RIGHT);
		scheduleLabel.setText("Schedule:");
		scheduleLabel.setLayoutData(gridData);
		scheduleText = new Label(upperSection,0);
		scheduleText.setLayoutData(gridData);
		
		Label noteLabel = new Label(upperSection, SWT.RIGHT);
		noteLabel.setText("Notes:");
		noteLabel.setLayoutData(gridData);
		noteText = new Label(upperSection, 0);
		noteText.setLayoutData(gridData);
		
		setDayHeaderInfo();
        
        createTable(parent);
        createTableViewer();

        tableWidth = table.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		
        viewer.setLabelProvider(new FlightLabelProvider());
		viewer.setContentProvider(new FlightContentProvider());
		
		//create the context menu when viewer is right-clicked
		MenuManager menuMgr = new MenuManager();
	    menuMgr.setRemoveAllWhenShown(true);
	    menuMgr.addMenuListener(new IMenuListener() {
	        public void menuAboutToShow(IMenuManager manager) {
	            FlightScheduleDetailView.this.fillContextMenu(manager);
	        }
	    });
	    Menu menu = menuMgr.createContextMenu(viewer.getControl());
	    viewer.getControl().setMenu(menu);
	    getSite().registerContextMenu(menuMgr, viewer);	
	    
		// Provide the input to the ContentProvider
		viewer.setInput(flights);

	}
	
	protected void fillContextMenu(IMenuManager manager) {
		StructuredSelection sel = (StructuredSelection) viewer.getSelection();
		Flight flight = (Flight) sel.getFirstElement();

		RemoveFlightAction removeAction = new RemoveFlightAction(flight);
		removeAction.addPropertyChangeListener(this);
		manager.add(removeAction);
	}

	private void createTable(Composite parent){
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		final int NUMBER_COLUMNS = 4;
	
		table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
	
		for(int colNum = 0; colNum < columnSpec.values().length; colNum++){
			columnSpec spec = columnSpec.values()[colNum];
			TableColumn column = new TableColumn(table, spec.getAlignment(), colNum);		
			column.setText(spec.getName());
			column.setWidth(spec.getWidth());
			columnNames.add(spec.getName());
			
		}
		
	}

	private void createTableViewer() {

		viewer = new FlightScheduleDetailTableViewer(table);
		viewer.setUseHashlookup(true);
		
		viewer.setColumnProperties(columnNames.toArray(new String[]{}));
		
		//setup horizontal tabs
		FlightScheduleDetailTableViewerEditor.create(viewer, new ColumnViewerEditorActivationStrategy(viewer), ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnSpec.values().length];

		// Column 1 : Completed (Checkbox)
		editors[columnSpec.COMPLETED_COLUMN.getColNumber()] = new CheckboxCellEditor(table);

		// Column 2 : Period
		ComboBoxStringCellEditor cbEditor = new ComboBoxStringCellEditor(table, (String[]) flights.getPeriodsArray(), SWT.READ_ONLY);
		cbEditor.getLayoutData().grabHorizontal = false;
		editors[columnSpec.PERIOD_COLUMN.getColNumber()] = cbEditor;

		// Column 3 : Hours 
		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).addVerifyListener(
				new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						e.doit = e.text.matches("^(\\d?\\.?\\d?)$");
					}
				});
		editors[columnSpec.HOURS_COLUMN.getColNumber()] = textEditor;

		// Column 4 : Tail Number
		editors[columnSpec.TAILNUMBER_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, availAircraft.values().toArray(new String[]{}));

		// PC
		editors[columnSpec.PC_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, fullTimeNames.values().toArray(new String[]{}));

		// Work Schedule
		editors[columnSpec.WORKSCHEDULE_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, schedules);

		// PI
		editors[columnSpec.PI_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, pis.values().toArray(new String[]{}));

		// CE1
		editors[columnSpec.CE1_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, ces.values().toArray(new String[]{}));

		// CE2
		editors[columnSpec.CE2_COLUMN.getColNumber()] = new ComboBoxStringCellEditor(table, ces.values().toArray(new String[]{}));
		
		//Entered by
		TextCellEditor enteredBy = new TextCellEditor(table);
		editors[columnSpec.ENTEREDBY_COLUMN.getColNumber()] = enteredBy;
		
		//Created Date
		TextCellEditor createdDate = new TextCellEditor(table);
		editors[columnSpec.CREATED_DATE_COLUMN.getColNumber()] = createdDate;
		
		//Remarks
		TextCellEditor remarks = new TextCellEditor(table);
		editors[columnSpec.REMARKS_COLUMN.getColNumber()] = remarks;

		// Assign the cell editors to the viewer 
		viewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		viewer.setCellModifier(new FlightCellModifier(this));
	}
	
	private void setDayHeaderInfo(){
		dayLabel.setText(dayOfWeek.format(currentLoadedDate.getTime()));
		scheduleText.setText("days or nights");
		noteText.setText("these are notes");
		
	}
	
	/**
	 * Loads field data for use when loading the table viewer
	 */
	private void loadData(){

		flights = new FlightList(currentLoadedDate);
		SQLServerAircraft sqlAircraft = new SQLServerAircraft();
		availAircraft = sqlAircraft.getAircraftMap();
		SQLServerEmployees sqlEmployees = new SQLServerEmployees();
		fullTimeNames = sqlEmployees.getAllEmployeesByName();
		schedules = sqlEmployees.getWorkSchedules();
		pis = sqlEmployees.getEmployeesByType(SQLServerEmployees.EMPLOYEE_TYPE.PI);
		ces = sqlEmployees.getEmployeesByType(SQLServerEmployees.EMPLOYEE_TYPE.CE);
	}
	
	public FlightList getFlights(){
		return flights;
	}
	public void refreshViewer(REFRESH_TYPE type){
		switch(type){
			case PERIOD:
				flights.refreshPeriods();
				((ComboBoxCellEditor) viewer.getCellEditors()[columnSpec.PERIOD_COLUMN.getColNumber()]).setItems(flights.getPeriodsArray());
				break;
			case EMPLOYEE:
				flights.refreshFlights(currentLoadedDate);
				SQLServerEmployees sqlEmployees = new SQLServerEmployees();
				fullTimeNames = sqlEmployees.getAllEmployeesByName();
				pis = sqlEmployees.getEmployeesByType(SQLServerEmployees.EMPLOYEE_TYPE.PI);
				ces = sqlEmployees.getEmployeesByType(SQLServerEmployees.EMPLOYEE_TYPE.CE);
				((ComboBoxCellEditor) viewer.getCellEditors()[columnSpec.PC_COLUMN.getColNumber()]).setItems(fullTimeNames.values().toArray(new String[]{}));
				((ComboBoxCellEditor) viewer.getCellEditors()[columnSpec.PI_COLUMN.getColNumber()]).setItems(pis.values().toArray(new String[]{}));
				((ComboBoxCellEditor) viewer.getCellEditors()[columnSpec.CE1_COLUMN.getColNumber()]).setItems(ces.values().toArray(new String[]{}));
				((ComboBoxCellEditor) viewer.getCellEditors()[columnSpec.CE2_COLUMN.getColNumber()]).setItems(ces.values().toArray(new String[]{}));
		}
		viewer.setInput(flights);
		viewer.refresh();
	}

	public List<String> getColumnNames() {
		return columnNames;
	}
	/**
	 * Return the array of choices for a multiple choice cell
	 */
	public String[] getChoices(String property) {
		
		switch(property){
			case PERIOD_COLUMN:
				return flights.getPeriodsArray();
			case TAILNUMBER_COLUMN:
				return availAircraft.values().toArray(new String[]{});
			case PC_COLUMN:
				return fullTimeNames.values().toArray(new String[]{});
			case PI_COLUMN:
				return pis.values().toArray(new String[]{});
			case CE_COLUMN:
				return ces.values().toArray(new String[]{});
			default:
				return new String[]{};
		}
	}
	/**
	 * Return the array of IDs for a multiple choice cell
	 */
	public BigInteger[] getIDs(String property) {
		
		switch(property){
			case PC_COLUMN:
				return fullTimeNames.keySet().toArray(new BigInteger[]{});
			case PI_COLUMN:
				return pis.keySet().toArray(new BigInteger[]{});
			case CE_COLUMN:
				return ces.keySet().toArray(new BigInteger[]{});
			case TAILNUMBER_COLUMN:
				return availAircraft.keySet().toArray(new BigInteger[]{});
			default:
				return new BigInteger[]{};
		}
	}
	
	public boolean canModify(Flight flight, String property){
		switch(property){
			case WORKSCHEDULE_COLUMN:
			case ENTEREDBY_COLUMN:
			case CREATED_DATE_COLUMN:
				return false;
			case PC_COLUMN:
				if(flight != null){
					if(flight.getPC() != null && flight.getPC().isFullTime()){
						return false;
					}else
						return true;
				}else{
					return true;
				}
			default:
				return true;
		}
	}
	public boolean canModify(int columnNumber){
		return canModify(null, columnNames.get(columnNumber));
	}

	@Override
	public void setFocus() {

	}
	
	/**
	 * Basic wrapper for flights.  Updates viewer when changes are made
	 * and manages listeners on flights.
	 * 
	 * @author andrew.j.jarosinski
	 *
	 */
	class FlightContentProvider implements IStructuredContentProvider, IListViewer<Flight>{
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if(newInput != null)
				((FlightList) newInput).addChangeListener(this);
			if(oldInput != null)
				((FlightList) oldInput).removeChangeListener(this);
		}
		@Override
		public Object[] getElements(Object inputElement) {
			return flights.getFlights().toArray();
		}
		@Override
		public void addObject(Flight flight) {
			viewer.add(flight);			
		}
		@Override
		public void removeObject(Flight flight) {
			viewer.remove(flight);
		}
		@Override
		public void updateObject(Flight flight) {
			viewer.update(flight, null);
		}		
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		refreshViewer(REFRESH_TYPE.EMPLOYEE);
	}
	
	public Table getTable(){
		return viewer.getTable();
	}
	
	@Override
	public int print(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) { /* We have only one page, and 'page' is zero-based */
            return NO_SUCH_PAGE;
        }
 
        //TODO loop through seven day week for printing detail
        
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         */
        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        Color lightGray = new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 70);
        
        FontMetrics fm = graphics.getFontMetrics();
        double pageWidth = pf.getImageableWidth();

        int lineHeight = fm.getAscent() + fm.getLeading() + fm.getDescent();
        int totalHeight = lineHeight;
 
        /* Now we perform our rendering */
		//datetime
		String dt = (dateTime.getMonth()+1) + "/" + dateTime.getDay() + "/" + dateTime.getYear();
        Double centered = (pageWidth - fm.stringWidth(dt))/2;
        g2d.drawString(dt, centered.intValue(), totalHeight);
		totalHeight += lineHeight;
		
		//day
        centered = (pageWidth - fm.stringWidth(dayLabel.getText()))/2;
        g2d.drawString(dayLabel.getText(), centered.intValue(), totalHeight);
		totalHeight += lineHeight;
		
		//schedule text
        centered = (pageWidth - fm.stringWidth(scheduleText.getText()))/2;
        g2d.drawString(scheduleText.getText(), centered.intValue(), totalHeight);
		totalHeight += lineHeight;
		
		//note text
        centered = (pageWidth - fm.stringWidth(noteText.getText()))/2;
        g2d.drawString(noteText.getText(), centered.intValue(), totalHeight);
		int headerStartRectY = totalHeight+5;
		totalHeight += lineHeight;

		
		//loop through columns
		java.awt.Font colHeaderFont = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 10);
		g2d.setFont(colHeaderFont);
		fm = g2d.getFontMetrics();
		lineHeight = fm.getHeight();
		List<Double> colWidths = new ArrayList<Double>();
		Double runningWidth = 0d;
		colWidths.add(runningWidth);
		int j = 0;
		Double prevWidth = 0d;
		for(TableColumn col : getTable().getColumns()){
			g2d.setPaint(Color.BLACK);
			g2d.drawString(col.getText(), runningWidth.intValue()+TEXT_MARGIN, totalHeight);
			Double colWidth = (double) columnSpec.values()[j].getWidth();
			Double d = (double) (colWidth / tableWidth * pageWidth);
			colWidths.add(d);
			
			runningWidth += d;
			g2d.setPaint(lightGray);
			g2d.draw(new Rectangle(prevWidth.intValue(), headerStartRectY, (runningWidth.intValue() - prevWidth.intValue()), lineHeight));
			prevWidth += d;
			j++;
		}
		int tableStartRectY = totalHeight+5;
		totalHeight += lineHeight;
		
		java.awt.Font smallFont = new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 9);
		g2d.setFont(smallFont);
		fm = g2d.getFontMetrics();
		lineHeight = fm.getHeight();
		
		
		
		//loop through table cells
		FlightLabelProvider flp = (FlightLabelProvider) viewer.getLabelProvider();
		for(Flight fl : flights.getFlights()){
			Double totalWidth = 0d;
			prevWidth = 0d;
			for(int i = 0; i < columnSpec.values().length; i++){
				String colText = flp.getColumnText(fl, i);
				g2d.setPaint(Color.BLACK);
				g2d.drawString(fitStringInWidth(fm, colText, colWidths.get(i+1)), totalWidth.intValue()+TEXT_MARGIN, totalHeight);
				
				totalWidth += colWidths.get(i+1);
				g2d.setPaint(lightGray);
				g2d.draw(new Rectangle(prevWidth.intValue(), tableStartRectY, (totalWidth.intValue() - prevWidth.intValue()), lineHeight));
				prevWidth += colWidths.get(i+1);
			}
			totalHeight += lineHeight;
			tableStartRectY += lineHeight;
		}
 
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;

	}
	
	private String fitStringInWidth(FontMetrics fm, String text, Double maxWidth){
		String result = "";
		TextLayout tl;
		
		if(text != null && !text.equals("")){
			tl = new TextLayout(text, fm.getFont(), fm.getFontRenderContext());
	
			if(tl.getBounds().getWidth() > maxWidth){
				int ellipsisWidth = fm.stringWidth("...");
				double currWidth = 0d;
				int currChar = 0;
				while(currWidth + fm.stringWidth(text.substring(currChar, currChar)+1) < maxWidth){
					if(currChar + 1 > text.length())
						break;
					result += text.substring(currChar, currChar+1);
					tl = new TextLayout(result, fm.getFont(), fm.getFontRenderContext());
					currWidth = tl.getBounds().getWidth();
					currChar++;
				}
				if(currChar < text.length())
					result += "...";
			}else{
				result = text;
			}
		}
		
		
		return result;
	}

}
