package com.flightSchedule.views;

import org.eclipse.jface.viewers.CellEditor.LayoutData;

import java.util.List;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.jface.viewers.ViewerRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;

import com.flightSchedule.baseObjects.Flight;

/**
 * Customized editor implementation for table.  Very similar to {@link TableViewerEditor}
 * with the addition of the enter key value
 * 
 * @author andrew.j.jarosinski
 *
 */
public class FlightScheduleDetailTableViewerEditor extends ColumnViewerEditor {

	private TableEditor tableEditor;
	private int feature;
	private ViewerCell cell;
	private CellEditor cellEditor;
	private ListenerList editorActivationListener;
	private ICellEditorListener cellEditorListener;
	private TraverseListener tabeditingListener;
	private DisposeListener disposeListener;
	private boolean inEditorDeactivation;

	
	protected FlightScheduleDetailTableViewerEditor(TableViewer viewer,
			ColumnViewerEditorActivationStrategy editorActivationStrategy, int feature) {
		super(viewer, editorActivationStrategy, feature);
		tableEditor = new TableEditor(viewer.getTable());
		this.feature = feature;
		this.disposeListener = e -> {
			if( viewer.isCellEditorActive() ) {
				cancelEditing();
			}
		};
		initCellEditorListener();
	}

	private void initCellEditorListener() {
		cellEditorListener = new ICellEditorListener() {
			@Override
			public void editorValueChanged(boolean oldValidState,
					boolean newValidState) {
				// Ignore.
			}

			@Override
			public void cancelEditor() {
				FlightScheduleDetailTableViewerEditor.this.cancelEditing();
			}

			@Override
			public void applyEditorValue() {
				FlightScheduleDetailTableViewerEditor.this.applyEditorValue();
			}
		};
	}
	
	/**
	 * Create a customized editor whose activation process is customized
	 *
	 * @param viewer
	 *            the viewer the editor is created for
	 * @param editorActivationStrategy
	 *            activation strategy to control if an editor activated
	 * @param feature
	 *            bit mask controlling the editor
	 *            <ul>
	 *            <li>{@link ColumnViewerEditor#DEFAULT}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_CYCLE_IN_ROW}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_HORIZONTAL}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_MOVE_TO_ROW_NEIGHBOR}</li>
	 *            <li>{@link ColumnViewerEditor#TABBING_VERTICAL}</li>
	 *            </ul>
	 */
	public static void create(TableViewer viewer,
			ColumnViewerEditorActivationStrategy editorActivationStrategy,
			int feature) {
		FlightScheduleDetailTableViewerEditor editor = new FlightScheduleDetailTableViewerEditor(viewer, editorActivationStrategy, feature);
		viewer.setColumnViewerEditor(editor);
	}

	@Override
	protected void setEditor(Control w, Item item, int columnNumber) {
		tableEditor.setEditor(w, (TableItem) item, columnNumber);
		
	}

	@Override
	protected void setLayoutData(LayoutData layoutData) {
		tableEditor.grabHorizontal = layoutData.grabHorizontal;
		tableEditor.horizontalAlignment = layoutData.horizontalAlignment;
		tableEditor.minimumWidth = layoutData.minimumWidth;
		tableEditor.verticalAlignment = layoutData.verticalAlignment;

		if (layoutData.minimumHeight != SWT.DEFAULT) {
			tableEditor.minimumHeight = layoutData.minimumHeight;
		}
		
	}

	@Override
	protected void updateFocusCell(ViewerCell focusCell, ColumnViewerEditorActivationEvent event) {
		// Update the focus cell when we activated the editor with these 2
		// events
		if (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC
				|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL) {

//			List l = getViewer().getSelectionFromWidget();

//			if (!l.contains(focusCell.getElement())) {
//				getViewer().setSelection(
//						new StructuredSelection(focusCell.getElement()), true);
//			}

			// Set the focus cell after the selection is updated because else
			// the cell is not scrolled into view
//			if (focusCellManager != null) {
//				focusCellManager.setFocusCell(focusCell);
//			}
		}
	}
	

	/**
	 * Enable the editor by mouse down
	 *
	 * @param event
	 */
	void handleEditorActivationEvent(ColumnViewerEditorActivationEvent event) {

		// Only activate if the event isn't tagged as canceled
		if (!event.cancel) {
			if (cellEditor != null) {
				applyEditorValue();
			}

			this.cell = (ViewerCell) event.getSource();

			// Only null if we are not in a deactivation process see bug 260892
			if( ! activateCellEditor(event) && ! inEditorDeactivation ) {
				this.cell = null;
				this.cellEditor = null;
			}
		}
	}
	
	@Override
	protected void processTraverseEvent(int columnIndex, ViewerRow row,
			TraverseEvent event) {

		ViewerCell cell2edit = null;

		if (event.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			event.doit = false;

			if ((event.stateMask & SWT.CTRL) == SWT.CTRL
					&& (feature & TABBING_VERTICAL) == TABBING_VERTICAL) {
				cell2edit = searchCellAboveBelow(row, getViewer(), columnIndex, true);
			} else if ((feature & TABBING_HORIZONTAL) == TABBING_HORIZONTAL) {
				cell2edit = searchPreviousCell(row, row.getCell(columnIndex),
						row.getCell(columnIndex), getViewer());
			}
		} else if (event.detail == SWT.TRAVERSE_TAB_NEXT) {
			event.doit = false;

			if ((event.stateMask & SWT.CTRL) == SWT.CTRL
					&& (feature & TABBING_VERTICAL) == TABBING_VERTICAL) {
				cell2edit = searchCellAboveBelow(row, getViewer(), columnIndex,
						false);
			} else if ((feature & TABBING_HORIZONTAL) == TABBING_HORIZONTAL) {
				cell2edit = searchNextCell(row, row.getCell(columnIndex), row
						.getCell(columnIndex), getViewer());
			}
		}

		if (cell2edit != null) {

			getViewer().getControl().setRedraw(false);
			ColumnViewerEditorActivationEvent acEvent = new ColumnViewerEditorActivationEvent(
					cell2edit, event);

			if (cellEditor != null) {
				applyEditorValue();
			}
			this.cell = (ViewerCell) acEvent.getSource();
			if( ! activateCellEditor(acEvent)) {
				this.cell = null;
				this.cellEditor = null;
			}
			
			getViewer().getControl().setRedraw(true);
		}
	}
	

	private ViewerCell searchCellAboveBelow(ViewerRow row, ColumnViewer viewer,
			int columnIndex, boolean above) {
		ViewerCell rv = null;

		ViewerRow newRow = null;

		if (above) {
			newRow = row.getNeighbor(ViewerRow.ABOVE, false);
		} else {
			newRow = row.getNeighbor(ViewerRow.BELOW, false);
		}

		if (newRow != null) {
			FlightCellModifier fcm = (FlightCellModifier) getViewer().getCellModifier();
			if (fcm != null && fcm.canModify(newRow.getElement(), columnIndex)) {
				rv = newRow.getCell(columnIndex);
			} else {
				rv = searchCellAboveBelow(newRow, viewer, columnIndex, above);
			}
		}

		return rv;
	}

	private boolean isCellEditable(ColumnViewer viewer, ViewerCell cell) {
		FlightCellModifier fcm = (FlightCellModifier) getViewer().getCellModifier();
		return fcm != null && fcm.canModify(cell.getElement(), cell.getColumnIndex());
	}

	private ViewerCell searchPreviousCell(ViewerRow row,
			ViewerCell currentCell, ViewerCell originalCell, ColumnViewer viewer) {
		ViewerCell rv = null;
		ViewerCell previousCell;

		if (currentCell != null) {
			previousCell = currentCell.getNeighbor(ViewerCell.LEFT, true);
		} else {
			if (row.getColumnCount() != 0) {
				previousCell = row.getCell(row.getColumnCount() - 1);
			} else {
				previousCell = row.getCell(0);
			}

		}

		// No endless loop
		if (originalCell.equals(previousCell)) {
			return null;
		}

		if (previousCell != null) {
			if (isCellEditable(viewer, previousCell)) {
				rv = previousCell;
			} else {
				rv = searchPreviousCell(row, previousCell, originalCell, viewer);
			}
		} else {
			if ((feature & TABBING_CYCLE_IN_ROW) == TABBING_CYCLE_IN_ROW) {
				rv = searchPreviousCell(row, null, originalCell, viewer);
			} else if ((feature & TABBING_MOVE_TO_ROW_NEIGHBOR) == TABBING_MOVE_TO_ROW_NEIGHBOR) {
				ViewerRow rowAbove = row.getNeighbor(ViewerRow.ABOVE, false);
				if (rowAbove != null) {
					rv = searchPreviousCell(rowAbove, null, originalCell,
							viewer);
				}
			}
		}

		return rv;
	}

	private ViewerCell searchNextCell(ViewerRow row, ViewerCell currentCell,
			ViewerCell originalCell, ColumnViewer viewer) {
		ViewerCell rv = null;

		ViewerCell nextCell;

		if (currentCell != null) {
			nextCell = currentCell.getNeighbor(ViewerCell.RIGHT, true);
		} else {
			nextCell = row.getCell(0);
		}

		// No endless loop
		if (originalCell.equals(nextCell)) {
			return null;
		}

		if (nextCell != null) {
			if (isCellEditable(viewer, nextCell)) {
				rv = nextCell;
			} else {
				rv = searchNextCell(row, nextCell, originalCell, viewer);
			}
		} else {
			if ((feature & TABBING_CYCLE_IN_ROW) == TABBING_CYCLE_IN_ROW) {
				rv = searchNextCell(row, null, originalCell, viewer);
			} else if ((feature & TABBING_MOVE_TO_ROW_NEIGHBOR) == TABBING_MOVE_TO_ROW_NEIGHBOR) {
				ViewerRow rowBelow = row.getNeighbor(ViewerRow.BELOW, false);
				if (rowBelow != null) {
					rv = searchNextCell(rowBelow, null, originalCell, viewer);
				}
			}
		}

		return rv;
	}
	
	

	private boolean activateCellEditor(final ColumnViewerEditorActivationEvent activationEvent) {

		FlightCellModifier fcm = (FlightCellModifier) getViewer().getCellModifier();

		if (fcm != null && fcm.canModify(cell.getElement(), cell.getColumnIndex())) {
			cellEditor = getViewer().getCellEditors()[cell.getColumnIndex()];
			if (cellEditor != null) {
				int timeout = Display.getCurrent().getDoubleClickTime();

				final int activationTime;

				if (timeout != 0) {
					activationTime = activationEvent.time + timeout;
				} else {
					activationTime = 0;
				}

				if (editorActivationListener != null
						&& !editorActivationListener.isEmpty()) {
					Object[] ls = editorActivationListener.getListeners();
					for (int i = 0; i < ls.length; i++) {
						((ColumnViewerEditorActivationListener) ls[i])
								.beforeEditorActivated(activationEvent);

						// Was the activation canceled ?
						if (activationEvent.cancel) {
							return false;
						}
					}
				}

				updateFocusCell(cell, activationEvent);

				cellEditor.addListener(cellEditorListener);
				cellEditor.setValue(fcm.getValue(cell.getElement(), cell.getColumnIndex()));
//				fcm.modify(cell.getElement(), cell.getColumnIndex(), fcm.getValue(cell.getElement(), cell.getColumnIndex()));

				// Tricky flow of control here:
				// activate() can trigger callback to cellEditorListener which
				// will clear cellEditor
				// so must get control first, but must still call activate()
				// even if there is no control.
				final Control control = cellEditor.getControl();
				cellEditor.activate(activationEvent);
				if (control == null) {
					return false;
				}
				setLayoutData(cellEditor.getLayoutData());
				setEditor(control, (Item) cell.getItem(), cell.getColumnIndex());
				cellEditor.setFocus();

//				if (cellEditor.dependsOnExternalFocusListener()) {
//					if (focusListener == null) {
//						focusListener = new FocusAdapter() {
//							@Override
//							public void focusLost(FocusEvent e) {
//								applyEditorValue();
//							}
//						};
//					}
//					control.addFocusListener(focusListener);
//				}


				if (tabeditingListener == null) {
					tabeditingListener = e -> {
						if ((feature & DEFAULT) != DEFAULT) {
							processTraverseEvent(cell.getColumnIndex(), cell.getViewerRow(), e);
						}
					};
				}

				control.addTraverseListener(tabeditingListener);

				if (editorActivationListener != null
						&& !editorActivationListener.isEmpty()) {
					Object[] ls = editorActivationListener.getListeners();
					for (int i = 0; i < ls.length; i++) {
						((ColumnViewerEditorActivationListener) ls[i])
								.afterEditorActivated(activationEvent);
					}
				}

				this.cell.getItem().addDisposeListener(disposeListener);

				return true;
			}

		}

		return false;
	}
		
	/**
	 * Applies the current value and deactivates the currently active cell
	 * editor.
	 */
	void applyEditorValue() {
		// avoid re-entering
		if (!inEditorDeactivation) {
			try {
				inEditorDeactivation = true;
				CellEditor c = this.cellEditor;
				if (c != null && this.cell != null) {
					ColumnViewerEditorDeactivationEvent tmp = new ColumnViewerEditorDeactivationEvent(
							cell);
					tmp.eventType = ColumnViewerEditorDeactivationEvent.EDITOR_SAVED;
					if (editorActivationListener != null
							&& !editorActivationListener.isEmpty()) {
						Object[] ls = editorActivationListener.getListeners();
						for (int i = 0; i < ls.length; i++) {

							((ColumnViewerEditorActivationListener) ls[i])
									.beforeEditorDeactivated(tmp);
						}
					}

					Item t = (Item) this.cell.getItem();

					// don't null out table item -- same item is still selected
					if (t != null && !t.isDisposed() && c.isValueValid()) {
						saveEditorValue(c);
					}
					if (!getViewer().getControl().isDisposed()) {
						setEditor(null, null, 0);
					}

					c.removeListener(cellEditorListener);
					Control control = c.getControl();
					if (control != null && !control.isDisposed()) {
//						if (mouseListener != null) {
//							control.removeMouseListener(mouseListener);
//							// Clear the instance not needed any more
//							mouseListener = null;
//						}
//						if (focusListener != null) {
//							control.removeFocusListener(focusListener);
//						}

						if (tabeditingListener != null) {
							control.removeTraverseListener(tabeditingListener);
						}
					}
					c.deactivate();

					if (editorActivationListener != null
							&& !editorActivationListener.isEmpty()) {
						Object[] ls = editorActivationListener.getListeners();
						for (int i = 0; i < ls.length; i++) {
							((ColumnViewerEditorActivationListener) ls[i])
									.afterEditorDeactivated(tmp);
						}
					}

					if( ! this.cell.getItem().isDisposed() ) {
						this.cell.getItem().removeDisposeListener(disposeListener);
					}
				}

				this.cellEditor = null;
				this.cell = null;
			} finally {
				inEditorDeactivation = false;
			}
		}
	}

	/**
	 * Cancel editing
	 */
	void cancelEditing() {
		// avoid re-entering
		if (!inEditorDeactivation) {
			try {
				inEditorDeactivation = true;
				if (cellEditor != null) {
					ColumnViewerEditorDeactivationEvent tmp = new ColumnViewerEditorDeactivationEvent(
							cell);
					tmp.eventType = ColumnViewerEditorDeactivationEvent.EDITOR_CANCELED;
					if (editorActivationListener != null
							&& !editorActivationListener.isEmpty()) {
						Object[] ls = editorActivationListener.getListeners();
						for (int i = 0; i < ls.length; i++) {

							((ColumnViewerEditorActivationListener) ls[i])
									.beforeEditorDeactivated(tmp);
						}
					}

					if (!getViewer().getControl().isDisposed()) {
						setEditor(null, null, 0);
					}

					cellEditor.removeListener(cellEditorListener);

					Control control = cellEditor.getControl();
					if (control != null && !getViewer().getControl().isDisposed()) {
//						if (mouseListener != null) {
//							control.removeMouseListener(mouseListener);
//							// Clear the instance not needed any more
//							mouseListener = null;
//						}
//						if (focusListener != null) {
//							control.removeFocusListener(focusListener);
//						}

						if (tabeditingListener != null) {
							control.removeTraverseListener(tabeditingListener);
						}
					}

					CellEditor oldEditor = cellEditor;
					oldEditor.deactivate();

					if (editorActivationListener != null
							&& !editorActivationListener.isEmpty()) {
						Object[] ls = editorActivationListener.getListeners();
						for (int i = 0; i < ls.length; i++) {
							((ColumnViewerEditorActivationListener) ls[i])
									.afterEditorDeactivated(tmp);
						}
					}

					if( ! this.cell.getItem().isDisposed() ) {
						this.cell.getItem().addDisposeListener(disposeListener);
					}

					this.cellEditor = null;
					this.cell = null;

				}
			} finally {
				inEditorDeactivation = false;
			}
		}
	}

	private void saveEditorValue(CellEditor cellEditor) {
		FlightCellModifier fcm = (FlightCellModifier) getViewer().getCellModifier();
		if (fcm != null && fcm.canModify(cell.getElement(), cell.getColumnIndex())) {
			fcm.modify(cell.getElement(), cell.getColumnIndex(), cellEditor.getValue());
		}
	}

}
