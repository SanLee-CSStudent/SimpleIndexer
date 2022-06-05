package Application.controller;

public class Article {
	public int id;
	public String title;
	public String url;
	public String content;
	
	public Article() {
		
	}
	
	public Article(int id, String title, String url, String content) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return String.format("{\"id\"=%d, \"title\"=\"%s\", \"url\"=\"%s\", \"content\"=\"\"}", id, title, url);
	}
}
