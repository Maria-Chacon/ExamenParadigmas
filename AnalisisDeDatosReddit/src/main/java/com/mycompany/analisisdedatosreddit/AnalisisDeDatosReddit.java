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
    private final String apiUrl = "https://www.reddit.com/r/";

    public AnalisisDeDatosReddit() {
        this.httpClient = HttpClients.createDefault();
    }

    public String getRedditData(String topic) {
        try {
            System.out.println("Realizando solicitud HTTP a la API de Reddit para el tema: " + topic);

            // Realiza la solicitud HTTP a la API de Reddit para obtener datos sobre el tema especificado.
            HttpGet request = new HttpGet(apiUrl + "subreddit/" + topic);
            CloseableHttpResponse response = httpClient.execute(request);

            System.out.println("Solicitud HTTP completada");

            // Lee la respuesta JSON
            String responseJson = EntityUtils.toString(response.getEntity());
            response.close();

            System.out.println("Respuesta JSON obtenida");

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

//    public String analyzeSentiment(String text) {
//        // Configura las propiedades para el análisis de sentimiento
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
//
//        // Crea un objeto StanfordCoreNLP con las propiedades
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//
//        // Crea una anotación con el texto a analizar
//        Annotation annotation = new Annotation(text);
//
//        // Procesa la anotación para realizar el análisis de sentimiento
//        pipeline.annotate(annotation);
//
//        // Obtiene el resultado del análisis de sentimiento
//        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
//            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
//            return sentiment;
//        }
//
//        // Si no se pudo determinar el sentimiento, puedes retornar un valor por defecto
//        return "Neutral";
//    }
    public void visualizeResults(Map<String, Integer> keywordCounts) {
        // Visualiza la frecuencia de palabras clave en la consola
        System.out.println("Keyword Frequency:");
        for (Map.Entry<String, Integer> entry : keywordCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Muestra el análisis de sentimiento en la consola
//        System.out.println("\nSentiment Analysis: " + sentimentAnalysis);
    }

    public static void main(String[] args) {
        AnalisisDeDatosReddit analyzer = new AnalisisDeDatosReddit();
        String redditData = analyzer.getRedditData("politica");
        Map<String, Integer> keywordCounts = analyzer.countKeywords(redditData, new String[]{"política", "elecciones", "gobierno", "partido"});
//        String sentimentAnalysis = analyzer.analyzeSentiment(redditData);
        analyzer.visualizeResults(keywordCounts);
    }
}
