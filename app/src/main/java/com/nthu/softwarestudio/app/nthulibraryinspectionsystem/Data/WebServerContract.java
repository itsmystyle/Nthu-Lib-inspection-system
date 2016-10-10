package com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data;

/**
 * Created by Ywuan on 19/07/2016.
 */
public class WebServerContract {
    /**
     * Base url link to server
     */
    public static final String BASE_URL = "http://10.0.2.2:28888/NTHU_lib_app";
    //public static final String BASE_URL = "http://192.168.1.105:8888/Nthu_lib_app";
    //public static final String BASE_URL = "http://s103062325.web.2y.idv.tw/NTHU_lib_app";
    //public static final String BASE_URL = "http://140.114.73.33:3333";
    public static final String ROR_BASE_URL = "http://140.114.73.33:3000/users";

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
    public static final String USER_AUTHORIZATION_USER_NAME = "name";


    // list
    public static final String LIST_ALL_PROBLEN_URL = "/list_perticulor_problem.php";
    public static final String SEARCH_INDEX = "/search_index.php";
    public static final String UPDATE_DATA = "/data_program.php";

    // get machine info
    public static final String MACHINE_INFO_URL = "/Machine_Info/machine_info.php";
    public static final String MACHINE_INFO_FORM_URL = "/Machine_Info/machine_info_form.php";
    public static final String MACHINE_INFO_POST_FORM_URL = "/Machine_Info/post_today_machine_info.php";


    //machine info
    public static final String MACHINE_INFO_SERVER = "server";
    public static final String MACHINE_NUMBER = "machine_number";
    public static final String MACHINE_NONE = "NONE";
    public static final String MACHINE_SERVER_ERROR = "UNFOUND";
    public static final String MACHINE_BRANCH = "branch";
    public static final String MACHINE_PLACE = "place";
    public static final String MACHINE_MAINTAIN_GROUP = "maintian_group";
    public static final String MACHINE_FLOOR = "floor";
    public static final String MACHINE_ID = "machine_id";
    public static final String MACHINE_DATE = "date";
    public static final int MACHINE_BRANCH_ZT = 0;
    public static final int MACHINE_BRANCH_RS = 1;
    public static final String MACHINE_PAST_1ST_DATE = "past1stDate";
    public static final String MACHINE_PAST_2ND_DATE = "past2ndDate";
    public static final String MACHINE_PAST_3RD_DATE = "past3rdDate";
    public static final String MACHINE_PAST_4TH_DATE = "past4thDate";
    public static final String MACHINE_PAST_5TH_DATE = "past5thDate";
    public static final String MACHINE_PAST_6TH_DATE = "past6thDate";
    public static final String MACHINE_PAST_7TH_DATE = "past7thDate";


    //dailies
    public static final String DAILIES_ID = "id";
    public static final String DAILIES_DATE = "date";
    public static final String DAILIES_MACHINE_ID = "machine_id";
    public static final String DAILIES_STATE = "state";
    public static final String DAILIES_USER_ID = "user_id";
    public static final String DAILIES_USER_NAME = "user_name";


    //daily problem
    public static final String DAILY_PROBLEM_ID = "id";
    public static final String DAILY_PROBLEM_DATE = "date";
    public static final String DAILY_PROBLEM_MACHINE_ID = "machine_id";
    public static final String DAILY_PROBLEM_USER_ID = "user_id";
    public static final String DAILY_PROBLEM_PROBLEM_DETAIL = "problem_detail";
    public static final String DAILY_PROBLEM_MAINTAINER_ID = "maintainer_id";
    //public static final String DAILY_PROBLEM_SOLVE_DETAIL = "solve_detail";
    public static final String DAILY_PROBLEM_SOLVE_DETAIL = "comment";
    public static final String DAILY_PROBLEM_SOLVE_DATE = "solve_date";

    //BulletinBoard
    public static final String USERNAME = "username";
    public static final String DATE = "date";
    public static final String POST_DATE = "postdate";
    public static final String END_DATE = "enddate";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_ID = "message_id";
    public static final String IMPORTANT = "important";
}
