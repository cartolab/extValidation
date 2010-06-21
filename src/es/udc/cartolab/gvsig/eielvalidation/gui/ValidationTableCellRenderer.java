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
