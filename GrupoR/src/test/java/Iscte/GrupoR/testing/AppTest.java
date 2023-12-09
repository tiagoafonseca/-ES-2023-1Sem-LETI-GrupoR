package Iscte.GrupoR.testing;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Iscte.GrupoR.CompareCSVFiles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testCSVFileCreationAndOrderChange() {
        // Criação inicial do arquivo CSV com ordem padrão
        try (FileWriter fileWriter = new FileWriter(testCSVFile)) {
            fileWriter.write("Unidade Curricular,Curso,Turma,Turno,Inscritos no turno,Dia da semana,Hora de inicio da aula,Hora fim da aula,Data da aula,Caracteristicas da sala de pedida para a aula,Sala atribuida a aula\n");
            fileWriter.write("Engenharia Software,LETI,ETI-1,2,30,Seg,08:00:00,10:00:00,2023-11-07,AA2.25,Auditorio\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verifica se o arquivo foi criado com sucesso
        assertTrue(testCSVFile.exists());

        // Simula o que foi passado pelo utilizador (nova ordem desejada)
        String orderInput = "Turno,Unidade Curricular,Turma,Curso,Inscritos no turno,Dia da semana,Hora de início da aula,Hora fim da aula,Data da aula,Características da sala de pedida para a aula,Sala atribuída à aula";

        // Alteração da ordem das colunas conforme especificado pelo utilizador
        try (FileWriter fileWriter = new FileWriter(testCSVFile)) {
            fileWriter.write(orderInput + "\n");
            fileWriter.write("2,Engenharia Software,ETI-1,LETI,30,Seg,08:00:00,10:00:00,2023-11-07,AA2.25,Auditório\n");
        } catch (IOException e) {
       
            e.printStackTrace();
        }

        // Verifica se a ordem das colunas foi alterada com sucesso
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


    
    
