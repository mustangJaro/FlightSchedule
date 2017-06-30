package com.flightSchedule.views;

import com.flightSchedule.baseObjects.BaseObject;

/**
 * Implemented on Content Providers in views to reinforce adding, removing, and modifying objects
 * 
 * @author andrew.j.jarosinski
 *
 * @param <T>
 */
public interface IListViewer <T extends BaseObject> {

	/**
	 * Update view to add object to list
	 * 
	 * @param object
	 */
	public void addObject(T object);
	
	/**
	 * Update view to remove object from list
	 * 
	 * @param object
	 */
	public void removeObject(T object);
	
	/**
	 * Update view with object that was modified
	 * 
	 * @param object
	 */
	public void updateObject(T object);
}
