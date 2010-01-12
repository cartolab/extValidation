package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.ConnectionJDBC;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EIELValidationPanel extends gvWindow implements TableModelListener, ActionListener{

	private final String ALL_COUNCILS = "** Todos **";

	private String TYPE_OF_VALIDATION = "VOL";

	private FormPanel formBody;

	public final String ID_COUNCILCB = "councilCB";
	private JComboBox councilCB;

	public final String ID_SELECTLA = "selectLA";
	private JLabel selectLA;

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
		councilCB.addItem(ALL_COUNCILS);
		for (int i = 0; i < councils.length; i++){
			councilCB.addItem(councils[i]);
		}

		// VALIDATIONS TABLE		
		DefaultTableModel model = new ValidationTableModel();
		validationTB.setModel(model);
		String[] columnNames = {"NUM", "COD", "GR", "Descripcion"};

		model.setRowCount(0);
		validationTB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		validationTB.setRowSelectionAllowed(true);
		validationTB.setColumnSelectionAllowed(false);

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
		validationTB.getColumnModel().getColumn(0).setMaxWidth(35);
		validationTB.getColumnModel().getColumn(1).setHeaderValue(columnNames[1]);
		validationTB.getColumnModel().getColumn(1).setMinWidth(100);
		validationTB.getColumnModel().getColumn(1).setMaxWidth(110);
		validationTB.getColumnModel().getColumn(2).setHeaderValue(columnNames[2]);
		validationTB.getColumnModel().getColumn(2).setMaxWidth(20);
		validationTB.getColumnModel().getColumn(3).setHeaderValue(columnNames[3]);

		validationTB.repaint();

		//Table content

		try {
			//String[][] tableContent = dbs.getTable("validacion_consultas", "eiel_aplicaciones", "1 = 1 AND codigo ORDER BY codigo");
			//[NachoV] Now only retrieves MAP validations
			String[][] tableContent = dbs.getTable("validacion_consultas", "eiel_aplicaciones", "codigo SIMILAR TO '"+
					TYPE_OF_VALIDATION +"%'ORDER BY codigo");

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

		validationTB.getModel().addTableModelListener(this);
		refreshSelectCount();
	}

	private void refreshSelectCount(){
		String text = "Validaciones   ";
		int count = 0;
		DefaultTableModel model = (DefaultTableModel) validationTB.getModel();
		for (int i = 0; i < model.getRowCount(); i++){
			Object value = model.getValueAt(i, 0);
			if (value instanceof Boolean && (Boolean)value){
				count++;
			}
		}
		if (count == 1) {
			selectLA.setText(text + "(" + count + " seleccionada)");
		} else {
			selectLA.setText(text + "(" + count + " seleccionadas)");
		}
	}

	private void changeValidationSets(boolean b){
		DefaultTableModel model = (DefaultTableModel) validationTB.getModel();
		for (int i = 0; i < model.getRowCount(); i++){
			model.setValueAt(b, i, 0);
		}
	}

	public void initWidgets() {
		councilCB = ((JComboBox)formBody.getComponentByName( ID_COUNCILCB));
		//councilCB.setEditable(true);
		//councilCB.removeAllItems();
		selectLA = ((JLabel) formBody.getComponentByName(ID_SELECTLA));
		validationTB = ((JTable)formBody.getComponentByName( ID_VALIDATIONTB));
		validationTB.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2){
					JTable target = (JTable)e.getSource();
					int rowIndex = target.getSelectedRow();
					// Codigo are on column = 1 on the JTable qPanel
					String code = (String) target.getModel().getValueAt(rowIndex, 1);
					EIELValidationSQLPanel sqlPanel = new EIELValidationSQLPanel(code);
					PluginServices.getMDIManager().addWindow(sqlPanel);
				}
			}
		} );
		//initJTable(validationTB, "NNNNNNNNNNNNN");
		//validationTB.addMouseListener(this);
		resultTA = ((JEditorPane)formBody.getComponentByName( ID_RESULTTA));
		resultTA.setEditable(false);
		resultTA.setContentType("text/html");
		selectAllB = ((JButton)formBody.getComponentByName( ID_SELECTALLB));
		selectAllB.addActionListener(this);
		selectCleanB = ((JButton)formBody.getComponentByName( ID_SELECTCLEANB));
		selectCleanB.addActionListener(this);
		selectLoadB = ((JButton)formBody.getComponentByName( ID_SELECTLOADB));
		selectLoadB.setVisible(false);
		selectLoadB.addActionListener(this);
		selectSaveB = ((JButton)formBody.getComponentByName( ID_SELECTSAVEB));
		selectSaveB.setVisible(false);
		selectSaveB.addActionListener(this);
		exportB = ((JButton)formBody.getComponentByName( ID_EXPORTB));
		exportB.setVisible(false);
		exportB.addActionListener(this);
		validateB = ((JButton)formBody.getComponentByName( ID_VALIDATEB));
		validateB.addActionListener(this);
	}

	public void tableChanged(TableModelEvent e) {
		//		int row = e.getFirstRow();
		//		int column = e.getColumn();
		//		DefaultTableModel model = (DefaultTableModel)e.getSource();
		//		System.out.println(model.getValueAt(row, column));
		refreshSelectCount();
	}

	private void executeValidations() {
		// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.
		String OLD_SCHEMA = "EIEL_MAP_MUNICIPAL";
		String NEW_SCHEMA = "eiel_map_municipal";

		StringBuffer sf = new StringBuffer();
		try {
			DBSession dbs = DBSession.getCurrentSession();
			String council = (String)councilCB.getSelectedItem();
			if (council.equals(ALL_COUNCILS)){
				//TODO
				DefaultTableModel model = (DefaultTableModel) validationTB.getModel();
				for (int i = 0; i < model.getRowCount(); i++){
					Object isChecked = model.getValueAt(i, 0);
					if (isChecked instanceof Boolean && (Boolean)isChecked){
						// Get CODE of the validation
						String code = (String) model.getValueAt(i, 1);
						String description = (String) model.getValueAt(i,3);
						sf.append("<h4 style=\"color: blue\">" + code + "  -  " + description + "</h4>");

						String[][] tableContent = dbs.getTable("validacion_consultas", 
								"eiel_aplicaciones", 
								"codigo = '"+ code + "'");
						String query = tableContent[0][1];
						// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.						
						query = query.replaceAll(OLD_SCHEMA, NEW_SCHEMA);

						Connection con = dbs.getJavaConnection();
						Statement stat = con.createStatement();

						System.out.println(query);

						ResultSet rs = stat.executeQuery(query);
						rs.next();
						int row = rs.getRow();
						

						//COPY FROM DBSession.getTable()
						String text = "";
						DatabaseMetaData metadataDB = con.getMetaData();
						ResultSet columns = metadataDB.getColumns(null, null, "validacion_consultas", "%");
						ArrayList<String> fieldNames = new ArrayList<String>();
						
						/*while (columns.next()) {
							fieldNames.add(columns.getString("Column_Name"));
						}*/
						//while (rs.next()) {
						int aux = fieldNames.size();
							for (int j=0; j<fieldNames.size(); j++) {
								String val = rs.getString(fieldNames.get(j));
								//if (val == null || val.compareTo("")==0) {
									//val = " ";
								//}
								text = text + val + "|";
							}
							text = text + "|#|";
							//text = text + rs.getString(fieldNames[fieldNames.length-1]);
						//}
						rs.close();
						
						if (row == 0) {
							sf.append("<p style=\"color: green\">" + "Validation OK" + "</p>");	
						}else {
							sf.append("<p style=\"color: red\">" + rs.getString(1) + "</p>");
							sf.append("<p style=\"color: red\">" + text + "</p>");
						}	
					}
				}
			} else {
				//TODO
			}			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			sf.append("<h2 style=\"color: red\"> ERROR: " + e1.getMessage() + "</h2>");
			e1.printStackTrace();
		}
		sf.append("<hr>");
		resultTA.setText(sf.toString());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectAllB){
			changeValidationSets(true);
			return;
		}

		if (e.getSource() == selectCleanB){
			changeValidationSets(false);
			return;
		}

		if (e.getSource() == selectLoadB){
			//TODO
			return;
		}

		if (e.getSource() == selectSaveB){
			//TODO
			return;
		}

		if (e.getSource() == validateB){
			//TODO
			executeValidations();
			return;

		}

		if (e.getSource() == exportB){
			//TODO
			return;
		}
	}
}
