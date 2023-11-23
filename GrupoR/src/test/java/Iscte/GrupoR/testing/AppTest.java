package Iscte.GrupoR.testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class AppTest {
    private File testCSVFile;

    @BeforeGroups
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
}
