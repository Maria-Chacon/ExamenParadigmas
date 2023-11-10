package com.mycompany.analisisdedatosreddit;

import java.io.IOException;
import java.util.Arrays;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.http.ParseException;

public class AnalisisDeDatosReddit {

    private final CloseableHttpClient httpClient;
    private final String apiUrl = "https://www.reddit.com/r/{subreddit}/.json";

    public AnalisisDeDatosReddit() {
        this.httpClient = HttpClients.createDefault();
    }

    public String getDataReddit(String topic) {
        try {
            System.out.println("Realizando solicitud HTTP a la API de Reddit para el tema: " + topic);

            HttpGet request = new HttpGet(apiUrl.replace("{subreddit}", topic));
            String responseJson;
            try ( CloseableHttpResponse response = httpClient.execute(request)) {
                System.out.println("Solicitud HTTP completada");

                responseJson = EntityUtils.toString(response.getEntity());
            }

            System.out.println("Respuesta JSON obtenida");

            return responseJson;
        } catch (IOException | ParseException e) {
            return null;
        }
    }

    public Map<String, Long> countKeywordsFrequency(String text, String[] keywords) {
        return Arrays.stream(keywords)
                .collect(Collectors.toMap(keyword -> keyword, keyword -> countKeywordOccurrences(text, keyword)));
    }

    private long countKeywordOccurrences(String text, String keyword) {
        return Arrays.stream(text.toLowerCase().split("\\s+"))
                .filter(word -> word.contains(keyword.toLowerCase()))
                .count();
    }

    public void showResults(Map<String, Long> keywordCounts) {
        System.out.println("Keyword Frequency:");
        System.out.println("+----------------------------+");
        System.out.println("| Palabra clave | Frecuencia |");
        System.out.println("+----------------------------+");

        keywordCounts.forEach((keyword, frequency)
                -> System.out.printf("| %-15s | %-10d|\n", keyword, frequency));

        System.out.println("+----------------------------+");
    }

    public static void main(String[] args) {
        AnalisisDeDatosReddit redditDataAnalysis = new AnalisisDeDatosReddit();
        int waitTime = 2000;

        while (true) {
            String redditData = redditDataAnalysis.getDataReddit("politics");

            if (redditData != null && !redditData.contains("Too Many Requests")) {
                Map<String, Long> keywordCounts = redditDataAnalysis.countKeywordsFrequency(redditData, new String[]{"politics", "government", "president", "country"});
                redditDataAnalysis.showResults(keywordCounts);
                break;
            } else {
                System.out.println("Límite de solicitudes alcanzado o se produjo un error al obtener los datos. Esperando " + (waitTime / 1000) + " segundos antes de reintentar.");
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
