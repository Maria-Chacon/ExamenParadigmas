/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.analisisdedatos;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 *
 * @author usuario
 */
public class AnalisisDeDatos {

    public static void main(String[] args) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("S4pwt77tZ2asI43knTY2fmDi9")
                .setOAuthConsumerSecret("w3m3IBt1fyXoQbwUwyUyeZmpeFeKmfl7GAnUiS1CpRaqVw0APq")
                .setOAuthAccessToken("1722396608334024704-J8NYoaQ2yNCWlXowvs6iSv7r2IwgEz")
                .setOAuthAccessTokenSecret("d8AUNCj5UBOIYzs03AYA7YPzQw1LEN8pSrNJlMlVOr8NN");

        Configuration configuration = cb.build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();

        String topic = "política";

        try {
            Query query = new Query(topic);
            QueryResult result = twitter.search(query);
            List<Status> tweets = result.getTweets();

            Map<String, Integer> keywordFrequency = countKeywordFrequency(tweets, "política");
            Map<String, Double> sentimentScores = analyzeSentiment(tweets);

            displayResults(keywordFrequency, sentimentScores);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    // Función para contar la frecuencia de palabras clave
    private static Map<String, Integer> countKeywordFrequency(List<Status> tweets, String keyword) {
        return tweets.stream()
                .collect(Collectors.toMap(
                        Status::getText,
                        tweet -> (int) tweet.getText()
                                .toLowerCase()
                                .split(keyword.toLowerCase())
                                .length - 1
                ));
    }

    // Función para realizar el análisis de sentimiento
    private static Map<String, Double> analyzeSentiment(List<Status> tweets) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        return tweets.stream()
                .collect(Collectors.toMap(
                        Status::getText,
                        tweet -> {
                            Annotation annotation = new Annotation(tweet.getText());
                            pipeline.annotate(annotation);
                            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                                String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                                // Mapear etiquetas de sentimiento a valores numéricos según tu necesidad
                                double sentimentScore = mapSentimentToScore(sentiment);
                                return sentimentScore;
                            }
                            return 0.5; // Valor por defecto
                        }
                ));
    }

    // Función para mostrar los resultados
    private static void displayResults(Map<String, Integer> keywordFrequency, Map<String, Double> sentimentScores) {
        keywordFrequency.forEach((tweetText, frequency) -> {
            double sentimentScore = sentimentScores.get(tweetText);

            System.out.println("Tweet: " + tweetText);
            System.out.println("Frecuencia de la palabra clave: " + frequency);
            System.out.println("Sentimiento: " + sentimentScore);
            System.out.println();
        });
    }

    // Función para mapear etiquetas de sentimiento a valores numéricos (personalízala según tus necesidades)
    private static double mapSentimentToScore(String sentiment) {
        switch (sentiment) {
            case "Very Negative": return 0.0;
            case "Negative": return 0.25;
            case "Neutral": return 0.5;
            case "Positive": return 0.75;
            case "Very Positive": return 1.0;
            default: return 0.5; // Valor por defecto
        }
    }
}
