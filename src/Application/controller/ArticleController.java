package Application.controller;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.web.bind.annotation.*;

import Application.search.Index;
import Application.search.Searcher;

@RestController
@CrossOrigin("*")
public class ArticleController {
	String indexDir = "src//Application//index";
	Index search = new Index("", indexDir);
	
	@RequestMapping("/")
	public String index() {
		return "Hello World";
	}
	
	@GetMapping("/search")
	@ResponseBody
	public List<Application.search.Article> searchArticle(@RequestParam(required=false, defaultValue="") String query){
		if(query.isEmpty()) {
			return null;
		}
		
		List<Application.search.Article> docs = null;
		docs = search.runQuery(query);

		return docs;
	}
}
