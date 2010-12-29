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

import javax.swing.table.DefaultTableModel;

public class ValidationTableModel extends DefaultTableModel{

	public Class getColumnClass(int index){
		if(index == 0){
			return Boolean.class;
		} else {
			return super.getColumnClass(index);
		}
	}

	public boolean isCellEditable(int row, int col){
		if (col == 0) {
			return true;
		} else {
			return false;
		}
	}

}

