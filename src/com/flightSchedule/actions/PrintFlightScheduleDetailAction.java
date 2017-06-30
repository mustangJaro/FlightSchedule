package com.flightSchedule.actions;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.flightSchedule.core.ExceptionHandler;
import com.flightSchedule.views.FlightScheduleDetailView;

public class PrintFlightScheduleDetailAction implements IWorkbenchAction {
	private static final String PRINT_FSDETAIL_ACTION_ID = "Print.FS.Detail.Action.ID";

	private Shell shell;
	
	public PrintFlightScheduleDetailAction(Shell shell){
		this.shell = shell;
	}
	
	private static PageFormat getMinimumMarginPageFormat(PrinterJob printJob) {
	    PageFormat pf0 = printJob.defaultPage();
	    PageFormat pf1 = (PageFormat) pf0.clone();
	    pf1.setOrientation(PageFormat.LANDSCAPE);
	    Paper p = pf0.getPaper();
	    p.setImageableArea(0, 0,pf0.getWidth(), pf0.getHeight());
	    pf1.setPaper(p);
	    PageFormat pf2 = printJob.validatePage(pf1);
	    return pf2;     
	}

	@Override
	public void runWithEvent(Event event) {
		IWorkbenchWindow wind = (IWorkbenchWindow) shell.getData();
		IWorkbenchPage page = wind.getActivePage();
		FlightScheduleDetailView view = (FlightScheduleDetailView) page.findView(FlightScheduleDetailView.ID);
		
		PrinterJob pj = PrinterJob.getPrinterJob();
		if(pj.printDialog()){
			pj.setPrintable(view, getMinimumMarginPageFormat(pj));
			pj.setJobName("Flight Schedule Detail View");
			try {
				pj.print();
			} catch (PrinterException e) {
				ExceptionHandler.logException(e);
			}
		}

//		PrintDialog printDialog = new PrintDialog(shell, SWT.NONE);
//        printDialog.setText("Print");
//        PrinterData printerData = printDialog.open();
//        if (!(printerData == null)) {
//          Printer p = new Printer(printerData);
//          p.startJob("PrintJob");
//          p.startPage();
//          p.
//          Rectangle trim = p.computeTrim(0, 0, 0, 0);
//          Point dpi = p.getDPI();
//          int leftMargin = dpi.x + trim.x;
//          int topMargin = dpi.y / 2 + trim.y;
//          GC gc = new GC(p);
//          gc.
//          Font font = gc.getFont();
//          //TODO convert TableViewer to printable GC
//          gc = view.print(gc);
////          gc.drawString(printText, leftMargin, topMargin
////              + font.getFontData()[0].getHeight());
//          p.endPage();
//          gc.dispose();
//          p.endJob();
//          p.dispose();
//        }
		
		
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
		return null;
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
		return PRINT_FSDETAIL_ACTION_ID;
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
		return "Print Flight Schedule Detail";
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
