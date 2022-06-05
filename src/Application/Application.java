package Application;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.lucene.queryparser.classic.ParseException;

import Application.indexer.Indexer;
import Application.search.Searcher;

@SuppressWarnings("unused")
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		/*
		// testing indexer
		Indexer index = new Indexer();
		
		if(args.length == 0) {
			System.out.println("Running in default setting at src/index");
			try {
				index.createIndex("src//index");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Directory not found");
			}
		}
		else if(args.length == 1) {
			System.out.println("Create index at " + args[0]);
			try {
				index.createIndex(args[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		/*
		// testing search function
		Searcher search = new Searcher();
		String indexDir = "src//index";
		try {
			System.out.println(search.search(indexDir, "auburn").toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Index not found");
		} catch (ParseException e) {
			System.out.println("Parse failed");
		}
		*/
		
		SpringApplication.run(Application.class, args);
	}
}
