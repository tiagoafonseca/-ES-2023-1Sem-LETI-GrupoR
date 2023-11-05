package Iscte.GrupoR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ColumnOrderChanger {

	public static String[] changeColumnOrder(List<String[]> records, String[] columnNames) {
		String[] orderedColumns = new String[columnNames.length];
		
		for(int i = 0; i < columnNames.length; i++) {
			String[] remainingColumns = new String[columnNames.length - i];
			int remainingColumnsIndex = 0;
			for(int j = 0; j < columnNames.length; j++) {
				remainingColumns[remainingColumnsIndex] = columnNames[j];
				remainingColumnsIndex++;
			}
			
			String selectedColumn = (String) JOptionPane.showInputDialog(null,
                    "Escolha a próxima coluna a ser exibida:",
                    "Selecionar Coluna",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    remainingColumns,
                    remainingColumns[0]);
			
			orderedColumns[i] = selectedColumn;
			for(int k = 0; k < remainingColumns.length; k++) {
				if(remainingColumns[k] == selectedColumn) {
					remainingColumns[k] = null;
				}
			}
			
		}
		return orderedColumns;
	}

}
