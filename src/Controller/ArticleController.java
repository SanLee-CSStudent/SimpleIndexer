package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.springframework.web.bind.annotation.*;

import Searcher.Searcher;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ArticleController {

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@GetMapping("/articles")
	public List<Article> searchArticle(@RequestParam(required=false, defaultValue="") String query){
		if(query.isEmpty()) {
			return null;
		}
		
		Searcher search = new Searcher();
		String indexDir = "src//index";
		List<Article> docs = null;
		try {
			docs = search.search(indexDir, query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Index not found");
		} catch (ParseException e) {
			System.out.println("Parse failed");
		}
		
		return docs;
	}

}
