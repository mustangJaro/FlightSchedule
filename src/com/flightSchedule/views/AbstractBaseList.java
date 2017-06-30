package com.flightSchedule.views;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.flightSchedule.baseObjects.BaseObject;

public abstract class AbstractBaseList<T extends BaseObject> {
	private Set<IListViewer<T>> changeListeners = new HashSet<IListViewer<T>>();

	/**
	 * @param task
	 */
	public void taskChanged(T object) {
		Iterator<IListViewer<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IListViewer<T>) iterator.next()).updateObject(object);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IListViewer<T> viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IListViewer<T> viewer) {
		changeListeners.add(viewer);
	}

	public abstract Object[] getPeriodsArray();
}
