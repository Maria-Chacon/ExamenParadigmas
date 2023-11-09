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

/**
 *
 * @author usuario
 */
public class MotorDeBusqueda {

    private static final String GOOGLE_API_KEY = "AIzaSyB6GKs-GXtLRyTHKvaaiB2meBOVMbKZY5E"; // Reemplaza con tu clave de API
    private static final String CUSTOM_SEARCH_ENGINE_ID = "f0ad635cb038a4099"; // Reemplaza con tu ID de motor de búsqueda
    private static final int RESULTS_PER_PAGE = 10; // Número de resultados por página
    private static int currentPage = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Motor de Búsqueda de Google");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        JTextArea resultsArea = new JTextArea(15, 40);
        resultsArea.setEditable(false);

        JButton prevPageButton = new JButton("Página anterior");
        JButton nextPageButton = new JButton("Página siguiente");
        JLabel pageInfoLabel = new JLabel();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                if (!query.isEmpty()) {
                    currentPage = 1; // Reiniciar a la primera página
                    updateResults(query, resultsArea, pageInfoLabel);
                }
            }
        });

        prevPageButton.addActionListener((ActionEvent e) -> {
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
        panel.add(prevPageButton);
        panel.add(nextPageButton);
        panel.add(pageInfoLabel);

        JScrollPane scrollPane = new JScrollPane(resultsArea);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private static void updateResults(String query, JTextArea resultsArea, JLabel pageInfoLabel) {
        int start = (currentPage - 1) * RESULTS_PER_PAGE + 1;
        try {
            List<GoogleSearchItem> results = performGoogleSearch(query, start, RESULTS_PER_PAGE);
            resultsArea.setText("");
            for (GoogleSearchItem result : results) {
                resultsArea.append(result.getTitle() + " - " + result.getLink() + "\n\n");
            }
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
