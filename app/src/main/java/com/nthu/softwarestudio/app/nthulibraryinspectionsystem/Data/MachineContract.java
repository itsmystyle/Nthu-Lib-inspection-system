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

    public static final String MACHINE_PROBLEM_Kiosk_Kiosk無法開機 =                "Kiosk - Kiosk無法開機";
    public static final String MACHINE_PROBLEM_Kiosk_Kiosk主機當機無法正常使用 =      "Kiosk - Kiosk主機當機無法正常使用";
    public static final String MACHINE_PROBLEM_Kiosk_Kiosk網路異常 =                "Kiosk - Kiosk網路異常";
    public static final String MACHINE_PROBLEM_Kiosk_Kiosk螢幕畫面不正常 =           "Kiosk - Kiosk螢幕畫面不正常";
    public static final String MACHINE_PROBLEM_讀卡機_讀卡機感應異常 =                "讀卡機 - 讀卡機感應異常";
    public static final String MACHINE_PROBLEM_讀卡機_讀卡機壓克力座鬆開或鬆脫 =       "讀卡機 - 讀卡機壓克力座鬆開或鬆脫";
    public static final String MACHINE_PROBLEM_讀卡機_讀卡機壓克力座破損或缺件 =       "讀卡機 - 讀卡機壓克力座破損或缺件";
    public static final String MACHINE_PROBLEM_主機_電腦主機無法開機 =                "主機 - 電腦主機無法開機";
    public static final String MACHINE_PROBLEM_主機_電腦主機當機無法正常使用 =         "主機 - 電腦主機當機無法正常使用";
    public static final String MACHINE_PROBLEM_主機_電腦作業系統異常無法正常使用 =      "主機 - 電腦作業系統異常無法正常使用";
    public static final String MACHINE_PROBLEM_主機_電腦網路異常 =                    "主機 - 電腦網路異常";
    public static final String MACHINE_PROBLEM_主機_電腦軟體功能無法正常使用 =         "主機 - 電腦軟體功能無法正常使用";
    public static final String MACHINE_PROBLEM_螢幕_螢幕無法開機 =                   "螢幕 - 螢幕無法開機";
    public static final String MACHINE_PROBLEM_螢幕_螢幕沒有畫面 =                   "螢幕 - 螢幕沒有畫面";
    public static final String MACHINE_PROBLEM_螢幕_螢幕畫面有亮點或亮線 =            "螢幕 - 螢幕畫面有亮點或亮線";
    public static final String MACHINE_PROBLEM_螢幕_螢幕畫面不正常 =                 "螢幕 - 螢幕畫面不正常";
    public static final String MACHINE_PROBLEM_周邊鍵盤_鍵盤無法正常使用 =            "周邊(鍵盤) - 鍵盤無法正常使用";
    public static final String MACHINE_PROBLEM_周邊鍵盤_鍵盤某按鍵異常 =              "周邊(鍵盤) - 鍵盤某按鍵異常";
    public static final String MACHINE_PROBLEM_周邊鍵盤_鍵盤USB_連線異常 =            "周邊(鍵盤) - 鍵盤USB 連線異常";
    public static final String MACHINE_PROBLEM_周邊滑鼠_滑鼠無法正常使用 =              "周邊(滑鼠) - 滑鼠無法正常使用";
    public static final String MACHINE_PROBLEM_周邊滑鼠_滑鼠某按鍵異常 =               "周邊(滑鼠) - 滑鼠某按鍵異常";
    public static final String MACHINE_PROBLEM_周邊滑鼠_滑鼠USB_連線異常 =            "周邊(滑鼠) - 滑鼠USB 連線異常";
    public static final String MACHINE_PROBLEM_周邊掃描機_掃描機無法開機 =          "周邊(掃描機) - 掃描機無法開機";
    public static final String MACHINE_PROBLEM_周邊掃描機_掃描機無法與主機連線 =       "周邊(掃描機) - 掃描機無法與主機連線";
    public static final String MACHINE_PROBLEM_周邊掃描機_掃描異常無法正常使用 =       "周邊(掃描機) - 掃描異常無法正常使用";
    public static final String MACHINE_PROBLEM_周邊擴視機_擴視機無法開機 =          "周邊(擴視機) - 擴視機無法開機";
    public static final String MACHINE_PROBLEM_周邊擴視機_擴視機無法與主機連線 =       "周邊(擴視機) - 擴視機無法與主機連線";
    public static final String MACHINE_PROBLEM_周邊擴視機_擴視機設備異常無法正常使用 = "周邊(擴視機) - 擴視機設備異常無法正常使用";
    public static final String MACHINE_PROBLEM_投影機_投影機沒有開啟_無正常運作 =    "投影機 - 投影機沒有開啟(無正常運作)";
    public static final String MACHINE_PROBLEM_音源孔_鏍絲鬆脫 = "音源孔 - 鏍絲鬆脫";
    public static final String MACHINE_PROBLEM_電燈座_桌燈故障 = "電燈座 - 桌燈故障";
    public static final String MACHINE_PROBLEM_椅子_椅子裂損或缺件 = "椅子 - 椅子裂損或缺件";
}
