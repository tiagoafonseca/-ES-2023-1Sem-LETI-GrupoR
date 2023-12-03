package Iscte.GrupoR.testing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Iscte.GrupoR.CompareCSVFiles;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class AppTest {
    private File testCSVFile;

    @BeforeEach
    void setup() {
        // Antes de cada teste, criamos um arquivo CSV temporário
        try {
            testCSVFile = File.createTempFile("test", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCSVFileCreationSuccess() {
        try (FileWriter fileWriter = new FileWriter(testCSVFile)) {
            // Escreva o cabeçalho com as colunas
            fileWriter.write("Curso,Unidade Curricular,Turno,Turma,Inscritos no turno,Dia da semana,Hora de início da aula,Hora fim da aula,Data da aula,Características da sala de pedida para a aula,Sala atribuída à aula\n");
            
            // Escreva uma linha de dados como exemplo
            fileWriter.write("Engenharia,Matemática,A1,A,30,Segunda,08:00,10:00,2023-11-07,Sala grande,101\n");
            // Adicione mais linhas de dados, se necessário
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verifique se o arquivo foi criado com sucesso
        assertTrue(testCSVFile.exists());
    }
    
    
    ///////
    @Test
    public void testGetColunasComX() {
        // Crie uma instância de CompareCSVFiles
        CompareCSVFiles comparer = new CompareCSVFiles();

        // Mock dos dados para o segundo arquivo
        List<String[]> secondFileData = new ArrayList<>();
        secondFileData.add(new String[]{"Curso", "Sala", "Caracteristica1", "Caracteristica2", "X", "X"});
        secondFileData.add(new String[]{"Informatica", "Sala1", "X", "-", "-", "X"});
        secondFileData.add(new String[]{"Matematica", "Sala2", "-", "X", "-", "-"});

        // Teste para uma sala existente
        String result = CompareCSVFiles.getColunasComX(secondFileData, "Sala1");
        assertEquals("Caracteristica1 ", result);

        // Teste para uma sala inexistente
        result = comparer.getColunasComX(secondFileData, "Sala3");
        assertEquals("", result);
    }

    @Test
    public void testTryParseInt() {
        // Crie uma instância de CompareCSVFiles
        CompareCSVFiles comparer = new CompareCSVFiles();

        // Teste para um valor inteiro válido
        int result = CompareCSVFiles.tryParseInt("123", 0);
        assertEquals(123, result);

        // Teste para um valor não inteiro
        result = comparer.tryParseInt("abc", 456);
        assertEquals(456, result);
    }
}


    
    
