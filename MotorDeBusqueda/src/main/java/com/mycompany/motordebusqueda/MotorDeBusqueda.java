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

   private static final String BING_API_KEY = "7514887a7e2a4434b0b14c13a7c4a453"; // Reemplaza con tu clave de API

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Motor de Búsqueda de Bing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Buscar");
        JTextArea resultsArea = new JTextArea(15, 40);
        resultsArea.setEditable(false);

        searchButton.addActionListener((ActionEvent e) -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                resultsArea.setText("");
                try {
                    List<BingSearchResult> results = performBingSearch(query);
                    for (BingSearchResult result : results) {
                        resultsArea.append(result.getName() + " - " + result.getUrl() + "\n");
                    }
                } catch (IOException ex) {
                    resultsArea.setText("Error en la búsqueda: " + ex.getMessage());
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

    public static List<BingSearchResult> performBingSearch(String query) throws IOException {
       OkHttpClient httpClient = new OkHttpClient();
        String url = "https://api.cognitive.microsoft.com/bing/v7.0/search?q=" + query;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Ocp-Apim-Subscription-Key", BING_API_KEY)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Error en la solicitud: " + response.code());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        BingSearchResponse searchResponse = objectMapper.readValue(response.body().byteStream(), BingSearchResponse.class);
        return searchResponse.getWebPages().getValue();
    }
}
