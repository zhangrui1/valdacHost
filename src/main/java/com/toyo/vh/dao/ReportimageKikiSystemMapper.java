package com.toyo.vh.dao;

import com.toyo.vh.entity.ReportimageKikiSystem;

import java.util.List;

/**
 * Created by Lsr on 2/2/15.
 */
public interface ReportimageKikiSystemMapper {

    public void addReportimageKikisystem(List<ReportimageKikiSystem> reportimageKikiSystemList);

    public void insertReportimageKikisystem(ReportimageKikiSystem reportimageKikiSystem);

    public List<ReportimageKikiSystem> getListByReportimageKikiSystem(ReportimageKikiSystem reportimageKikiSystem);

    void deleteByReportimageKikiSystem(ReportimageKikiSystem reportimageKikiSystem);

    List<ReportimageKikiSystem> getListByKoujiId(Integer koujiId);

    void deleteByKoujiIdAndKikisysId(ReportimageKikiSystem reportimageKikiSystem);
}
