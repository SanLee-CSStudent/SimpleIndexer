package Application.controller;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.web.bind.annotation.*;

import Application.search.Searcher;

@RestController
@CrossOrigin("*")
public class ArticleController {

	@RequestMapping("/")
	public String index() {
		return "Hello World";
	}
	
	@GetMapping("/search")
	@ResponseBody
	public List<Article> searchArticle(@RequestParam(required=false, defaultValue="") String query){
		if(query.isEmpty()) {
			return null;
		}
		
		Searcher search = new Searcher();
		String indexDir = "src//Application//index";
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
