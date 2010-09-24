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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ValidationTableCellRenderer extends DefaultTableCellRenderer {


	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component cmp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


		if (column == 3) {
			String toolTipText = value.toString();
			((JLabel) cmp).setToolTipText(toolTipText);
		}

		return cmp;
	}

}
