package com.toyo.vh.service;

import com.toyo.vh.dao.MasterMapper;
import com.toyo.vh.entity.Master;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhangrui on 2014/10/15.
 */
@Service
public class MasterService {

    @Resource
    MasterMapper masterMapper;

    /**タイプ名により、masterテーブルからデータを取得**/
    public List<Master> getMasterByType(String type){
        List<Master> masterList =masterMapper.findMasterByType(type);
        return masterList;
    }
}
