package Iscte.GrupoR.testing;

import org.junit.jupiter.api.Test;

import Iscte.GrupoR.CompareCSVFiles;
import Iscte.GrupoR.OpenFileCSV;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompareCSVFilesTest {

   

    @Test
    void testCompareInscritosLugares() {
        // Testa o método compareInscritosLugares
        int result1 = CompareCSVFiles.compareInscritosLugares("10", "5");
        int result2 = CompareCSVFiles.compareInscritosLugares("5", "10");
        int result3 = CompareCSVFiles.compareInscritosLugares("5", "5");

        assertEquals(1, result1);
        assertEquals(0, result2);
        assertEquals(0, result3);
    }

    @Test
    void testReadCSV() throws IOException {
        // Criar um arquivo de teste CSV temporário
        Path tempFile = Files.createTempFile("test", ".csv");
        String csvContent = "Coluna1;Coluna2;Coluna3\nValor1A;Valor1B;Valor1C\nValor2A;Valor2B;Valor2C";
        Files.write(tempFile, csvContent.getBytes());

        
        File testFile = tempFile.toFile();
        List<String[]> result = CompareCSVFiles.readCSV(testFile);

        assertEquals(3, result.size());

        String[] header = result.get(0);
        assertEquals("Coluna1", header[0]);
        assertEquals("Coluna2", header[1]);
        assertEquals("Coluna3", header[2]);


        String[] row1 = result.get(1);
        assertEquals("Valor1A", row1[0]);
        assertEquals("Valor1B", row1[1]);
        assertEquals("Valor1C", row1[2]);


        String[] row2 = result.get(2);
        assertEquals("Valor2A", row2[0]);
        assertEquals("Valor2B", row2[1]);
        assertEquals("Valor2C", row2[2]);


        Files.delete(tempFile);
    }



    @Test
    void testTryParseInt() {
        // Testa o método tryParseInt
        int result1 = CompareCSVFiles.tryParseInt("10", 0);
        int result2 = CompareCSVFiles.tryParseInt("abc", 5);

        assertEquals(10, result1);
        assertEquals(5, result2);
    }


    @Test
    void testGetColunasComX() {
  
        List<String[]> secondFileData = new ArrayList<>();
        secondFileData.add(new String[]{"Col1", "Col2", "Col3", "Col4", "Col5", "Col6", "Col7"});
        secondFileData.add(new String[]{"Row1", "Room101", "Value", "Value", "Value", "X", "X", "X"});


        String result = CompareCSVFiles.getColunasComX(secondFileData, "Room101", "");


        assertEquals("Col5|Col6|Col7|", result);
    }

    @Test
    void testGetColunasComXWhenNoXInColumns() {
        // Dados de exemplo para o segundo arquivo
        List<String[]> secondFileData = new ArrayList<>();
        secondFileData.add(new String[]{"Col1", "Col2", "Col3", "Col4", "Col5", "Col6", "Col7"});
        secondFileData.add(new String[]{"Row1", "Room101", "Value", "Value", "Value", "", "", ""});

        // Chama a função getColunasComX
        String result = CompareCSVFiles.getColunasComX(secondFileData, "Room101", "");

        // Verifica se o resultado é vazio, pois não há "X" nas colunas esperadas
        assertEquals("", result);
    }

    @Test
    void testGetColunasComXWhenSalaNotFound() {
        // Dados de exemplo para o segundo arquivo
        List<String[]> secondFileData = new ArrayList<>();
        secondFileData.add(new String[]{"Col1", "Col2", "Col3", "Col4", "Col5", "Col6", "Col7"});
        secondFileData.add(new String[]{"Row1", "Room102", "Value", "Value", "Value", "X", "X", "X"});

      
        String result = CompareCSVFiles.getColunasComX(secondFileData, "Room101", "");

   
        assertEquals("", result);
    }
 
    @Test
    void testMainWithValidFiles() {
 
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

   
        File firstFile = new File("path/to/firstFile.csv");
        File secondFile = new File("path/to/secondFile.csv");


        CompareCSVFiles.main(new String[]{firstFile.getAbsolutePath(), secondFile.getAbsolutePath()});


        System.setOut(System.out);

        String expectedOutput = "Turma, Dia, Hora Inicio, Data Aula, Sala Atribuida, Numero Inscritos, Número de Lugares Normal, " +
                "Colunas com 'X', Caracteristicas da Sala Pedida, Ideal=0/Falso=1, ComparacaoInscritosLugares, " +
                "SomaUltimasColunas, ResultadoFinal\n"; // Adicione a saída esperada aqui

        assertEquals(expectedOutput, outContent.toString());
    }
    
}
