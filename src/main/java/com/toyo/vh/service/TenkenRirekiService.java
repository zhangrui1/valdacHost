package com.toyo.vh.service;

import com.toyo.vh.dao.TenkenRirekiMapper;
import com.toyo.vh.entity.TenkenRireki;
import com.toyo.vh.entity.TenkenRirekiUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangrui on 14/11/18.
 */
@Service
public class TenkenRirekiService {

    @Resource
    TenkenRirekiMapper tenkenRirekiMapper;

    /**IDから点検機器を取得*/
    public TenkenRireki getTenkenRirekiById(String id){
        TenkenRireki tenkenRireki = tenkenRirekiMapper.findTenkenRirekiById(id);
        return tenkenRireki;
    }

    /**IDから画像情報を取得*/
    public List<TenkenRireki> getTenkenRirekiByTenkenRireki(TenkenRireki tenkenRireki){
        List<TenkenRireki> tenkenrirekis= tenkenRirekiMapper.findTenkenRirekiByTenkenRireki(tenkenRireki);
        return  tenkenrirekis;
    }

    /**KoujiIDから点検機器情報を取得*/
    public List<TenkenRirekiUtil> getTenkenRirekiByKoujiId(String koujiId){
        List<TenkenRirekiUtil> tenkenKikiForms= tenkenRirekiMapper.findTenkenRirekiByKoujiId(koujiId);
        return  tenkenKikiForms;
    }

    /**KikiIDから点検機器情報を取得*/
    public List<TenkenRirekiUtil> getTenkenRirekiByKikiId(String kikiId){
        List<TenkenRirekiUtil> tenkenKikiForms= tenkenRirekiMapper.findTenkenRirekiByKikiId(kikiId);
        return  tenkenKikiForms;
    }

    //工事関係から点検データを取得
    public TenkenRirekiUtil getTenkenRirekiByKoujirelationId(String koujirelationId){
        TenkenRirekiUtil tenkenKikiForm= tenkenRirekiMapper.findTenkenRirekiByKoujirelationId(koujirelationId);
        return  tenkenKikiForm;
    }

    /**
     * 工事IDと完了フラグから点検機器を取得
     * 更新時間のDESC順
     * */
    public List<TenkenRirekiUtil> getTenkenRirekiByKoujiIdAndKanryoFlg(String koujiId,String kanryoFlg){
        TenkenRireki tenkenRireki=new TenkenRireki();
        tenkenRireki.setKanryoFlg(kanryoFlg);
        tenkenRireki.setKoujiId(Integer.parseInt(koujiId));

        List<TenkenRirekiUtil> tenkenKikiForms= tenkenRirekiMapper.findTenkenRirekiByKoujiIdAndKanryoFlg(tenkenRireki);
        return  tenkenKikiForms;
    }

    /**新規点検機器を追加する*/
    public TenkenRireki addTenkenRireki(TenkenRireki tenkenRireki){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        tenkenRireki.setUpdDate(sdf1.format(date));
        tenkenRireki.setTrkDate(sdf1.format(date));

        tenkenRirekiMapper.insertTenkenRireki(tenkenRireki);
        tenkenRireki.setId(tenkenRirekiMapper.getLastInsertId());
        return tenkenRireki;
    }

    /**点検機器を更新する*/
    public TenkenRireki updateTenkenRireki(TenkenRireki tenkenRireki){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");
        tenkenRireki.setUpdDate(sdf1.format(date));
        tenkenRireki.setTenkenDate(sdf1.format(date));
        tenkenRireki.setTenkenNendo(sdf2.format(date));
        tenkenRirekiMapper.updateTenkenRireki(tenkenRireki);
        return tenkenRireki;
    }
    /**工事IDにより、点検機器の点検ランクを更新する　一括設定*/
    public TenkenRireki updateTenkenRankByKoujiId(String koujiID,String tenkenrank,String tenkennaiyo,String kanryoFlg){
        Date date=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy");

        TenkenRireki tenkenRireki=new TenkenRireki();
        tenkenRireki.setTenkenRank(tenkenrank);
        tenkenRireki.setTenkennaiyo(tenkennaiyo);
        tenkenRireki.setKoujiId(Integer.parseInt(koujiID));
        tenkenRireki.setKanryoFlg(kanryoFlg);
        tenkenRireki.setUpdDate(sdf1.format(date));
        tenkenRireki.setTenkenDate(sdf1.format(date));
        tenkenRireki.setTenkenNendo(sdf2.format(date));

        tenkenRirekiMapper.updateTenkenRirekiAllRankByKoujiID(tenkenRireki);
        return tenkenRireki;
    }

    /**点検機器を削除する*/
    public void deleteTenkenRireki(TenkenRireki tenkenRireki){

        tenkenRirekiMapper.deleteTenkenRireki(tenkenRireki);
    }

    /**工事IDにより、点検機器を削除する*/
    public void deleteTenkenRirekiByKoujiid(String koujiId){

        tenkenRirekiMapper.deleteTenkenRirekiByKoujiId(koujiId);
    }
}
