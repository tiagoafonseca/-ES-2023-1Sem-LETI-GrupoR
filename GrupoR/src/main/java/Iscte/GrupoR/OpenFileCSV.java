package Iscte.GrupoR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class OpenFileCSV {

    private static String[] expectedColumns = {
            "Curso", "Unidade Curricular", "Turno", "Turma", "Inscritos no turno",
            "Dia da semana", "Hora inicio da aula", "Hora fim da aula", "Data da aula",
            "Caracteristicas da sala pedida para a aula", "Sala atribuida a aula"
    };

    public static void main(String[] args) {
        // Configuração da janela
        JFrame frame = new JFrame("Ler Arquivo CSV Manualmente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        // Painel e botões
        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openButton = new JButton("Escolher Arquivo CSV");
        panel.add(openButton);

        JButton setOrderButton = new JButton("Definir Ordem Personalizada");
        panel.add(setOrderButton);

        // Ação do botão para escolher o arquivo CSV
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Permitir que o usuário forneça a ordem desejada das colunas
                File selectedFile = chooseCSVFile();
                if (selectedFile != null) {
                    // Mostra um diálogo de confirmação
                    int option = JOptionPane.showConfirmDialog(null, "As colunas do arquivo são as esperadas?", "Confirmação", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // As colunas são as esperadas, chama o método readCSV como antes
                        readCSV(selectedFile, expectedColumns);
                    } else {
                        // As colunas não são as esperadas, solicita ao usuário a associação
                        String orderInput = JOptionPane.showInputDialog("Digite a ordem desejada das colunas separadas por vírgulas:");

                        if (orderInput != null && !orderInput.trim().isEmpty()) {
                            // Atualizar a ordem esperada das colunas
                            expectedColumns = orderInput.split(",");
                            System.out.println("Nova ordem definida: " + Arrays.toString(expectedColumns));

                            // Chama o método readCSV com a nova ordem definida
                            readCSV(selectedFile, expectedColumns);
                        } else {
                            JOptionPane.showMessageDialog(null, "Você não forneceu uma ordem de colunas válida.");
                        }
                    }
                }
            }
        });

        // Ação do botão para definir uma ordem personalizada
        setOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Permitir que o usuário forneça a ordem desejada das colunas
                File selectedFile = chooseCSVFile();
                if (selectedFile != null) {
                    String orderInput = JOptionPane.showInputDialog("Digite a ordem desejada das colunas separadas por vírgulas:");

                    if (orderInput != null && !orderInput.trim().isEmpty()) {
                        // Atualizar a ordem esperada das colunas
                        expectedColumns = orderInput.split(",");
                        System.out.println("Nova ordem definida: " + Arrays.toString(expectedColumns));

                        // Chama o método readCSV com a nova ordem definida
                        readCSV(selectedFile, expectedColumns);
                    } else {
                        JOptionPane.showMessageDialog(null, "Você não forneceu uma ordem de colunas válida.");
                    }
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


    private static void readCSV(File file, String[] expectedColumns) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            // Lê a linha de cabeçalho
            String header = br.readLine();
            String[] columns = header.split(";"); // Usamos ponto e vírgula como delimitador

            // Imprime as colunas esperadas e encontradas em um formato mais legível
            System.out.println("Colunas Esperadas: " + Arrays.toString(expectedColumns));
            System.out.println("Colunas Encontradas : " + Arrays.toString(columns));

            // Verifica se as colunas esperadas estão presentes
            if (!Arrays.asList(columns).containsAll(Arrays.asList(expectedColumns))) {
                JOptionPane.showMessageDialog(null, "O arquivo CSV não possui todas as colunas esperadas.");
                return;
            }

            // Lê as linhas do arquivo
            List<String[]> records = new java.util.ArrayList<>();
            while ((line = br.readLine()) != null) {
                // Substitui as vírgulas dentro de aspas por um caractere temporário
                line = line.replaceAll("\"([^\"]*)\"", "temporario");
                // Substitui as vírgulas por ponto e vírgula e restaura as vírgulas dentro de aspas
                line = line.replace(";", ";").replace("temporario", ";");

                // Divide a linha em colunas
                String[] values = line.split(";"); // Usamos ponto e vírgula como delimitador

                // Processa as colunas de acordo com a ordem desejada
                String[] orderedFields = new String[expectedColumns.length];
                for (int i = 0; i < expectedColumns.length; i++) {
                    int columnIndex = Arrays.asList(columns).indexOf(expectedColumns[i]);
                    if (columnIndex != -1) {
                        orderedFields[i] = values[columnIndex];
                    } else {
                        // Se a coluna não for encontrada, insira um valor padrão
                        orderedFields[i] = "NA";
                    }
                }

                records.add(orderedFields);
            }

            // Exibe os registros na ordem desejada
            for (String[] record : records) {
                System.out.println(Arrays.toString(record));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



