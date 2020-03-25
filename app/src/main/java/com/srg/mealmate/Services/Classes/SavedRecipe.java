package com.srg.mealmate.Services.Classes;

public class SavedRecipe {
    private String name;
    private String imgURL;
    private String source;
    private String docID;

    public SavedRecipe(String name, String imgURL, String source, String docID) {
        this.name = name;
        this.imgURL = imgURL;
        this.source = source;
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
}
