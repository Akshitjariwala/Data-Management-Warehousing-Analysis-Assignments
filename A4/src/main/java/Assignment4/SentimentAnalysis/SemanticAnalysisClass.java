package Assignment4.SentimentAnalysis;

import com.mongodb.client.*;
import org.bson.Document;
import java.lang.Math;
import java.util.*;

public class SemanticAnalysisClass {
    
    static MongoClient mongoClient = MongoClients.create();
    static MongoDatabase database = mongoClient.getDatabase("myMongoNews");
    static MongoCollection<Document> articleCollection = database.getCollection("newsArticles");
    
    public void semanticAnalysis(){
        FindIterable<Document> newArticles = articleCollection.find();
        Iterator articleIterator = newArticles.iterator();
        List<String> articleList = new ArrayList<>();
        int totalArticles;
        String[] searchKeywords = {"Canada","Moncton","Toronto"};
        int articleCount;
        String format = "";
        String headingFormat = "%-20s%-90s%n";
        int tf_idf;
        Map<String,List<Integer>> canadaArticleFrequency = new HashMap<String,List<Integer>>();
        List<Integer> frequency = new ArrayList<>();
        int totalCanadaCount=0;
        String articleName;
        int articleC=1;
        Map<String,Float> frequencyMap = new HashMap<String,Float>();
        float maxFrequency=0;
        String maxFreqArticle=null;
        
        
        if(mongoClient!= null) {
            System.out.println("Database Connected Successfully.");
        } else {
            System.out.println("Database Not Connected.");
        }
        
        /*Fetching Articles from MongoDB Database*/
        while(articleIterator.hasNext()){
            Document doc = (Document) articleIterator.next();
            articleList.add(doc.get("Description").toString());
        }
    
        totalArticles = articleList.size();
    
        System.out.println("\n\n------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf(headingFormat,"Total Documents(N) : ",totalArticles);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%s |%33s |%60s |%25s%n","Search Query","Documents Containing Terms(df)","Total Documents(N)/ number of documents term appeared (df)","Log10(N/df)");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
        
        /* Printing TF-IDF (term frequency-inverse document frequency) table */
       
        for(int i=0;i<searchKeywords.length;i++){
            articleCount=0;
            for(String article:articleList){
                article = article.replace("'s", " ").replace(",", " ").replace("'", "").replace("(", "").replace(")", "");
                article = article.replaceAll("\\s+", " ");
                String[] articleStr = article.split("\\s+");
                if(Arrays.asList(articleStr).contains(searchKeywords[i])){
                    articleCount++;
                }
            }
            tf_idf = totalArticles/articleCount;
            System.out.printf("%12s |%33s |%60s |%25s%n",searchKeywords[i],articleCount,totalArticles+"/"+articleCount,Math.log10(tf_idf));
        }
        
        /*Fetching frequency of word #Canada*/
    
        for(String article:articleList){
            article = article.replace("'s", " ").replace(",", " ").replace("'", "").replace("(", "").replace(")", "");
            article = article.replaceAll("\\s+", " ");
            String[] articleStr = article.split("\\s+");
            if(Arrays.asList(articleStr).contains("Canada")){
                totalCanadaCount++;
            }
        }
        
        System.out.println("\n\n");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%s%5s%n","Term : ","Canada");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%s |%25s |%25s |%25s %n","Canada appeared in "+totalCanadaCount+" Documents","Total Words (m)","Frequency (f)","f/m");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    
        int sum=0;
        for(String article:articleList){
            articleName="Article #";
            article = article.replace("'s", " ").replace(",", " ").replace("'", "").replace("(", "").replace(")", "");
            article = article.replaceAll("\\s+", " ");
            String[] articleStr = article.split("\\s+");
            if(Arrays.asList(articleStr).contains("Canada")){
                int canadaCount=0;
                articleName = articleName+articleC;
                articleC++;
                for(int i=0;i<articleStr.length;i++){
                    if(articleStr[i].equals("Canada")){
                        canadaCount++;
                    }
                }
                float freq  = (float)canadaCount/articleStr.length;
                frequencyMap.put(articleName,freq);
                System.out.printf("%32s |%25s |%25s |%25s%n",articleName,articleStr.length,canadaCount,freq);
            }
        }

        Iterator<Map.Entry<String, Float>> iterator = frequencyMap.entrySet().iterator();
        
        while(iterator.hasNext()){
            Map.Entry<String,Float> map = iterator.next();
            if(map.getValue() > maxFrequency){
                maxFrequency = map.getValue();
                maxFreqArticle = map.getKey();
            }
        }
    
        System.out.println("\n\n---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("Article With Maximum Frequency : "+maxFreqArticle+"      Maximum Frequency : "+maxFrequency);
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------");
        
    }
}
