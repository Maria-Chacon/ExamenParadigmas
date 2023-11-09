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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.ExecutionException;
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                if (!query.isEmpty()) {
                    resultsArea.setText("");
                    try {
                        List<GoogleSearchItem> results = performGoogleSearch(query);
                        for (GoogleSearchItem result : results) {
                            resultsArea.append(result.getTitle() + " - " + result.getLink() + "\n");
                        }
                    } catch (IOException ex) {
                        resultsArea.setText("Error en la búsqueda: " + ex.getMessage());
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(searchField);
        panel.add(searchButton);

        JScrollPane scrollPane = new JScrollPane(resultsArea);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static List<GoogleSearchItem> performGoogleSearch(String query) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        String apiKey = GOOGLE_API_KEY;
        String customSearchEngineId = CUSTOM_SEARCH_ENGINE_ID;
        String searchUrl = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + customSearchEngineId + "&q=" + query;

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
