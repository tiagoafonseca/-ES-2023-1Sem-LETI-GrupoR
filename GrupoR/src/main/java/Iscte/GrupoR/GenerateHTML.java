package Iscte.GrupoR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class GenerateHTML {
    private static String[] expectedColumns = {
            "Curso", "Unidade Curricular", "Turno", "Turma", "Inscritos no turno",
            "Dia da semana", "Hora inicio da aula", "Hora fim da aula", "Data da aula",
            "Caracteristicas da sala pedida para a aula", "Sala atribuida a aula"
    };

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gerar HTML a partir de Arquivo CSV");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton generateHTMLButton = new JButton("Gerar HTML");
        panel.add(generateHTMLButton);

        generateHTMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = chooseCSVFile();
                if (selectedFile != null) {
                    generateHTML(selectedFile, expectedColumns);
                }
            }
        });

        frame.setVisible(true);
    }

    private static File chooseCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private static void generateHTML(File file, String[] expectedColumns) {
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter("output.html"))) {

            String header = br.readLine();
            String[] columns = header.split(";");

            if (!Arrays.asList(columns).containsAll(Arrays.asList(expectedColumns))) {
                JOptionPane.showMessageDialog(null, "O arquivo CSV não possui todas as colunas esperadas.");
                return;
            }

            // Início da construção da tabela HTML
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table border=\"1\">");

            // Cabeçalho da tabela
            htmlTable.append("<tr>");
            for (String column : expectedColumns) {
                htmlTable.append("<th>").append(column).append("</th>");
            }
            htmlTable.append("</tr>");

            // Dados da tabela
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                htmlTable.append("<tr>");
                for (String value : values) {
                    htmlTable.append("<td>").append(value).append("</td>");
                }
                htmlTable.append("</tr>");
            }

            // Fim da tabela HTML
            htmlTable.append("</table>");

            // Escreve a tabela no arquivo HTML
            bw.write(htmlTable.toString());

            // Adicione a geração do arquivo HTML temporário
            createTempHtmlFile(htmlTable.toString());

            // Adicione a abertura do HTML no navegador
            openHtmlInBrowser();

            JOptionPane.showMessageDialog(null, "HTML gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createTempHtmlFile(String htmlContent) {
        try {
            File htmlFile = new File("temp.html");
            FileWriter writer = new FileWriter(htmlFile);
            writer.write(htmlContent);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void openHtmlInBrowser() {
        try {
            File htmlFile = new File("temp.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

