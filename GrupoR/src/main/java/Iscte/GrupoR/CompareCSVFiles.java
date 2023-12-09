package Iscte.GrupoR;

import javax.swing.*;
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
		File firstFile = chooseCSVFile();
		List<String[]> firstFileData = readCSV(firstFile);

		File secondFile = chooseCSVFile();
		List<String[]> secondFileData = readCSV(secondFile);

		Map<String, String> salaCapacidadeMap = new HashMap<>();

		for (String[] row : secondFileData.subList(1, secondFileData.size())) {
			String sala = (row.length > 1) ? row[1] : "-";
			String capacidadeNormal = (row.length > 2) ? row[2] : "-";

			salaCapacidadeMap.put(sala, capacidadeNormal);
		}

		List<String[]> outputTable = new ArrayList<>();
		outputTable.add(new String[] { "Turma", "Dia", "Hora Inicio", "Data Aula", "Sala Atribuida", "Numero Inscritos",
				"Número de Lugares Normal", "Colunas com 'X'", "Caracteristicas da Sala Pedida", "Ideal=0/Falso=1",
				"ComparacaoInscritosLugares", "SomaUltimasColunas", "ResultadoFinal" });

		for (String[] firstRow : firstFileData.subList(1, firstFileData.size())) {
			String salaAtribuida = (firstRow.length > 9) ? firstRow[9] : "-";
			String turma = (firstRow.length > 3) ? firstRow[3] : "-";
			String dia = (firstRow.length > 5) ? firstRow[5] : "-";
			String horaInicio = (firstRow.length > 6) ? firstRow[6] : "-";
			String numeroInscritos = (firstRow.length > 4) ? firstRow[4] : "-";
			String numLugaresNormal = salaCapacidadeMap.getOrDefault(salaAtribuida, "--");
			String dataaula = (firstRow.length > 7) ? firstRow[7] : "-";
			String CaracteristicaSalaPedida = (firstRow.length > 10) ? firstRow[10] : "";
			String colunasComX = getColunasComX(secondFileData, salaAtribuida, CaracteristicaSalaPedida);

			// Comparar cada string individual com a característica da sala pedida
			String[] caracteristicasArray = colunasComX.split("\\|");
			int resultadoComparacao = 1; // Inicializar como 1 (se não houver correspondência)

			for (String caracteristica : caracteristicasArray) {
				if (caracteristica.trim().compareToIgnoreCase(CaracteristicaSalaPedida) == 0) {
					resultadoComparacao = 0;
					break; // Se encontrar uma correspondência, atribuir 0 e interromper o loop
				}
			}

			// Comparar "Numero Inscritos" e "Número de Lugares Normal"
			int comparacaoInscritosLugares = compareInscritosLugares(numeroInscritos, numLugaresNormal);

			// Calcular a soma das últimas duas colunas
			int somaUltimasColunas = resultadoComparacao + comparacaoInscritosLugares;

			// Determinar se é "Ideal" ou "Não Ideal" com base na soma
			String resultadoFinal = (somaUltimasColunas == 0) ? "Ideal" : "Não Ideal";

			outputTable.add(new String[] { turma, dia, horaInicio, dataaula, salaAtribuida, numeroInscritos,
					numLugaresNormal, colunasComX, CaracteristicaSalaPedida, String.valueOf(resultadoComparacao),
					String.valueOf(comparacaoInscritosLugares), String.valueOf(somaUltimasColunas), resultadoFinal });
		}

		// Imprimir a tabela
		for (String[] row : outputTable) {
			System.out.println(Arrays.toString(row));
		}
	}

	public static String getColunasComX(List<String[]> secondFileData, String salaAtribuida,
			String caracteristicaSalaPedida) {
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
							colunasComX.append(secondFileData.get(0)[colIndex]).append("|");
						}
					}
					break; // Interrompe após encontrar a linha correspondente
				}
			}
		}

		return colunasComX.toString().trim();
	}

	public static int compareInscritosLugares(String numeroInscritos, String numLugaresNormal) {
		int inscritos = tryParseInt(numeroInscritos, 0);
		int lugaresNormais = tryParseInt(numLugaresNormal, 0);

		return (inscritos > lugaresNormais) ? 1 : 0;
	}

	public static List<String[]> readCSV(File file) {
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

	public static File chooseCSVFile() {
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
