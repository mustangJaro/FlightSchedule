package flightschedule;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.flightSchedule.actions.ConfigurationAction;
import com.flightSchedule.actions.FlightScheduleDetailAction;
import com.flightSchedule.actions.PrintFlightScheduleDetailAction;
import com.flightSchedule.dialogs.*;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
    private IWorkbenchAction newWindowAction;
    private IWorkbenchAction configAction;
    private IWorkbenchAction changePasswordAction;
    private IWorkbenchAction viewPeriodsAction;
    private IWorkbenchAction viewEmployeesAction;
    private IWorkbenchAction openFlightSchedDetailAction;
    private IWorkbenchAction printFSDetailAction;
    private PreferenceManager prefManager;
    

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	
	@Override
	public void makeActions(final IWorkbenchWindow window) {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
        
        configAction = new ConfigurationAction("Configure", window);
        register(configAction);
        
        changePasswordAction = new ChangePasswordDialog(window.getShell());
        register(changePasswordAction);
        
        openFlightSchedDetailAction = new FlightScheduleDetailAction();
        register(openFlightSchedDetailAction);
        
        
        viewPeriodsAction = new PeriodEditorDialog(window.getShell());
        register(viewPeriodsAction);        
        
        viewEmployeesAction = new EmployeeEditorDialog(window.getShell());
        register(viewEmployeesAction);
        
        printFSDetailAction = new PrintFlightScheduleDetailAction(window.getShell());
        register(printFSDetailAction);
        
//        openViewAction = new OpenViewAction(window, "Open Another Message View", View.ID);
//        register(openViewAction);
//        
//        messagePopupAction = new MessagePopupAction("Open Message", window);
//        register(messagePopupAction);
    }

	@Override
	public void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager viewMenu = new MenuManager("&View", IWorkbenchActionConstants.VIEW_EXT);
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        // Add a group marker indicating where action set menus will appear.
//        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(configAction);
        fileMenu.add(changePasswordAction);
        fileMenu.add(new Separator());
        fileMenu.add(printFSDetailAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        
        //View
        viewMenu.add(viewPeriodsAction);
        viewMenu.add(viewEmployeesAction);
        
        // Help
        helpMenu.add(aboutAction);
    }
	
	@Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager fileToolbar1 = new ToolBarManager();
	    fileToolbar1.add(new Separator(IWorkbenchActionConstants.NEW_GROUP));
	    fileToolbar1.add(openFlightSchedDetailAction);
	    coolBar.add(fileToolbar1);
    }
	
	@Override
	public void fillActionBars(int flags) {
		super.fillActionBars(flags);
	}
	
	

}
