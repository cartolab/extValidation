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

package es.udc.cartolab.gvsig.eielvalidation.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class gvWindow extends JPanel implements IWindow {

	protected WindowInfo viewInfo = null;
	private boolean INITIAL_SIZE = false;
	private int width = 400;
	private int height = 400;
	int code;
	boolean modal;

	public gvWindow() {
		this(400, 400, true);
	}

	public gvWindow(int width, int height) {
		this(width, height, true);
	}

	public gvWindow(int width, int height, boolean resizable) {
		this(width, height, true, false);
	}

	public gvWindow(int width, int height, boolean resizable, boolean modal) {
		INITIAL_SIZE = true;
		this.modal = modal;
		if (modal) {
			code = WindowInfo.MODALDIALOG;
		} else {
			code = WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE;
		}
		if (resizable) {
			code = code | WindowInfo.RESIZABLE;
		}
		viewInfo = new WindowInfo(code);
		viewInfo.setWidth(width);
		viewInfo.setHeight(height);
		this.width = width;
		this.height = height;
	}

	public WindowInfo getWindowInfo() {
		if (INITIAL_SIZE) {
			if (viewInfo == null) {
				viewInfo = new WindowInfo(code);
			}
			viewInfo.setHeight(height);
			viewInfo.setWidth(width);
		} else {
			JFrame aux = new JFrame();
			aux.add(this);
			aux.pack();
			if (viewInfo == null) {
				viewInfo = new WindowInfo(code);
			}
			viewInfo.setHeight(aux.getHeight());
			viewInfo.setWidth(aux.getWidth());
			aux.remove(this);
			aux.dispose();
		}
		return viewInfo;
	}

	public void setTitle(String title) {
		viewInfo.setTitle(title);
	}

	public Object getWindowProfile() {
		return null;
	}

	public void open() {
		if (!modal) {
			PluginServices.getMDIManager().addCentredWindow(this);
		} else {
			PluginServices.getMDIManager().addWindow(this);
		}
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}
}
