package com.toyo.vh.dto;

/**
 * Created by zhangrui on 10/21/14.
 * Lucene　Index作成用
 */
public class SearchResultObject {
    public String id;
    public String body;
    public String trkDate;
    public String updDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTrkDate() {
        return trkDate;
    }

    public void setTrkDate(String trkDate) {
        this.trkDate = trkDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
}
