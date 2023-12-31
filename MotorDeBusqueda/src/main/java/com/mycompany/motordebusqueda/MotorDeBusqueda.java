/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.motordebusqueda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MotorDeBusqueda {

    private static final String GOOGLE_API_KEY = "AIzaSyB6GKs-GXtLRyTHKvaaiB2meBOVMbKZY5E"; 
    private static final String CUSTOM_SEARCH_ENGINE_ID = "f0ad635cb038a4099"; 
    private static final int RESULTS_PER_PAGE = 10; 
    private static int currentPage = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showSearchEngine());
    }

    private static void showSearchEngine() {
        JFrame frame = new JFrame("Motor de Búsqueda de Google");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        JTextArea resultsArea = new JTextArea(15, 40);
        resultsArea.setEditable(false);

        JButton previousPageButton = new JButton("Página anterior");
        JButton nextPageButton = new JButton("Página siguiente");
        JLabel pageInfoLabel = new JLabel();

        searchButton.addActionListener((ActionEvent e) -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                currentPage = 1; 
                updateResults(query, resultsArea, pageInfoLabel);
            }
        });

        previousPageButton.addActionListener((ActionEvent e) -> {
            if (currentPage > 1) {
                currentPage--;
                updateResults(searchField.getText(), resultsArea, pageInfoLabel);
            }
        });

        nextPageButton.addActionListener((ActionEvent e) -> {
            currentPage++;
            updateResults(searchField.getText(), resultsArea, pageInfoLabel);
        });

        JPanel panel = new JPanel();
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(previousPageButton);
        panel.add(nextPageButton);
        panel.add(pageInfoLabel);

        JScrollPane scrollPane = new JScrollPane(resultsArea);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        frame.setMinimumSize(new Dimension(900, 700));
        frame.pack();
        frame.setVisible(true);
    }

    private static void updateResults(String query, JTextArea resultsArea, JLabel pageInfoLabel) {
        int start = (currentPage - 1) * RESULTS_PER_PAGE + 1;
        try {
            List<GoogleSearchItem> results = performGoogleSearch(query, start, RESULTS_PER_PAGE);
            resultsArea.setText("");

            results.stream().forEach(result -> {
                resultsArea.append(result.getTitle() + " - " + result.getLink() + "\n\n");
            });

            pageInfoLabel.setText("Página " + currentPage);
        } catch (IOException ex) {
            resultsArea.setText("Error en la búsqueda: " + ex.getMessage());
        }
    }

    public static List<GoogleSearchItem> performGoogleSearch(String query, int start, int numResults) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        String apiKey = GOOGLE_API_KEY;
        String customSearchEngineId = CUSTOM_SEARCH_ENGINE_ID;
        String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + apiKey
                + "&cx=" + customSearchEngineId
                + "&q=" + query
                + "&start=" + start
                + "&num=" + numResults;

        Request request = new Request.Builder()
                .url(searchUrl)
                .build();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Error en la solicitud: " + response.code());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ResponseBody responseBody = response.body();

        if (responseBody == null) {
            throw new IOException("Respuesta vacía");
        }

        GoogleSearchResponse searchResponse = objectMapper.readValue(responseBody.byteStream(), GoogleSearchResponse.class);

        return searchResponse.getItems();
    }
}
