package com.smartcrowd.patch.smart_crowd;

/**
 * Created by user on 26/02/2017.
 */
public class Post {
    String image;
    String posttitle;
    String postcont;
    String posttag;

    public Post(String image, String posttitle, String postcont, String posttag) {
        this.image = image;
        this.posttitle = posttitle;
        this.postcont = postcont;
        this.posttag = posttag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostcont() {
        return postcont;
    }

    public void setPostcont(String postcont) {
        this.postcont = postcont;
    }

    public String getPosttag() {
        return posttag;
    }

    public void setPosttag(String posttag) {
        this.posttag = posttag;
    }
}
