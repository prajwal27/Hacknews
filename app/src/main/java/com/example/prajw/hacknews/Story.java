package com.example.prajw.hacknews;

public class Story {

    private String by, title, url;
    private int descendants, score;
    private long id, time;
    private Boolean isFavourite;

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
    // setter maadbeku;

    public Story(){

    }

    public Story(String by, String title, String url, int descendants, int score, long id, long time) {
        this.by = by;
        this.title = title;
        this.url = url;
        this.descendants = descendants;
        this.score = score;
        this.id = id;
        this.time = time;
    }

    public String getBy() {
        return by;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getDescendants() {
        return descendants;
    }

    public int getScore() {
        return score;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescendants(int descendants) {
        this.descendants = descendants;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
