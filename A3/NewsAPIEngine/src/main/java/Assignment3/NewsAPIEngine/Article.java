package Assignment3.NewsAPIEngine;

public class Article {

    private String title;
    private String description;
    private String author;
    private String url;
    private String urlToImage;
    private String published;
    private String source;

    public void settitle(String title){
        this.title = title;
    }
    public String gettitle()
    {
        return this.title;
    }

    public void setdescription(String description){
        this.description = description;
    }
    public String getdescription()
    {
        return this.description;
    }

    public void setauthor(String author){
        this.author = author;
    }
    public String getauthor()
    {
        return this.author;
    }

    public void seturl(String url){
        this.url = url;
    }
    public String geturl()
    {
        return this.url;
    }

    public void seturlToImage(String urlToImage){
        this.urlToImage = urlToImage;
    }
    public String geturlToImage()
    {
        return this.urlToImage;
    }

    public void setpublished(String published){
        this.published = published;
    }
    public String getpublished()
    {
        return this.published;
    }

    public void setsource(String source){
        this.source = source;
    }
    public String getsource()
    {
        return this.source;
    }
}
