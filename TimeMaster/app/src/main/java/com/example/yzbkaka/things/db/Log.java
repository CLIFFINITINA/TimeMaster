package com.example.yzbkaka.things.db;

import org.litepal.crud.LitePalSupport;


public class Log extends LitePalSupport {
    int id;
    String logWrite;

    public void setId(int id) {
        this.id = id;
    }

    public void setLogWrite(String logWrite) {
        this.logWrite = logWrite;
    }

    public int getId() {
        return id;
    }

    public String getLogWrite() {
        return logWrite;
    }

}
