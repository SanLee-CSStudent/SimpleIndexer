package Application.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Index {
    private int max_num_results;
    private Path document_path;
    private Path index_path;
    private final IndexWriterConfig config;
    private final MultiFieldQueryParser query_parser;


    public Index(String document_path, String index_path) {
        setNumResults(10);
        setDocumentPath(document_path);
        setIndexPath(index_path);
        Analyzer analyzer = new StandardAnalyzer();
        config = new IndexWriterConfig(analyzer);
        String [] indexable_fields = {"content", "title"};
        Map<String, Float> scaling = new HashMap<>();
        scaling.put(indexable_fields[0], 1.0f);
        scaling.put(indexable_fields[1], 0.75f);
        query_parser = new MultiFieldQueryParser(indexable_fields, analyzer, scaling);
    }

    public void buildIndex() {
        try(Directory dir = FSDirectory.open(index_path);IndexWriter index_writer = new IndexWriter(dir, config); 
        	DirectoryStream<Path> doc_stream = Files.newDirectoryStream(document_path)) {
            JSONParser json_parser = new JSONParser();
            for(Path doc_path : doc_stream) {
                try {
                    JSONObject json_doc = (JSONObject) json_parser.parse(new FileReader(doc_path.toAbsolutePath().toString()));
                    Document doc = new Document();
                    doc.add(new Field("content", json_doc.get("content").toString(), TextField.TYPE_NOT_STORED));
                    doc.add(new Field("title", json_doc.get("title").toString(), TextField.TYPE_STORED));
                    doc.add(new Field("url", json_doc.get("url").toString(), StringField.TYPE_STORED));
                    doc.add(new Field("path", doc_path.toAbsolutePath().toString(), StringField.TYPE_STORED));
                    index_writer.addDocument(doc);
                }
                 catch (org.json.simple.parser.ParseException e) {
                    System.out.println("Failure to parse input json document: " + doc_path.toAbsolutePath());
                }
            }
        }
        catch(IOException e) {
            System.out.println("Failure in index creation: " + e.getMessage());
        }
    }

    public List<Article> runQuery(String query_string) {
        List<Article> results = new ArrayList<>();

        try(Directory dir = FSDirectory.open(index_path); DirectoryReader dir_reader = DirectoryReader.open(dir)){
            IndexSearcher index_searcher = new IndexSearcher(dir_reader);
            Query query = query_parser.parse(query_string);
            ScoreDoc [] hits = index_searcher.search(query, max_num_results).scoreDocs;
//            System.out.println("Num hits: " + hits.length);
            for(int i = 0; i < hits.length; ++i) {
                Document hit = index_searcher.doc(hits[i].doc); // Get document from index searcher by doc id
                results.add(new Article(hits[i].score, hit.get("title"), hit.get("url"), ""));
//                System.out.println("Rank:    " + (i + 1));
//                System.out.println("Score:   " + hits[i].score);
//                System.out.println("Title:   " + hit.get("title"));
//                System.out.println("URL:     " + hit.get("url"));
//                System.out.println("Path:    " + hit.get("path"));
//                System.out.println();
            }
        }
        catch(ParseException e) {
            System.out.println("Failed to parse query: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("Failure to search query: " + e.getMessage());
        }
        return results;
    }

    public void setDocumentPath(String document_path) {
        this.document_path = Paths.get(document_path);
        System.out.println("document_path: " + this.document_path.toAbsolutePath());
    }

    public String getDocumentPath() {
        return document_path.toString();
    }

    public void setIndexPath(String index_path) {
        this.index_path = Paths.get(index_path);
        if(!Files.isDirectory(this.index_path)) {
            try {
                Files.createDirectory(this.index_path);
            }
            catch (IOException e) {
                System.out.println("Failure to create index path: " + e.getMessage());
            }
        }
        System.out.println("index_path: " + this.index_path.toAbsolutePath());
    }

    public String getIndexPath() {
        return index_path.toString();
    }

    public void setNumResults(int max_num_results) {
        this.max_num_results = max_num_results;
    }

    public int getNumResults() {
        return max_num_results;
    }
}
