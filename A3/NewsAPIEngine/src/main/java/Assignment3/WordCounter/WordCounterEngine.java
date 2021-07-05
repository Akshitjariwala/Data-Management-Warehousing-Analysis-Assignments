package Assignment3.WordCounter;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.*;
import java.util.*;

public class WordCounterEngine {

    public static void counterEngine() throws IOException {

        Collection<Map<String,Integer>> mapCollection = new HashSet<Map<String,Integer>>();
        SparkConf configuration = new SparkConf().setMaster("local").setAppName("Spark Word Counter Engine");
        JavaSparkContext context = new JavaSparkContext(configuration);
        JavaRDD<String> articleFile = context.textFile("word.txt");
        Map<String,Integer> reducerMap;
        File diretory = new File("/home/akshitjariwala96/");
        /*File diretory = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/CleanJsonFiles/");
*/
        String fileList[] = diretory.list();
        for(int i=0;i<fileList.length;i++){
            Map<String,Integer> map = WCMapper(fileList[i],articleFile);
            mapCollection.add(map);
        }

        reducerMap = WCReducer(mapCollection);
        MinMaxFunction(reducerMap);
    }


    // Spark Mapper.
    public static Map<String, Integer> WCMapper(String fileName,JavaRDD<String> articleFile) throws IOException {

        String filePath = "/home/wordCounter/word.txt";
        File file = new File("/home/akshitjariwala96/"+fileName);
        /*  String filePath = "D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/word.txt";
         *  File file = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment3/NewsAPIEngine/CleanJsonFiles/"+fileName);
         */

        FileReader fileReader = new FileReader(file);
        BufferedReader br=new BufferedReader(fileReader);
        String articleLine = "";
        while((articleLine = br.readLine())!= null){
            FileWriter wordFileWriter = new FileWriter(filePath,true);
            wordFileWriter.write(customJSONParser(articleLine,"Title").trim());
            wordFileWriter.write(System.getProperty( "line.separator" ));
            wordFileWriter.write(customJSONParser(articleLine,"Description").trim());
            wordFileWriter.write(System.getProperty( "line.separator" ));
            wordFileWriter.close();
        }

        File wordFile = new File(filePath);
        ArrayList<String> wordList = new ArrayList<String>();

        // Dividing File into array of words.
        articleFile.collect().forEach(line -> {
            line = line.replace("\n",". ").replace("\r",". ");
            if(line.contains("Nova Scotia")){
                wordList.add("Nova Scotia");
            }
            String words[] = line.split(" ");
            for(String word:words){
                wordList.add(word);
            }
        });

        Map<String,Integer> wordMap = new HashMap<String,Integer>();
        for(int i=0;i<wordList.size()-1;i++){
            int count=1;
            for (int j = i + 1; j < wordList.size() - 2; j++) {
                if (wordList.get(i).equals(wordList.get(j))) {
                    wordList.set(j,"NA");
                    count++;
                } else {
                    continue;
                }
            }
            if(!wordList.get(i).equals("NA")){
                wordMap.put(wordList.get(i), count);
            }
        }

        if(!wordFile.delete()){
            System.out.println("Failed to delete the file");
        }

        return wordMap;
    }

    // Custom JSON Parser.
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

    // Spark Reducer.
    public static Map<String,Integer> WCReducer(Collection<Map<String,Integer>> mapCollection){

        Iterator<Map<String,Integer>> collectionIterator = mapCollection.iterator();
        int newValue;
        Map<String,Integer> wordCountMap = new HashMap<>();
        Map<String,Integer> tempMap;
        String[] wordArray = {"Canada","Nova Scotia","education","higher","learning","city","accommodation","price"};

        while(collectionIterator.hasNext()){
            tempMap = collectionIterator.next();
            for(String word: wordArray){
                if(tempMap.containsKey(word)){
                    if(wordCountMap.containsKey(word)){
                        newValue = wordCountMap.get(word) + tempMap.get(word);
                        wordCountMap.put(word,newValue);
                    }else{
                        wordCountMap.put(word,tempMap.get(word));
                    }
                }else{
                    continue;
                }
            }
        }

        for(String word: wordArray){
            if(wordCountMap.containsKey(word)){
                continue;
            }else{
                wordCountMap.put(word,0);
            }
        }

        System.out.println(wordCountMap);

        return wordCountMap;
    }

    public static void MinMaxFunction(Map<String,Integer> wordCountMap){

        String max = null;
        String min = null;
        int minInt=0, maxInt=0;

        for(Map.Entry<String,Integer> map : wordCountMap.entrySet()){
            if(map.getValue() > maxInt){
                maxInt = map.getValue();
                max = map.getKey();
            }else{
                continue;
            }
        }

        for(Map.Entry<String,Integer> map : wordCountMap.entrySet()) {
            if(map.getValue() == 0){
                minInt = 0;
                min = map.getKey();
            }else if(map.getValue() < minInt){
                minInt = map.getValue();
                min = map.getKey();
            }
            else{
                continue;
            }
        }

        System.out.println("Word with minimum frequency : "+min);
        System.out.println("Word with maximum frequency : "+max);
    }
}


