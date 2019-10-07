package com.pollra.web.blog.domain.en;

public enum BlogUpdateRange {
    ID("ID"),
    TITLE("TITLE"),
    EXPLANATION("EXPLANATION"),
    IMG_PATH("IMG_PATH"),
    META_TITLE("META_TITLE"),

    TITLE_EXPLANATION("TITLE_EXPLANATION"),
    TITLE_IMG_PATH("TITLE_IMG_PATH"),
    IMG_PATH_EXPLANATION("IMG_PATH_EXPLANATION"),

    TITLE_IMG_PATH_EXPLANATION("TITLE_IMG_PATH_EXPLANATION");


    final private String name;

    public String getName(){
        return name;
    }

    private BlogUpdateRange(String name){
        this.name = name;
    }
}
