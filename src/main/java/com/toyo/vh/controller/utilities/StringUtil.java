package com.toyo.vh.controller.utilities;

import java.util.List;

/**
 * Created by zhangrui on 2015/04/09.
 */
public class StringUtil {

    /**
     * 指定文字列が空文字列であるか否かの検証
     *
     * @param value
     *            文字列
     * @return　検証結果のブール値
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 指定文字列が空文字列でないか否かの検証
     *
     * @param value
     *            文字列
     * @return 検証結果のブール値
     */
    public static boolean isNotEmpty(String value) {
        return value != null && value.length() != 0;
    }

    /**
     * 文字列を結合する
     *
     * @param buff
     *            対象文字列を格納する配列
     * @return　結合後の文字列
     */
    public static String concat(String... buff) {
        StringBuffer sb = new StringBuffer("");
        for (String value : buff) {
            if (isNotEmpty(value)) {
                if (sb.length() != 0) {
                    sb.append(" ");
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }

    /**
     * 文字列を結合する
     *
     * @param delimit
     *            デリミタ文字列
     * @param buff
     *            対象文字列を格納する配列
     * @return　結合後の文字列
     */
    public static String concatWithDelimit(String delimit, String... buff) {
        StringBuffer sb = new StringBuffer("");
        for (String value : buff) {
            if (isNotEmpty(value)) {
                if (sb.length() != 0) {
                    sb.append(delimit);
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }
    /**
     * 文字列を結合する
     *
     * @param list
     *            対象文字列を格納する配列
     * @return　結合後の文字列
     */
    public static String concat(List<String> list) {
        StringBuffer sb = new StringBuffer("");
        for (String value : list) {
            if (isNotEmpty(value)) {
                if (sb.length() != 0) {
                    sb.append(" ");
                }
                sb.append(value);
            }
        }
        return sb.toString();
    }
    /**
     * 文字列はnullの場合は、空文字を戻す
     *
     */
    public static String nullCheck(String target){
        String tmp=target.toLowerCase();
        if(tmp.equals("null")){
            return "";
        }else{
            return target;
        }
    }

    //文字列 sort用　比べる
    public static int compareString(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        else if (s1 == null) return -1;
        else if (s2 == null) return  1;
        else return s1.compareTo(s2);
    }

}
