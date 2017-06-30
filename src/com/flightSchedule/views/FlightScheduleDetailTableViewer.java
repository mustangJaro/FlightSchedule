package com.flightSchedule.views;

import java.util.List;

import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class FlightScheduleDetailTableViewer extends TableViewer {

	public FlightScheduleDetailTableViewer(Composite parent, int style) {
		super(parent, style);
	}

	public FlightScheduleDetailTableViewer(Table table) {
		super(table);
	}

	public FlightScheduleDetailTableViewer(Composite parent) {
		super(parent);
	}
	 @Override
	protected List getSelectionFromWidget() {
		// TODO Auto-generated method stub
		return super.getSelectionFromWidget();
	}
	 
	 @Override
	/**
	 * Invoking this method fires an editor activation event which tries to
	 * enable the editor but before this event is passed to {@link
	 * ColumnViewerEditorActivationStrategy} to see if this event should really
	 * trigger editor activation
	 *
	 * @param event
	 * 		the activation event
	 */
	protected void triggerEditorActivationEvent(
			ColumnViewerEditorActivationEvent event) {
		((FlightScheduleDetailTableViewerEditor) getColumnViewerEditor()).handleEditorActivationEvent(event);
	}
}
