package com.toyo.vh.entity;

/**
 * Created by zhangrui on 19/11/14.
 * 工事画像エンティティ
 */
public class ReportImage {

    public int id;
    public int koujiId;
    public String imagesyu;
    public int page;
    public String imagename;
    public String papersize;
    public String trkDate;
    public String updDate;
    public String bikou;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKoujiId() {
        return koujiId;
    }

    public void setKoujiId(int koujiId) {
        this.koujiId = koujiId;
    }

    public String getImagesyu() {
        return imagesyu;
    }

    public void setImagesyu(String imagesyu) {
        this.imagesyu = imagesyu;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getPapersize() {
        return papersize;
    }

    public void setPapersize(String papersize) {
        this.papersize = papersize;
    }

    public String getBikou() {
        return bikou;
    }

    public void setBikou(String bikou) {
        this.bikou = bikou;
    }

    public String getTrkDate(){return trkDate;}

    public void setTrkDate(String trkDate){this.trkDate=trkDate;}

    public String getUpdDate(){return  updDate;}

    public void setUpdDate(String updDate){this.updDate=updDate;}
}

