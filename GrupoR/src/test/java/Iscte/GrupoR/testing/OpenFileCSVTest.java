package Iscte.GrupoR.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import Iscte.GrupoR.OpenFileCSV;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OpenFileCSVTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testReadCSVWithExpectedColumns() throws IOException {
        File mockFile = createTestCsv();
        String[] expectedColumns = {
                "Curso", "Unidade Curricular", "Turno", "Turma", "Inscritos no turno",
                "Dia da semana", "Hora inicio da aula", "Hora fim da aula", "Data da aula",
                "Caracteristicas da sala pedida para a aula", "Sala atribuida a aula"
        };

        OpenFileCSV.readCSV(mockFile, expectedColumns);

        String expectedOutput = "Colunas Esperadas: " + Arrays.toString(expectedColumns) + System.lineSeparator() +
                "Colunas Encontradas : [Curso, Unidade Curricular, Turno, Turma, Inscritos no turno, " +
                "Dia da semana, Hora inicio da aula, Hora fim da aula, Data da aula, " +
                "Caracteristicas da sala pedida para a aula, Sala atribuida a aula]" + System.lineSeparator() +
                "[Engenharia, Matemática, Manhã, A101, 30, Segunda, 08:00, 10:00, 2023-01-01, Projetor, A101]" + System.lineSeparator() +
                "[Ciência da Computação, Algoritmos, Tarde, B202, 25, Terça, 14:00, 16:00, 2023-01-02, Quadro branco, B202]" + System.lineSeparator();

        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testReadCSVWithInvalidFile() {
        File invalidFile = new File("path/to/nonexistentfile.csv");
        assertThrows(IOException.class, () -> OpenFileCSV.readCSV(invalidFile, OpenFileCSV.expectedColumns));
    }

    @Test
    void testChooseCSVFile() {
        // Simula a escolha de um arquivo
        File selectedFile = simulateFileChooser("test.csv");

        // Verifica se o arquivo selecionado é não nulo
        assertNotNull(selectedFile);
        assertEquals("test.csv", selectedFile.getName());
    }

    @Test
    void testChooseCSVFileNoSelection() {
        // Simula a não seleção de um arquivo
        File selectedFile = simulateFileChooser(null);

        // Verifica se o arquivo selecionado é nulo
        assertNull(selectedFile);
    }

    private File simulateFileChooser(String selectedFileName) {
        // Cria um mock para JFileChooser
        JFileChooser fileChooser = new JFileChooser();

        // Configura o arquivo selecionado no mock
        if (selectedFileName != null) {
            fileChooser.setSelectedFile(new File(selectedFileName));
        }

        // Chama o método simulado e retorna o resultado
        return OpenFileCSV.chooseCSVFile();
    }


    private File createTestCsv() throws IOException {
        File testFile = File.createTempFile("test", ".csv");

        List<String> lines = Arrays.asList(
                "Curso;Unidade Curricular;Turno;Turma;Inscritos no turno;Dia da semana;Hora inicio da aula;Hora fim da aula;Data da aula;Caracteristicas da sala pedida para a aula;Sala atribuida a aula",
                "Engenharia;Matemática;Manhã;A101;30;Segunda;08:00;10:00;2023-01-01;Projetor;A101",
                "Ciência da Computação;Algoritmos;Tarde;B202;25;Terça;14:00;16:00;2023-01-02;Quadro branco;B202"
        );

        Files.write(testFile.toPath(), lines);

        return testFile;
    }

    private JFileChooser createMockFileChooser(String selectedFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(selectedFileName));
        return fileChooser;
    }
    @Test
    void testMain() throws IOException {
       
        PrintStream originalOut = System.out;
        
 
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));


        String[] args = null;
        OpenFileCSV.main(args);
        System.setOut(originalOut);

        String output = outContent.toString();

       
    }
    @Test
    void testMainWithMissingColumns() throws IOException {
        // Salvar a saída padrão antes de chamar o método main
        PrintStream originalOut = System.out;

        // Criar stream para capturar a saída
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Chamar o método main com colunas esperadas que não existem no CSV
        String[] args = null; // ou qualquer array de argumentos necessário
        OpenFileCSV.expectedColumns = new String[]{"Coluna1", "Coluna2", "Coluna3"}; // Colunas que não existem
        OpenFileCSV.main(args);

        // Restaurar a saída padrão
        System.setOut(originalOut);

        // Obter a saída do sistema após a execução do método main
        String output = outContent.toString();


        assertTrue(output.contains("O arquivo CSV não possui todas as colunas esperadas."));
    }

    @Test
    void testReadCSVWithMissingColumns() throws IOException {
       
        File mockFile = createTestCsvWithMissingColumns();
        String[] expectedColumns = {"Coluna1", "Coluna2", "Coluna3"};

     
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

     
        OpenFileCSV.readCSV(mockFile, expectedColumns);

       
        System.setOut(originalOut);

       
        String output = outContent.toString();
        assertTrue(output.contains("NA, NA, NA"));
    }

    private File createTestCsvWithMissingColumns() throws IOException {
        File testFile = File.createTempFile("test", ".csv");

      
        List<String> lines = Arrays.asList(
                "Coluna1;Coluna2;Coluna4", // Coluna3 está ausente
                "Valor1;Valor2;Valor4"
        );

        Files.write(testFile.toPath(), lines);

        return testFile;
    }
    @Test
    void testShowConfirmationDialog() {
        // Chama o método que retorna null
        Object result = OpenFileCSV.showConfirmationDialog();

        // Verifica se o resultado é null
        assertNull(result);
    }
    @Test
    void testActionPerformedWithNullSelectedFile() {

        JFileChooser mockFileChooser = Mockito.mock(JFileChooser.class);

     
        when(mockFileChooser.showOpenDialog(null)).thenReturn(JFileChooser.CANCEL_OPTION);

        JOptionPane mockOptionPane = Mockito.mock(JOptionPane.class);


        OpenFileCSV openFileCSV = new OpenFileCSV();

      ;

    }

}
