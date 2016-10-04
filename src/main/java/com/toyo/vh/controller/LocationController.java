package com.toyo.vh.controller;

import com.google.gson.Gson;
import com.toyo.vh.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Lsr on 11/12/14.
 */

@Controller
@RequestMapping("/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    /**
     * 会社名　取得
     *
     * @return String　会社名リスト
     * */
    @RequestMapping(value = "/getKCodeLJson", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKCodeL(){
        List<String> kCodeLList = locationService.getKCodeL();
        Gson gson = new Gson();
        return gson.toJson(kCodeLList);
    }

    /**
     * 会社名により、発電所取得
     *
     * @param kCodeL 会社名
     *
     * @return String　発電所リスト
     * */
    @RequestMapping(value = "/getKcodeMJsonBykCodeL", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKCodeMByKCodeL(@RequestParam("kCodeL")String kCodeL){
        List<String> kCodeMList = locationService.getKCodeMByL(kCodeL);
        Gson gson = new Gson();
        return gson.toJson(kCodeMList);
    }

    /**
     * 会社名と発電所により、何号機取得
     *
     * @param kCodeL 会社名
     * @param kCodeM 発電所
     *
     * @return String　ユニットリスト
     * */
    @RequestMapping(value = "/getKcodeSJsonBykCodeLkCodeM", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getKcodeSJsonBykCodeLkCodeM(@RequestParam("kCodeL")String kCodeL,
                                              @RequestParam("kCodeM")String kCodeM){
        List<String> kCodeSList = locationService.getKCodeSByLM(kCodeL,kCodeM);
        Gson gson = new Gson();
        return gson.toJson(kCodeSList);
    }
}
