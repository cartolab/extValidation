package es.udc.cartolab.gvsig.eielvalidation;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.udc.cartolab.gvsig.eielvalidation.gui.EIELValidationPanel;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EIELValidationExtension extends Extension {

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		EIELValidationPanel validationPanel = new EIELValidationPanel();
		PluginServices.getMDIManager().addWindow(validationPanel);
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		if (DBSession.getCurrentSession() != null) {
			return true;
		}else {
			return false;
		}
	}

	public boolean isVisible() {
		return true;
	}

}
