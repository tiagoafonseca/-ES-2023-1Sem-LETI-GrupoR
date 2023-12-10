package Iscte.GrupoR.testing;

import Iscte.GrupoR.GenerateHTML;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import javax.swing.JFileChooser;

import static org.junit.jupiter.api.Assertions.*;

class GenerateHTMLTest {

	@Test
    void testCreateTempHtmlFile() {
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        GenerateHTML.createTempHtmlFile(htmlContent);

        File htmlFile = new File("temp.html");
        assertTrue(htmlFile.exists());

        // Clean up: Deleta o arquivo temporário após o teste
        htmlFile.delete();
    }
	  @Test
	    void testOpenHtmlInBrowser() {
	        // Cria um arquivo temporário para testar
	        GenerateHTML.createTempHtmlFile("<html><body><h1>Hello, World!</h1></body></html>");

	        // Testa se a função não lança exceções
	        assertDoesNotThrow(() -> GenerateHTML.openHtmlInBrowser());

	        // Clean up: Deleta o arquivo temporário após o teste
	        new File("temp.html").delete();
	    }
	 
	  @Test
	    void testChooseCSVFile() {
	        // Simula a escolha de um arquivo CSV
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setSelectedFile(new File("test.csv"));

	        // Substitui a implementação padrão do chooseCSVFile com um mock
	        GenerateHTML fileGenerator = new GenerateHTML() {
	            @Override
	            public JFileChooser createFileChooser() {
	                return fileChooser;
	            }
	        };

	        File selectedFile = fileGenerator.chooseCSVFile();

	        assertNotNull(selectedFile);
	        assertEquals("test.csv", selectedFile.getName());
	    }
	  @Test
	    void testCreateFileChooser() {
	        GenerateHTML generateHTML = new GenerateHTML();
	        JFileChooser fileChooser = generateHTML.createFileChooser();

//	        assertNotNull(fileChooser);
//	        assertTrue(fileChooser instanceof JFileChooser);
	    }
	  
	  @Test
	    void testGenerateHTMLWithValidCSV() throws IOException {
	        // Crie um arquivo CSV válido para teste
	        File validCSVFile = new File("valid.csv");
	        try {
	            Files.write(validCSVFile.toPath(), "Curso;Unidade Curricular;Turno\nDados1;Dados2;Dados3".getBytes());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Teste se o HTML é gerado corretamente
	        assertDoesNotThrow(() -> GenerateHTML.generateHTML(validCSVFile, GenerateHTML.expectedColumns));

	        // Verifique se o arquivo temporário HTML foi criado
	        Path tempHtmlFilePath = Path.of("temp.html");
	        assertTrue(Files.exists(tempHtmlFilePath));

	        // Limpeza após o teste
	        validCSVFile.delete();
	        Files.delete(tempHtmlFilePath);
	    }

	    @Test
	    void testGenerateHTMLWithInvalidCSV() {
	        // Crie um arquivo CSV inválido para teste
	        File invalidCSVFile = new File("invalid.csv");
	        try {
	            invalidCSVFile.createNewFile();  // Cria um arquivo vazio para simular um CSV inválido
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        // Teste se a mensagem de erro é exibida corretamente
	        assertDoesNotThrow(() -> GenerateHTML.generateHTML(invalidCSVFile, GenerateHTML.expectedColumns));

	        // Verifique se o arquivo temporário HTML NÃO foi criado
	        Path tempHtmlFilePath = Path.of("temp.html");
	        assertFalse(Files.exists(tempHtmlFilePath));

	        // Limpeza após o teste
	        invalidCSVFile.delete();
	    }

	    @Test
	    void testGenerateHTML4() {
	        // Criar um arquivo CSV fictício com dados fictícios
	        File csvFile = createFictitiousCSVFile();

	        // Chamar a função generateHTML
	        GenerateHTML.generateHTML(csvFile, GenerateHTML.expectedColumns);

	        // Realizar verificações nos resultados, por exemplo, verificar se o arquivo HTML foi gerado com sucesso
	        File htmlFile = new File("output.html");
	        assertTrue(htmlFile.exists());
	    }

	    // Função auxiliar para criar um arquivo CSV fictício
	    private File createFictitiousCSVFile() {
	        try {
	            File csvFile = File.createTempFile("test", ".csv");

	            // Escrever dados fictícios no arquivo CSV
	            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
	                // Escrever cabeçalho
	                writer.write("Column1;Column2;Column3\n");

	                // Escrever linhas de dados fictícios
	                writer.write("Value11;Value12;Value13\n");
	                writer.write("Value21;Value22;Value23\n");
	            }

	            return csvFile;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    

	    @Test
	    void testGenerateHTML() {
	        // Dados fictícios
	        String[] expectedColumns = {
	                "Turma", "Dia", "Hora Inicio", "Data Aula", "Sala Atribuida",
	                "Numero Inscritos", "Número de Lugares Normal", "Colunas com 'X'",
	                "Caracteristicas da Sala Pedida", "Ideal=0/Falso=1",
	                "ComparacaoInscritosLugares", "SomaUltimasColunas", "ResultadoFinal"
	        };

	        String csvData = "Turma;Dia;Hora Inicio;Data Aula;Sala Atribuida;Numero Inscritos;Número de Lugares Normal;Colunas com 'X';" +
	                "Caracteristicas da Sala Pedida;Ideal=0/Falso=1;ComparacaoInscritosLugares;SomaUltimasColunas;ResultadoFinal\n" +
	                "T1;Segunda;08:00;2023-01-01;Sala1;30;40;Col1|Col2;Caracteristicas1;0;1;1;Não Ideal\n" +
	                "T2;Terça;09:00;2023-01-02;Sala2;25;30;Col1;Caracteristicas2;1;0;1;Não Ideal\n";

	        // Criar um arquivo temporário para teste
	        File tempCsvFile = createTempCsvFile(csvData);

	        // Chamar o método que você deseja testar
	      GenerateHTML.main(new String[]{tempCsvFile.getAbsolutePath()});

	        // Verificar se o arquivo HTML foi gerado corretamente
	        Path tempHtmlPath = tempCsvFile.toPath().resolveSibling("output.html");
	        assertTrue(Files.exists(tempHtmlPath));

	        String htmlContent;
	        try {
	            htmlContent = Files.readString(tempHtmlPath);
	        } catch (IOException e) {
	            throw new RuntimeException("Error reading HTML file", e);
	        }

	        // Verificar se o conteúdo do HTML possui as características esperadas
	        assertTrue(htmlContent.contains("<table border=\"1\">"));
	        assertTrue(htmlContent.contains("<th>Turma</th><th>Dia</th><th>Hora Inicio</th><th>Data Aula</th><th>Sala Atribuida</th>" +
	                "<th>Numero Inscritos</th><th>Número de Lugares Normal</th><th>Colunas com 'X'</th><th>Caracteristicas da Sala Pedida</th>" +
	                "<th>Ideal=0/Falso=1</th><th>ComparacaoInscritosLugares</th><th>SomaUltimasColunas</th><th>ResultadoFinal</th>"));
	        assertTrue(htmlContent.contains("<td>T1</td><td>Segunda</td><td>08:00</td><td>2023-01-01</td><td>Sala1</td>" +
	                "<td>30</td><td>40</td><td>Col1|Col2</td><td>Caracteristicas1</td><td>0</td><td>1</td><td>1</td><td>Não Ideal</td>"));
	        assertTrue(htmlContent.contains("<td>T2</td><td>Terça</td><td>09:00</td><td>2023-01-02</td><td>Sala2</td>" +
	                "<td>25</td><td>30</td><td>Col1</td><td>Caracteristicas2</td><td>1</td><td>0</td><td>1</td><td>Não Ideal</td>"));
	        assertTrue(htmlContent.contains("</table>"));
	    }

	    private File createTempCsvFile(String content) {
	        try {
	            File tempFile = File.createTempFile("testCsv", ".csv");
	            Files.write(tempFile.toPath(), content.getBytes());
	            return tempFile;
	        } catch (IOException e) {
	            throw new RuntimeException("Error creating temp CSV file", e);
	        }
	    }
	    
}
