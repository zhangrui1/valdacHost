package com.toyo.vh.service;

import com.toyo.vh.dao.ReportimageKikiSystemMapper;
import com.toyo.vh.entity.ReportimageKikiSystem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lsr on 2/2/15.
 */

@Service
public class ReportimageKikiSystemService {

    @Resource
    public ReportimageKikiSystemMapper reportimageKikiSystemMapper;

    public void addReportimageKikisystem(String koujiId,String imagename, List<String> selectIdList){

        List<ReportimageKikiSystem> reportimageKikiSystemList = new ArrayList<ReportimageKikiSystem>();
        for (int i = 0; i < selectIdList.size(); i++) {

            ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
            reportimageKikiSystem.setKikiSysId(Integer.valueOf(selectIdList.get(i)));
            reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
            reportimageKikiSystem.setImagename(imagename);
            reportimageKikiSystemMapper.insertReportimageKikisystem(reportimageKikiSystem);
        }
    }

    public void addReportimageKikisystemByReportimageKikiSystem(ReportimageKikiSystem reportimageKikiSystem){
            reportimageKikiSystemMapper.insertReportimageKikisystem(reportimageKikiSystem);
    }

    public List<ReportimageKikiSystem> makeReportimageKikiSystemListByIdList(String koujiId, String imagename, List<String> selecteIdList){
        List<ReportimageKikiSystem> reportimageKikiSystemList = new ArrayList<ReportimageKikiSystem>();
        for (int i = 0; i < selecteIdList.size(); i++) {
            ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
            reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
            reportimageKikiSystem.setImagename(imagename);
            reportimageKikiSystem.setKikiSysId(Integer.valueOf(selecteIdList.get(i)));
            reportimageKikiSystemList.add(reportimageKikiSystem);
        }

        return reportimageKikiSystemList;
    }

    public List<ReportimageKikiSystem> getListByReportimageKikiSystem(String koujiId, String imagename){
        ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
        reportimageKikiSystem.setImagename(imagename);
        reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
        return reportimageKikiSystemMapper.getListByReportimageKikiSystem(reportimageKikiSystem);
    }


    public void deleteByKoujiIdAndImagename(String koujiId, String imagename) {
        ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
        reportimageKikiSystem.setImagename(imagename);
        reportimageKikiSystem.setKoujiId(Integer.valueOf(koujiId));
        reportimageKikiSystemMapper.deleteByReportimageKikiSystem(reportimageKikiSystem);
    }

    public List<ReportimageKikiSystem> getListByKoujiId(String id) {
        return reportimageKikiSystemMapper.getListByKoujiId(Integer.valueOf(id));
    }

    public void deleteByKoujiIdAndKikisysId(String id, String s) {
        ReportimageKikiSystem reportimageKikiSystem = new ReportimageKikiSystem();
        reportimageKikiSystem.setKoujiId(Integer.valueOf(id));
        reportimageKikiSystem.setKikiSysId(Integer.valueOf(s));
        reportimageKikiSystemMapper.deleteByKoujiIdAndKikisysId(reportimageKikiSystem);
    }
}
