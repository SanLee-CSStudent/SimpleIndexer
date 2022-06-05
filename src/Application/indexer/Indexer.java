package Application.indexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Indexer {
	
	private final String inputDir = "src//pages";
	
	public Indexer() {
		
	}
	
	Directory createDirectory(String output) throws IOException {
		return FSDirectory.open(Paths.get(output));
	}
	
	IndexWriter initializeIndexWriter(String output) throws IOException {
		Directory indexDir = createDirectory(output);
		
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		return new IndexWriter(indexDir, config);
	}
	
	Document getDocument(File f) {
		Document doc = new Document();
		try {
			// have to parse JSON and add contents to document
			Scanner json = new Scanner(f);
			String page = "";
			while(json.hasNextLine()) {
				page += json.nextLine();
			}
			json.close();
			
			JSONObject obj = (JSONObject) JSONValue.parse(page);
			if(obj == null) {
				System.out.println("Failed to parse JSON");
				return null;
			}
			String content = (String) obj.get("content");
			long id = (long) obj.get("id");
			String title = (String) obj.get("title");
			String url = (String) obj.get("url");
			String idStr = Long.toString(id);

			doc.add(new TextField("content", new StringReader(content)));
			// title and url will be displayed in the result
			// so original form will be stored
			doc.add(new StringField("title", title, Field.Store.YES));
			doc.add(new StringField("id", idStr, Field.Store.YES));
			doc.add(new StringField("url", url, Field.Store.YES));
			
			// extract snippet here and add it as a StringField
			
			return doc;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("JSON Not Found");
		}

		return null;
	}
	
	public void createIndex(String output) throws IOException {
		System.out.println("Initializing indexer...");
		IndexWriter indexer = initializeIndexWriter(output);
		File pagesDir = new File(inputDir);
		if(!pagesDir.isDirectory()) {
			System.out.println("Input is not directory!");
			return;
		}
		
		for(File file : pagesDir.listFiles()) {
			Document doc = getDocument(file);
			if(doc == null) {
				System.out.println("Invalid file");
				continue;
			}
			System.out.println(doc.get("title"));
			indexer.addDocument(doc);
		}
		
		System.out.println("Indexer is closed...");
		indexer.close();
	}
}
