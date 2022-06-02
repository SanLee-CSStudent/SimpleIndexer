package Searcher;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	final int MAX_PAGES = 100;
	
	IndexSearcher initializeIndexSearcher(String d) throws IOException {
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(d)));
		return new IndexSearcher(reader);
	}
	
	QueryParser initializeQueryParser() {
		return new QueryParser("content", new StandardAnalyzer());
	}
	
	public void search(String index, String queryStr) throws IOException, ParseException {
		QueryParser parser = initializeQueryParser();
		IndexSearcher searcher = initializeIndexSearcher(index);
		
		Query q = parser.parse(queryStr);
		
		// return top 10 documents
		TopDocs hits = searcher.search(q, 10);
		for(ScoreDoc s : hits.scoreDocs) {
			Document doc = searcher.doc(s.doc);
			
			System.out.println(doc.get("url"));
		}
	}
}
