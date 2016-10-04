package com.toyo.vh.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangrui on 15/01/29.
 * 共有部分を定義する
 */
public class Config {

    /**機器分類　種別*/
    public static final String KikiBunRuiA = "弁本体";
    public static final String KikiBunRuiB = "駆動部";
    public static final String KikiBunRuiC = "補助部";
    public static final String KikiBunRuiD = "付属部";

    /**機器部類　Map sort用*/
    public static Map<String, String> kikiBunruiMap = createKikiBunruiMap();
    public static Map<String, String> createKikiBunruiMap() {
        Map<String,String> kikiBunruiMapTmp=new HashMap<String, String>();
        kikiBunruiMapTmp.put(KikiBunRuiA,"1");
        kikiBunruiMapTmp.put(KikiBunRuiB,"2");
        kikiBunruiMapTmp.put(KikiBunRuiC,"3");
        kikiBunruiMapTmp.put(KikiBunRuiD,"4");
        return kikiBunruiMapTmp;
    }

    /**部品　種別*/
    public static final String BuhinBunRuiA = "パッキン";
    public static final String BuhinBunRuiB = "ガスケット";
    public static final String BuhinBunRuiC = "グランドパッキン";

    /**部品　非石綿化*/
    public static final String AsbkbnA = "A";
    public static final String AsbkbnN = "N";
    public static final String AsbkbnF = "F";

    /**懸案　対応種別*/
    public static final String KenanNoTaiyo = "未対応";
    public static final String KenanFlg = "1";

    /**画像DL　種別*/
    public static final String ImageTypeGP = "GP";
    public static final String ImageTypeValve01="図面仕様書";
    public static final String ImageTypeValve02="点検報告書";
    public static final String ImageTypeValve03="懸案事項一覧";
    public static final String ImageTypeValve04="完成図面";
    public static final String ImageTypeValve05="作業指示書";
    public static final String ImageTypeValve06="弁棒ねじ検索記録";


    /**弁単位で　DLファイル名*/
    //弁機器部品情報ファイル名
    public static final String ValveDetail="弁機器部品リスト.xlsx";
    public static final String ValveDetailTemplate="excel/KikisysBuhinDetail.xlsx";

    //弁GPファイル名
    public static final String ValveDetailForGP="弁GP仕様リスト.xlsx";
    public static final String ValveDetailForGPTemplate="excel/KikisysGP.xlsx";

    //弁懸案ファイル名
    public static final String  ValveKenan="弁懸案リスト.xlsx";
    public static final String ValveKenanTemplate="excel/KikisysKenan.xlsx";

    //弁台帳ファイル名
    public static final String  ValveDaityo="弁台帳リスト.xlsx";
    public static final String ValveDaityoTemplate="excel/KikisysDaityo.xlsx";

    //弁の特別加工記録明細書ファイル名
    public static final String  ValveTokuBetuKako="弁特別加工記録明細書.xlsx";
    public static final String ValveTokuBetuKakoTemplate="excel/KikisysTokuBetuKakoKiroku.xlsx";

    //弁の指示書ファイル名
    public static final String  ValveSijisyo="弁指示書.xlsx";
    public static final String ValveSijisyoTemplate="excel/KikisysSijisyo.xlsx";

    /**工事単位で　DLファイル名*/
    //未対応懸案ファイル名
    public static final String  KoujiKenan="工事の懸案リスト.xlsx";

    //作業ラベル
    public static final String  KoujiSagyoRabel="工事の作業ラベル.xlsx";
    public static final String  KoujiSagyoRabelTemplate="excel/KoujiSagyoRabel.xlsx";

    //弁ラベル
    public static final String  KoujiValveRabel="工事の弁ラベル.xlsx";
    public static final String  KoujiValveRabelNaikei="工事の弁ラベル（内径あり）.xlsx";
    public static final String  KoujiValveRabelTemplate="excel/KoujiValveRabel.xlsx";

    //点検リスト
    public static final String  KoujiTenkenList="工事の点検リスト.xlsx";
    public static final String  KoujiTenkenListTemplate="excel/KoujiTenkenList.xlsx";

    //点検報告書
    public static final String  KoujiTenkenReport="工事の点検内容一覧.xlsx";
    public static final String  KoujiTenkenReportTemplate="excel/KoujiTenkenReport.xlsx";

    //点検報告書new
    public static final String  KoujiTenkenReportNewTemplate="excel/KoujiTenkenReportNew.xlsx";

    //工具点検リスト
    public static final String  KoujiToolList="工事の工具点検リスト.xlsx";
    public static final String  KoujiToolListTemplate="excel/KoujiToolList.xlsx";

    //ICS点検リスト
    public static final String  KoujiIcsList="工事のICS点検リスト.xlsx";
    public static final String  KoujiIcsListTemplate="excel/KoujiIcsList.xlsx";

    //工場修理品入出荷管理台帳
    public static final String  KoujiKanriDaiTyoList="工事の工場修理品入出荷管理台帳.xlsm";
    public static final String  KoujiKanriDaiTyoListTemplate="excel/KoujiKanriDaiTyoList.xlsm";

}
