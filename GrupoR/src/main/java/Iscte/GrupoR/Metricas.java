package Iscte.GrupoR;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * varias metricas de comparacao entre os ficheiros de modo a establecer a qualidade de cada horario
 * 
 * @version 10/12/2023
 * @author Tomas Pereira Rita Azevedo Tiago Afonseca
 */

public class Metricas extends JFrame {

    private JComboBox<String> firstColumnComboBox;
    private JComboBox<String> secondColumnComboBox;
    private JComboBox<String> operatorComboBox;
    private JComboBox<String> operationComboBox;
    private JTextField comparisonValueField;
    private JButton loadFirstFileButton;
    private JButton loadSecondFileButton;
    private JButton compareButton;
    public JTextArea resultTextArea;
    public JTable previewTable;

    public List<String[]> firstFileData;
    public List<String[]> secondFileData;
    public Map<String, String> salaCapacidadeMap;

    public boolean isFirstFileLoaded = false;
    public boolean isSecondFileLoaded = false;

    public Metricas() {
        setTitle("Comparação de Colunas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        firstColumnComboBox = new JComboBox<>();
        secondColumnComboBox = new JComboBox<>();
        operatorComboBox = new JComboBox<>(new String[]{"<", ">", "="});
        operationComboBox = new JComboBox<>(new String[]{"+", "-"});
        comparisonValueField = new JTextField(5);
        loadFirstFileButton = new JButton("1File");
        loadSecondFileButton = new JButton("2File");
        compareButton = new JButton("Comparar");
        resultTextArea = new JTextArea();
        previewTable = new JTable();

        loadFirstFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile(true);
            }
        });

        loadSecondFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile(false);
            }
        });

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compareColumns();
            }
        });

        inputPanel.add(new JLabel("1ª Coluna:"));
        inputPanel.add(firstColumnComboBox);
        inputPanel.add(new JLabel("2ª Coluna:"));
        inputPanel.add(secondColumnComboBox);
        inputPanel.add(new JLabel("Escolha o Operador:"));
        inputPanel.add(operatorComboBox);
        inputPanel.add(new JLabel("Escolha a Operação:"));
        inputPanel.add(operationComboBox);
        inputPanel.add(new JLabel("Valor de Comparação:"));
        inputPanel.add(comparisonValueField);
        inputPanel.add(loadFirstFileButton);
        inputPanel.add(loadSecondFileButton);
        inputPanel.add(compareButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        panel.add(new JScrollPane(previewTable), BorderLayout.SOUTH);
    }

    public void compareColumns() {
        if (!isFirstFileLoaded || !isSecondFileLoaded) {
            JOptionPane.showMessageDialog(null, "Carregue ambos os ficheiros antes de comparar.");
            return;
        }

        int count = 0;
        resultTextArea.setText("");
        resultTextArea.append(String.format("%-15s %-15s %-15s %-15s %-15s%n", "Turma", "Dia", "Hora Inicio", "Data Aula", "Resultado"));
        resultTextArea.append("------------------------------------------------------------\n");

        for (int i = 1; i < firstFileData.size(); i++) {
            String[] firstRow = firstFileData.get(i);
            String salaAtribuida = (firstRow.length > 9) ? firstRow[9] : "-";
            String[] secondRow = findRowBySala(secondFileData, salaAtribuida);

            if (secondRow == null) {
                continue;
            }

            String turma = (firstRow.length > 3) ? firstRow[3] : "-";
            String dia = (firstRow.length > 5) ? firstRow[5] : "-";
            String horaInicio = (firstRow.length > 6) ? firstRow[6] : "-";
            String dataAula = (firstRow.length > 7) ? firstRow[7] : "-";
            String CaracteristicaSalaPedida = (firstRow.length > 10) ? firstRow[10] : "";
            String colunasComX = CompareCSVFiles.getColunasComX(secondFileData, salaAtribuida, CaracteristicaSalaPedida);

            String[] caracteristicasArray = colunasComX.split("\\|");
            int resultadoComparacao = 1;

            for (String caracteristica : caracteristicasArray) {
                if (caracteristica.trim().compareToIgnoreCase(CaracteristicaSalaPedida) == 0) {
                    resultadoComparacao = 0;
                    break;
                }
            }

            int comparacaoInscritosLugares = CompareCSVFiles.compareInscritosLugares(firstRow[4], salaCapacidadeMap.getOrDefault(salaAtribuida, "--"));
            int somaUltimasColunas = resultadoComparacao + comparacaoInscritosLugares;
            String resultadoFinal = (somaUltimasColunas == 0) ? "Ideal" : "Não Ideal";
            String line = String.format("%-15s %-15s %-15s %-15s %-15s%n", turma, dia, horaInicio, dataAula, resultadoFinal);
            resultTextArea.append(line);

            if (resultadoFinal.equals("Ideal")) {
                count++;
            }
        }
int a = getNumberOfLinesInFirstFile() - count -1;
        resultTextArea.append("------------------------------------------------------------\n");
        resultTextArea.append(String.format("Total de Linhas: %d%n", a));

        JFrame resultFrame = new JFrame("Resultados da Comparação");
        resultFrame.setSize(600, 400);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea resultArea = new JTextArea(resultTextArea.getText());
        resultArea.setEditable(false);
        resultFrame.add(new JScrollPane(resultArea));
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    public String[] findRowBySala(List<String[]> data, String col10) {
        // Supondo que os dados contêm as informações da sala no índice 1
        for (String[] row : data.subList(1, data.size())) {
            if (row.length > 1 && row[1].equals(col10)) {
                return row;
            }
        }
        return null;
    }

    public void loadFile(boolean isFirstFile) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos CSV", "csv");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadFileData(file, isFirstFile);

            if (!isFirstFile) {
                isSecondFileLoaded = true;
                updatePreviewTable();
            }

            compareButton.setEnabled(isFirstFileLoaded && isSecondFileLoaded);
        }
    }

    public void loadFileData(File file, boolean isFirstFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String[]> data = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                data.add(values);
            }

            System.out.println("Loaded data:");
            for (String[] row : data) {
                System.out.println(Arrays.toString(row));
            }

            if (isFirstFile) {
                isFirstFileLoaded = true;
                firstFileData = data;
                updateComboBox(firstColumnComboBox, data.get(0));
            } else {
                secondFileData = data;
                updateComboBox(secondColumnComboBox, data.get(0));
                salaCapacidadeMap = createSalaCapacidadeMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> createSalaCapacidadeMap() {
        Map<String, String> salaCapacidadeMap = new HashMap<>();

        for (String[] row : secondFileData.subList(1, secondFileData.size())) {
            String sala = (row.length > 1) ? row[1] : "-";
            String capacidadeNormal = (row.length > 2) ? row[2] : "-";
            salaCapacidadeMap.put(sala, capacidadeNormal);
        }

        return salaCapacidadeMap;
    }

    public void updateComboBox(JComboBox<String> comboBox, String[] columnNames) {
        comboBox.removeAllItems();
        for (String columnName : columnNames) {
            comboBox.addItem(columnName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Metricas());
    }
    public void updatePreviewTable() {
        if (firstFileData != null && secondFileData != null) {
            String[] columnNames = {"Sala Correspondente", "Colunas com 'X'", "Número Inscritos", "Número de Lugares Normal"};

            Object[][] data = new Object[firstFileData.size() - 1][columnNames.length];

            for (int i = 1; i < firstFileData.size(); i++) {
                String[] firstRow = firstFileData.get(i);

                String col10 = (firstRow.length > 9) ? firstRow[9] : "-";

              
                String colunasComX = CompareCSVFiles.getColunasComX(secondFileData, col10, "");

        
                String numInscritos = (firstRow.length > 4) ? firstRow[4] : "-";

                
                String salaCapacidade = (salaCapacidadeMap.containsKey(col10)) ? salaCapacidadeMap.get(col10) : "--";
                int numLugaresNormal = CompareCSVFiles.tryParseInt(salaCapacidade, 0);

                data[i - 1] = new Object[]{col10, colunasComX, numInscritos, numLugaresNormal};
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            previewTable.setModel(model);
        }
    }

    public int getNumberOfLinesInFirstFile() {
        if (firstFileData != null) {
            return firstFileData.size();
        } else {
            return 0;
        }
    }

    
    
    }

