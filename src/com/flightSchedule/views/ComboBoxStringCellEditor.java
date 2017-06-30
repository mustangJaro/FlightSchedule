package com.flightSchedule.views;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * Custom class to filter based on entered values
 * 
 * @author andrew.j.jarosinski
 *
 */
public class ComboBoxStringCellEditor extends ComboBoxCellEditor {
	private String enteredValue = "";

	public ComboBoxStringCellEditor() {
		super();
		addKeyListener();
	}

	public ComboBoxStringCellEditor(Composite parent, String[] items) {
		super(parent, items);
		addKeyListener();
	}

	public ComboBoxStringCellEditor(Composite parent, String[] items, int style) {
		super(parent, items, style);
		addKeyListener();
	}
	
	public void addKeyListener(){
		CCombo cbo = (CCombo) getControl();
		cbo.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(Character.isAlphabetic(e.character) || Character.isDigit(e.character)){
					enteredValue += e.character;
				}else{
					switch(Character.toString(e.character)){
						case "\b":
							if(enteredValue.length() > 0)
								enteredValue = enteredValue.substring(0, enteredValue.length()-1);
							break;
						case "\n":
						case "\r":
						case "\f":
						case "\t":
						case "\'":
						case "\"":
						case "\\":
						default:
							break;
					}
				}
				System.out.println("ComboBoxStringCellEditor.keyReleased: " + enteredValue);
				int i = 0;
				boolean foundMatch = false;
				for(String item : getItems()){
					if(item.toUpperCase().startsWith(enteredValue.toUpperCase())){
						System.out.println(" match with[" + i + "]: " + item);
						setValue(i);
						foundMatch = true;
						break;
					}
					i++;
				}
				//remove extra characters if match wasn't found
				if(!foundMatch){
					enteredValue = enteredValue.substring(0, enteredValue.length()-1);
				}
				System.out.println("ComboBoxStringCellEditor.keyReleased ending value: " + enteredValue);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
	}

}
