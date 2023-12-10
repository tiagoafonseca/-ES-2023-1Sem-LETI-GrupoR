package Iscte.GrupoR.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Iscte.GrupoR.Metricas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MetricasTest {

    @Test
    public void testFindRowBySala() {
        Metricas metricas = new Metricas();
        List<String[]> testData = Arrays.asList(
                new String[]{"", "sala1", ""},
                new String[]{"", "sala2", ""},
                new String[]{"", "sala3", ""}
        );

        String[] resultRow = metricas.findRowBySala(testData, "sala2");

        assertNotNull(resultRow);
        assertArrayEquals(new String[]{"", "sala2", ""}, resultRow);
    }

    @Test
    public void testCompareColumns() {
        Metricas metricas = spy(new Metricas());
        metricas.firstFileData = Arrays.asList(
                new String[]{"", "sala1", "", "", "", "", "", "", "", "", ""},
                new String[]{"", "sala2", "", "", "", "", "", "", "", "", ""},
                new String[]{"", "sala3", "", "", "", "", "", "", "", "", ""}
        );

        metricas.secondFileData = Arrays.asList(
                new String[]{"", "sala1", "", "", "", "", "", "", "", "", ""},
                new String[]{"", "sala2", "", "", "", "", "", "", "", "", ""},
                new String[]{"", "sala4", "", "", "", "", "", "", "", "", ""}
        );

        metricas.salaCapacidadeMap = new HashMap<>();
        metricas.salaCapacidadeMap.put("sala1", "30");
        metricas.salaCapacidadeMap.put("sala2", "25");
        metricas.salaCapacidadeMap.put("sala4", "20");

        metricas.compareColumns();
    }

    @Test
    public void testGetNumberOfLinesInFirstFile() {
        Metricas metricas = new Metricas();
        metricas.firstFileData = Arrays.asList(
                new String[]{"", "sala1", "", "", "", "", "", "", "", "", ""},
                new String[]{"", "sala2", "", "", "", "", "", "", "", "", ""}
        );

        int numberOfLines = metricas.getNumberOfLinesInFirstFile();

        assertEquals(2, numberOfLines);
    }

    @Test
    public void testCreateSalaCapacidadeMap() {
        Metricas metricas = new Metricas();
        metricas.secondFileData = Arrays.asList(
                new String[]{"", "sala1", "30"},
                new String[]{"", "sala2", "25"},
                new String[]{"", "sala3", "20"},
                new String[]{"", "sala4"},  // Testando sem capacidadeNormal
                new String[]{"", "sala5", "ABC"}  // Testando com capacidadeNormal não numérica
        );

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("sala1", "30");
        expectedMap.put("sala2", "25");
        expectedMap.put("sala3", "20");
        expectedMap.put("sala4", "-");  // Esperado: valor padrão "-"
        expectedMap.put("sala5", "-");  // Esperado: valor padrão "-"

        Map<String, String> result = metricas.createSalaCapacidadeMap();

        assertEquals(expectedMap, result);
    }

    @Test
    public void testLoadFile2() {
        Metricas metricas = spy(new Metricas());

      
        JFileChooser fileChooser = mock(JFileChooser.class);
        when(fileChooser.showOpenDialog(null)).thenReturn(JFileChooser.APPROVE_OPTION);

        File mockFile = mock(File.class);
        when(fileChooser.getSelectedFile()).thenReturn(mockFile);

      
        List<String[]> mockData = new ArrayList<>();
        mockData.add(new String[]{"data1", "data2", "data3"});
        doNothing().when(metricas).loadFileData(mockFile, true);

       
        doNothing().when(metricas).updatePreviewTable();
        doNothing().when(metricas).updateComboBox(any(), any());

      
        metricas.loadFile(true);

      
        verify(metricas).loadFileData(mockFile, true);
        verify(metricas).updateComboBox(any(), any());
        assertTrue(metricas.isFirstFileLoaded);
        assertFalse(metricas.isSecondFileLoaded);
    }

    private Metricas metricas;
    private JComboBox<String> comboBox;

    @Before
    public void setUps() {
        SwingUtilities.invokeLater(() -> {
            metricas = new Metricas();
            comboBox = new JComboBox<>();
        });
    }

    @After
    public void tearDowns() {
        metricas.dispose();
    }

    @Test
    public void testUpdateComboBox() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            String[] columnNames = {"Column1", "Column2", "Column3"};
            metricas.updateComboBox(comboBox, columnNames);

            assertEquals(columnNames.length, comboBox.getItemCount());

            for (int i = 0; i < columnNames.length; i++) {
                assertEquals(columnNames[i], comboBox.getItemAt(i));
            }
        });
    }

    private File firstFile;
    private File secondFile;

    @Before
    public void setUp() {
        metricas = new Metricas();
        firstFile = createTempFile("1File");
        secondFile = createTempFile("2File");
    }

    @After
    public void tearDown() {
        firstFile.delete();
        secondFile.delete();
        metricas.dispose();
    }

    @Test
    public void testLoadFile() {
        JFileChooser fileChooserMock = mock(JFileChooser.class);
        when(fileChooserMock.showOpenDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooserMock.getSelectedFile()).thenReturn(firstFile);
        metricas.loadFile(true);

        assertTrue(metricas.isFirstFileLoaded);
        assertNotNull(metricas.firstFileData);
        assertEquals(firstFile.getName(), Objects.requireNonNull(metricas.firstFileData.get(0))[0]);
    }

    @Test
    public void testCompareColumns_Loaded() {
        metricas.isFirstFileLoaded = true;
        metricas.isSecondFileLoaded = true;
        metricas.firstFileData = new ArrayList<>();
        metricas.secondFileData = new ArrayList<>();
        metricas.firstFileData.add(new String[]{"Header1"});
        metricas.secondFileData.add(new String[]{"Header2"});

        spy(metricas);

     
        metricas.compareColumns();

        verify(metricas, times(1)).compareColumns();
    }

    private File createTempFile(String fileName) {
        try {
            File tempFile = File.createTempFile(fileName, ".csv");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write("Header\n");
                writer.write("Data\n");
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Metricas metrica;

    @Before
    public void setUpps() {
        metrica = new Metricas();
    }

    @Test
    public void testUpdatePreviewTable_Success() {
        
        metricas.firstFileData = Arrays.asList(
                new String[]{"Header1", "Header2", "Header3", "Header4", "Header5", "Header6", "Header7", "Header8", "Header9", "Header10", "Header11"},
                new String[]{"Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7", "Data8", "Data9", "Sala1", "Caracteristica1", "OtherData"}
        );
        metricas.secondFileData = Arrays.asList(
                new String[]{"Header1", "Header2", "Header3", "Header4", "Header5", "Header6", "Header7", "Header8", "Header9", "Header10", "Header11"},
                new String[]{"Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7", "Data8", "Data9", "Sala1", "Caracteristica1", "OtherData"}
        );
        metricas.salaCapacidadeMap = new HashMap<>();
        metricas.salaCapacidadeMap.put("Sala1", "100"); // Exemplo de mapeamento de sala para capacidade

        metricas.updatePreviewTable();

        DefaultTableModel model = (DefaultTableModel) metricas.previewTable.getModel();
        assertEquals(1, model.getRowCount());
        assertEquals("Sala1", model.getValueAt(0, 0));
        assertEquals("Caracteristica1", model.getValueAt(0, 1));
        assertEquals("Data5", model.getValueAt(0, 2));
        assertEquals(100, model.getValueAt(0, 3));
    }

    @Test
    public void testUpdatePreviewTable_NoData() {
        metricas.updatePreviewTable();

       
    }

    @Test
    public void testUpdatePreviewTable_NullData() {
        metricas.firstFileData = null;
        metricas.secondFileData = null;

        metricas.updatePreviewTable();

        DefaultTableModel model = (DefaultTableModel) metricas.previewTable.getModel();
        assertEquals(0, model.getRowCount());
    }
}
