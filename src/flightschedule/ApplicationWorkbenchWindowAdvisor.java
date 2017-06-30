package flightschedule;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}
	
	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setTitle("Flight Schedule");
//		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
//		configurer.setShowMenuBar(false);
	}
	
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		List<String> unwantedItems = Arrays.asList(
			"org.eclipse.ui.openLocalFile", 
			"converstLineDelimitersTo", 
			"org.eclipse.ui.cheatsheets.actions.CheatSheetHelpMenuAction",
			"org.eclipse.debug.ui.actions.BreakpointTypesContribution" ,
			"ExternalToolsGroup" ,
			"org.eclipse.ui.externaltools.ExternalToolMenuDelegateMenu" ,
			"navigate" ,
			"org.eclipse.search.menu", 
			"org.eclipse.ui.run",
			"org.eclipse.oomph.setup.editor.perform",
			"org.eclipse.equinox.p2.ui.sdk.update",
			"org.eclipse.equinox.p2.ui.sdk.install",
			"org.eclipse.equinox.p2.ui.sdk.installationDetails",
			"org.eclipse.epp.mpc.ui.command.showMarketplaceWizard",
			"org.eclipse.ui.actions.showKeyAssistHandler"
		);
		
        IMenuManager menuManager = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
        removeUnwantedItems(unwantedItems, menuManager);
	        
	    }
	    
    private void removeUnwantedItems(final List<String> unwantedItems, final IMenuManager menuManager) {
		IContributionItem[] items = menuManager.getItems();
		
		for (IContributionItem item : items) {
		    if (item instanceof IMenuManager) {
			removeUnwantedItems(unwantedItems, (IMenuManager) item);
		    }
		    
		    if (unwantedItems.contains(item.getId())) {
			menuManager.remove(item);
		    }
		}

	}
}
