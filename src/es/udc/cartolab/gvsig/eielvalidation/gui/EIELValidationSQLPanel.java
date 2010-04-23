package es.udc.cartolab.gvsig.eielvalidation.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JEditorPane;

import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EIELValidationSQLPanel extends gvWindow implements ActionListener {

	private String validation_code = null;

	private FormPanel formBody = null;

	public final String ID_DESCRIPTIONTA = "descriptionTA";
	private JEditorPane descriptionTA;

	public final String ID_SQLTA = "sqlTA";
	private JEditorPane sqlTA;

	public final String ID_OKB = "okB";
	private JButton okB;


	public EIELValidationSQLPanel(String code){
		super(500, 400);
		formBody = new FormPanel("forms/validationSQL.jfrm");
		formBody.setVisible(true);
		this.validation_code = code;
		this.add(formBody, BorderLayout.CENTER);
		this.setTitle("SQL Panel");
		initWidgets();
		initValues();
		getValidations();
	}

	public void initValues() {

		DBSession dbs = DBSession.getCurrentSession();

	}

	public void initWidgets() {
		descriptionTA = ((JEditorPane)formBody.getComponentByName( ID_DESCRIPTIONTA));
		descriptionTA.setEditable(false);
		//resultTA.setContentType("text/html");

		sqlTA = ((JEditorPane)formBody.getComponentByName( ID_SQLTA));
		sqlTA.setEditable(false);
		//resultTA.setContentType("text/html");

		okB = ((JButton)formBody.getComponentByName( ID_OKB));
		okB.addActionListener(this);

	}

	private void getValidations() {
		// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.
		String OLD_SCHEMA = "EIEL_MAP_MUNICIPAL";
		String NEW_SCHEMA = "eiel_map_municipal";

		StringBuffer sf = new StringBuffer();
		try {
			DBSession dbs = DBSession.getCurrentSession();
			String[][] tableContent = dbs.getTable("validacion_consultas",
					"eiel_aplicaciones",
					"codigo = '"+ validation_code + "'");
			String query = tableContent[0][1];
			// [NACHOV] On the LBD all queries refers to OLD_SCHEMA... This is to make a quick replace.
			query = query.replaceAll(OLD_SCHEMA, NEW_SCHEMA);

			Connection con = dbs.getJavaConnection();
			Statement stat = con.createStatement();

			System.out.println(query);

			ResultSet rs = stat.executeQuery(query);

			//COPY FROM DBSession.getTable()
			String text = "";
			DatabaseMetaData metadataDB = con.getMetaData();
			ResultSet columns = metadataDB.getColumns(null, null, "validacion_consultas", "%");
			List<String> fieldNames = new ArrayList<String>();

			while (columns.next()) {
				fieldNames.add(columns.getString("Column_Name"));
			}
			while (rs.next()) {
				for (int j=0; j<fieldNames.size(); j++) {
					String val = rs.getString(fieldNames.get(j));
					if ((val == null) || (val.compareTo("")==0)) {
						val = " ";
					}
					text = text + val + "|";
				}
				text = text + "|#|";
				//text = text + rs.getString(fieldNames[fieldNames.length-1]);
			}
			rs.close();

			sf.append("<h4 style=\"color: blue\">" + validation_code + "</h4>");
			sf.append("<p style=\"color: green\">" + tableContent[0][1] + "</p>");
			sf.append("<p style=\"color: red\">" + text + "</p>");

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			sf.append("<h2 style=\"color: red\"> ERROR: " + e1.getMessage() + "</h2>");
			e1.printStackTrace();
		}
		sf.append("<hr>");
		sqlTA.setText(sf.toString());
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == okB){
			this.setVisible(false);
			return;
		}
	}
}
