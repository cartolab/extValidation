package es.udc.cartolab.gvsig.eielvalidation.gui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JTable;

import com.jeta.forms.components.panel.FormPanel;

public class EIELValidationPanel extends gvWindow {

    private FormPanel formBody;
	
	public final String ID_COUNCILCB = "councilCB";
	private JComboBox councilCB;

	public final String ID_VALIDATIONTB = "validationTB";
	private JTable validationTB;

	public final String ID_RESULTTA = "resultTA";
	private JEditorPane resultTA;

	public final String ID_SELECTALLB = "selectAllB";
	private JButton selectAllB;

	public final String ID_SELECTCLEANB = "selectCleanB";
	private JButton selectCleanB;

	public final String ID_SELECTLOADB = "selectLoadB";
	private JButton selectLoadB;

	public final String ID_SELECTSAVEB = "selectSaveB";
	private JButton selectSaveB;

	public final String ID_EXPORTB = "exportB";
	private JButton exportB;

	public final String ID_VALIDATEB = "validateB";
	private JButton validateB;


	public EIELValidationPanel(){
		java.io.File file = new java.io.File("validationGUI.jfrm");
		System.out.println(file.exists() + "  " + file.getAbsolutePath());
		//formBody = new FormPanel("validationGUI.jfrm");
		formBody = new FormPanel("/home/uve/openGisEIEL/_fwAndami/gvSIG/extensiones/es.udc.cartolab.eielvalidation/validationGUI.jfrm");
		initWidgets();
		this.add(formBody);
	}

	public void initWidgets() {
		councilCB = ((JComboBox)formBody.getComponentByName( ID_COUNCILCB));
		//councilCB.setEditable(true);
		//councilCB.removeAllItems();
		validationTB = ((JTable)formBody.getComponentByName( ID_VALIDATIONTB));
		//initJTable(validationTB, "NNNNNNNNNNNNN");
		//validationTB.addMouseListener(this);
		resultTA = ((JEditorPane)formBody.getComponentByName( ID_RESULTTA));
		selectAllB = ((JButton)formBody.getComponentByName( ID_SELECTALLB));
		selectCleanB = ((JButton)formBody.getComponentByName( ID_SELECTCLEANB));
		selectLoadB = ((JButton)formBody.getComponentByName( ID_SELECTLOADB));
		selectSaveB = ((JButton)formBody.getComponentByName( ID_SELECTSAVEB));
		exportB = ((JButton)formBody.getComponentByName( ID_EXPORTB));
		validateB = ((JButton)formBody.getComponentByName( ID_VALIDATEB));
	}

}
