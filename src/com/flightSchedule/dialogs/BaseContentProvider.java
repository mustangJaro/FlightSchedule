package com.flightSchedule.dialogs;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.flightSchedule.baseObjects.BaseObject;
import com.flightSchedule.views.AbstractBaseList;
import com.flightSchedule.views.IListViewer;

/**
 * Basic wrapper for periods.  Updates viewer when changes are made
 * and manages listeners on flights.
 * 
 * @author andrew.j.jarosinski
 *
 */
public class BaseContentProvider<T extends BaseObject, S extends AbstractBaseList<T>> implements IStructuredContentProvider, IListViewer<T> {
	private AbstractBaseList<T> baseList;
	private TableViewer viewer;
	
	public BaseContentProvider(TableViewer viewer, AbstractBaseList<T> baseList) {
		this.viewer = viewer;
		this.baseList = baseList;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(newInput != null)
			((S) newInput).addChangeListener(this);
		if(oldInput != null)
			((S) oldInput).removeChangeListener(this);
	}
	@Override
	public Object[] getElements(Object inputElement) {
		return baseList.getPeriodsArray();
	}
	@Override
	public void addObject(T object) {
		viewer.add(object);			
	}
	@Override
	public void removeObject(T object) {
		viewer.remove(object);
	}
	@Override
	public void updateObject(T object) {
		viewer.update(object, null);
	}		

}
