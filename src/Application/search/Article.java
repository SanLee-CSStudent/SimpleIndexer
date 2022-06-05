package Application.search;

public class Article {
    public float rank;
    public String title;
    public String url;
    public String content;

    public Article() {
    }

    public Article(float rank, String title, String url, String content) {
        this.rank = rank;
        this.title = title;
        this.url = url;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("{\"rank\"=%f, \"title\"=\"%s\", \"url\"=\"%s\", \"content\"=\"\"}", rank, title, url);
    }
}