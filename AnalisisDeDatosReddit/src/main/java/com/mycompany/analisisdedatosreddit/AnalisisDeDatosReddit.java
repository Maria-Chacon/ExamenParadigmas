/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.analisisdedatosreddit;

import edu.stanford.nlp.ling.CoreAnnotations;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.util.Properties;

public class AnalisisDeDatosReddit {

    private final CloseableHttpClient httpClient;
    private final String apiUrl = "https://www.reddit.com/r/{subreddit}/.json";

    public AnalisisDeDatosReddit() {
        this.httpClient = HttpClients.createDefault();
    }

    public String getRedditData(String topic) {
        try {
            System.out.println("Realizando solicitud HTTP a la API de Reddit para el tema: " + topic);

            // Realiza la solicitud HTTP a la API de Reddit para obtener datos sobre el tema especificado.
            HttpGet request = new HttpGet(apiUrl.replace("{subreddit}", topic));
            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println("Solicitud HTTP completada");

            // Lee la respuesta JSON
            String responseJson = EntityUtils.toString(response.getEntity());
            response.close();

            System.out.println("Respuesta JSON obtenida");

            // Imprime la respuesta JSON
            System.out.println("Respuesta JSON: " + responseJson);

            // Procesa la respuesta JSON y devuelve los datos.
            return responseJson;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Integer> countKeywords(String text, String[] keywords) {
        Map<String, Integer> keywordCounts = new HashMap<>();

        // Dividir el texto en palabras (tokenización)
        String[] words = text.toLowerCase().split("\\s+"); // Divide el texto en palabras separadas por espacios

        for (String keyword : keywords) {
            // Inicializa el contador para cada palabra clave
            keywordCounts.put(keyword, 0);

            for (String word : words) {
                // Comprueba si la palabra clave está contenida en la palabra actual
                if (word.contains(keyword.toLowerCase())) {
                    // Aumenta el contador si la palabra clave se encuentra en la palabra actual
                    keywordCounts.put(keyword, keywordCounts.get(keyword) + 1);
                }
            }
        }

        return keywordCounts;
    }

    public void visualizeResults(Map<String, Integer> keywordCounts) {
        // Visualiza la frecuencia de palabras clave de manera visual o tabular
        System.out.println("Keyword Frequency:");
        System.out.println("+----------------------------+");
        System.out.println("| Palabra clave | Frecuencia |");
        System.out.println("+----------------------------+");

        for (Map.Entry<String, Integer> entry : keywordCounts.entrySet()) {
            String keyword = entry.getKey();
            String frequency = entry.getValue().toString();
            System.out.printf("| %-15s | %-10s|\n", keyword, frequency);
        }

        System.out.println("+----------------------------+");
    }

    public static void main(String[] args) {
        AnalisisDeDatosReddit analyzer = new AnalisisDeDatosReddit();
        int tiempoEspera = 2000; // 2 segundos

        while (true) {
            String redditData = analyzer.getRedditData("politics");

            // Verifica si la respuesta es diferente a "Too Many Requests" (código de error 429)
            if (redditData != null && !redditData.contains("Too Many Requests")) {
                Map<String, Integer> keywordCounts = analyzer.countKeywords(redditData, new String[]{"politics", "government", "president", "country"});
                analyzer.visualizeResults(keywordCounts);
                break; // Detiene el bucle
            } else {
                System.out.println("Límite de solicitudes alcanzado o se produjo un error al obtener los datos. Esperando " + (tiempoEspera / 1000) + " segundos antes de reintentar.");
                try {
                    Thread.sleep(tiempoEspera);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
