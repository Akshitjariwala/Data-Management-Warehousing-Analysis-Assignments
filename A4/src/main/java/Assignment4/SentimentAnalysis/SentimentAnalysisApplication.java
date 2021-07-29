package Assignment4.SentimentAnalysis;

import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoCredential;
import com.mongodb.client.*;
import java.io.*;
import java.util.*;

@SpringBootApplication
public class SentimentAnalysisApplication {



	public static void main(String[] args) throws IOException {
		SpringApplication.run(SentimentAnalysisApplication.class, args);

		SentimentAnalysis analysis = new SentimentAnalysis();
		SemanticAnalysisClass semantic = new SemanticAnalysisClass();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Select Options from below.");
		System.out.println("1. Sentiment Analysis");
		System.out.println("2. Semantic Analysis");
		int choice = scanner.nextInt();
		
		switch(choice) {
			case 1 :    analysis.ArticleSentimentAnalysis(); break;
			case 2 :    semantic.semanticAnalysis(); break;
		}
	}

}
