package com.toyo.vh.dao;

import com.toyo.vh.entity.ReportImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangrui on 19/11/14.
 */
public interface ReportImageMapper {

    /**IDから画像情報を取得*/
    public ReportImage findReportImageById(String id);

    /**工事の画像リストを取得*/
    public List<ReportImage> findReportImageByKoujiId(String koujiId);

    /**工事の画像リストを取得*/
    public List<ReportImage> findReportImageByKikisysId(String kikiSysId);

    /**新規画像テーブルに追加する*/
    public void insertReportImage(ReportImage reportImage);

    /**最新追加した画像のIDを取得*/
    public int getLastInsertId();

    /**工事の最後のPage数を取得*/
    public int getLastPageByKoujiId(String koujiId);

    /**画像を更新する*/
    public void updateReportImage(ReportImage reportImage);

    /**画像備考を更新する*/
    public void updateReportImageBikouByKoujiId(ReportImage reportImage);

    /**画像を削除する*/
    public void deleteReportImage(ReportImage reportImage);

    /**画像名により、画像データを取得*/
    public ReportImage findReportImageByImagename(String imagename);

    /**工事IDよりとゲージナンバーより、画像リストを取得 */
    public List<ReportImage> findReportImageByKoujiIdAndPage(@Param("koujiId") String koujiId, @Param("page") int page);

    /**工事IDより、工事に関する画像の数を取る*/
    public int getPageByKoujiId(String koujiId);
}
