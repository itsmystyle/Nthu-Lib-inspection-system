package com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data;

/**
 * Created by Ywuan on 26/07/2016.
 */
public class MachineContract {
    public static final String MACHINE_NUMBER = "machine_number";
    public static final String MACHINE_STATE_STRING_未紀錄 = "未紀錄";
    public static final String MACHINE_STATE_STRING_使用中 = "使用中";
    public static final String MACHINE_STATE_STRING_良好 = "良好";
    public static final String MACHINE_STATE_STRING_問題排除 = "問題排除";
    public static final String MACHINE_STATE_STRING_通知人員 = "通知人員";
    public static final String MACHINE_STATE_STRING_其他 = "其他";
    public static final int MACHINE_STATE_未紀錄 = 0;
    public static final int MACHINE_STATE_使用中 = 1;
    public static final int MACHINE_STATE_良好 = 2;
    public static final int MACHINE_STATE_問題排除 = 3;
    public static final int MACHINE_STATE_通知人員 = 4;
    public static final int MACHINE_STATE_其他 = 5;

    public static final String MACHINE_PROBLEM_其他 = "其他";
    public static final String MACHINE_PROBLEM_電腦主機電源線被拔掉 = "電腦主機電源線被拔掉";
    public static final String MACHINE_PROBLEM_電腦主機螢幕訊號線鬆動 = "電腦主機螢幕訊號線鬆動";
    public static final String MACHINE_PROBLEM_螢幕畫面解析度錯亂 = "螢幕畫面解析度錯亂";
    public static final String MACHINE_PROBLEM_Kiosk主機當機 = "Kiosk主機當機";
    public static final String MACHINE_PROBLEM_電腦主機不正常 = "電腦主機不正常";

    public static final String MACHINE_SOLUTION_其他 = "其他";
    public static final String MACHINE_SOLUTION_電源線重新安裝 = "電源線重新安裝";
    public static final String MACHINE_SOLUTION_螢幕訊號線重新安裝 = "螢幕訊號線重新安裝";
    public static final String MACHINE_SOLUTION_電腦主機重新調整解析度 = "電腦主機重新調整解析度";
    public static final String MACHINE_SOLUTION_按紅色鈕重新開機 = "按紅色鈕重新開機";
    public static final String MACHINE_SOLUTION_重新開機 = "重新開機";
}
