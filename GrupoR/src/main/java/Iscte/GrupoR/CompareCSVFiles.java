package Iscte.GrupoR;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareCSVFiles {

	public static void main(String[] args) {
		// Carrega o primeiro arquivo CSV
		File firstFile = chooseCSVFile();
		List<String[]> firstFileData = readCSV(firstFile);

		// Carrega o segundo arquivo CSV
		File secondFile = chooseCSVFile();
		List<String[]> secondFileData = readCSV(secondFile);

		// Cria um mapa para armazenar a Capacidade Normal com base nas salas
		Map<String, String> salaCapacidadeMap = new HashMap<>();

		for (String[] row : secondFileData.subList(1, secondFileData.size())) {
			String sala = (row.length > 1) ? row[1] : "-";
			String capacidadeNormal = (row.length > 2) ? row[2] : "-";
			salaCapacidadeMap.put(sala, capacidadeNormal);
		}

		// Cria uma tabela de saída
		List<String[]> outputTable = new ArrayList<>();
		outputTable.add(new String[] { "Turma", "Dia", "Hora Inicio", "Sala Atribuida", "Numero Inscritos",
				"Caracteristicas da Sala", "Colunas com 'X'", "Caracteristicas Sala Pedida" });

		// Preenche a tabela de saída
		for (String[] firstRow : firstFileData.subList(1, firstFileData.size())) {
			String salaAtribuida = (firstRow.length > 9) ? firstRow[9] : "-";

			String turma = (firstRow.length > 3) ? firstRow[3] : "-";
			String dia = (firstRow.length > 5) ? firstRow[5] : "-";
			String horaInicio = (firstRow.length > 6) ? firstRow[6] : "-";
			String numeroInscritos = (firstRow.length > 4) ? firstRow[4] : "-";

			// Obtém a Capacidade Normal do mapa (se existir)
			String capacidadeNormal = salaCapacidadeMap.getOrDefault(salaAtribuida, "--");

			// Obtém as características da sala a partir da coluna 5
			StringBuilder caracteristicasSala = new StringBuilder();
			for (int i = 5; i < firstRow.length; i++) {
				if ("x".equalsIgnoreCase(firstRow[i])) {
					caracteristicasSala.append(" ").append(firstFileData.get(0)[i]);
				}
			}

			// Adiciona à tabela de saída
			outputTable.add(new String[] { turma, dia, horaInicio, salaAtribuida, numeroInscritos, capacidadeNormal,
					getColunasComX(secondFileData, salaAtribuida), (firstRow.length > 10) ? firstRow[10] : "-" });
		}

		// Exibe a tabela de saída
		for (String[] row : outputTable) {
			System.out.println(Arrays.toString(row));
		}
	}

	public static String getColunasComX(List<String[]> secondFileData, String salaAtribuida) {
		StringBuilder colunasComX = new StringBuilder();

		// Verifica se a lista não está vazia e se possui elementos suficientes
		if (!secondFileData.isEmpty() && secondFileData.get(0).length > 1) {
			// Encontrar a linha correspondente à sala atribuída no segundo arquivo
			for (int rowIndex = 1; rowIndex < secondFileData.size(); rowIndex++) {
				String[] secondRow = secondFileData.get(rowIndex);

				// Verifica se a linha tem a estrutura esperada
				if (secondRow != null && secondRow.length > 1 && salaAtribuida.equals(secondRow[1])) {
					// Verifica a partir da coluna 5
					for (int colIndex = 5; colIndex < secondRow.length; colIndex++) {
						// Verifica se o índice é válido
						if (colIndex < secondFileData.get(0).length && "X".equalsIgnoreCase(secondRow[colIndex])) {
							colunasComX.append(secondFileData.get(0)[colIndex]).append(" ");
						}
					}
					break; // Interrompe após encontrar a linha correspondente
				}
			}
		}

		return colunasComX.toString().trim();
	}

	// Métodos de leitura de arquivo semelhantes aos do primeiro arquivo
	private static List<String[]> readCSV(File file) {
		List<String[]> data = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;

			// Lê a linha de cabeçalho
			line = br.readLine(); // Lê o cabeçalho
			String[] header = line.split(";"); // Obtém os nomes das colunas
			data.add(header);

			// Lê as linhas do arquivo
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";"); // Assumindo ponto e vírgula como delimitador
				data.add(values);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	private static File chooseCSVFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}

		return null;
	}

	public static int tryParseInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
	    } catch (NumberFormatException e) {
	        return defaultValue;
	    }
	}
}


