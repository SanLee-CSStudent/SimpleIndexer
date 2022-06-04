package Runner;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;

import Indexer.Indexer;
import Searcher.Searcher;

public class Main {
	public static void main(String[] args) {
		
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
	}
}
