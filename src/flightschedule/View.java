package flightschedule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.db.Database;
import com.flightSchedule.core.ExceptionHandler;
import com.flightSchedule.preferencePages.ConfigurationPreference;

public class View extends ViewPart {
	public static final String ID = "FlightSchedule.view";
	public static final String PREF_STORE = "pref.store";

	private TableViewer viewer;
	
	private class StringLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			return super.getText(element);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTable().setLinesVisible(true);

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.setLabelProvider(new StringLabelProvider());

		viewer.getTable().getColumn(0).setWidth(200);
		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		// Provide the input to the ContentProvider
		viewer.setInput(createInitialDataModel());
	}


	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private List<String> createInitialDataModel() {
		ConfigurationPreference config = new ConfigurationPreference();		
		Database db = new Database(config.getServerName(), config.getDBName());
		db.connect();
		try{
			ResultSet rs = db.executeQuery("select ei_id from eiacft");
			while (rs.next()){
//				System.out.println(rs.getString("ei_id"));
			}
		}catch(SQLException e){
			ExceptionHandler.logException("failure getting aicraft", e);
		}
		
		db.close();
		List<String> imageNames = new ArrayList<String>();
		return Arrays.asList("One", "Two", "Three");
	}
}