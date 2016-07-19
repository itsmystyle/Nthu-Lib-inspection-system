package com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data;

/**
 * Created by Ywuan on 19/07/2016.
 */
public class WebServerContract {
    public static final String BASE_URL = "http://192.186.1.109:8888/NTHU_lib_app";

    // user authorization
    public static final String USER_AUTHORIZATION_URL = "/User_Authorization/user_authorization.php";
    public static final String USER_AUTHORIZATION_SERVER = "server";
    public static final String USER_AUTHORIZATION_SERVER_ERROR = "UNFOUND";
    public static final String USER_AUTHORIZATION_SERVER_SECURE = "secure";
    public static final String USER_AUTHORIZATION_SERVER_SECURE_AUTHORIZED = "AUTHORIZED";
    public static final String USER_AUTHORIZATION_SERVER_SECURE_UNAUTHORIZED = "UNAUTHORIZED";
    public static final String USER_AUTHORIZATION_USERID = "userid";
    public static final String USER_AUTHORIZATION_USERPASSWORD = "user_pass";
    public static final String USER_AUTHORIZATION_USER_ID_AUTOINCREAMENT = "user-id";
    public static final String USER_AUTHORIZATION_USER_ACCESS_TOKEN = "access-token";
    public static final String USER_AUTHORIZATION_USER_NAME = "name";

}
