package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.iver.andami.PluginServices;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EIELValidationPanel extends gvWindow implements TableModelListener, ActionListener, PropertyChangeListener{

	private final String ALL_COUNCILS = "** Todos **";

	private String TYPE_OF_VALIDATION = "VOL";

	private FormPanel formBody;

	public final String ID_COUNCILCB = "councilCB";
	private JComboBox councilCB;

	public final String ID_SELECTLA = "selectLA";
	private JLabel selectLA;

	public final String ID_VALIDATIONTB = "validationTB";
	private JTable validationTB;

	//	public final String ID_RESULTTA = "resultTA";
	//	private JEditorPane resultTA;

	public final String ID_SELECTALLB = "selectAllB";
	private JButton selectAllB;

	public final String ID_SELECTCLEANB = "selectCleanB";
	private JButton selectCleanB;

	public final String ID_SELECTLOADB = "selectLoadB";
	private JButton selectLoadB;

	public final String ID_SELECTSAVEB = "selectSaveB";
	private JButton selectSaveB;

	//	public final String ID_EXPORTB = "exportB";
	//	private JButton exportB;

	public final String ID_VALIDATEB = "validateB";
	private JButton validateB;

	private JProgressBar progressBar;
	private gvWindow progressBarDialog;

	private class ValidateTask extends SwingWorker<String, Void> {

		@Override
		protected String doInBackground() throws Exception {

			// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.
			String OLD_SCHEMA = "EIEL_MAP_MUNICIPAL";
			String NEW_SCHEMA = "eiel_map_municipal";

			int validationsFail = 0;

			StringBuffer sf = new StringBuffer();

			try {
				DBSession dbs = DBSession.getCurrentSession();
				String council = (String)councilCB.getSelectedItem();

				//TODO
				DefaultTableModel model = (DefaultTableModel) validationTB.getModel();

				setProgress(0);

				//get number of validations
				int total=0;
				for (int i = 0; i < model.getRowCount(); i++){
					Object isChecked = model.getValueAt(i, 0);
					if ((isChecked instanceof Boolean) && (Boolean)isChecked){
						total++;
					}
				}

				//check validations
				int count=0;
				for (int i = 0; i < model.getRowCount(); i++){
					Object isChecked = model.getValueAt(i, 0);
					if ((isChecked instanceof Boolean) && (Boolean)isChecked){
						// Get CODE of the validation
						String code = (String) model.getValueAt(i, 1);
						String description = (String) model.getValueAt(i,3);
						sf.append("<h4 style=\"color: blue\">" + code + "  -  " + description + "</h4>");

						String[][] tableContent = dbs.getTable("validacion_consultas",
								"eiel_aplicaciones",
								"codigo = '"+ code + "'");
						String query = tableContent[0][1];
						String whereC = tableContent[0][8];
						String codigoConsulta =  tableContent[0][0];
						// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.
						query = query.replaceAll(OLD_SCHEMA, NEW_SCHEMA);

						Connection con = dbs.getJavaConnection();
						Statement stat = con.createStatement();

						if (!council.equals(ALL_COUNCILS)){
							//sustituir [[WHERE]] por el valor de la columna where y el codigo adecuado
							whereC = whereC.replaceAll("\\[\\[DENOMINACI\\]\\]", council.toUpperCase());
							//						whereC = "	  and municipio = (select municipio	from eiel_map_municipal.municipio where upper(denominaci)='" + council +"' limit 1)";
							query = query.replaceAll("\\[\\[WHERE\\]\\]", whereC);
						} else {
							query = query.replaceAll("\\[\\[WHERE\\]\\]", "");
						}
						System.out.println(codigoConsulta + ": " + query);

						ResultSet rs = stat.executeQuery(query);
						//rs.next();
						int row = 0;

						//COPY FROM DBSession.getTable()
						String text = "<table border=\"1\"><tr>";
						DatabaseMetaData metadataDB = con.getMetaData();
						ResultSet columns = metadataDB.getColumns(null, null, "validacion_consultas", "%");
						ArrayList<String> fieldNames = new ArrayList<String>();

						ResultSetMetaData metaData = rs.getMetaData();
						int numColumns = metaData.getColumnCount();
						String tableName = metaData.getTableName(1);

						/*while (columns.next()) {
								fieldNames.add(columns.getString("Column_Name"));
							}*/

						//Getting the field names of the table
						for (int k=0; k<numColumns; k++)
						{
							text = text + "<td>" + metaData.getColumnLabel(k+1) + "</td>";
						}
						text = text + "</tr>";

						//Getting values of the rows that have failed
						while (rs.next()) {
							row = rs.getRow();
							validationsFail = validationsFail + 1;
							text = text + "<tr>";
							for (int j=1; j<=numColumns; j++) {
								String val = rs.getString(j);
								text = text + "<td>" + val + "</td>";
							}
							text = text + "</tr>";
						}
						text = text + "</table>";
						rs.close();

						if (row == 0) {
							sf.append("<p style=\"color: green\">" + PluginServices.getText(this, "validationOK")  + "</p>");
						}else {
							sf.append("<p style=\"color: red\">" + PluginServices.getText(this, "validationFail") + " " + tableName + "</p>");
							sf.append(text);
						}
						count++;
						setProgress(count*100/total);
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				sf.append("<h2 style=\"color: red\"> ERROR: " + e1.getMessage() + "</h2>");
				e1.printStackTrace();
			}
			if (validationsFail == 1) {
				sf.append("<h2 style=\"color: red\">" + (validationsFail) + " " + PluginServices.getText(this, "validationFailOne")  + "</h2>");
				sf.append("<hr>");
			} else if (validationsFail > 1) {
				sf.append("<h2 style=\"color: red\">" + (validationsFail-1) + " " + PluginServices.getText(this, "validationFailNumber")  + "</h2>");
				sf.append("<hr>");
			} else {
				sf.append("<hr>");
			}

			//			resultTA.setText(sf.toString());
			return sf.toString();
		}

		@Override
		public void done() {
			try {
				String str = get();
				PluginServices.getMDIManager().closeWindow(progressBarDialog);
				EIELValidationResultPanel resultPanel;
				if (councilCB.getSelectedIndex() > 0) {
					resultPanel = new EIELValidationResultPanel(councilCB.getSelectedItem().toString());
				} else {
					resultPanel = new EIELValidationResultPanel();
				}
				PluginServices.getMDIManager().addCentredWindow(resultPanel);
				resultPanel.setResult(str);
				PluginServices.getMDIManager().restoreCursor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public EIELValidationPanel(){
		super(800, 500);
		formBody = new FormPanel("forms/validationGUI.jfrm");
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
		validationTB.getColumnModel().getColumn(3).setCellRenderer(new ValidationTableCellRenderer());

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
			if ((value instanceof Boolean) && (Boolean)value){
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
			@Override
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
		//		resultTA = ((JEditorPane)formBody.getComponentByName( ID_RESULTTA));
		//		resultTA.setEditable(false);
		//		resultTA.setContentType("text/html");
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
		//		exportB = ((JButton)formBody.getComponentByName( ID_EXPORTB));
		//		exportB.setVisible(true);
		//		exportB.addActionListener(this);
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

		//create progress bar
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		JPanel panel = new JPanel();
		panel.add(progressBar);

		progressBarDialog = new gvWindow(180, 30);
		progressBarDialog.add(panel);
		progressBarDialog.setTitle("Validando...");
		PluginServices.getMDIManager().addCentredWindow(progressBarDialog);

		//start validation task
		PluginServices.getMDIManager().setWaitCursor();
		ValidateTask vt = new ValidateTask();
		vt.addPropertyChangeListener(this);
		vt.execute();

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

		//		if (e.getSource() == exportB){
		//			//TODO
		//			JFileChooser fc = new JFileChooser();
		//			fc.showSaveDialog(fc);
		//			File fFile=fc.getSelectedFile();
		//			String filePath = fFile.getPath();
		//			FileOutputStream fo;
		//			try {
		//				fo = new FileOutputStream(filePath);
		//				PrintStream ps=new PrintStream(fo);
		//				ps.println(resultTA.getText());
		//				ps.close();
		//				fo.close();
		//			} catch (IOException e1) {
		//				// TODO Auto-generated catch block
		//				e1.printStackTrace();
		//			}
		//			return;
		//		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		Object obj = evt.getNewValue();
		if (obj instanceof Integer) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
}
