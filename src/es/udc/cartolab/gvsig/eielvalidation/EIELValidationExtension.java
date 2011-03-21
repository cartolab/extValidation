/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of EIEL Validation
 * 
 * EIEL Validation is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, either 
 * version 3 of the License, or any later version.
 * 
 * EIEL Validation is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with EIEL Validation
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.eielvalidation;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

import es.udc.cartolab.gvsig.eielvalidation.gui.EIELValidationPanel;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EIELValidationExtension extends Extension {

	public void execute(String actionCommand) {
		EIELValidationPanel validationPanel = new EIELValidationPanel();
		validationPanel.open();
	}

	public void initialize() {
//		About about = (About) PluginServices.getExtension(About.class);
//		FPanelAbout panelAbout = about.getAboutPanel();
//		java.net.URL aboutURL = this.getClass().getResource("/about.html");
//		panelAbout.addAboutUrl("Validation", aboutURL);
	}

	public boolean isEnabled() {
		if (DBSession.getCurrentSession() != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isVisible() {
		return true;
	}

}
