package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

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
		super(600, 500);
		formBody = new FormPanel("validationGUI.jfrm");
		formBody.setVisible(true);
		this.add(formBody, BorderLayout.CENTER);
		this.setTitle("Validaciones");
		initWidgets();
		initValues();
	}

	public void initValues() {
		
		DBSession dbs = DBSession.getCurrentSession();
		String[] councils = null;
		try {
			councils = dbs.getDistinctValues("municipio", "denominaci");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//TODO Add "Todos" to text.properties file
		councilCB.addItem("Todos");
		for (int i = 0; i < councils.length; i++){
			councilCB.addItem(councils[i]);
		}
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
