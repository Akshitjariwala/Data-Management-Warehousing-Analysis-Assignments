package Assignment4.SentimentAnalysis;

import com.mongodb.MongoTimeoutException;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SentimentAnalysis {

    static MongoClient mongoClient = MongoClients.create();
    static MongoDatabase database = mongoClient.getDatabase("myMongoNews");
    static MongoCollection<Document> articleCollection = database.getCollection("newsArticles");

    public void ArticleSentimentAnalysis() throws IOException {
        FindIterable<Document> newArticles = articleCollection.find();
        File positiveFile = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment4/SentimentAnalysis/positive-words.txt");
        File negativeFile = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment4/SentimentAnalysis/negative-words.txt");
        List<String> positiveList = new ArrayList<>();
        List<String> negativeList = new ArrayList<>();
        String sentimentWords;
        int countNumber =0;
        List<String> listOfSentimentWords = new ArrayList<>();
        int positive=0;
        int negative=0;
    
    
        try{
            if(mongoClient!= null) {
            System.out.println("Database Connected Successfully.");
            } else {
                System.out.println("Database Not Connected.");
            }

        BufferedReader reader = new BufferedReader(new FileReader(positiveFile));
        while((sentimentWords = reader.readLine()) != null){
            positiveList.add(sentimentWords.trim());
        }

        reader = new BufferedReader(new FileReader(negativeFile));
        while((sentimentWords = reader.readLine()) != null){
            negativeList.add(sentimentWords.trim());
        }

        Iterator articleIterator = newArticles.iterator();
        List<String> articleList = new ArrayList<>();

        /*Fetching Articles from MongoDB Database*/
        while(articleIterator.hasNext()){
            Document doc = (Document) articleIterator.next();
            articleList.add(doc.get("Description").toString());
        }
    
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%10s |%90s |%50s |%10s |%15s%n","Number","Article","Match","Polarity","Polarity Value");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        /*Calculating Polarity for each Article.*/
        for(String article:articleList) {
            listOfSentimentWords = new ArrayList<>();
            positive = 0;
            negative = 0;
            int polarityInt;
            String polarity;
            countNumber++;
            article = article.replace("'s", " ").replace(",", " ").replace("'", "").replace("(", "").replace(")", "");
            article = article.replaceAll("\\s+", " ");
            String[] words = article.split("\\s+");
            List<String> wordList = new ArrayList<>();
            HashMap<String, Integer> bagOfWords = new HashMap<>();

            for (int i = 0; i < words.length; i++) {
                wordList.add(words[i]);
            }

            for (int i = 0; i < wordList.size() - 1; i++) {
                int count = 1;
                for (int j = i + 1; j < wordList.size() - 2; j++) {
                    if (wordList.get(i).equals(wordList.get(j))) {
                        wordList.set(j, "NA");
                        count++;
                    } else {
                        continue;
                    }
                }
                if (!wordList.get(i).equals("NA")) {
                    bagOfWords.put(wordList.get(i), count);
                }
            }

            Iterator<Map.Entry<String, Integer>> iterator = bagOfWords.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Integer> bagWord = iterator.next();
                if (positiveList.contains(bagWord.getKey())) {
                    positive = positive + bagWord.getValue();
                    listOfSentimentWords.add(bagWord.getKey());
                } else if (negativeList.contains(bagWord.getKey())) {
                    negative = negative + bagWord.getValue();
                    listOfSentimentWords.add(bagWord.getKey());
                } else {
                }
            }

            polarityInt = positive - negative;

            if (polarityInt > 0) {
                polarity = "Positive";
            } else if (polarityInt < 0) {
                polarity = "Negative";
            } else {
                polarity = "Neutral";
            }

            System.out.printf("%10s |%90s |%50s |%10s |%15s%n", countNumber, truncateString(article), listOfSentimentWords.toString(), polarity, ((polarityInt < 0) ? "-" : "") + Math.abs(polarityInt));
            bagOfWords.clear();
        }
        } catch(Exception e) {
            MongoTimeoutException exception;
        }
    }

    public static String truncateString(String article)
    {
        return article.length() <= 80 ? article : article.substring(0,80) + "...";
    }

}
