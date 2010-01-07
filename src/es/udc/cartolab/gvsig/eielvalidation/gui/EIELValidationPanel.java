package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

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
		super(800, 500);
		formBody = new FormPanel("validationGUI.jfrm");
		formBody.setVisible(true);
		this.add(formBody, BorderLayout.CENTER);
		this.setTitle("Validaciones");
		initWidgets();
		initValues();
	}

	public void initValues() {
		
		DBSession dbs = DBSession.getCurrentSession();
		
		// COUNCILS CB
		String[] councils = null;
		try {
			councils = dbs.getDistinctValues("municipio", "denominaci");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO Add "Todos" to text.properties file
		councilCB.addItem("** Todos **");
		for (int i = 0; i < councils.length; i++){
			councilCB.addItem(councils[i]);
		}
		
		// VALIDATIONS TABLE		
		DefaultTableModel model = new ValidationTableModel();
		validationTB.setModel(model);
		String[] columnNames = {"NUM", "COD", "GR", "Descripcion"};
		
		model.setRowCount(0);
		
		//TODO Add CHECKBOX, GROUP and SQL? column!!!
		
		TableColumn column00 = new TableColumn();
		((DefaultTableModel)validationTB.getModel()).addColumn(column00);
		
		TableColumn column01 = new TableColumn();
		((DefaultTableModel)validationTB.getModel()).addColumn(column01);
		
		TableColumn column02 = new TableColumn();
		((DefaultTableModel)validationTB.getModel()).addColumn(column02);
		
		TableColumn column03 = new TableColumn();
		((DefaultTableModel)validationTB.getModel()).addColumn(column03);
		
		validationTB.getColumnModel().getColumn(0).setHeaderValue(columnNames[0]);
		validationTB.getColumnModel().getColumn(0).setMaxWidth(50);
		validationTB.getColumnModel().getColumn(1).setHeaderValue(columnNames[1]);
		validationTB.getColumnModel().getColumn(1).setMinWidth(220);
		validationTB.getColumnModel().getColumn(1).setMinWidth(250);
		validationTB.getColumnModel().getColumn(2).setHeaderValue(columnNames[2]);
		validationTB.getColumnModel().getColumn(2).setMaxWidth(20);
		validationTB.getColumnModel().getColumn(3).setHeaderValue(columnNames[3]);

		validationTB.repaint();
		
		//Table content
		
		try {
			String[][] tableContent = dbs.getTable("validacion_consultas", "eiel_aplicaciones", "1 = 1 ORDER BY codigo");
			
			int numRows = 0;
			for (int i=0; i<tableContent.length; i++) {
					Object[] row = new Object[4];
					row[0] = new Boolean(true);
					// 0: Codigo
					row[1] = tableContent[i][0];
					// 1: consulta
					// 2: grupo
					row[2] = tableContent[i][2];
					// 3: nombre validacion
					row[3] = tableContent[i][3];
					model.addRow(row);
				numRows++;
			}
			if (numRows==0) {
				String[] row = new String[3];
				for (int i=0; i<row.length; i++) {
					row[i] = "";
				}
				model.addRow(row);
			}
			model.fireTableRowsInserted(0, model.getRowCount()-1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
