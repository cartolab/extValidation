package es.udc.cartolab.gvsig.eielvalidation.gui;

/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 *
 * This file is part of extEIELForms
 *
 * extEIELForms is based on the forms application of GisEIEL <http://giseiel.forge.osor.eu/>
 * devoloped by Laboratorio de Bases de Datos (Universidade da Coruña)
 *
 * extEIELForms is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extEIELForms is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extEIELForms.
 * If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Arrays;
import java.util.HashSet;

import javax.swing.table.DefaultTableModel;

public class ResultTableModel extends DefaultTableModel {

	HashSet<String> editableColumns = null;
	HashSet<String> invisibleColumns = null;

	private final String code;
	private final String description;

	private boolean validationFailure = false;
	private String tableNameFailure = "";
	private String errorMessage = "";
	private boolean error = false;

	public ResultTableModel(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public final String getCode() {
		return code;
	}

	public final String getDescription() {
		return description;
	}

	public final boolean getValidationFailure() {
		return validationFailure;
	}

	public final void setValidationFailure(boolean validationFailure) {
		this.validationFailure = validationFailure;
	}

	public void setTableNameFailure(String tableNameFailure) {
		this.tableNameFailure = tableNameFailure;
	}

	public String getTableNameFailure() {
		return tableNameFailure;
	}

	public void setErroMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public final boolean getError() {
		return error;
	}

	public final void setError(boolean error) {
		this.error = error;
	}

	// Setters to define some features of the TableModel
	/**
	 * define which columns of the table are editable
	 * @param editableColumns Name that identifies the column. This is the name of the field
	 * + in the recordset
	 */
	public void setEditableColumns(String[] editableColumns) {
		if (editableColumns!=null || editableColumns.length > 0) {
			this.editableColumns = new HashSet<String>(Arrays.asList(editableColumns));
		}
	}


	@Override
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}


	/**
	 * Define which columns of the table exists in the model but not
	 * in the view
	 * @param invisibleColumns Name that identifies the column. This is the name of the field
	 * + in the recordset
	 */
	public void setInvisibleColumns (String[] invisibleColumns) {
		if (invisibleColumns!=null || invisibleColumns.length > 0) {
			this.invisibleColumns = new HashSet<String>(Arrays.asList(invisibleColumns));
		}
	}



	// Methods overided of the AbstractTableModel
	@Override
	public boolean isCellEditable (int row, int col) {
		boolean editable = false;

		if (this.editableColumns == null || this.editableColumns.contains(getColumnName(col))) {
			editable = true;
		}

		return editable;
	}

	public String getHTML() {

		int columnCount = getColumnCount();

		String html = "<table border=\"1\"><tr>";

		for (int i=0; i<columnCount; i++) {
			html = html + "<td>" + getColumnName(i) + "</td>";
		}
		html = html + "</tr>";

		for (int row=0; row < getRowCount(); row++) {
			html = html + "<tr>";
			for (int column=0; column<columnCount; column++) {
				html = html + "<td>" + getValueAt(row, column) + "</td>";
			}
			html = html + "</tr>";
		}

		html = html + "</table>";


		return html;
	}

}
