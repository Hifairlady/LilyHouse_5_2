package com.edgar.lilyhouse.Items;

public class AuthorItem {

    private String authorName, authorUrl;

    public AuthorItem(String authorName, String authorUrl) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
}
