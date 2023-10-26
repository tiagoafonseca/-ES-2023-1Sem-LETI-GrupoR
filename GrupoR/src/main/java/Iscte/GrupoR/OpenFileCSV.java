package Iscte.GrupoR;


	import javax.swing.*;
	import java.awt.*;
	import java.awt.event.*;
	import java.io.*;
	import java.util.ArrayList;
	import java.util.List;

	public class OpenFileCSV {

	    public static void main(String[] args) {
	        JFrame frame = new JFrame("Ler Arquivo CSV Manualmente");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(1080, 720);

	        JPanel panel = new JPanel();
	        frame.add(panel);

	        JButton openButton = new JButton("Escolher Arquivo CSV");
	        panel.add(openButton);

	        openButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                JFileChooser fileChooser = new JFileChooser();
	                int returnValue = fileChooser.showOpenDialog(null);

	                if (returnValue == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileChooser.getSelectedFile();
	                    try {
	                        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
	                        String line;
	                        List<String[]> records = new ArrayList<>();

	                        while ((line = reader.readLine()) != null) {
	                            String[] fields = line.split(",");
	                            records.add(fields);
	                        }

	                        for (String[] record : records) {
	                            for (String field : record) {
	                                System.out.print(field);
	                            }
	                            System.out.println();
	                        }

	                        reader.close();
	                    } catch (IOException ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }
	        });

	        frame.setVisible(true);
	    }
	}


