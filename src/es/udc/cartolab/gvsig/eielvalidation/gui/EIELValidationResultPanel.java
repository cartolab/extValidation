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

		super(800, 500, false);

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
			//TODO
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(fc);
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
			return;
		}
	}

}
