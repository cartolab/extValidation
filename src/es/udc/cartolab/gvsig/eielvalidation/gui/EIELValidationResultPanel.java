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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;

public class EIELValidationResultPanel extends gvWindow implements ActionListener {

	private JEditorPane resultTA;
	private JButton exportB;

	public EIELValidationResultPanel() {
		this(null);
	}

	public EIELValidationResultPanel(String council) {

		super(800, 500, false);
		if (council != null && !council.equals("")) {
			setTitle("Resultado de la validación de " + council);
		} else {
			setTitle("Resultado de la validación");
		}

		MigLayout layout = new MigLayout("inset 0, align center",
				"[grow]",
		"[grow][]");

		setLayout(layout);

		resultTA = new JEditorPane();
		resultTA.setEditable(false);
		resultTA.setContentType("text/html");
		JScrollPane scrollPane = new JScrollPane(resultTA);
		JPanel panel = new JPanel();
		exportB = new JButton("Exportar");
		exportB.addActionListener(this);
		panel.add(exportB);

		add(scrollPane, "growx, growy, wrap");
		add(panel, "shrink, align right" );

	}

	public void setResult(String result) {
		resultTA.setText(result);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exportB){
			JFileChooser fc = new JFileChooser();
			int answer = fc.showSaveDialog(fc);
			if (answer == JFileChooser.APPROVE_OPTION) {
				File fFile=fc.getSelectedFile();
				String filePath = fFile.getPath();
				FileOutputStream fo;
				try {
					fo = new FileOutputStream(filePath);
					PrintStream ps=new PrintStream(fo);
					ps.println(resultTA.getText());
					ps.close();
					fo.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PluginServices.getMDIManager().closeWindow(this);
			}
			return;
		}
	}

}
