package Assignment3.NewsAPIEngine;

import Assignment3.WordCounter.WordCounterEngine;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class NewsApiEngineApplication {

	public static void main(String[] args) throws IOException, JSONException {
		SpringApplication.run(NewsApiEngineApplication.class, args);

		NewsAPIService service = new NewsAPIService();
		WordCounterEngine wordcounter = new WordCounterEngine();

		Scanner scanner = new Scanner(System.in);
		System.out.println("1. Extraction Engine.");
		System.out.println("2. Run MapReduce Engine.");
		int choice = scanner.nextInt();

		switch(choice){
			case 1 : service.extractionEngine();break;
			case 2 : wordcounter.counterEngine();break;
		}
	}

}
