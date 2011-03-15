/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of extValidation
 * 
 * extEIELForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * extEIELForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extValidation.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class ProgressBarDialog extends gvWindow implements IWindowListener,
		PropertyChangeListener {

	SwingWorker sw;
	JProgressBar progressBar;

	public ProgressBarDialog(SwingWorker worker) {
		super(180, 40, false, true);
		this.sw = worker;
		sw.addPropertyChangeListener(this);
		sw.execute();
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		setGUI(progressBar);
	}

	private void setGUI(JProgressBar progressBar) {
		JPanel myPanel = new JPanel();
		myPanel.add(progressBar);
		JButton cancelButton = new JButton(PluginServices.getText(this,
				"cancel"));
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				close();
			}

		});
		this.add(myPanel);
		this.add(cancelButton);

		setTitle("Validando...");

	}

	public void windowActivated() {

	}

	public void windowClosed() {
		sw.cancel(true);
	}

	public WindowInfo getWindowInfo() {
		return viewInfo;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getNewValue();
		if (obj instanceof Integer) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		} else if (obj.equals(SwingWorker.StateValue.DONE)) {
			close();
		}
	}

}
