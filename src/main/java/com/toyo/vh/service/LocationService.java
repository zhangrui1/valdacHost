package com.toyo.vh.service;

import com.toyo.vh.dao.LocationMapper;
import com.toyo.vh.entity.Location;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangrui on 14/11/12.
 */
@Service
public class LocationService {

    @Resource
    LocationMapper locationMapper;

    /**全てのマスターを抽出する*/
    public List<Location> getAllLocation(){
        List<Location> locationList = locationMapper.findAllLocation();
        return locationList;
    }

    /**ユーザ会社のlocationのみを抽出する*/
    public List<Location> getLocationByUserDepartment(String userDepart){
        Location tmpLocation=new Location();
        tmpLocation.setkCodeL(userDepart);
        List<Location> locationList = locationMapper.findALocationByUserDepartment(tmpLocation);
        return locationList;
    }

    /**kCodeL　会社名を抽出する*/
    public List<String> getKCodeL(){
        List<String> kCodeLList = locationMapper.findKCodeL();
        return kCodeLList;
    }

    /**kCodeM　発電所を抽出する*/
    public List<String> getKCodeMByL(String kCodeL){
        Location location=new Location();
        location.setkCodeL(kCodeL);
        List<String> kCodeMList = locationMapper.findKCodeMByL(location);
        return kCodeMList;
    }

    /**kCodeS　ユニーとを抽出する*/
    public List<String> getKCodeSByLM(String kCodeL,String kCodeM){
        Location location=new Location();
        location.setkCodeL(kCodeL);
        location.setkCodeM(kCodeM);
        List<String> kCodeSList = locationMapper.findKCodeSByLM(location);
        return kCodeSList;
    }

}
