package Iscte.GrupoR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenFileCSV {

    public static void main(String[] args) {
//    	Configuracao da janela 
        JFrame frame = new JFrame("Ler Arquivo CSV Manualmente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
//Painel e botão
        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openButton = new JButton("Escolher Arquivo CSV");
        panel.add(openButton);

//        Ação do botão
        
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//            	Escolhere ficheiro, ver se é válido 
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
//                    	Leitura do csv
                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                        // Defina a ordem esperada das colunas
                        String[] expectedColumns = {
                        	    "Curso","Unidade Curricular"
                        	    ,"Turno","Turma","Inscritos no turno"
                        	    ,"Dia da semana","Hora inicio da aula",
                        	    "Hora fim da aula","Data da aula" , "Caracteristicas da sala pedida para a aula",
                        	    "Sala atribuida a aula"
                        	};
                        String firstLine = reader.readLine();
                        if (firstLine != null) {
                            String[] actualColumns = firstLine.split("[,;]");

                            Map<String, Integer> columnIndices = new HashMap<>();
                            for (int i = 0; i < actualColumns.length; i++) {
                                columnIndices.put(actualColumns[i].trim(), i);
                            }

                            // Organize as colunas com base na ordem esperada
                            String[] orderedColumns = new String[expectedColumns.length];
                            for (int i = 0; i < expectedColumns.length; i++) {
                                orderedColumns[i] = actualColumns[columnIndices.get(expectedColumns[i])];
                            }

                            System.out.println(Arrays.toString(orderedColumns));

                            List<String[]> records = new ArrayList<>();

                            String line;
                            int linesToRead = 50;
                            int currentLine = 0;

                            while ((line = reader.readLine()) != null && currentLine < linesToRead) {
                                String[] fields = line.split("[,;]");
                                // Organize os campos de acordo com a ordem esperada
                                String[] orderedFields = new String[expectedColumns.length];
                                for (int i = 0; i < expectedColumns.length; i++) {
                                    orderedFields[i] = fields[columnIndices.get(expectedColumns[i])];
                                }
                                records.add(orderedFields);
                                currentLine++;
                            }

                            for (String[] record : records) {
                                System.out.println(Arrays.toString(record));
                            }

                            reader.close();
                            
                         // Verifique a ordem das colunas na consola, atraves do usoda funcao da  classe columnOrderChecker
                            ColumnOrderChecker.checkColumnOrder(records, expectedColumns);

                            JButton openWebButton = new JButton("Abrir Tabela na Web");
                            panel.add(openWebButton);

                            openWebButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    openWebPageWithTable(orderedColumns, records);
                                }
                            });
                        
                            frame.revalidate();
                            
                        
                    }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                // Fecha a janela após carregar o arquivo
//                frame.dispose();
            }
        });
    

        frame.setVisible(true);
    }
    private static void openWebPageWithTable(String[] columns, List<String[]> data) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Tabela de Dados</title></head><body><table border='1'><tr>");
        for (String column : columns) {
            htmlContent.append("<th>").append(column).append("</th>");
        }
        htmlContent.append("</tr>");
        for (String[] row : data) {
            htmlContent.append("<tr>");
            for (String cell : row) {
                htmlContent.append("<td>").append(cell).append("</td>");
            }
            htmlContent.append("</tr>");
        }
        htmlContent.append("</table></body></html>");

        try {
            File tempHtmlFile = File.createTempFile("table", ".html");
            PrintWriter writer = new PrintWriter(tempHtmlFile);
            writer.write(htmlContent.toString());
            writer.close();

            Desktop.getDesktop().browse(tempHtmlFile.toURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

    
    
    
    
    
    
    
    
    

