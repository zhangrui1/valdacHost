package com.toyo.vh.service;

import com.google.gson.Gson;
import com.toyo.vh.dao.ReportImageMapper;
import com.toyo.vh.entity.ReportImage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangrui on 19/11/14.
 */
@Service
public class ReportImageService {

    @Resource
    ReportImageMapper reportImageMapper;
    /**IDから画像情報を取得*/
    public ReportImage getReportImageById(String id){

        ReportImage reportImage=reportImageMapper.findReportImageById(id);
        return  reportImage;
    }

    /**画像名から画像情報を取得*/
    public ReportImage getReportImageByImagename(String imagename){

        ReportImage reportImage = reportImageMapper.findReportImageByImagename(imagename);
        return reportImage;
    }

    /**工事の画像リストを取得*/
    public List<ReportImage> getReportImageByKoujiId(String koujiId){

        List<ReportImage> reportImages=reportImageMapper.findReportImageByKoujiId(koujiId);
        return  reportImages;
    }

    /**弁IDにより、画像を取得*/
    public List<ReportImage> getReportImageByKikisysId(String kikiSysId){

        List<ReportImage> reportImages=reportImageMapper.findReportImageByKikisysId(kikiSysId);
        return  reportImages;
    }

    /**新規画像テーブルに追加する*/
    public ReportImage addReportImage(ReportImage reportImage){
        //append Date
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        reportImage.setTrkDate(sdf1.format(date));
        reportImage.setUpdDate(sdf1.format(date));


        String koujiId=Integer.toString(reportImage.getKoujiId());
        System.out.println(koujiId);
        List<ReportImage> temReportImages=reportImageMapper.findReportImageByKoujiId(koujiId);
        Gson gsonvalves = new Gson();
        System.out.println(gsonvalves.toJson(temReportImages));

        if((temReportImages.size()>0)){
            System.out.println("not null");
            reportImage.setPage((reportImageMapper.getLastPageByKoujiId(koujiId))+1);
        }else{
            System.out.println("null");
            reportImage.setPage(1);
        }
        reportImageMapper.insertReportImage(reportImage);
        //make id
        reportImage.setId(reportImageMapper.getLastInsertId());
        return reportImage;
    }
    /**画像を更新する*/
    public ReportImage updateReportImage(ReportImage reportImage){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        reportImage.setUpdDate(sdf1.format(date));

        reportImageMapper.updateReportImage(reportImage);
        return reportImage;
    }

    /**画像名を更新する*/
    public void updateReportImageBikouByKoujiId(int id,String bikou){

        ReportImage reportImage=new ReportImage();
        reportImage.setBikou(bikou);
        reportImage.setKoujiId(id);
        reportImageMapper.updateReportImageBikouByKoujiId(reportImage);

    }

    public int getLastPageByKoujiId(String id){
        return reportImageMapper.getLastPageByKoujiId(id);
    }

    /**画像を削除する*/
    public void deleteReportImage(ReportImage reportImage){
        reportImageMapper.deleteReportImage(reportImage);
    }


    public List<ReportImage> getReportImageByKoujiIdAndPage(String koujiId, String page) {

        return reportImageMapper.findReportImageByKoujiIdAndPage(koujiId,Integer.valueOf(page));
    }

    /**画像の数を取る*/
    public int getPageByKoujiId(String koujiId){
        return reportImageMapper.getPageByKoujiId(koujiId);
    }

}
