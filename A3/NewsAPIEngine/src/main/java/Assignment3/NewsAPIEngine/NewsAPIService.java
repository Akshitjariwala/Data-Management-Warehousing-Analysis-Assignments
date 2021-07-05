package Assignment3.NewsAPIEngine;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;

public class NewsAPIService {

    static NewsApiClient newsApiClient = new NewsApiClient("261aabbe9e3e473c9e8bb80b58effdd9");
    static MongoClient dbClient = MongoClients.create();
    static MongoDatabase database = dbClient.getDatabase("myMongoNews");
    static MongoCollection<Document> articleCollection = database.getCollection("newsArticles");

    public static void extractionEngine() {

            ArrayList<String> wordList = new ArrayList<String>();
		    wordList.add("Canada");
		    wordList.add("University"); wordList.add("Dalhousie");
		    wordList.add("Halifax"); wordList.add("Canada Education");
		    wordList.add("Moncton"); wordList.add("Toronto");

		    for(String keyWord: wordList) {
            newsApiClient.getEverything(
                    new EverythingRequest.Builder()
                            .q(keyWord).pageSize(100).build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            int i = 0;
                            File file = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/RawFiles/RawData.json");
                            while (response != null && i < 100) {
                                String fileName = keyWord + ".json";
                                try {
                                    JSONObject jsonArticle = new JSONObject();
                                    FileWriter writer = new FileWriter("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/RawFiles/" + fileName, true);

                                    if (response.getArticles().get(i).getTitle() != null) {
                                        jsonArticle.put("Title", filtrationEngine(response.getArticles().get(i).getTitle()));
                                    } else {
                                        jsonArticle.put("Title", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getDescription() != null) {
                                        jsonArticle.put("Description", filtrationEngine(response.getArticles().get(i).getDescription().replace("\n", " ").replace("\r", " ")));
                                    } else {
                                        jsonArticle.put("Description", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getAuthor() != null) {
                                        jsonArticle.put("Author", filtrationEngine(response.getArticles().get(i).getAuthor()));
                                    } else {
                                        jsonArticle.put("Author", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getUrl() != null) {
                                        jsonArticle.put("URL", response.getArticles().get(i).getUrl());
                                    } else {
                                        jsonArticle.put("URL", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getPublishedAt() != null) {
                                        jsonArticle.put("Published On", filtrationEngine(response.getArticles().get(i).getPublishedAt()));
                                    } else {
                                        jsonArticle.put("Published On", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getUrlToImage() != null) {
                                        jsonArticle.put("UrlToImage", response.getArticles().get(i).getUrlToImage());
                                    } else {
                                        jsonArticle.put("UrlToImage", "No Data Available");
                                    }

                                    if (response.getArticles().get(i).getSource() != null) {
                                        jsonArticle.put("Source", String.valueOf(response.getArticles().get(i).getSource()));
                                    } else {
                                        jsonArticle.put("Source", "No Data Available");
                                    }
                                    System.out.println(jsonArticle);
                                    writer.write(String.valueOf(jsonArticle));
                                    writer.write(System.getProperty("line.separator"));
                                    writer.close();
                                    i++;
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            try {
                                createDatChunks(keyWord);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    }
            );
        }
       /* System.exit(1);*/
    }

    public static void createDatChunks(String keyWord) throws IOException {
        File file = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/RawFiles/"+keyWord+".json");
        FileReader fileReader = new FileReader(file);
        BufferedReader br=new BufferedReader(fileReader);
        StringBuffer sb=new StringBuffer();
        String articleLine;
        int count=0,fileCount=1;

            String cleanFile = "";

            while((articleLine = br.readLine())!= null){
                Article article = new Article();
                try {
                    if(count<5){
                        cleanFile = keyWord+fileCount+".json";
                        FileWriter writer = new FileWriter("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/CleanJsonFiles/"+cleanFile,true);
                        article.settitle(filtrationEngine(customJSONParser(articleLine,"Title")));
                        article.setdescription(filtrationEngine(customJSONParser(articleLine,"Description")));
                        article.setauthor(filtrationEngine(customJSONParser(articleLine,"Author")));
                        article.seturlToImage(customJSONParser(articleLine,"UrlToImage"));
                        article.seturl(customJSONParser(articleLine,"URL"));
                        article.setsource(customJSONParser(articleLine,"Source"));
                        article.setpublished(filtrationEngine(customJSONParser(articleLine,"Published On")));
                        saveDataToMongoDB(article);
                        writer.write(articleLine);
                        writer.write(System.getProperty( "line.separator" ));
                        writer.close();
                        count++;
                    }else{
                        fileCount++;
                        cleanFile = keyWord+fileCount+".json";
                        FileWriter writer = new FileWriter("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/CleanJsonFiles/"+cleanFile,true);
                        article.settitle(filtrationEngine(customJSONParser(articleLine,"Title")));
                        article.setdescription(filtrationEngine(customJSONParser(articleLine,"Description")));
                        article.setauthor(filtrationEngine(customJSONParser(articleLine,"Author")));
                        article.seturlToImage(customJSONParser(articleLine,"UrlToImage"));
                        article.seturl(customJSONParser(articleLine,"URL"));
                        article.setsource(customJSONParser(articleLine,"Source"));
                        article.setpublished(filtrationEngine(customJSONParser(articleLine,"Published On")));
                        saveDataToMongoDB(article);
                        writer.write(articleLine);
                        writer.write(System.getProperty( "line.separator" ));
                        writer.close();
                        count=1;
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    // Filtration Engine to Clean the data.
    public static String filtrationEngine(String line) throws IOException, JSONException {
                    // Removing Html components from articles
                    line = line.replace("<ol>","").replace("<li>","").replace("</ol>","").replace("</li>","");
                    // Removing URLs from articles
                    line = line.replaceAll("\\W*[A-Za-z0-9]+\\.[a-z]{2,3}\\W*","");
                    // Removing All non characters from articles.
                    line = line.replaceAll("[^a-zA-Z0-9' '-,]"," ").replace("[']","").replace(" .",".");
                    // Removing multiple .s from articles.
                    line = line.replaceAll("[.]+",".");
                    // Removing multiple spaces from articles.
                    line = line.replaceAll("\\s+"," ");
                    return line;
    }

    // Save the cleaned data in MongoDB database.
    public static void saveDataToMongoDB(Article articleObject) throws JSONException {
        if(dbClient != null){
            Document document = new Document("Title",articleObject.gettitle()).append("Description",articleObject.getdescription()).append("Published On",articleObject.getpublished()).append("URLToImage",articleObject.geturlToImage()).append("Author",articleObject.getauthor()).append("URL",articleObject.geturl()).append("Source",articleObject.getsource());
            articleCollection.insertOne(document);
        }else{
            System.out.println("Database Connection failed.");
        }

    }

    public static String customJSONParser(String data, String value) {
        int length = value.length();
        String finalText = "";
        String subString = "";
        int valueIndex = data.indexOf(value);
        int dataIndex = valueIndex + length + 3;
        subString = data.substring(dataIndex);
        String endOfData = "\",\"";
        int endOfDataIndex = subString.indexOf(endOfData);
        if(endOfDataIndex == -1){
            endOfDataIndex = subString.indexOf("\"}");
        }
        for (int i = 0; i < endOfDataIndex; i++)
        {
            finalText = finalText+subString.charAt(i);
        }
        return finalText;
    }
}
