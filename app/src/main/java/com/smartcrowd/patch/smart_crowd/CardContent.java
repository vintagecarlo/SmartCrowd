package com.smartcrowd.patch.smart_crowd;

/**
 * Created by user on 21/01/2017.
 */

public class CardContent {
    private  String profileImageUrl;
    private  String user_id;
    private  String post_id;
    private  String name;
    private  String rating;
    private  String title;
    private  String content;
    private  String contentImageUrl;
    private  String location;
    private  String tags;
    private  String status;
    private  String rateText;


   /* public CardContent() {
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.contentImageUrl = contentImageUrl;
        this.location = location;
        this.tags = tags;
    }
*/

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getRateText() {
       return rateText;
   }

    public void setRateText(String rateText) {
        this.rateText = rateText;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentImageUrl() {
        return contentImageUrl;
    }

    public void setContentImageUrl(String contentImageUrl) {
        this.contentImageUrl = contentImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
