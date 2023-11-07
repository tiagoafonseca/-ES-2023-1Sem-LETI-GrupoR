package Iscte.GrupoR;

import java.util.List;

import javax.swing.JOptionPane;

public class ColumnOrderChecker {
    public static void checkColumnOrder(List<String[]> records, String[] expectedColumns) {
        boolean columnsInOrder = true;

        for (String[] record : records) {
            for (int i = 0; i < expectedColumns.length; i++) {
                if (record[i].equals(expectedColumns[i])) {
                    columnsInOrder = true;
                    break;
                }
            }
        }

        if (columnsInOrder) {
            JOptionPane.showMessageDialog(null, "As colunas estão na ordem correta na consola.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "As colunas não estão na ordem correta na consola.", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

