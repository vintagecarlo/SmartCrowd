package com.smartcrowd.patch.smart_crowd;

/**
 * Created by user on 14/01/2017.
 */

public class Config {
    public static final String URL = "10.0.0.5";
    public static final String REGISTER_URL = "http://"+URL+"/api/register";
    public static final String DATA_URL = "http://"+URL+"/apiuserdata/";
    public static final String PROF_URL = "http://"+URL+"/apiposerprofile/";
    public static final String POST_URL = "http://"+URL+"/api/postreport";
    public static final String DUMMY_URL = "http://"+URL+"/api/poster2";
    public static final String FEED_URL  = "http://"+URL+"/api/userpost";
    public static final String PROF_FEED_URL  = "http://"+URL+"/api/profilepost/";
    public static final String image_url = "http://"+URL+"/";
    public static final String address_url = "http://"+URL+"/getciti";
    public static final String brgy_url = "http://"+URL+"/getbrngy/";
    public static final String update_url = "http://"+URL+"/update/user/";
    public static final String update_password_url = "http://"+URL+"/update/user/pass/";
    public static final String deactivate_url = "http://"+URL+"/api/deactivate/";
    public static final String verify_url = "http://"+URL+"/verifyme/";
    public static final String approved_url = "http://"+URL+"/approve/post/";
    public static final String disapproved_url = "http://"+URL+"/disapprove/post/";
    public static final String delete_url = "http://"+URL+"/delete/post/";
    public static final String map_url = "http://"+URL+"/mapsdata";
    public static final String post_map_url = "http://"+URL+"/givemepost/";
    public static final String rate_url = "http://"+URL+"/api/rate/";
    public static final String profpic_url = "http://"+URL+"/update/user/pic/";
    public static final String updateaddress_url = "http://"+URL+"/update/user/loc/";
    public static final String tag_url = "http://"+URL+"/alltags";
    public static final String tagpop_url = "http://"+URL+"/returntag/";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    public static final String KEY_MNAME = "mname";
    public static final String KEY_RATING= "rating";
    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BDAY = "bday";
}
