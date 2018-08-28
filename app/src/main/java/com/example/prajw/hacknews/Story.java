package com.example.prajw.hacknews;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Story implements Parcelable {

    private String by, title, url;
    private int descendants, score;
    private long id, time;
    private int isFavourite;
    private String commentList;

    public String getCommentList() {
        return commentList;
    }

    public void setCommentList(String commentList) {
        this.commentList = commentList;
    }

    public int getFavourite() {
        return isFavourite;
    }

    public void setFavourite(int favourite) {
        isFavourite = favourite;
    }
    // setter maadbeku;

    public Story(){

    }

    public Story(String by, String title, String url, int descendants, int score, long id, long time, String commentList) {
        this.by = by;
        this.title = title;
        this.url = url;
        this.descendants = descendants;
        this.score = score;
        this.id = id;
        this.time = time;
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return by + String.valueOf(id);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(by);
        dest.writeLong(id);
        dest.writeLong(time);
        dest.writeString(url);
        dest.writeInt(score);
        dest.writeInt(descendants);

    }
    public static final Parcelable.Creator<Story> CREATOR =
            new Parcelable.Creator<Story>() {
                @Override
                public Story createFromParcel(Parcel source) {
                    Story story = new Story();
                    story.setTitle(source.readString());
                    story.setBy(source.readString());
                    story.setUrl(source.readString());
                    story.setDescendants(source.readInt());
                    story.setScore(source.readInt());
                    story.setId(source.readLong());
                    story.setTime(source.readLong());
                    story.setFavourite(source.readInt());
                    return story;
                }

                @Override
                public Story[] newArray(int size) {
                    return new Story[size];
                }

            };
}
