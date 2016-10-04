package com.toyo.vh.dto;

import com.toyo.vh.entity.Kiki;

/**
 * Created by Lsr on 11/25/14.
 * valve追加用
 */
public class ValveKikiSelectUtil {
    private String id;
    private String kikiSysId;
    private Kiki kiki;
    private String status;

    public String getKikiSysId() {
        return kikiSysId;
    }

    public void setKikiSysId(String kikiSysId) {
        this.kikiSysId = kikiSysId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Kiki getKiki() {
        return kiki;
    }

    public void setKiki(Kiki kiki) {
        this.kiki = kiki;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
